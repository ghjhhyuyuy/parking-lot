CREATE TABLE IF NOT EXISTS `storage`
(
    `id`                varchar(36) NOT NULL,
    `zone`              varchar(36) NOT NULL,
    `address`           varchar(36) NOT NULL,
    `car_id`            varchar(36),
    `parking_id`        varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `cid` FOREIGN KEY (`car_id`) REFERENCES `car` (`id`),
    CONSTRAINT `pid` FOREIGN KEY (`parking_id`) REFERENCES `parking` (`id`)
);
