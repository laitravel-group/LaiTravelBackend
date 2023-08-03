/*DROP TABLE IF EXISTS trip_plan;
DROP TABLE IF EXISTS place;
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS city;*/


CREATE TABLE IF NOT EXISTS users (
                       username     VARCHAR(255) NOT NULL PRIMARY KEY,
                       password     VARCHAR(255) NOT NULL,
                       display_name VARCHAR(255),
                       avatar       TEXT,
                       enabled  TINYINT      NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS city (
                      city_id   VARCHAR(255) NOT NULL PRIMARY KEY,
                      city_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS trip_plan (
                      trip_id    INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                      owner_id   VARCHAR(255) NOT NULL,
                      city_id    VARCHAR(255) NOT NULL,
                      start_date TIMESTAMP NOT NULL,
                      end_date   TIMESTAMP NOT NULL,
                      details    TEXT,
                      FOREIGN KEY (owner_id) REFERENCES users(username) ON DELETE CASCADE,
                      FOREIGN KEY (city_id) REFERENCES city(city_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS place (
                       place_id             VARCHAR(255) NOT NULL PRIMARY KEY,
                       place_name           VARCHAR(255),
                       city_id              VARCHAR(255) NOT NULL,
                       lat                  DOUBLE,
                       lng                  DOUBLE,
                       photo                TEXT,
                       types                TEXT,
                       formatted_address    TEXT,
                       description          TEXT,
                       rating               FLOAT,
                       opening_hours        TEXT,
                       last_updated         TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       FOREIGN KEY (city_id) REFERENCES city(city_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS authorities
(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    username  VARCHAR(255) NOT NULL,
    authority VARCHAR(255) NOT NULL,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE ON UPDATE CASCADE
);
