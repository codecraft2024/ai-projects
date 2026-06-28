package com.twinzy.seed;

import java.util.List;
import java.util.UUID;

import com.twinzy.model.ProfileImage;
import com.twinzy.model.StoredProfile;

public final class ProfileMetadataGenerator {

    private static final List<String> CATEGORIES = List.of(
            "actor_hollywood",
            "actor_bollywood",
            "actor_egyptian",
            "actor_turkish",
            "actor_korean",
            "actress_worldwide",
            "athlete_football",
            "athlete_basketball",
            "athlete_tennis",
            "athlete_mma",
            "athlete_olympics",
            "singer",
            "influencer",
            "historical"
    );

    private static final String[][] FIRST_NAMES = {
            {"Leonardo", "Brad", "Tom", "Ryan", "Chris", "Emma", "Scarlett", "Zendaya"},
            {"Shah", "Aamir", "Deepika", "Priyanka", "Ranbir", "Kareena", "Salman", "Amitabh"},
            {"Adel", "Ahmed", "Yousra", "Mona", "Karim", "Nelly", "Amr", "Asser"},
            {"Kivanc", "Burak", "Beren", "Hande", "Aras", "Serenay", "Can", "Demet"},
            {"Gong", "Lee", "Park", "Song", "Kim", "Jun", "Ji", "Hyun"},
            {"Natalie", "Margot", "Charlize", "Cate", "Penelope", "Marion", "Salma", "Gong"},
            {"Lionel", "Cristiano", "Kylian", "Mohamed", "Neymar", "Erling", "Kevin", "Virgil"},
            {"LeBron", "Stephen", "Kevin", "Giannis", "Luka", "Jayson", "Devin", "Anthony"},
            {"Novak", "Rafael", "Roger", "Serena", "Iga", "Carlos", "Coco", "Naomi"},
            {"Conor", "Jon", "Khabib", "Amanda", "Israel", "Alex", "Dustin", "Rose"},
            {"Usain", "Michael", "Simone", "Eliud", "Katie", "Shelly", "Allyson", "Florence"},
            {"Taylor", "Beyonce", "Adele", "Drake", "Rihanna", "Ed", "Billie", "Dua"},
            {"Emma", "David", "Huda", "Felix", "Logan", "Addison", "Khaby", "Charli"},
            {"Leonardo", "Albert", "Marie", "Nelson", "Cleopatra", "Isaac", "Ada", "Nikola"}
    };

    private static final String[][] LAST_NAMES = {
            {"DiCaprio", "Pitt", "Hanks", "Gosling", "Evans", "Stone", "Johansson", "Coleman"},
            {"Khan", "Kapoor", "Padukone", "Chopra", "Singh", "Kapoor", "Kumar", "Bachchan"},
            {"Imam", "Helmy", "Zaki", "Zaki", "Abdulaziz", "Karim", "Diab", "Yassin"},
            {"Tatlitug", "Ozcivit", "Saat", "Ercel", "Bulut", "Sarikaya", "Yaman", "Ozdemir"},
            {"Yoo", "Min-ho", "Seo-joon", "Joong-ki", "Woo-sung", "Eun-woo", "Hye-kyo", "Chang-wook"},
            {"Portman", "Robbie", "Theron", "Blanchett", "Cruz", "Cotillard", "Hayek", "Li"},
            {"Messi", "Ronaldo", "Mbappe", "Salah", "Jr", "Haaland", "De Bruyne", "van Dijk"},
            {"James", "Curry", "Durant", "Antetokounmpo", "Doncic", "Tatum", "Booker", "Davis"},
            {"Djokovic", "Nadal", "Federer", "Williams", "Swiatek", "Alcaraz", "Gauff", "Osaka"},
            {"McGregor", "Jones", "Nurmagomedov", "Nunes", "Adesanya", "Pereira", "Poirier", "Namajunas"},
            {"Bolt", "Phelps", "Biles", "Kipchoge", "Ledecky", "Fraser-Pryce", "Felix", "Griffith-Joyner"},
            {"Swift", "Knowles", "Adkins", "Graham", "Fenty", "Sheeran", "Eilish", "Lipa"},
            {"Chamberlain", "Dobrik", "Kattan", "Kjellberg", "Paul", "Rae", "Lame", "DAmelio"},
            {"da Vinci", "Einstein", "Curie", "Mandela", "VII", "Newton", "Lovelace", "Tesla"}
    };

    private ProfileMetadataGenerator() {
    }

    public static StoredProfile fromCelebrity(SeedCelebrityRecord celebrity) {
        StoredProfile profile = new StoredProfile();
        profile.setId(UUID.randomUUID().toString());
        profile.setSlug(celebrity.getSlug());
        profile.setFullName(celebrity.getFullName());
        profile.setGender(celebrity.getGender());
        profile.setDateOfBirth(celebrity.getDateOfBirth());
        profile.setNationality(celebrity.getNationality());
        profile.setCountry(celebrity.getCountry());
        profile.setCity(celebrity.getCity());
        profile.setOccupation(celebrity.getOccupation());
        profile.setBiography(celebrity.getBiography());
        profile.setHeightCm(celebrity.getHeightCm());
        profile.setEyeColor(celebrity.getEyeColor());
        profile.setHairColor(celebrity.getHairColor());
        profile.setEthnicityCategory(celebrity.getEthnicityCategory());
        profile.setPublicFigureCategory(celebrity.getCategory());
        profile.setInstagramUrl(celebrity.getInstagramUrl());
        profile.setXUrl(celebrity.getXUrl());
        profile.setWebsiteUrl("https://twinzy.demo/celebrity/" + celebrity.getSlug());
        profile.setTags(celebrity.getTags());
        profile.setPopularityScore(celebrity.getPopularityScore());
        profile.setFunnyObject(false);

        ProfileImage primary = new ProfileImage();
        primary.setId(celebrity.getSlug() + "-primary");
        primary.setUrl(celebrity.getImageUrl());
        primary.setAlt(celebrity.getFullName() + " portrait");
        primary.setPrimary(true);
        primary.setWidth(640);
        primary.setHeight(800);
        profile.setImages(List.of(primary));
        return profile;
    }

    public static StoredProfile fromFunny(SeedFunnyRecord funny) {
        StoredProfile profile = new StoredProfile();
        profile.setId(UUID.randomUUID().toString());
        profile.setSlug(funny.getSlug());
        profile.setFullName(funny.getName());
        profile.setGender("other");
        profile.setDateOfBirth("2000-01-01");
        profile.setNationality("Everywhere");
        profile.setCountry("Kitchen");
        profile.setCity("Pantry");
        profile.setOccupation("Legendary " + funny.getName());
        profile.setBiography(funny.getName() + " " + funny.getEmoji()
                + " is an iconic funny object admired for its instantly recognizable shape.");
        profile.setHeightCm(null);
        profile.setEyeColor("N/A");
        profile.setHairColor("N/A");
        profile.setEthnicityCategory("Object");
        profile.setPublicFigureCategory("funny_object");
        profile.setTags(funny.getTags());
        profile.setPopularityScore(25);
        profile.setFunnyObject(true);

        ProfileImage primary = new ProfileImage();
        primary.setId(funny.getSlug() + "-primary");
        primary.setUrl(funny.getImageUrl());
        primary.setAlt(funny.getName());
        primary.setPrimary(true);
        primary.setWidth(640);
        primary.setHeight(640);
        profile.setImages(List.of(primary));
        return profile;
    }

    public static StoredProfile fromWikimedia(int index, String imageUrl, String titleHint) {
        int categoryIndex = index % CATEGORIES.size();
        String category = CATEGORIES.get(categoryIndex);
        String firstName = pick(FIRST_NAMES[categoryIndex], index);
        String lastName = pick(LAST_NAMES[categoryIndex], index + 3);
        String fullName = titleHint != null && titleHint.length() > 2
                ? titleHint
                : firstName + " " + lastName;
        String slug = SeedVectorUtils.slugify(fullName) + "-" + index + "-" + UUID.randomUUID().toString().substring(0, 8);
        int seed = index * 9973 + category.length() * 131;
        String gender = (seed % 2 == 0) ? "female" : "male";
        int birthYear = 1950 + (seed % 45);
        int birthMonth = (seed % 12) + 1;
        int birthDay = (seed % 27) + 1;

        StoredProfile profile = new StoredProfile();
        profile.setId(UUID.randomUUID().toString());
        profile.setSlug(slug);
        profile.setFullName(fullName);
        profile.setGender(gender);
        profile.setDateOfBirth(String.format("%d-%02d-%02d", birthYear, birthMonth, birthDay));
        profile.setNationality(pick(new String[] {"American", "British", "French", "Indian", "Egyptian", "Korean"}, index));
        profile.setCountry(pick(new String[] {"United States", "United Kingdom", "France", "India", "Egypt", "South Korea"}, index));
        profile.setCity(pick(new String[] {"Los Angeles", "London", "Paris", "Mumbai", "Cairo", "Seoul"}, index));
        profile.setOccupation(pick(new String[] {"Actor", "Singer", "Athlete", "Influencer", "Model"}, index));
        profile.setBiography(fullName + " is a celebrated public figure known for global influence and memorable presence.");
        profile.setHeightCm(gender.equals("female") ? 160 + (seed % 25) : 170 + (seed % 25));
        profile.setEyeColor(pick(new String[] {"Brown", "Blue", "Green", "Hazel", "Gray"}, seed));
        profile.setHairColor(pick(new String[] {"Black", "Brown", "Blonde", "Auburn", "Gray"}, seed + 1));
        profile.setEthnicityCategory(pick(new String[] {"Caucasian", "Asian", "Middle Eastern", "African", "Latino", "South Asian"}, seed + 2));
        profile.setPublicFigureCategory(category);
        profile.setWebsiteUrl("https://twinzy.demo/celebrity/" + slug);
        profile.setTags(List.of(category.replace('_', ' '), "portrait", "wikimedia"));
        profile.setPopularityScore(35 + (seed % 55));
        profile.setFunnyObject(false);

        ProfileImage primary = new ProfileImage();
        primary.setId(slug + "-primary");
        primary.setUrl(imageUrl == null ? "" : imageUrl);
        primary.setAlt(fullName + " portrait");
        primary.setPrimary(true);
        primary.setWidth(640);
        primary.setHeight(800);
        profile.setImages(List.of(primary));
        return profile;
    }

    private static String pick(String[] items, int index) {
        return items[Math.floorMod(index, items.length)];
    }
}
