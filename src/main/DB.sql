CREATE DATABASE `project_Y`;
USE `project_Y`;
SHOW DATABASES;
DROP TABLE `member`;
CREATE TABLE MEMBER (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        oauthId VARCHAR(100) NOT NULL UNIQUE,
                        nickName VARCHAR(100),
                        email VARCHAR(100)
);