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
    PlatformStatus SET("Design","Production","Operation","Repair") NOT NULL,
    PlatformType SET("Platform A","Platform B") NOT NULL,
    PRIMARY KEY (TailNumber),
    UNIQUE (TailNumber),
    FOREIGN KEY (LocationName) REFERENCES Locations(LocationName)
);

#Parts types for each platform are stored here. Parts for platform 1 and platform 2 have the same name but due to the size and design differences between a convential drone and
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
    PRIMARY KEY (PartNumber),
    UNIQUE (PartNumber),
    FOREIGN KEY (AircraftTailNumber) REFERENCES Aircraft(TailNumber),
    FOREIGN KEY (LocationName) REFERENCES Locations(LocationName),
    FOREIGN KEY (PartID) REFERENCES PartTypes(PartID)
);

#name may not need to be stored but this would need to be clarified with the client.
CREATE TABLE Users (
	UserID int NOT NULL AUTO_INCREMENT,
    FirstName varchar(255) NOT NULL,
    Surname varchar(255) NOT NULL,
    UserPassword varchar(255) NOT NULL,
    Email varchar(255) NOT NULL,
    UserRole SET ("CEO","COO","CTO","Logistics Officer") NOT NULL,
    PRIMARY KEY (UserID)
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

#platform a parts
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
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType) VALUES ("G-001","St Athen","Design","Platform A");
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType) VALUES ("G-002","St Athen","Design","Platform B");
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType) VALUES ("G-003","St Athen","Production","Platform A");
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType) VALUES ("G-004","St Athen","Production","Platform B");
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType) VALUES ("G-005","St Athen","Operation","Platform A");
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType) VALUES ("G-006","St Athen","Operation","Platform B");
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType) VALUES ("G-007","St Athen","Repair","Platform A");
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType) VALUES ("G-008","St Athen","Repair","Platform B");
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType) VALUES ("G-009","Cardiff","Operation","Platform A");
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType) VALUES ("G-010","Cardiff","Operation","Platform B");
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType) VALUES ("G-011","London","Operation","Platform A");
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType) VALUES ("G-012","London","Operation","Platform B");
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType) VALUES ("G-013","Edinburgh","Operation","Platform A");
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType) VALUES ("G-014","Edinburgh","Operation","Platform B");
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType) VALUES ("G-015","Nevada","Operation","Platform A");
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType) VALUES ("G-016","Nevada","Operation","Platform B");
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType) VALUES ("G-017","Ankara","Operation","Platform A");
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType) VALUES ("G-018","Ankara","Operation","Platform B");
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType) VALUES ("G-019","Dublin","Operation","Platform A");
INSERT INTO Aircraft (TailNumber, LocationName, PlatformStatus,PlatformType) VALUES ("G-020","Dublin","Operation","Platform B");

#4 users with the individual user roles
INSERT INTO Users (FirstName, Surname, UserPassword, Email, UserRole) VALUES ("John","Smith","password","email@email.com","CEO");
INSERT INTO Users (FirstName, Surname, UserPassword, Email, UserRole) VALUES ("Tim","Smith","password","email1@email.com","COO");
INSERT INTO Users (FirstName, Surname, UserPassword, Email, UserRole) VALUES ("Bob","Smith","password","email2@email.com","CTO");
INSERT INTO Users (FirstName, Surname, UserPassword, Email, UserRole) VALUES ("Lisa","Smith","password","email3@email.com","Logistics Officer");

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




