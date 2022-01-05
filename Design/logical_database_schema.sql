
-- db name: telcoservicedb

CREATE TABLE `user` (
    `id` int(11) NOT NULL AUTO_INCREMENT,

    `username` varchar(45) NOT NULL,
    `email` varchar(45) NOT NULL,
    `password` varchar(45) NOT NULL,
    `insolventFlag` bit default 0,
    `numFailedPayments` int default 0,

    PRIMARY KEY (`id`),
    UNIQUE KEY `username_UNIQUE` (`username`)
);

CREATE TABLE `auditing` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `userId` INT(11) NOT NULL,
    `username` VARCHAR(45) NOT NULL,
    `email` VARCHAR(45) NOT NULL,
    `amount` DECIMAL(9 , 2 ) NOT NULL,
    `lastRejectionTime` DATETIME NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_auditing_userid` FOREIGN KEY (`userId`)
        REFERENCES `user` (`id`)
);

CREATE TABLE `optProduct` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(45) NOT NULL,
    `monthlyFee` decimal(9,2) NOT NULL,

    PRIMARY KEY (`id`),
    UNIQUE KEY `prodname_UNIQUE` (`name`)
);

CREATE TABLE `employee` (
    `id` int(11) NOT NULL AUTO_INCREMENT,

    `username` varchar(45) NOT NULL,
    `password` varchar(45) NOT NULL,

    PRIMARY KEY (`id`)
);

CREATE TABLE `servicePkg` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(45) NOT NULL,

    PRIMARY KEY (`id`),
    UNIQUE KEY `spname_UNIQUE` (`name`)
);

CREATE TABLE `validityPeriod` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `monthDuration` int(11) NOT NULL,
    `price` decimal(9,2)  NOT NULL,
    `servicePkgId` int(11),

    PRIMARY KEY (`id`),
    CONSTRAINT `fk_validityPeriod_servicePkgId` FOREIGN KEY (`servicePkgId`) REFERENCES `servicePkg`(`id`)
);

CREATE TABLE `service` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(45) NOT NULL,
    `includedMin` int(11),
    `includedSMS` int(11),
    `includedGB` int(11),
    `extraMinFee` decimal(9,2),
    `extraSMSFee` decimal(9,2),
    `extraGBFee` decimal(9,2),


    PRIMARY KEY (`id`),
    UNIQUE KEY `srvname_UNIQUE` (`name`)
);

CREATE TABLE `orders` (
    `id` int(11) NOT NULL AUTO_INCREMENT,

    `userId` int(11),
    `servicePkgId` int(11),

    `dateOfCreation` date NOT NULL, 
    `hourOfCreation` int NOT NULL,


    `validityPeriodId` int(11) NOT NULL,

    `totalValue` decimal(9,2) NOT NULL,
    `startDate` date NOT NULL,
    `validityFlag` bit NOT NULL,
    `rejectedFlag` bit NOT NULL,

    PRIMARY KEY (`id`),
    -- foreign key
    CONSTRAINT `fk_order_userid` FOREIGN KEY (`userId`) REFERENCES `user`(`id`),
    CONSTRAINT `fk_order_servicePkgId` FOREIGN KEY (`servicePkgId`) REFERENCES `servicePkg`(`id`),
    CONSTRAINT `fk_order_validityPeriodId` FOREIGN KEY (`validityPeriodId`) REFERENCES `validityPeriod`(`id`)
);

CREATE TABLE `activationSchedule` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `dateOfActivation` date NOT NULL,
    `dateOfDeactivation` date NOT NULL,
    `orderId` int(11) NOT NULL,

    PRIMARY KEY (`id`),
    CONSTRAINT `fk_activationSchedule_orderId` FOREIGN KEY (`orderId`) REFERENCES `orders`(`id`)
);

-- relationships
CREATE TABLE `servicesInPkg` (
    `servicePkgId` int(11) NOT NULL,
    `serviceId` int(11) NOT NULL,

    PRIMARY KEY (`servicePkgId`, `serviceId`),
    CONSTRAINT `fk_servicesInPkg_servicePkgId` FOREIGN KEY (`servicePkgId`) REFERENCES `servicePkg`(`id`),
    CONSTRAINT `fk_servicesInPkg_serviceId` FOREIGN KEY (`serviceId`) REFERENCES `service`(`id`)
);

CREATE TABLE `optProdInPkg` (
    `servicePkgId` int(11) NOT NULL,
    `optProdId` int(11) NOT NULL,

    PRIMARY KEY (`servicePkgId`, `optProdId`),
    CONSTRAINT `fk_optProdInPkg_servicePkgId` FOREIGN KEY (`servicePkgId`) REFERENCES `servicePkg`(`id`),
    CONSTRAINT `fk_optProdInPkg_optProdId` FOREIGN KEY (`optProdId`) REFERENCES `optProduct`(`id`)
);

CREATE TABLE `chosenOptProd` (
    `orderId` INT(11) NOT NULL,
    `optProdId` INT(11) NOT NULL,
    PRIMARY KEY (`orderId` , `optProdId`),
    CONSTRAINT `fk_chosenOptProd_servicePkgId` FOREIGN KEY (`orderId`)
        REFERENCES `orders` (`id`),
    CONSTRAINT `fk_chosenOptProd_optProdId` FOREIGN KEY (`optProdId`)
        REFERENCES `optProduct` (`id`)
);

