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
	PartTypeID int NOT NULL AUTO_INCREMENT,
    PartTypeName SET("Wing A","Wing B","Fuselage","Tail","Propeller","Motor","Communications Radio","Payload Electo Optical","Payload Infra-Red","Gimble","Quad Arm") NOT NULL,
    PRIMARY KEY (PartTypeID)
);

#specific parts are stored in this table. If they are linked to an aircraft a tailnumber can be used as a foreign key. Failure time isnt required as the part may not have failed yet.
CREATE TABLE Parts (
	PartID int NOT NULL AUTO_INCREMENT,
	PartTypeID int NOT NULL,
    PartName TEXT NOT NULL,
    Manufacture DATETIME NOT NULL,
	Price decimal(10,2) NOT NULL,
    Weight int NOT NULL,
    TypicalFailureTime int NOT NULL,
    PRIMARY KEY (PartID),
    UNIQUE (PartID),
    FOREIGN KEY (PartTypeID) REFERENCES PartTypes(PartTypeID)
);

CREATE TABLE Platforms (
	PlatformID int NOT NULL AUTO_INCREMENT,
    PlatformType varchar(255) NOT NULL,
    PRIMARY KEY(PlatformID)
);

CREATE TABLE PlatformParts (
	PlatformPartID int NOT NULL AUTO_INCREMENT,
    PlatformID int NOT NULL,
    PartTypeID int NOT NULL,
    PRIMARY KEY(PlatformPartId),
    FOREIGN KEY (PlatformID) REFERENCES Platforms(PlatformID),
    FOREIGN KEY (PartTypeID) REFERENCES PartTypes(PartTypeID)
);

CREATE TABLE AircraftPart (
	AircraftPartID int NOT NULL AUTO_INCREMENT,
    AircraftTailNumber varchar(255) NOT NULL,
    PartID INT (11),
	PartStatus SET("Operational","Awaiting Repair","Being Repaired","Beyond Repair") NOT NULL,
    FlightHours INT (11),
    PRIMARY KEY(AircraftPartId),
    FOREIGN KEY (AircraftTailNumber) REFERENCES Aircraft(TailNumber),
    FOREIGN KEY (PartID) REFERENCES Parts(PartID)
);

#unsure on exact design for repairs table as client never mentioned it but it stores the part number as a foreign key so the number of repairs and their costs can be looked up
#for a specific part
CREATE TABLE Repairs (
	RepairID int NOT NULL AUTO_INCREMENT,
    AircraftPartID int NOT NULL,
    cost decimal(10,2) NOT NULL,
    PRIMARY KEY(RepairID),
    FOREIGN KEY (AircraftPartID) REFERENCES AircraftPart(AircraftPartID)
);

CREATE TABLE Stock (
	StockID int NOT NULL AUTO_INCREMENT,
    PartID int NOT NULL,
    StockQuantity INT (11),
	LocationName varchar(255) NOT NULL,
    PRIMARY KEY(StockID),
    FOREIGN KEY (PartID) REFERENCES Parts(PartID),
    FOREIGN KEY (LocationName) REFERENCES Aircraft(LocationName)
);


# Needed to store the orders for new parts.
CREATE TABLE Orders (
	OrderID INT NOT NULL AUTO_INCREMENT,
    LocationName VARCHAR(255) NOT NULL,
    SupplierEmail VARCHAR(255) NOT NULL,
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
    FOREIGN KEY(PartID) REFERENCES parts(PartID)
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
INSERT INTO PartTypes (PartTypeName) VALUES ("Wing A");
INSERT INTO PartTypes (PartTypeName) VALUES ("Wing B");
INSERT INTO PartTypes (PartTypeName) VALUES ("Fuselage");
INSERT INTO PartTypes (PartTypeName) VALUES ("Tail");
INSERT INTO PartTypes (PartTypeName) VALUES ("Propeller");
INSERT INTO PartTypes (PartTypeName) VALUES ("Motor");
INSERT INTO PartTypes (PartTypeName) VALUES ("Communications Radio");
INSERT INTO PartTypes (PartTypeName) VALUES ("Payload Electo Optical");
INSERT INTO PartTypes (PartTypeName) VALUES ("Payload Infra-Red");
INSERT INTO PartTypes (PartTypeName) VALUES ("Quad Arm");
INSERT INTO PartTypes (PartTypeName) VALUES ("Gimble");


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

# Example parts.
INSERT INTO Parts (PartTypeID, PartName, Manufacture,Price,Weight,TypicalFailureTime) VALUES ("1","Boeing Wing A", "2022-02-09 00:00:00","200","50000","600");
INSERT INTO Parts (PartTypeID, PartName, Manufacture,Price,Weight,TypicalFailureTime) VALUES ("2","Boeing Wing B","2022-02-09 00:00:00","250","55000","600");
INSERT INTO Parts (PartTypeID, PartName, Manufacture,Price,Weight,TypicalFailureTime) VALUES ("3","Boeing Fuselage","2022-02-09 00:00:00","4000","100000","1000");
INSERT INTO Parts (PartTypeID, PartName, Manufacture,Price,Weight,TypicalFailureTime) VALUES ("4","Boeing Tail","2022-02-09 00:00:00","2000","30000","1250");
INSERT INTO Parts (PartTypeID, PartName, Manufacture,Price,Weight,TypicalFailureTime) VALUES ("5","Boeing Propeller","2022-02-09 00:00:00","899.99","1000","250");
INSERT INTO Parts (PartTypeID, PartName, Manufacture,Price,Weight,TypicalFailureTime) VALUES ("6","Boeing Motor","2022-02-09 00:00:00","400","5500","1200");
INSERT INTO Parts (PartTypeID, PartName, Manufacture,Price,Weight,TypicalFailureTime) VALUES ("7","Boeing Communications Radio","2022-02-09 00:00:00","1000","600","5000");
INSERT INTO Parts (PartTypeID, PartName, Manufacture,Price,Weight,TypicalFailureTime) VALUES ("8","Boeing Payload Electo Optical","2022-02-09 00:00:00","1000","600","5000");
INSERT INTO Parts (PartTypeID, PartName, Manufacture,Price,Weight,TypicalFailureTime) VALUES ("9","Boeing Payload Infra-Red","2022-02-09 00:00:00","1000","600","4000");
INSERT INTO Parts (PartTypeID, PartName, Manufacture,Price,Weight,TypicalFailureTime) VALUES ("11","Boeing Gimble","2022-02-09 00:00:00","700","600","4000");
INSERT INTO Parts (PartTypeID, PartName, Manufacture,Price,Weight,TypicalFailureTime) VALUES ("10","Boeing Quad Arm","2022-02-09 00:00:00","250.00","3000","600");

#examples data for parts that haven't been assigned to aircraft yet (stock)
INSERT INTO Stock (PartID, StockQuantity, LocationName) VALUES ("1", "10","Cardiff");
INSERT INTO Stock (PartID, StockQuantity, LocationName) VALUES ("1", "10","London");
INSERT INTO Stock (PartID, StockQuantity, LocationName) VALUES ("2", "1","London");
INSERT INTO Stock (PartID, StockQuantity, LocationName) VALUES ("3", "55","London");

INSERT INTO Platforms (PlatformType) VALUES ("Platform A");
INSERT INTO Platforms (PlatformType) VALUES ("Platform B");

INSERT INTO PlatformParts (PlatformID, PartTypeID) VALUES ("1", "1");
INSERT INTO PlatformParts (PlatformID, PartTypeID) VALUES ("2", "1");
INSERT INTO PlatformParts (PlatformID, PartTypeID) VALUES ("1", "2");
INSERT INTO PlatformParts (PlatformID, PartTypeID) VALUES ("2", "2");
INSERT INTO PlatformParts (PlatformID, PartTypeID) VALUES ("2", "3");
INSERT INTO PlatformParts (PlatformID, PartTypeID) VALUES ("1", "4");

INSERT INTO AircraftPart (AircraftTailNumber, PartID, PartStatus, FlightHours) VALUES ("G-001", "1", "Operational", 0);
INSERT INTO AircraftPart (AircraftTailNumber, PartID, PartStatus, FlightHours) VALUES ("G-001", "2", "Operational", 0);
INSERT INTO AircraftPart (AircraftTailNumber, PartID, PartStatus, FlightHours) VALUES ("G-001", "3", "Operational", 0);
INSERT INTO AircraftPart (AircraftTailNumber, PartID, PartStatus, FlightHours) VALUES ("G-001", "4", "Operational", 0);

#Only a small number of repair examples as I am unsure if the table will change
INSERT INTO Repairs (AircraftPartID, cost) VALUES ("1","200");
INSERT INTO Repairs (AircraftPartID, cost) VALUES ("1","16.99");
INSERT INTO Repairs (AircraftPartID, cost) VALUES ("1","12");
INSERT INTO Repairs (AircraftPartID, cost) VALUES ("2","100");
INSERT INTO Repairs (AircraftPartID, cost) VALUES ("3","50");
INSERT INTO Repairs (AircraftPartID, cost) VALUES ("4","12");

#USER roles
INSERT INTO ROLES (roleid, NAME) VALUES ("1", "ROLE_USER_LOGISTIC");
INSERT INTO ROLES (roleid, NAME) VALUES ("2", "ROLE_USER_CTO");
INSERT INTO ROLES (roleid, NAME) VALUES ("3", "ROLE_USER");
INSERT INTO ROLES (roleid, NAME) VALUES ("4", "ROLE_USER_CEO");
INSERT INTO ROLES (roleid, NAME) VALUES ("5", "ROLE_USER_COO");

#Users
INSERT INTO `users` (`userid`, `email`, `firstName`, `lastName`, `password`, `resetPasswordToken`, `username`) VALUES ('1', 'logisticOne@snc.ac.uk', 'Logistic', 'One', '$2a$10$X1KqzKsRpkhXIfFPE1GJ5eqgE2VH/UJx8l0M.2QF4w6hmsbROCol.', '4ed60a87-d858-4757-a10d-f7e97d23ee61', 'logisticOne@snc.ac.uk');
INSERT INTO `users` (`userid`, `email`, `firstName`, `lastName`, `password`, `resetPasswordToken`, `username`) VALUES ('2', 'userOne@snc.ac.uk', 'Thomas', 'Anderson', '$2a$10$X1KqzKsRpkhXIfFPE1GJ5eqgE2VH/UJx8l0M.2QF4w6hmsbROCol.', '4ed60a87-d858-4757-a10d-f7e97d23ee61', 'userOne@snc.ac.uk');
# The password is password
INSERT INTO `users` (`userid`, `email`, `firstName`, `lastName`, `password`, `resetPasswordToken`, `username`) VALUES ('3', 'ceo@test.com', 'Ceo', 'Test', '$2a$10$4OJsYCo6zbcLTTPSmzpXIuoBMtZoilpF9L/FEknvvbRZovGE6PEfG', '26c07eb1-67ae-410d-83bf-1c4184b082ff', 'ceo@test.com');
# The password is password
INSERT INTO `users` (`userid`, `email`, `firstName`, `lastName`, `password`, `resetPasswordToken`, `username`) VALUES ('4', 'coo@test.com', 'Coo', 'Test', '$2a$10$nvKlA2dK94Fe65bPaZsj9.2Fm0VWrKBSx4i87AvWG9/.nuoB7.vrS', 'e93a7072-d2c1-40ca-a2b0-fa4a5293c83a', 'coo@test.com');
# The password is password
INSERT INTO `users` (`userid`, `email`, `firstName`, `lastName`, `password`, `resetPasswordToken`, `username`) VALUES ('5', 'cto@test.com', 'Cto', 'Test', '$2a$10$QQwaVmjidMm1ZpGRRN/Y1Og.5pkn/qBHCO8JUijMqNTDDlIAhrYk6', 'dbb690e3-5d1b-43e1-8848-73083a5254fd', 'cto@test.com');
# The password is password
INSERT INTO `users` (`userid`, `email`, `firstName`, `lastName`, `password`, `resetPasswordToken`, `username`) VALUES ('6', 'user@test.com', 'User', 'Test', '$2a$10$NsqO3x5Zbueg3P1NFph52Oz.efibatA/GaMj.qr7XfblVEbflL96y', '4b289ec8-24a4-4a7f-a281-289d4d6fa124', 'user@test.com');
# The password is password
INSERT INTO `users` (`userid`, `email`, `firstName`, `lastName`, `password`, `resetPasswordToken`, `username`) VALUES ('7', 'logistic@test.com', 'Logistic', 'Test', '$2a$10$.EhAuFGWPy5Q1B/rRt9/5e3f18D8zDwQiiGoKxTSNUh3Dfhc6xqbW', '6a036834-9015-40b3-9016-51b7c26de2c3', 'logistic@test.com');

#User roles
INSERT INTO `UserRoles` (`userid`, `roleid`) VALUES ('1', '1');
INSERT INTO `UserRoles` (`userid`, `roleid`) VALUES ('2', '3');
INSERT INTO `UserRoles` (`userid`, `roleid`) VALUES ('3', '4');
INSERT INTO `UserRoles` (`userid`, `roleid`) VALUES ('4', '5');
INSERT INTO `UserRoles` (`userid`, `roleid`) VALUES ('5', '2');
INSERT INTO `UserRoles` (`userid`, `roleid`) VALUES ('6', '3');
INSERT INTO `UserRoles` (`userid`, `roleid`) VALUES ('7', '1');


#User assign Aircraft
INSERT INTO aircraft_user (UserID, TailNumber, FlyingHours) VALUES (2, "G-001", 250);
INSERT INTO aircraft_user (UserID, TailNumber, FlyingHours) VALUES (2, "G-002", 175);
