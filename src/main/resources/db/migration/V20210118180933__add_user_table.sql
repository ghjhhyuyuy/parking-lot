CREATE TABLE IF NOT EXISTS `user`
(
    `id`                varchar(36) NOT NULL,
    `name`              varchar(36) NOT NULL,
    `role`              varchar(36) NOT NULL,
    `create_date`       varchar(36) NOT NULL,
    `remove_date`       varchar(36) DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `r` FOREIGN KEY (`role`) REFERENCES `role` (`id`)
);