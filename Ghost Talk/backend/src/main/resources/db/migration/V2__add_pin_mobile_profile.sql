ALTER TABLE users
    ADD COLUMN mobile VARCHAR(20),
    ADD COLUMN pin_hash VARCHAR(72),
    ADD COLUMN display_name VARCHAR(64),
    ADD COLUMN bio VARCHAR(280);

CREATE UNIQUE INDEX uq_users_mobile ON users (mobile) WHERE mobile IS NOT NULL;

UPDATE users SET display_name = username WHERE display_name IS NULL;
