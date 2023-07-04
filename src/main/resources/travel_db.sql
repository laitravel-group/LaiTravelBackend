-- Drop database travel_db 
CREATE DATABASE IF NOT EXISTS travel_db DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
USE travel_db;



CREATE TABLE IF NOT EXISTS User (
UserID VARCHAR (64)  NOT NULL DEFAULT ' ',
    -- eg: zorainus@gmail.com
Password VARCHAR(32) NOT NULL DEFAULT ' ' ,
    -- eg: 123456
DisplayName VARCHAR(32) NOT NULL DEFAULT ' ' ,
    -- eg: RanZhang
Avatar TEXT NOT NULL,
    -- eg: http.XXXXXX
PRIMARY KEY(`UserID`))ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE IF NOT EXISTS City (
CityID VARCHAR (255)  NOT NULL DEFAULT ' ',
    -- eg: XXXXXXX
CityName VARCHAR(255) NOT NULL DEFAULT ' ' ,
    -- eg: Boston
PRIMARY KEY(`CityID`))ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS Trip (
TripID int(4) NOT NULL AUTO_INCREMENT,
    -- eg: 1
OwnerID VARCHAR (64)  NOT NULL DEFAULT ' ',
    -- eg: zorainus@gmail.com
CityID VARCHAR(255) NOT NULL DEFAULT ' ' ,
    -- eg: 2023-06-23
StartDate timestamp NOT NULL,
    -- eg: 2023-06-23-7:30:00
EndDate timestamp NOT NULL,
    -- eg: 2023-07-01-19:00:00
Details TEXT NOT NULL,
    -- eg: XXXXXXXXXXXXXXXXXXXXX
PRIMARY KEY (`TripID`),
FOREIGN KEY (OwnerID) REFERENCES User(UserID),
FOREIGN KEY (CityID) REFERENCES City(CityID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE IF NOT EXISTS Place (
PlaceID VARCHAR (255)  NOT NULL DEFAULT ' ',
    -- eg: zorainus@gmail.com
NameofPlace VARCHAR(50) NOT NULL,
    -- eg: MIT
LAT double NOT NULL DEFAULT 0,
    -- eg: 23.50000 
LGT double NOT NULL DEFAULT 0,
    -- eg: 352.60000   
Photo TEXT NOT NULL DEFAULT ' ' ,
    -- eg: http:xxxxx
TypeOfPlace VARCHAR(32) NOT NULL DEFAULT ' ' ,
    -- eg: School
FormattedAddress TEXT NOT NULL,
    -- eg: 77 Massachusetts Ave, Cambridge, MA 02139, USA
Descriptions TEXT NOT NULL,
    -- eg: XXXXXXXXXXXXXXXXXXXXX
CityID VARCHAR(255) NOT NULL DEFAULT ' ' ,
    -- eg: XXXXXXXXXXX
OpenningHours VARCHAR(32) NOT NULL DEFAULT ' ' ,
    -- eg: XXXXXXXXXXXXXXXXXXXXX
Updatetime timestamp default current_timestamp on update current_timestamp,
PRIMARY KEY (`PlaceID`),
FOREIGN KEY (CityID) REFERENCES City(CityID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;






