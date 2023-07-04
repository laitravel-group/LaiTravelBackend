DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS city;
DROP TABLE IF EXISTS trip;
DROP TABLE IF EXISTS place;

CREATE TABLE users (
                       UserID      VARCHAR(64) NOT NULL PRIMARY KEY,
                       Password    VARCHAR(32) NOT NULL,
                       DisplayName VARCHAR(32) NOT NULL,
                       Avatar      TEXT
);

CREATE TABLE city (
                      CityID   VARCHAR(255) NOT NULL PRIMARY KEY,
                      CityName VARCHAR(255) NOT NULL
);

CREATE TABLE trip (
                      TripID    INT(4) NOT NULL PRIMARY KEY AUTO_INCREMENT,
                      OwnerID   VARCHAR(64) NOT NULL,
                      CityID    VARCHAR(255) NOT NULL,
                      StartDate TIMESTAMP NOT NULL,
                      EndDate   TIMESTAMP NOT NULL,
                      FOREIGN KEY (OwnerID) REFERENCES users(UserID) ON DELETE CASCADE,
                      FOREIGN KEY (CityID) REFERENCES `city`(CityID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE place (
                       PlaceID          VARCHAR(255) NOT NULL PRIMARY KEY,
                       NameOfPlace      VARCHAR(50),
                       LAT              DOUBLE,
                       LGT              DOUBLE,
                       Photo            TEXT,
                       TypeOfPlace      JSON,
                       FormattedAddress TEXT,
                       Descriptions     TEXT,
                       CityID           VARCHAR(255) NOT NULL,
                       OpeningHours     JSON,
                       UpdateTime       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       FOREIGN KEY (CityID) REFERENCES `city`(CityID) ON UPDATE CASCADE
);
