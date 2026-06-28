import type { FeatureBreakdown, Gender, Profile, ProfileCategory } from "@twinzy/types";

export interface CategoryConfig {
  category: ProfileCategory;
  label: string;
  occupations: string[];
  countries: string[];
  nationalities: string[];
  cities: string[];
  firstNames: string[];
  lastNames: string[];
  tags: string[];
  genders: Gender[];
  isFunnyObject?: boolean;
}

export const FUNNY_OBJECTS: Array<{ name: string; emoji: string; tags: string[] }> = [
  { name: "Potato", emoji: "🥔", tags: ["root vegetable", "starchy", "comfort food"] },
  { name: "Tomato", emoji: "🍅", tags: ["fruit", "salad", "red"] },
  { name: "Onion", emoji: "🧅", tags: ["layered", "aromatic", "kitchen staple"] },
  { name: "Carrot", emoji: "🥕", tags: ["orange", "crunchy", "healthy"] },
  { name: "Pumpkin", emoji: "🎃", tags: ["autumn", "round", "festive"] },
  { name: "Banana", emoji: "🍌", tags: ["curved", "tropical", "potassium"] },
  { name: "Panda", emoji: "🐼", tags: ["bamboo lover", "black and white", "cuddly"] },
  { name: "Koala", emoji: "🐨", tags: ["sleepy", "eucalyptus", "fluffy ears"] },
  { name: "Golden Retriever", emoji: "🐕", tags: ["loyal", "friendly", "golden fur"] },
  { name: "Cat", emoji: "🐱", tags: ["whiskers", "independent", "curious"] },
];

export const CATEGORY_CONFIGS: CategoryConfig[] = [
  {
    category: "actor_hollywood",
    label: "Hollywood Actor",
    occupations: ["Actor", "Producer", "Director"],
    countries: ["United States", "United Kingdom", "Canada", "Australia"],
    nationalities: ["American", "British", "Canadian", "Australian"],
    cities: ["Los Angeles", "New York", "London", "Toronto"],
    firstNames: ["Leonardo", "Brad", "Tom", "Ryan", "Chris", "Matt", "George", "John", "Robert", "Will"],
    lastNames: ["DiCaprio", "Pitt", "Hanks", "Gosling", "Evans", "Damon", "Clooney", "Depp", "Downey", "Smith"],
    tags: ["blockbuster", "oscar", "red carpet", "drama", "action"],
    genders: ["male"],
  },
  {
    category: "actor_bollywood",
    label: "Bollywood Actor",
    occupations: ["Actor", "Producer", "Singer"],
    countries: ["India"],
    nationalities: ["Indian"],
    cities: ["Mumbai", "Delhi", "Kolkata", "Bangalore"],
    firstNames: ["Shah", "Aamir", "Salman", "Ranbir", "Hrithik", "Akshay", "Ajay", "Ranveer", "Varun", "Tiger"],
    lastNames: ["Khan", "Kapoor", "Roshan", "Kumar", "Devgn", "Singh", "Dhawan", "Shroff", "Bachchan", "Malhotra"],
    tags: ["masala", "dance", "romance", "bollywood", "superstar"],
    genders: ["male"],
  },
  {
    category: "actor_egyptian",
    label: "Egyptian Actor",
    occupations: ["Actor", "Director", "Presenter"],
    countries: ["Egypt"],
    nationalities: ["Egyptian"],
    cities: ["Cairo", "Alexandria", "Giza"],
    firstNames: ["Adel", "Ahmed", "Karim", "Youssef", "Amr", "Asser", "Mohamed", "Hany", "Mena", "Yasser"],
    lastNames: ["Imam", "Helmy", "Abdulaziz", "El Sebaei", "Waked", "Yassin", "Salem", "Salama", "Shalaby", "Galal"],
    tags: ["drama", "comedy", "cinema", "arabic", "legend"],
    genders: ["male"],
  },
  {
    category: "actor_turkish",
    label: "Turkish Actor",
    occupations: ["Actor", "Model", "Producer"],
    countries: ["Turkey"],
    nationalities: ["Turkish"],
    cities: ["Istanbul", "Ankara", "Izmir", "Antalya"],
    firstNames: ["Kivanc", "Burak", "Can", "Aras", "Kerem", "Engin", "Haluk", "Cagatay", "Baris", "Mert"],
    lastNames: ["Tatlitug", "Ozcivit", "Yaman", "Bulut", "Bursin", "Akyurek", "Bilginer", "Ulusoy", "Arduc", "Yazici"],
    tags: ["dizi", "romance", "drama", "heartthrob", "series"],
    genders: ["male"],
  },
  {
    category: "actor_korean",
    label: "Korean Actor",
    occupations: ["Actor", "Idol", "Model"],
    countries: ["South Korea"],
    nationalities: ["Korean"],
    cities: ["Seoul", "Busan", "Incheon"],
    firstNames: ["Gong", "Lee", "Park", "Song", "Hyun", "Ji", "Kim", "Cha", "Yoo", "Jung"],
    lastNames: ["Yoo", "Min-ho", "Seo-joon", "Joong-ki", "Bin", "Chang-wook", "Woo-sung", "Eun-woo", "Ah-in", "Hae-in"],
    tags: ["kdrama", "hallyu", "romance", "thriller", "global"],
    genders: ["male"],
  },
  {
    category: "actress_worldwide",
    label: "Actress",
    occupations: ["Actress", "Producer", "Activist"],
    countries: ["United States", "France", "United Kingdom", "India", "South Korea", "Egypt"],
    nationalities: ["American", "French", "British", "Indian", "Korean", "Egyptian"],
    cities: ["Los Angeles", "Paris", "London", "Mumbai", "Seoul", "Cairo"],
    firstNames: ["Scarlett", "Emma", "Natalie", "Zendaya", "Margot", "Deepika", "Priyanka", "Son", "Yousra", "Mona"],
    lastNames: ["Johansson", "Stone", "Portman", "Coleman", "Robbie", "Padukone", "Chopra", "Ye-jin", "Zaki", "Zaki"],
    tags: ["leading lady", "award winner", "fashion", "drama", "icon"],
    genders: ["female"],
  },
  {
    category: "athlete_football",
    label: "Footballer",
    occupations: ["Professional Footballer", "Captain", "Striker"],
    countries: ["Brazil", "Argentina", "Portugal", "France", "Egypt", "Norway"],
    nationalities: ["Brazilian", "Argentine", "Portuguese", "French", "Egyptian", "Norwegian"],
    cities: ["Madrid", "Manchester", "Paris", "Milan", "Liverpool", "Munich"],
    firstNames: ["Lionel", "Cristiano", "Kylian", "Mohamed", "Erling", "Neymar", "Kevin", "Luka", "Virgil", "Robert"],
    lastNames: ["Messi", "Ronaldo", "Mbappe", "Salah", "Haaland", "Jr", "De Bruyne", "Modric", "van Dijk", "Lewandowski"],
    tags: ["champions league", "world cup", "striker", "captain", "legend"],
    genders: ["male"],
  },
  {
    category: "athlete_basketball",
    label: "Basketball Player",
    occupations: ["NBA Player", "Point Guard", "Forward"],
    countries: ["United States", "Canada", "Serbia", "Greece"],
    nationalities: ["American", "Canadian", "Serbian", "Greek"],
    cities: ["Los Angeles", "Boston", "Miami", "Chicago", "Dallas"],
    firstNames: ["LeBron", "Stephen", "Kevin", "Giannis", "Luka", "Jayson", "Devin", "Anthony", "Kawhi", "Damian"],
    lastNames: ["James", "Curry", "Durant", "Antetokounmpo", "Doncic", "Tatum", "Booker", "Davis", "Leonard", "Lillard"],
    tags: ["nba", "finals", "mvp", "dunk", "clutch"],
    genders: ["male"],
  },
  {
    category: "athlete_tennis",
    label: "Tennis Player",
    occupations: ["Professional Tennis Player"],
    countries: ["Serbia", "Spain", "Switzerland", "Poland", "United States"],
    nationalities: ["Serbian", "Spanish", "Swiss", "Polish", "American"],
    cities: ["Belgrade", "Barcelona", "Basel", "Warsaw", "Palm Beach"],
    firstNames: ["Novak", "Rafael", "Roger", "Carlos", "Iga", "Serena", "Naomi", "Stefanos", "Jannik", "Coco"],
    lastNames: ["Djokovic", "Nadal", "Federer", "Alcaraz", "Swiatek", "Williams", "Osaka", "Tsitsipas", "Sinner", "Gauff"],
    tags: ["grand slam", "wimbledon", "ace", "baseline", "champion"],
    genders: ["male", "female"],
  },
  {
    category: "athlete_mma",
    label: "MMA Fighter",
    occupations: ["UFC Fighter", "Mixed Martial Artist"],
    countries: ["United States", "Ireland", "Russia", "Brazil"],
    nationalities: ["American", "Irish", "Russian", "Brazilian"],
    cities: ["Las Vegas", "Dublin", "Moscow", "Rio de Janeiro"],
    firstNames: ["Conor", "Jon", "Khabib", "Amanda", "Israel", "Charles", "Alex", "Dustin", "Max", "Rose"],
    lastNames: ["McGregor", "Jones", "Nurmagomedov", "Nunes", "Adesanya", "Oliveira", "Pereira", "Poirier", "Holloway", "Namajunas"],
    tags: ["ufc", "knockout", "octagon", "champion", "grappling"],
    genders: ["male", "female"],
  },
  {
    category: "athlete_olympics",
    label: "Olympian",
    occupations: ["Olympic Athlete", "Sprinter", "Swimmer"],
    countries: ["United States", "Jamaica", "China", "Kenya", "Australia"],
    nationalities: ["American", "Jamaican", "Chinese", "Kenyan", "Australian"],
    cities: ["Los Angeles", "Kingston", "Beijing", "Nairobi", "Sydney"],
    firstNames: ["Usain", "Michael", "Simone", "Eliud", "Katie", "Shelly", "Caeleb", "Sydney", "Allyson", "Florence"],
    lastNames: ["Bolt", "Phelps", "Biles", "Kipchoge", "Ledecky", "Fraser-Pryce", "Dressel", "McLaughlin", "Felix", "Griffith-Joyner"],
    tags: ["gold medal", "world record", "olympics", "track", "inspiration"],
    genders: ["male", "female"],
  },
  {
    category: "singer",
    label: "Singer",
    occupations: ["Singer", "Songwriter", "Performer"],
    countries: ["United States", "United Kingdom", "Canada", "Barbados", "South Korea"],
    nationalities: ["American", "British", "Canadian", "Barbadian", "Korean"],
    cities: ["Los Angeles", "London", "Toronto", "Bridgetown", "Seoul"],
    firstNames: ["Taylor", "Beyonce", "Adele", "Drake", "Rihanna", "Ed", "Billie", "The Weeknd", "Dua", "Jungkook"],
    lastNames: ["Swift", "Knowles", "Adkins", "Graham", "Fenty", "Sheeran", "Eilish", "Tesfaye", "Lipa", "Jeon"],
    tags: ["chart topper", "tour", "grammy", "pop", "hit single"],
    genders: ["male", "female"],
  },
  {
    category: "influencer",
    label: "Influencer",
    occupations: ["Content Creator", "YouTuber", "Streamer"],
    countries: ["United States", "United Kingdom", "UAE", "Brazil", "Japan"],
    nationalities: ["American", "British", "Emirati", "Brazilian", "Japanese"],
    cities: ["Los Angeles", "London", "Dubai", "Sao Paulo", "Tokyo"],
    firstNames: ["Emma", "David", "Huda", "Felix", "Chiara", "Logan", "Addison", "Khaby", "Charli", "MrBeast"],
    lastNames: ["Chamberlain", "Dobrik", "Kattan", "Kjellberg", "Ferragni", "Paul", "Rae", "Lame", "DAmelio", "Donaldson"],
    tags: ["viral", "lifestyle", "beauty", "gaming", "creator economy"],
    genders: ["male", "female"],
  },
  {
    category: "historical",
    label: "Historical Figure",
    occupations: ["Leader", "Scientist", "Philosopher", "Inventor"],
    countries: ["Italy", "Germany", "United States", "India", "United Kingdom"],
    nationalities: ["Italian", "German", "American", "Indian", "British"],
    cities: ["Florence", "Berlin", "Washington", "Delhi", "London"],
    firstNames: ["Leonardo", "Albert", "Marie", "Nelson", "Cleopatra", "Isaac", "Ada", "Nikola", "Martin", "Rosa"],
    lastNames: ["da Vinci", "Einstein", "Curie", "Mandela", "VII", "Newton", "Lovelace", "Tesla", "Luther King Jr", "Parks"],
    tags: ["legacy", "innovation", "history", "icon", "timeless"],
    genders: ["male", "female"],
  },
];

export const SEED_COUNTS = {
  celebrity: 500,
  athlete: 500,
  singer: 500,
  influencer: 500,
  funny: 100,
} as const;

export const SMALL_SEED_COUNTS = {
  celebrity: 50,
  athlete: 50,
  singer: 50,
  influencer: 50,
  funny: 10,
} as const;

export const CELEBRITY_CATEGORIES: ProfileCategory[] = [
  "actor_hollywood",
  "actor_bollywood",
  "actor_egyptian",
  "actor_turkish",
  "actor_korean",
  "actress_worldwide",
];

export const ATHLETE_CATEGORIES: ProfileCategory[] = [
  "athlete_football",
  "athlete_basketball",
  "athlete_tennis",
  "athlete_mma",
  "athlete_olympics",
];

export function getCategoryConfig(category: ProfileCategory): CategoryConfig {
  const config = CATEGORY_CONFIGS.find((item) => item.category === category);
  if (!config) {
    throw new Error(`Missing category config for ${category}`);
  }
  return config;
}

export function seededBreakdown(seed: number): FeatureBreakdown {
  const next = (offset: number) => 70 + ((seed + offset) % 31);
  return {
    faceShape: next(1),
    eyes: next(2),
    eyebrows: next(3),
    nose: next(4),
    lips: next(5),
    smile: next(6),
    forehead: next(7),
    jawline: next(8),
    hairline: next(9),
  };
}

export function seededVector(seed: number, dimensions = 512): number[] {
  const vector: number[] = [];
  let state = seed;
  for (let i = 0; i < dimensions; i++) {
    state = (state * 1664525 + 1013904223) % 2147483647;
    vector.push((state / 2147483647) * 2 - 1);
  }
  const magnitude = Math.sqrt(vector.reduce((sum, value) => sum + value * value, 0));
  return vector.map((value) => value / magnitude);
}

export function slugify(value: string): string {
  return value
    .toLowerCase()
    .replace(/[^a-z0-9]+/g, "-")
    .replace(/(^-|-$)/g, "");
}

export function pick<T>(items: T[], index: number): T {
  return items[index % items.length]!;
}

export function buildBiography(fullName: string, occupation: string, country: string, tags: string[]): string {
  return `${fullName} is a celebrated ${occupation.toLowerCase()} from ${country}, known for ${tags.slice(0, 3).join(", ")}. With a global fanbase and decades of cultural impact, ${fullName.split(" ")[0]} continues to inspire audiences worldwide through memorable performances and public presence.`;
}

export function buildProfileImages(slug: string, fullName: string, count: number, isFunnyObject: boolean) {
  const imageCount = Math.max(5, Math.min(20, count));
  return Array.from({ length: imageCount }, (_, index) => ({
    id: `${slug}-image-${index + 1}`,
    url: isFunnyObject
      ? `https://picsum.photos/seed/${slug}-${index}/640/640`
      : `https://picsum.photos/seed/${slug}-portrait-${index}/480/640`,
    alt: `${fullName} portrait ${index + 1}`,
    isPrimary: index === 0,
    width: isFunnyObject ? 640 : 480,
    height: isFunnyObject ? 640 : 640,
  }));
}

export function createHumanProfile(
  index: number,
  category: ProfileCategory,
  suffix = "",
): Profile & { storedFeatures: FeatureBreakdown } {
  const config = getCategoryConfig(category);
  const firstName = pick(config.firstNames, index);
  const lastName = pick(config.lastNames, index + 3);
  const fullName = `${firstName} ${lastName}${suffix}`;
  const slug = `${slugify(fullName)}${suffix ? `-${suffix}` : ""}`;
  const gender = pick(config.genders, index);
  const country = pick(config.countries, index);
  const nationality = pick(config.nationalities, index);
  const city = pick(config.cities, index);
  const occupation = pick(config.occupations, index);
  const seed = index * 9973 + category.length * 131;
  const birthYear = 1950 + (seed % 45);
  const birthMonth = (seed % 12) + 1;
  const birthDay = (seed % 27) + 1;

  const profile: Profile = {
    id: crypto.randomUUID(),
    slug,
    fullName,
    gender,
    dateOfBirth: `${birthYear}-${String(birthMonth).padStart(2, "0")}-${String(birthDay).padStart(2, "0")}`,
    nationality,
    country,
    city,
    occupation,
    biography: buildBiography(fullName, occupation, country, config.tags),
    heightCm: gender === "female" ? 160 + (seed % 25) : 170 + (seed % 25),
    eyeColor: pick(["Brown", "Blue", "Green", "Hazel", "Gray"], seed),
    hairColor: pick(["Black", "Brown", "Blonde", "Auburn", "Gray"], seed + 1),
    ethnicityCategory: pick(["Caucasian", "Asian", "Middle Eastern", "African", "Latino", "South Asian"], seed + 2),
    publicFigureCategory: category,
    instagramUrl: `https://instagram.com/${slug.replace(/-/g, "")}`,
    xUrl: `https://x.com/${slug.replace(/-/g, "")}`,
    websiteUrl: `https://twinzy.demo/profile/${slug}`,
    images: buildProfileImages(slug, fullName, 6 + (seed % 8), false),
    tags: [...config.tags.slice(0, 3), occupation.toLowerCase()],
    faceEmbedding: seededVector(seed),
    popularityScore: 40 + (seed % 60),
    isFunnyObject: false,
  };

  return {
    ...profile,
    storedFeatures: seededBreakdown(seed),
  };
}

export function createFunnyProfile(index: number): Profile & { storedFeatures: FeatureBreakdown } {
  const object = pick(FUNNY_OBJECTS, index);
  const fullName = object.name;
  const slug = `${slugify(fullName)}-funny-${index + 1}`;
  const seed = 50000 + index * 313;

  const profile: Profile = {
    id: crypto.randomUUID(),
    slug,
    fullName,
    gender: "other",
    dateOfBirth: "2000-01-01",
    nationality: "Everywhere",
    country: "Kitchen",
    city: "Pantry",
    occupation: `Legendary ${fullName}`,
    biography: `${fullName} ${object.emoji} is an iconic funny object admired for its instantly recognizable shape, texture, and meme-worthy charm.`,
    heightCm: null,
    eyeColor: "N/A",
    hairColor: "N/A",
    ethnicityCategory: "Object",
    publicFigureCategory: "funny_object",
    images: buildProfileImages(slug, fullName, 5, true),
    tags: object.tags,
    faceEmbedding: seededVector(seed),
    popularityScore: 20 + (seed % 30),
    isFunnyObject: true,
  };

  return {
    ...profile,
    storedFeatures: seededBreakdown(seed),
  };
}
