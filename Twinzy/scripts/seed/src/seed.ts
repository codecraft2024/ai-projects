import { mkdir, writeFile } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";
import {
  REAL_CELEBRITIES,
  FUNNY_OBJECT_SEEDS,
  celebrityToProfile,
  funnyObjectToProfile,
} from "./catalog";

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const outputPath = path.join(__dirname, "../../..", "data", "profiles.json");

async function main() {
  const humanProfiles = REAL_CELEBRITIES.map(celebrityToProfile);
  const funnyProfiles = FUNNY_OBJECT_SEEDS.map(funnyObjectToProfile);
  const profiles = [...humanProfiles, ...funnyProfiles];

  await mkdir(path.dirname(outputPath), { recursive: true });
  await writeFile(
    outputPath,
    JSON.stringify(
      {
        version: 3,
        generatedAt: new Date().toISOString(),
        source: "real-celebrities-wikimedia-v3",
        profiles,
      },
      null,
      0,
    ),
    "utf8",
  );

  console.log(
    `Seeded ${profiles.length} profiles (${humanProfiles.length} celebrities + ${funnyProfiles.length} funny)`,
  );
}

main().catch((error) => {
  console.error(error);
  process.exit(1);
});
