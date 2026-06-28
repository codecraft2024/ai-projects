package com.twinzy.seed;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twinzy.analysis.FaceAnalysisService;
import com.twinzy.config.TwinzyProperties;
import com.twinzy.domain.FaceAnalysisResult;
import com.twinzy.media.ProfileImageStorageService;
import com.twinzy.model.ProfileImage;
import com.twinzy.model.StoredProfile;
import com.twinzy.persistence.ProfilePersistenceService;

@Service
public class DatasetSeedService {

    private static final Logger log = LoggerFactory.getLogger(DatasetSeedService.class);
    private static final int DOWNLOAD_THREADS = 4;

    private final TwinzyProperties properties;
    private final ObjectMapper objectMapper;
    private final WikimediaImageFetcher wikimediaImageFetcher;
    private final ImageDownloadService imageDownloadService;
    private final FaceAnalysisService faceAnalysisService;
    private final ProfilePersistenceService profilePersistenceService;
    private final ProfileImageStorageService profileImageStorageService;

    public DatasetSeedService(
            TwinzyProperties properties,
            ObjectMapper objectMapper,
            WikimediaImageFetcher wikimediaImageFetcher,
            ImageDownloadService imageDownloadService,
            FaceAnalysisService faceAnalysisService,
            ProfilePersistenceService profilePersistenceService,
            ProfileImageStorageService profileImageStorageService
    ) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.wikimediaImageFetcher = wikimediaImageFetcher;
        this.imageDownloadService = imageDownloadService;
        this.faceAnalysisService = faceAnalysisService;
        this.profilePersistenceService = profilePersistenceService;
        this.profileImageStorageService = profileImageStorageService;
    }

    public void seedDataset() {
        int totalProfiles = properties.getSeed().getTotalProfiles();
        int funnyCount = properties.getSeed().getFunnyCount();
        int humanTarget = totalProfiles - funnyCount;

        log.info("Starting Java seed into PostgreSQL: {} total profiles ({} humans + {} funny)",
                totalProfiles, humanTarget, funnyCount);

        profileImageStorageService.clearAll();

        List<SeedCelebrityRecord> celebrities = loadCatalog("celebrities.json", new TypeReference<>() {});
        List<SeedFunnyRecord> funnyObjects = loadCatalog("funny-objects.json", new TypeReference<>() {});

        Set<String> usedUrls = Collections.synchronizedSet(new HashSet<>());
        Set<String> failedUrls = Collections.synchronizedSet(new HashSet<>());
        List<StoredProfile> profiles = Collections.synchronizedList(new ArrayList<>());

        List<SeedCelebrityRecord> curated = celebrities.stream()
                .filter(record -> hasImageUrl(record.getImageUrl()))
                .limit(humanTarget)
                .toList();
        seedInParallel(curated.stream()
                .map(celebrity -> new SeedTask(
                        ProfileMetadataGenerator.fromCelebrity(celebrity),
                        celebrity.getImageUrl(),
                        celebrity.getFullName()))
                .toList(), profiles, usedUrls, failedUrls, "curated celebrities");

        int bulkRound = 0;
        while (profiles.size() < humanTarget && bulkRound < 12) {
            int bulkNeeded = humanTarget - profiles.size();
            log.info("Round {}: need {} more human profiles", bulkRound + 1, bulkNeeded);
            Set<String> exclude = new HashSet<>(usedUrls);
            exclude.addAll(failedUrls);
            List<String> portraitUrls = wikimediaImageFetcher.fetchPortraitUrls(bulkNeeded + 500, exclude);
            if (portraitUrls.isEmpty()) {
                break;
            }

            List<SeedTask> bulkTasks = new ArrayList<>();
            int index = profiles.size();
            for (String imageUrl : portraitUrls) {
                if (bulkTasks.size() >= bulkNeeded) {
                    break;
                }
                if (usedUrls.contains(imageUrl)) {
                    continue;
                }
                bulkTasks.add(new SeedTask(
                        ProfileMetadataGenerator.fromWikimedia(index++, imageUrl, null),
                        imageUrl,
                        null));
            }
            seedInParallel(bulkTasks, profiles, usedUrls, failedUrls, "wikimedia portraits round " + (bulkRound + 1));
            bulkRound++;
        }

        List<SeedTask> funnyTasks = funnyObjects.stream()
                .filter(record -> hasImageUrl(record.getImageUrl()))
                .limit(funnyCount)
                .map(funny -> new SeedTask(
                        ProfileMetadataGenerator.fromFunny(funny),
                        funny.getImageUrl(),
                        funny.getName()))
                .toList();
        seedInParallel(funnyTasks, profiles, usedUrls, failedUrls, "funny objects");

        int funnyAttempts = 0;
        while (profiles.size() < totalProfiles && funnyAttempts < 3) {
            int remaining = totalProfiles - profiles.size();
            log.info("Need {} more profiles, fetching additional Wikimedia portraits", remaining);
            Set<String> exclude = new HashSet<>(usedUrls);
            exclude.addAll(failedUrls);
            List<String> portraitUrls = wikimediaImageFetcher.fetchPortraitUrls(remaining + 100, exclude);
            if (portraitUrls.isEmpty()) {
                break;
            }
            List<SeedTask> extraTasks = new ArrayList<>();
            int index = profiles.size();
            for (String imageUrl : portraitUrls) {
                if (extraTasks.size() >= remaining) {
                    break;
                }
                if (usedUrls.contains(imageUrl)) {
                    continue;
                }
                extraTasks.add(new SeedTask(
                        ProfileMetadataGenerator.fromWikimedia(index++, imageUrl, null),
                        imageUrl,
                        null));
            }
            seedInParallel(extraTasks, profiles, usedUrls, failedUrls, "top-up portraits");
            funnyAttempts++;
        }

        if (profiles.size() < humanTarget) {
            throw new IllegalStateException("Unable to complete human seed with real images only: "
                    + profiles.size() + " of " + humanTarget);
        }

        if (profiles.size() < totalProfiles) {
            log.warn("Seed reached {} of {} requested profiles; saving all profiles with real images",
                    profiles.size(), totalProfiles);
        }

        validateProfilesHaveImages(profiles);
        profilePersistenceService.replaceAllProfiles(profiles);
        log.info("Seed complete: {} profiles with stored images in PostgreSQL", profiles.size());
    }

    private void seedInParallel(
            List<SeedTask> tasks,
            List<StoredProfile> profiles,
            Set<String> usedUrls,
            Set<String> failedUrls,
            String label
    ) {
        if (tasks.isEmpty()) {
            return;
        }

        ExecutorService executor = Executors.newFixedThreadPool(DOWNLOAD_THREADS);
        try {
            List<Future<StoredProfile>> futures = tasks.stream()
                    .map(task -> executor.submit(() -> enrichWithImageAnalysis(
                            task.profile(),
                            task.imageUrl(),
                            task.searchQuery())))
                    .toList();

            int added = 0;
            int skipped = 0;
            for (int i = 0; i < futures.size(); i++) {
                try {
                    StoredProfile profile = futures.get(i).get();
                    if (profile != null) {
                        profiles.add(profile);
                        String sourceUrl = tasks.get(i).imageUrl();
                        if (sourceUrl != null && !sourceUrl.isBlank()) {
                            usedUrls.add(sourceUrl);
                        }
                        added++;
                        if (added % 250 == 0) {
                            log.info("Built {} {}", added, label);
                        }
                    } else {
                        skipped++;
                        markFailedUrl(failedUrls, tasks.get(i).imageUrl());
                    }
                } catch (Exception ex) {
                    skipped++;
                    markFailedUrl(failedUrls, tasks.get(i).imageUrl());
                    log.debug("Skipped profile: {}", ex.getMessage());
                }
            }
            log.info("Added {} {} (skipped {} without images)", added, label, skipped);
        } finally {
            executor.shutdown();
        }
    }

    private StoredProfile enrichWithImageAnalysis(StoredProfile profile, String imageUrl, String searchQuery) {
        byte[] imageBytes = imageDownloadService.downloadWithFallbacks(imageUrl, searchQuery);
        if (imageBytes == null) {
            log.debug("Skipping profile {} because no real image could be downloaded", profile.getSlug());
            return null;
        }

        ProfileImage primaryImage = profile.getImages().get(0);
        String storedUrl = profileImageStorageService.store(profile.getId(), primaryImage.getId(), imageBytes);
        primaryImage.setUrl(storedUrl);

        FaceAnalysisResult analysis = faceAnalysisService.analyze(imageBytes);
        profile.setFaceEmbedding(analysis.getEmbedding());
        profile.setStoredFeatures(analysis.getFeatures());
        return profile;
    }

    private void validateProfilesHaveImages(List<StoredProfile> profiles) {
        for (StoredProfile profile : profiles) {
            if (profile.getImages() == null || profile.getImages().isEmpty()) {
                throw new IllegalStateException("Profile " + profile.getSlug() + " has no images");
            }
            for (ProfileImage image : profile.getImages()) {
                if (image.getUrl() == null || image.getUrl().isBlank()) {
                    throw new IllegalStateException("Profile " + profile.getSlug() + " has blank image URL");
                }
            }
        }
    }

    private void markFailedUrl(Set<String> failedUrls, String imageUrl) {
        if (imageUrl != null && !imageUrl.isBlank()) {
            failedUrls.add(imageUrl);
        }
    }

    private boolean hasImageUrl(String imageUrl) {
        return imageUrl != null && !imageUrl.isBlank();
    }

    private <T> T loadCatalog(String filename, TypeReference<T> type) {
        try {
            Path path = Paths.get(properties.getDatabase().getSeedPath(), filename).toAbsolutePath().normalize();
            return objectMapper.readValue(Files.readString(path), type);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed loading seed catalog " + filename, ex);
        }
    }

    private record SeedTask(StoredProfile profile, String imageUrl, String searchQuery) {
    }
}
