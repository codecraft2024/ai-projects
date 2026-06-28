CREATE TABLE IF NOT EXISTS profiles (
    id UUID PRIMARY KEY,
    slug VARCHAR(255) NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    gender VARCHAR(32) NOT NULL,
    date_of_birth VARCHAR(16) NOT NULL,
    nationality VARCHAR(128) NOT NULL,
    country VARCHAR(128) NOT NULL,
    city VARCHAR(128) NOT NULL,
    occupation VARCHAR(255) NOT NULL,
    biography TEXT NOT NULL,
    height_cm INTEGER,
    eye_color VARCHAR(64) NOT NULL,
    hair_color VARCHAR(64) NOT NULL,
    ethnicity_category VARCHAR(64) NOT NULL,
    public_figure_category VARCHAR(64) NOT NULL,
    instagram_url VARCHAR(512),
    x_url VARCHAR(512),
    website_url VARCHAR(512),
    popularity_score DOUBLE PRECISION NOT NULL DEFAULT 0,
    is_funny_object BOOLEAN NOT NULL DEFAULT FALSE,
    tags JSONB NOT NULL DEFAULT '[]'::jsonb,
    face_embedding JSONB NOT NULL,
    stored_features JSONB NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS profile_images (
    id VARCHAR(128) PRIMARY KEY,
    profile_id UUID NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    url TEXT NOT NULL,
    alt TEXT NOT NULL,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    width INTEGER NOT NULL DEFAULT 640,
    height INTEGER NOT NULL DEFAULT 800
);

CREATE INDEX IF NOT EXISTS idx_profiles_slug ON profiles(slug);
CREATE INDEX IF NOT EXISTS idx_profiles_funny ON profiles(is_funny_object);
CREATE INDEX IF NOT EXISTS idx_profiles_category ON profiles(public_figure_category);
CREATE INDEX IF NOT EXISTS idx_profile_images_profile_id ON profile_images(profile_id);
