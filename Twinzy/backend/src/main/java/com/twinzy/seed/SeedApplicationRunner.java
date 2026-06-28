package com.twinzy.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("seed")
public class SeedApplicationRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SeedApplicationRunner.class);

    private final DatasetSeedService datasetSeedService;

    public SeedApplicationRunner(DatasetSeedService datasetSeedService) {
        this.datasetSeedService = datasetSeedService;
    }

    @Override
    public void run(String... args) {
        log.info("Twinzy Java seed profile active");
        datasetSeedService.seedDataset();
        log.info("Seed finished successfully");
        System.exit(0);
    }
}
