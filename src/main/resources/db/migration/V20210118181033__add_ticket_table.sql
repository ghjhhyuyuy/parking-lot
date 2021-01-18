CREATE TABLE IF NOT EXISTS `ticket`
(
    `id`                varchar(36) NOT NULL,
    `timeout_date`      varchar(36) NOT NULL,
    `parking_lot_id`    varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `pid` FOREIGN KEY (`parking_lot_id`) REFERENCES `parking` (`id`)
);
