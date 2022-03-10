DROP DATABASE IF EXISTS uasLifecycleManagement;

CREATE DATABASE uasLifecycleManagement;

USE uasLifecycleManagement;

#Location name could be supplier names but for now has just been set to location for ease of use
CREATE TABLE Locations (
	LocationName varchar(255) NOT NULL,
    AddressLine1 varchar(255) NOT NULL,
    AddressLine2 varchar(255),
    PostCode varchar(255),
    Country varchar(255),
    PRIMARY KEY (LocationName),
    UNIQUE (LocationName)
);

#individual uas aircraft are added to this table. Identied by tail number.
CREATE TABLE Aircraft (
	TailNumber varchar(255) NOT NULL,
    LocationName varchar(255) NOT NULL,
    PlatformStatus SET("Design","Production","Operational","Repair") NOT NULL,
    PlatformType SET("Platform A","Platform B") NOT NULL,
    FlyingHours INT NOT NULL,
    HoursOperational int,
    PRIMARY KEY (TailNumber),
    UNIQUE (TailNumber),
    FOREIGN KEY (LocationName) REFERENCES Locations(LocationName)
);

#Parts types for each Platform_Are stored here. Parts for platform 1 and platform 2 have the same name but due to the size and design differences between a convential drone and
# a quadcopter its likely they are different so a platform type is stored here aswell as the part types provided.
CREATE TABLE PartTypes (
	PartID int NOT NULL AUTO_INCREMENT,
    PartType SET("Wing A","Wing B","Fuselage","Tail","Propeller","Motor","Communications Radio","Payload Electo Optical","Payload Infra-Red","Gimble","Quad Arm") NOT NULL,
	Price decimal(10,2) NOT NULL,
    Weight int NOT NULL,
    TypicalFailureTime int NOT NULL,
    PRIMARY KEY (PartID)
);

#specific parts are stored in this table. If they are linked to an aircraft a tailnumber can be used as a foreign key. Failure time isnt required as the part may not have failed yet.
CREATE TABLE Parts (
	PartNumber int NOT NULL AUTO_INCREMENT,
	PartID int NOT NULL,
    AircraftTailNumber varchar(255),
    LocationName varchar(255) NOT NULL,
    Manufacture DATETIME NOT NULL,
    FailureTime int,
    PartStatus SET("Operational","Awaiting Repair","Being Repaired","Beyond Repair") NOT NULL,
    flyTimeHours int,
    PRIMARY KEY (PartNumber),
    UNIQUE (PartNumber),
    FOREIGN KEY (AircraftTailNumber) REFERENCES Aircraft(TailNumber),
    FOREIGN KEY (LocationName) REFERENCES Locations(LocationName),
    FOREIGN KEY (PartID) REFERENCES PartTypes(PartID)
);

#unsure on exact design for repairs table as client never mentioned it but it stores the part number as a foreign key so the number of repairs and their costs can be looked up
#for a specific part
CREATE TABLE Repairs (
	RepairID int NOT NULL AUTO_INCREMENT,
    PartNumber int NOT NULL,
    cost decimal(10,2) NOT NULL,
    PRIMARY KEY(RepairID),
    FOREIGN KEY (PartNumber) REFERENCES Parts(PartNumber)
);

# Needed to store the orders for new parts.
CREATE TABLE Orders (
	OrderID INT NOT NULL AUTO_INCREMENT,
    LocationName VARCHAR(255) NOT NULL,
    TotalCost decimal(10,2) NOT NULL,
    OrderDateTime DATETIME NOT NULL,
    PRIMARY KEY(OrderID),
    FOREIGN KEY(LocationName) REFERENCES locations(LocationName)
    );
# Many orders could have many parts, so link from stock to orders.
CREATE TABLE StockToOrders (
	StockToOrderID INT NOT NULL AUTO_INCREMENT,
	OrderID INT NOT NULL,
    PartID INT NOT NULL,
    Quantity INT NOT NULL,
    PRIMARY KEY(StockToOrderID),
    FOREIGN KEY(OrderID) REFERENCES Orders(OrderID),
    FOREIGN KEY(PartID) REFERENCES parttypes(PartID)
);

CREATE TABLE ROLES(
    RoleID INT NOT NULL AUTO_INCREMENT,
    NAME TEXT NOT NULL,
    PRIMARY KEY(RoleID)
);

CREATE TABLE USERS(
    UserID INT NOT NULL AUTO_INCREMENT,
    USERNAME TEXT NOT NULL,
    EMAIL TEXT NOT NULL,
    PASSWORD TEXT NOT NULL,
    FirstName TEXT,
    LastName TEXT,
    ResetPasswordToken TEXT,
    PRIMARY KEY(UserID)
);

CREATE TABLE UserRoles(
    UserID INT,
    RoleID INT,
    FOREIGN KEY(UserID) REFERENCES USERS(UserID),
    FOREIGN KEY(RoleID) REFERENCES ROLES(RoleID)
);

# Link table for aircraft assigned to users.
CREATE TABLE Aircraft_User(
    UserID INT NOT NULL,
    TailNumber varchar(255) NOT NULL,
    FlyingHours INT NOT NULL,
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (TailNumber) REFERENCES Aircraft(TailNumber)
);

#Platform_A parts
INSERT INTO PartTypes (PartType, Price, Weight, TypicalFailureTime) VALUES ("Wing A","200","50000","600");
INSERT INTO PartTypes (PartType, Price, Weight, TypicalFailureTime) VALUES ("Wing B","250","55000","600");
INSERT INTO PartTypes (PartType, Price, Weight, TypicalFailureTime) VALUES ("Fuselage","4000","100000","1000");
INSERT INTO PartTypes (PartType, Price, Weight, TypicalFailureTime) VALUES ("Tail","2000","30000","1250");
INSERT INTO PartTypes (PartType, Price, Weight, TypicalFailureTime) VALUES ("Propeller","899.99","1000","250");
INSERT INTO PartTypes (PartType, Price, Weight, TypicalFailureTime) VALUES ("Motor","400","5500","1200");
INSERT INTO PartTypes (PartType, Price, Weight, TypicalFailureTime) VALUES ("Communications Radio","1000","600","5000");
INSERT INTO PartTypes (PartType, Price, Weight, TypicalFailureTime) VALUES ("Payload Electo Optical","1000","600","5000");
INSERT INTO PartTypes (PartType, Price, Weight, TypicalFailureTime) VALUES ("Payload Infra-Red","1000","600","4000");
INSERT INTO PartTypes (PartType, Price, Weight, TypicalFailureTime) VALUES ("Quad Arm","700","600","4000");
INSERT INTO PartTypes (PartType, Price, Weight, TypicalFailureTime) VALUES ("Gimble","250.00","3000","600");


#location examples
INSERT INTO Locations (LocationName, AddressLine1,PostCode,Country) VALUES ("Cardiff", "123 Street name","CF000AA","Wales");
INSERT INTO Locations (LocationName, AddressLine1,PostCode,Country) VALUES ("St Athen", "99 Street name","CF620AA","Wales");
INSERT INTO Locations (LocationName, AddressLine1,PostCode,Country) VALUES ("London", "123 Example road","WC2R2PP","England");
INSERT INTO Locations (LocationName, AddressLine1,PostCode,Country) VALUES ("Edinburgh", "32 Street name","EH000AA","Scotland");
INSERT INTO Locations (LocationName, AddressLine1,PostCode,Country) VALUES ("Nevada", "1 Street name","89108","USA");
INSERT INTO Locations (LocationName, AddressLine1,PostCode,Country) VALUES ("Ankara", "67 Street name","06570","Turkey");
INSERT INTO Locations (LocationName, AddressLine1,PostCode,Country) VALUES ("Dublin", "123 Street name","D000AA","Ireland");

#aircraft data examples from all locations with all aircraft statuses
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType, FlyingHours) VALUES ("G-001","St Athen","Design","Platform A", 250);
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType, FlyingHours) VALUES ("G-002","St Athen","Design","Platform B", 375);
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType, FlyingHours) VALUES ("G-003","St Athen","Production","Platform A", 200);
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType, FlyingHours) VALUES ("G-004","St Athen","Production","Platform B", 200);
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType, FlyingHours) VALUES ("G-005","St Athen","Operational","Platform A", 0);
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType, FlyingHours) VALUES ("G-006","St Athen","Operational","Platform B", 50);
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType, FlyingHours) VALUES ("G-007","St Athen","Repair","Platform A", 50);
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType, FlyingHours) VALUES ("G-008","St Athen","Repair","Platform B", 50);
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType, FlyingHours) VALUES ("G-009","Cardiff","Operational","Platform A", 400);
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType, FlyingHours) VALUES ("G-010","Cardiff","Operational","Platform B", 670);
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType, FlyingHours) VALUES ("G-011","London","Operational","Platform A", 500);
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType, FlyingHours) VALUES ("G-012","London","Operational","Platform B", 400);
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType, FlyingHours) VALUES ("G-013","Edinburgh","Operational","Platform A", 300);
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType, FlyingHours) VALUES ("G-014","Edinburgh","Operational","Platform B", 675);
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType, FlyingHours) VALUES ("G-015","Nevada","Operational","Platform A", 75);
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType, FlyingHours) VALUES ("G-016","Nevada","Operational","Platform B", 25);
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType, FlyingHours) VALUES ("G-017","Ankara","Operational","Platform A", 800);
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType, FlyingHours) VALUES ("G-018","Ankara","Operational","Platform B", 230);
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType, FlyingHours) VALUES ("G-019","Dublin","Operational","Platform A", 240);
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType, FlyingHours) VALUES ("G-020","Dublin","Operational","Platform B", 150);

#parts that have been assigned to aircrafts
INSERT INTO Parts (PartID, AircraftTailNumber,LocationName,Manufacture,PartStatus) VALUES ("1","G-001","St Athen","2022-02-09 00:00:00","Operational");
INSERT INTO Parts (PartID, AircraftTailNumber,LocationName,Manufacture,PartStatus) VALUES ("2","G-001","St Athen","2022-02-09 00:00:00","Operational");
INSERT INTO Parts (PartID, AircraftTailNumber,LocationName,Manufacture,PartStatus) VALUES ("3","G-001","St Athen","2022-02-09 00:00:00","Operational");
INSERT INTO Parts (PartID, AircraftTailNumber,LocationName,Manufacture,PartStatus) VALUES ("4","G-001","St Athen","2022-02-09 00:00:00","Operational");
INSERT INTO Parts (PartID, AircraftTailNumber,LocationName,Manufacture,PartStatus) VALUES ("5","G-001","St Athen","2022-02-09 00:00:00","Operational");
INSERT INTO Parts (PartID, AircraftTailNumber,LocationName,Manufacture,PartStatus) VALUES ("6","G-001","St Athen","2022-02-09 00:00:00","Operational");
INSERT INTO Parts (PartID, AircraftTailNumber,LocationName,Manufacture,PartStatus) VALUES ("7","G-001","St Athen","2022-02-09 00:00:00","Operational");
INSERT INTO Parts (PartID, AircraftTailNumber,LocationName,Manufacture,PartStatus) VALUES ("8","G-001","St Athen","2022-02-09 00:00:00","Operational");
INSERT INTO Parts (PartID, AircraftTailNumber,LocationName,Manufacture,PartStatus) VALUES ("9","G-001","St Athen","2022-02-09 00:00:00","Operational");
INSERT INTO Parts (PartID, AircraftTailNumber,LocationName,Manufacture,PartStatus) VALUES ("11","G-001","St Athen","2022-02-09 00:00:00","Operational");
INSERT INTO Parts (PartID, AircraftTailNumber,LocationName,Manufacture,PartStatus) VALUES ("10","G-002","St Athen","2022-02-09 00:00:00","Operational");
INSERT INTO Parts (PartID, AircraftTailNumber,LocationName,Manufacture,PartStatus) VALUES ("3","G-002","St Athen","2022-02-09 00:00:00","Operational");
INSERT INTO Parts (PartID, AircraftTailNumber,LocationName,Manufacture,PartStatus) VALUES ("4","G-002","St Athen","2022-02-09 00:00:00","Operational");
INSERT INTO Parts (PartID, AircraftTailNumber,LocationName,Manufacture,PartStatus) VALUES ("5","G-002","St Athen","2022-02-09 00:00:00","Operational");
INSERT INTO Parts (PartID, AircraftTailNumber,LocationName,Manufacture,PartStatus) VALUES ("6","G-002","St Athen","2022-02-09 00:00:00","Operational");
INSERT INTO Parts (PartID, AircraftTailNumber,LocationName,Manufacture,PartStatus) VALUES ("7","G-002","St Athen","2022-02-09 00:00:00","Operational");
INSERT INTO Parts (PartID, AircraftTailNumber,LocationName,Manufacture,PartStatus) VALUES ("8","G-002","St Athen","2022-02-09 00:00:00","Operational");
INSERT INTO Parts (PartID, AircraftTailNumber,LocationName,Manufacture,PartStatus) VALUES ("9","G-002","St Athen","2022-02-09 00:00:00","Operational");



#examples data for parts that haven't been assigned to aircraft yet
INSERT INTO Parts (PartID, LocationName,Manufacture,PartStatus) VALUES ("5","St Athen","2022-02-09 00:00:00","Operational");
INSERT INTO Parts (PartID, LocationName,Manufacture,PartStatus) VALUES ("5","St Athen","2022-02-09 00:00:00","Awaiting Repair");
INSERT INTO Parts (PartID, LocationName,Manufacture,PartStatus) VALUES ("5","St Athen","2022-02-09 00:00:00","Being Repaired");
INSERT INTO Parts (PartID, LocationName,Manufacture,PartStatus) VALUES ("5","St Athen","2022-02-09 00:00:00","Beyond Repair");
INSERT INTO Parts (PartID, LocationName,Manufacture,PartStatus) VALUES ("6","St Athen","2022-02-09 00:00:00","Operational");
INSERT INTO Parts (PartID, LocationName,Manufacture,PartStatus) VALUES ("6","St Athen","2022-02-09 00:00:00","Awaiting Repair");
INSERT INTO Parts (PartID, LocationName,Manufacture,PartStatus) VALUES ("6","St Athen","2022-02-09 00:00:00","Being Repaired");
INSERT INTO Parts (PartID, LocationName,Manufacture,PartStatus) VALUES ("6","St Athen","2022-02-09 00:00:00","Beyond Repair");
INSERT INTO Parts (PartID, LocationName,Manufacture,PartStatus) VALUES ("7","St Athen","2022-02-09 00:00:00","Operational");
INSERT INTO Parts (PartID, LocationName,Manufacture,PartStatus) VALUES ("7","St Athen","2022-02-09 00:00:00","Awaiting Repair");
INSERT INTO Parts (PartID, LocationName,Manufacture,PartStatus) VALUES ("7","St Athen","2022-02-09 00:00:00","Being Repaired");
INSERT INTO Parts (PartID, LocationName,Manufacture,PartStatus) VALUES ("7","St Athen","2022-02-09 00:00:00","Beyond Repair");
INSERT INTO Parts (PartID, LocationName,Manufacture,PartStatus) VALUES ("7","Cardiff","2022-02-09 00:00:00","Beyond Repair");
INSERT INTO Parts (PartID, LocationName,Manufacture,PartStatus) VALUES ("7","Cardiff","2022-02-09 00:00:00","Beyond Repair");

#Only a small number of repair examples as I am unsure if the table will change
INSERT INTO Repairs (PartNumber, cost) VALUES ("1","200");
INSERT INTO Repairs (PartNumber, cost) VALUES ("1","16.99");
INSERT INTO Repairs (PartNumber, cost) VALUES ("1","12");
INSERT INTO Repairs (PartNumber, cost) VALUES ("2","100");
INSERT INTO Repairs (PartNumber, cost) VALUES ("3","50");
INSERT INTO Repairs (PartNumber, cost) VALUES ("4","12");


#USER roles
INSERT INTO ROLES (roleid, NAME) VALUES ("1", "ROLE_USER_LOGISTIC");
INSERT INTO ROLES (roleid, NAME) VALUES ("2", "ROLE_USER_CTO");
INSERT INTO ROLES (roleid, NAME) VALUES ("3", "ROLE_USER");

#Users
INSERT INTO `users` (`userid`, `email`, `firstName`, `lastName`, `password`, `resetPasswordToken`, `username`) VALUES ('1', 'logisticOne@snc.ac.uk', 'Logistic', 'One', '$2a$10$X1KqzKsRpkhXIfFPE1GJ5eqgE2VH/UJx8l0M.2QF4w6hmsbROCol.', '4ed60a87-d858-4757-a10d-f7e97d23ee61', 'logisticOne@snc.ac.uk');
INSERT INTO `users` (`userid`, `email`, `firstName`, `lastName`, `password`, `resetPasswordToken`, `username`) VALUES ('2', 'userOne@snc.ac.uk', 'Thomas', 'Anderson', '$2a$10$X1KqzKsRpkhXIfFPE1GJ5eqgE2VH/UJx8l0M.2QF4w6hmsbROCol.', '4ed60a87-d858-4757-a10d-f7e97d23ee61', 'userOne@snc.ac.uk');

#User roles
INSERT INTO `UserRoles` (`userid`, `roleid`) VALUES ('1', '1');
INSERT INTO `UserRoles` (`userid`, `roleid`) VALUES ('2', '3');

#User assignr Aircraft
INSERT INTO aircraft_user (UserID, TailNumber, FlyingHours) VALUES (2, "G-001", 250);
INSERT INTO aircraft_user (UserID, TailNumber, FlyingHours) VALUES (2, "G-002", 175);
