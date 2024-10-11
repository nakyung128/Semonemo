-- MySQL dump 10.13  Distrib 8.0.21, for Win64 (x86_64)
--
-- Host: 43.201.33.160    Database: semonemo
-- ------------------------------------------------------
-- Server version	8.0.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `asset_image`
--

DROP TABLE IF EXISTS `asset_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asset_image` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `creator` bigint NOT NULL,
  `image_url` varchar(500) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `asset_image_users_id_fk` (`creator`),
  CONSTRAINT `asset_image_users_id_fk` FOREIGN KEY (`creator`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asset_image`
--

LOCK TABLES `asset_image` WRITE;
/*!40000 ALTER TABLE `asset_image` DISABLE KEYS */;
INSERT INTO `asset_image` VALUES (1,1,'https://semonemo.s3.amazonaws.com/picture/20f04fc5-ddee-45d6-b509-7291d152d76f_image_1728544755747.png','2024-10-10 16:19:17'),(2,4,'https://semonemo.s3.amazonaws.com/picture/5f44565a-ee8f-4b75-b3de-799e3b078b35_image_1728545517777.png','2024-10-10 16:31:59'),(3,4,'https://semonemo.s3.amazonaws.com/picture/4377e3bc-93a8-4293-9d04-840fb675e8c9_image_1728545534616.png','2024-10-10 16:32:15'),(4,4,'https://semonemo.s3.amazonaws.com/picture/91fa6156-cc9e-41d7-9c9b-179dfff4dcaf_image_1728545552235.png','2024-10-10 16:32:33'),(5,4,'https://semonemo.s3.amazonaws.com/picture/32aab14b-87f2-4ae2-a096-9e9ab80f7b0a_image_1728545667588.png','2024-10-10 16:34:29'),(6,5,'https://semonemo.s3.amazonaws.com/picture/a8fe1d61-56d8-4edf-be8f-2cd165af6fb2_image_1728545869645.png','2024-10-10 16:37:51'),(7,4,'https://semonemo.s3.amazonaws.com/picture/ff204254-ea45-412f-a79d-c27b7351fc45_image_1728545935003.png','2024-10-10 16:38:56'),(8,5,'https://semonemo.s3.amazonaws.com/picture/d98e789a-bc37-4409-8795-7c2e83ffe000_image_1728546005526.png','2024-10-10 16:40:07'),(9,6,'https://semonemo.s3.amazonaws.com/picture/29d22287-3728-48e2-87f0-343b628f461d_image_1728546040805.png','2024-10-10 16:40:41'),(10,5,'https://semonemo.s3.amazonaws.com/picture/755a300b-0520-4e73-9f10-4782ede8e02c_image_1728546078711.png','2024-10-10 16:41:20'),(11,5,'https://semonemo.s3.amazonaws.com/picture/85a48db6-5fed-42ca-a3f2-b202445b2052_image_1728546179554.png','2024-10-10 16:43:01'),(12,6,'https://semonemo.s3.amazonaws.com/picture/d90dfc12-15eb-499a-86bb-da3942f5b1bc_image_1728546266276.png','2024-10-10 16:44:27'),(13,4,'https://semonemo.s3.amazonaws.com/picture/62e48a63-d02a-4c86-bfe1-2e84a9e5754a_image_1728546270031.png','2024-10-10 16:44:32'),(14,5,'https://semonemo.s3.amazonaws.com/picture/3aa45986-85c3-488c-93ea-c2d8be4527d2_image_1728546298818.png','2024-10-10 16:45:00'),(15,1,'https://semonemo.s3.amazonaws.com/picture/c04c3e64-c7b0-4e7a-94b9-3c295a1ac5ae_image_1728546518713.png','2024-10-10 16:48:39'),(16,2,'https://semonemo.s3.amazonaws.com/picture/db781d65-e8be-41b2-afd7-767d97ed0233_image_1728546611539.png','2024-10-10 16:50:14'),(17,1,'https://semonemo.s3.amazonaws.com/picture/fe0e173e-66b2-4d67-8243-1dcfaef041ec_image_1728546660937.png','2024-10-10 16:51:02'),(18,3,'https://semonemo.s3.amazonaws.com/picture/71e094bf-750c-471f-8937-84f388da50dc_image_1728546658877.png','2024-10-10 16:51:04'),(19,6,'https://semonemo.s3.amazonaws.com/picture/112ca41a-5e0b-404c-bbfc-1c8a0db7c26b_image_1728546666378.png','2024-10-10 16:51:07'),(20,7,'https://semonemo.s3.amazonaws.com/picture/8abf4b4d-48bc-4b89-b845-3d6614b25144_image_1728546681807.png','2024-10-10 16:51:21'),(21,1,'https://semonemo.s3.amazonaws.com/picture/9c216343-92d4-4142-9ae5-e251761ef8fd_1000032280.png','2024-10-10 16:51:27'),(22,2,'https://semonemo.s3.amazonaws.com/picture/fe95ce27-ec4b-4f23-812f-986f71ef2405_image_1728547426936.png','2024-10-10 17:03:49'),(23,4,'https://semonemo.s3.amazonaws.com/picture/70e41b6f-0ec8-4ec9-8077-44ddb6dcc30e_image_1728547483277.png','2024-10-10 17:04:46'),(24,4,'https://semonemo.s3.amazonaws.com/picture/54698b85-09b4-40e1-851c-2719a23753b6_image_1728547549514.png','2024-10-10 17:05:51'),(25,4,'https://semonemo.s3.amazonaws.com/picture/d7dcf5e7-99b6-4191-94ac-9ffead8c58bb_image_1728547590450.png','2024-10-10 17:06:32'),(26,2,'https://semonemo.s3.amazonaws.com/picture/a2bd7dca-891a-4a14-8eca-f6e1a68b95a7_image_1728549227348.png','2024-10-10 17:33:54');
/*!40000 ALTER TABLE `asset_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asset_like`
--

DROP TABLE IF EXISTS `asset_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asset_like` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `asset_sell_id` bigint DEFAULT NULL,
  `user_id` bigint NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `asset_like_asset_sell_id_fk` (`asset_sell_id`),
  KEY `asset_like_users_id_fk` (`user_id`),
  CONSTRAINT `asset_like_asset_sell_id_fk` FOREIGN KEY (`asset_sell_id`) REFERENCES `asset_sell` (`id`),
  CONSTRAINT `asset_like_users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asset_like`
--

LOCK TABLES `asset_like` WRITE;
/*!40000 ALTER TABLE `asset_like` DISABLE KEYS */;
INSERT INTO `asset_like` VALUES (1,2,6,'2024-10-10 17:00:05'),(2,10,3,'2024-10-10 17:03:06');
/*!40000 ALTER TABLE `asset_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asset_purchase`
--

DROP TABLE IF EXISTS `asset_purchase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asset_purchase` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `asset_sell_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `asset_purchase_asset_sell_id_fk` (`asset_sell_id`),
  KEY `asset_purchase_users_id_fk` (`user_id`),
  CONSTRAINT `asset_purchase_asset_sell_id_fk` FOREIGN KEY (`asset_sell_id`) REFERENCES `asset_sell` (`id`),
  CONSTRAINT `asset_purchase_users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asset_purchase`
--

LOCK TABLES `asset_purchase` WRITE;
/*!40000 ALTER TABLE `asset_purchase` DISABLE KEYS */;
INSERT INTO `asset_purchase` VALUES (1,1,6,'2024-10-10 16:43:18');
/*!40000 ALTER TABLE `asset_purchase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asset_sell`
--

DROP TABLE IF EXISTS `asset_sell`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asset_sell` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `asset_id` bigint NOT NULL,
  `price` bigint NOT NULL DEFAULT '0',
  `hits` bigint NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `like_count` bigint NOT NULL DEFAULT '0',
  `purchase_count` bigint NOT NULL DEFAULT '0',
  `is_on_sale` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `asset_sell_asset_image_id_fk` (`asset_id`),
  CONSTRAINT `asset_sell_asset_image_id_fk` FOREIGN KEY (`asset_id`) REFERENCES `asset_image` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asset_sell`
--

LOCK TABLES `asset_sell` WRITE;
/*!40000 ALTER TABLE `asset_sell` DISABLE KEYS */;
INSERT INTO `asset_sell` VALUES (1,8,20,8,'2024-10-10 16:40:31',0,0,1),(2,9,10,4,'2024-10-10 16:41:10',1,0,1),(3,11,30,4,'2024-10-10 16:43:23',0,0,1),(4,12,20,2,'2024-10-10 16:44:41',0,0,1),(5,14,500,3,'2024-10-10 16:45:26',0,0,1),(6,18,100,2,'2024-10-10 16:51:42',0,0,1),(7,7,100,0,'2024-10-10 17:00:24',0,0,1),(8,3,300,3,'2024-10-10 17:00:37',0,0,1),(9,5,250,2,'2024-10-10 17:00:51',0,0,1),(10,4,1000,6,'2024-10-10 17:01:18',1,0,1);
/*!40000 ALTER TABLE `asset_sell` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asset_tag`
--

DROP TABLE IF EXISTS `asset_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asset_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `atag_id` bigint NOT NULL,
  `asset_sell_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `asset_tag_asset_sell_id_fk` (`asset_sell_id`),
  KEY `asset_tag_atags_id_fk` (`atag_id`),
  CONSTRAINT `asset_tag_asset_sell_id_fk` FOREIGN KEY (`asset_sell_id`) REFERENCES `asset_sell` (`id`),
  CONSTRAINT `asset_tag_atags_id_fk` FOREIGN KEY (`atag_id`) REFERENCES `atags` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asset_tag`
--

LOCK TABLES `asset_tag` WRITE;
/*!40000 ALTER TABLE `asset_tag` DISABLE KEYS */;
INSERT INTO `asset_tag` VALUES (1,1,1),(2,2,2),(3,3,2),(4,4,2),(5,5,3),(6,6,3),(7,7,3),(8,8,4),(9,9,4),(10,10,5),(11,11,5),(12,12,5),(13,13,5),(14,14,6),(15,15,6),(16,16,6),(17,17,7),(18,18,8),(19,17,9),(20,19,10);
/*!40000 ALTER TABLE `asset_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `atags`
--

DROP TABLE IF EXISTS `atags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `atags` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT 'none',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `atags`
--

LOCK TABLES `atags` WRITE;
/*!40000 ALTER TABLE `atags` DISABLE KEYS */;
INSERT INTO `atags` VALUES (1,'제리','2024-10-10 16:40:31'),(2,'칼','2024-10-10 16:41:10'),(3,'기사','2024-10-10 16:41:10'),(4,'검','2024-10-10 16:41:10'),(5,'여우','2024-10-10 16:43:23'),(6,'메타마스크','2024-10-10 16:43:23'),(7,'다이어트필요','2024-10-10 16:43:24'),(8,'고양이','2024-10-10 16:44:41'),(9,'가면','2024-10-10 16:44:41'),(10,'베트맨','2024-10-10 16:45:26'),(11,'고담시는','2024-10-10 16:45:26'),(12,'내가','2024-10-10 16:45:26'),(13,'지킨다','2024-10-10 16:45:27'),(14,'싸피','2024-10-10 16:51:42'),(15,'SSAFY','2024-10-10 16:51:42'),(16,'싸피최고미남','2024-10-10 16:51:42'),(17,'루피','2024-10-10 17:00:24'),(18,'흠','2024-10-10 17:00:37'),(19,'아하','2024-10-10 17:01:18');
/*!40000 ALTER TABLE `atags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auctions`
--

DROP TABLE IF EXISTS `auctions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auctions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nft_id` bigint NOT NULL,
  `status` varchar(15) NOT NULL,
  `start_price` int NOT NULL,
  `winner` bigint DEFAULT NULL,
  `final_price` int DEFAULT NULL,
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `auction_nft_fk_idx` (`nft_id`),
  CONSTRAINT `auction_nft_fk` FOREIGN KEY (`nft_id`) REFERENCES `nfts` (`nft_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auctions`
--

LOCK TABLES `auctions` WRITE;
/*!40000 ALTER TABLE `auctions` DISABLE KEYS */;
INSERT INTO `auctions` VALUES (1,9,'READY',200,NULL,NULL,'2024-10-11 17:05:39',NULL,'2024-10-10 17:05:39'),(2,11,'READY',50000,NULL,NULL,'2024-10-11 17:44:54',NULL,'2024-10-10 17:44:54');
/*!40000 ALTER TABLE `auctions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coin_price`
--

DROP TABLE IF EXISTS `coin_price`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coin_price` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `price` bigint NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `changed` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=355 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coin_price`
--

LOCK TABLES `coin_price` WRITE;
/*!40000 ALTER TABLE `coin_price` DISABLE KEYS */;
INSERT INTO `coin_price` VALUES (1,1000,'2024-10-07 08:50:49',0),(2,999,'2024-10-08 16:44:52',9.66),(3,992,'2024-10-08 16:48:01',-0.8),(4,1006,'2024-10-08 16:58:01',0.6),(5,989,'2024-10-08 17:08:01',-1.1),(6,988,'2024-10-08 17:18:01',8.45),(7,1008,'2024-10-08 17:20:21',10.65),(8,988,'2024-10-08 17:24:25',-1.2),(9,978,'2024-10-08 17:34:24',-2.2),(10,975,'2024-10-08 17:44:24',-2.5),(11,987,'2024-10-08 17:48:14',-1.3),(12,1007,'2024-10-08 17:49:53',0.7),(13,1027,'2024-10-08 17:56:12',2.7),(14,1006,'2024-10-08 18:06:11',0.6),(15,1025,'2024-10-08 18:16:11',2.5),(16,1020,'2024-10-08 18:26:11',11.96),(17,1036,'2024-10-08 18:36:11',13.72),(18,1021,'2024-10-08 18:46:11',12.07),(19,1030,'2024-10-08 18:56:11',13.06),(20,1022,'2024-10-08 19:06:11',12.18),(21,1027,'2024-10-08 19:16:11',12.73),(22,1048,'2024-10-08 19:26:11',15.04),(23,1069,'2024-10-08 19:36:11',17.34),(24,1090,'2024-10-08 19:46:11',19.65),(25,1086,'2024-10-08 19:56:11',19.21),(26,1104,'2024-10-08 20:06:11',21.19),(27,1082,'2024-10-08 20:16:11',18.77),(28,1090,'2024-10-08 20:26:11',19.65),(29,1082,'2024-10-08 20:36:11',18.77),(30,1061,'2024-10-08 20:41:59',6.1),(31,1056,'2024-10-08 20:51:58',5.6),(32,1072,'2024-10-08 21:01:58',7.2),(33,1068,'2024-10-08 21:11:02',6.8),(34,1049,'2024-10-08 21:21:02',4.9),(35,1058,'2024-10-08 21:27:32',16.14),(36,1037,'2024-10-08 21:31:02',3.7),(37,1037,'2024-10-08 21:32:43',3.7),(38,1049,'2024-10-08 21:42:42',4.9),(39,1054,'2024-10-08 21:52:42',5.4),(40,1040,'2024-10-08 22:02:42',14.16),(41,1019,'2024-10-08 22:12:42',11.86),(42,1025,'2024-10-08 22:22:42',12.51),(43,1005,'2024-10-08 22:32:42',10.32),(44,1013,'2024-10-08 22:42:42',11.2),(45,1004,'2024-10-08 22:52:42',10.21),(46,999,'2024-10-08 23:02:42',9.66),(47,1007,'2024-10-08 23:12:42',10.54),(48,1027,'2024-10-08 23:22:42',12.73),(49,1048,'2024-10-08 23:32:42',15.04),(50,1067,'2024-10-08 23:42:42',17.12),(51,1046,'2024-10-08 23:52:42',14.82),(52,1039,'2024-10-09 00:02:42',0.68),(53,1060,'2024-10-09 00:12:42',2.71),(54,1047,'2024-10-09 00:22:42',1.45),(55,1050,'2024-10-09 00:32:42',1.74),(56,1029,'2024-10-09 00:42:42',-0.29),(57,1014,'2024-10-09 00:52:42',-1.74),(58,1010,'2024-10-09 01:02:42',-2.13),(59,1000,'2024-10-09 01:12:42',-3.1),(60,1010,'2024-10-09 01:22:42',-2.13),(61,1007,'2024-10-09 01:32:42',-2.42),(62,1020,'2024-10-09 01:42:42',-1.16),(63,1035,'2024-10-09 01:52:42',0.29),(64,1038,'2024-10-09 02:02:42',0.58),(65,1031,'2024-10-09 02:12:42',-0.1),(66,1028,'2024-10-09 02:22:42',-0.39),(67,1013,'2024-10-09 02:32:42',-1.84),(68,1033,'2024-10-09 02:42:42',0.1),(69,1034,'2024-10-09 02:52:42',0.19),(70,1053,'2024-10-09 03:02:42',2.03),(71,1032,'2024-10-09 03:12:42',0),(72,1031,'2024-10-09 03:22:42',-0.1),(73,1029,'2024-10-09 03:32:42',-0.29),(74,1042,'2024-10-09 03:42:42',0.97),(75,1026,'2024-10-09 03:52:42',-0.58),(76,1017,'2024-10-09 04:02:42',-1.45),(77,999,'2024-10-09 04:12:42',-3.2),(78,1000,'2024-10-09 04:22:42',-3.1),(79,1018,'2024-10-09 04:32:42',-1.36),(80,1012,'2024-10-09 04:42:42',-1.94),(81,997,'2024-10-09 04:52:42',-3.39),(82,989,'2024-10-09 05:02:42',-4.17),(83,1009,'2024-10-09 05:12:42',-2.23),(84,989,'2024-10-09 05:22:42',-4.17),(85,984,'2024-10-09 05:32:42',-4.65),(86,964,'2024-10-09 05:42:42',-6.59),(87,965,'2024-10-09 05:52:42',-6.49),(88,957,'2024-10-09 06:02:42',-7.27),(89,969,'2024-10-09 06:12:42',-6.1),(90,951,'2024-10-09 06:22:42',-7.85),(91,968,'2024-10-09 06:32:42',-6.2),(92,977,'2024-10-09 06:42:42',-5.33),(93,997,'2024-10-09 06:52:42',-3.39),(94,977,'2024-10-09 07:02:42',-5.33),(95,959,'2024-10-09 07:12:42',-7.07),(96,978,'2024-10-09 07:22:42',-5.23),(97,998,'2024-10-09 07:32:42',-3.29),(98,985,'2024-10-09 07:42:42',-4.55),(99,997,'2024-10-09 07:52:42',-3.39),(100,977,'2024-10-09 08:02:42',-5.33),(101,994,'2024-10-09 08:12:42',-3.68),(102,975,'2024-10-09 08:22:42',-5.52),(103,990,'2024-10-09 08:32:42',-4.07),(104,979,'2024-10-09 08:42:42',-5.14),(105,981,'2024-10-09 08:52:42',-4.94),(106,978,'2024-10-09 09:02:42',-5.23),(107,998,'2024-10-09 09:12:42',-3.29),(108,978,'2024-10-09 09:22:42',-5.23),(109,966,'2024-10-09 09:32:42',-6.4),(110,955,'2024-10-09 09:42:42',-7.46),(111,967,'2024-10-09 09:52:42',-6.3),(112,976,'2024-10-09 10:02:42',-5.43),(113,969,'2024-10-09 10:12:42',-6.1),(114,971,'2024-10-09 10:22:42',-5.91),(115,970,'2024-10-09 10:32:42',-6.01),(116,989,'2024-10-09 10:42:42',-4.17),(117,996,'2024-10-09 10:52:42',-3.49),(118,998,'2024-10-09 11:02:42',-3.29),(119,978,'2024-10-09 11:12:42',-5.23),(120,971,'2024-10-09 11:22:42',-5.91),(121,981,'2024-10-09 11:32:42',-4.94),(122,990,'2024-10-09 11:42:42',-4.07),(123,1005,'2024-10-09 11:52:42',-2.62),(124,985,'2024-10-09 12:02:42',-4.55),(125,1005,'2024-10-09 12:12:42',-2.62),(126,1003,'2024-10-09 12:22:42',-2.81),(127,1008,'2024-10-09 12:32:42',-2.33),(128,1020,'2024-10-09 12:42:42',-1.16),(129,1005,'2024-10-09 12:52:42',-2.62),(130,985,'2024-10-09 13:02:42',-4.55),(131,965,'2024-10-09 13:12:42',-6.49),(132,984,'2024-10-09 13:22:42',-4.65),(133,964,'2024-10-09 13:32:42',-6.59),(134,953,'2024-10-09 13:42:42',-7.66),(135,972,'2024-10-09 13:52:42',-5.81),(136,969,'2024-10-09 14:02:42',-6.1),(137,982,'2024-10-09 14:12:42',-4.84),(138,975,'2024-10-09 14:22:42',-5.52),(139,960,'2024-10-09 14:32:42',-6.98),(140,941,'2024-10-09 14:42:42',-8.82),(141,939,'2024-10-09 14:52:42',-9.01),(142,920,'2024-10-09 15:02:42',-10.85),(143,928,'2024-10-09 15:12:42',-10.08),(144,910,'2024-10-09 15:22:42',-11.82),(145,892,'2024-10-09 15:32:42',-13.57),(146,877,'2024-10-09 15:42:42',-15.02),(147,895,'2024-10-09 15:52:42',-13.28),(148,888,'2024-10-09 16:02:42',-13.95),(149,874,'2024-10-09 16:12:42',-15.31),(150,885,'2024-10-09 16:22:42',-14.24),(151,903,'2024-10-09 16:32:42',-12.5),(152,906,'2024-10-09 16:42:42',-12.21),(153,912,'2024-10-09 16:52:42',-11.63),(154,930,'2024-10-09 17:02:42',-9.88),(155,911,'2024-10-09 17:12:42',-11.72),(156,924,'2024-10-09 17:22:42',-10.47),(157,910,'2024-10-09 17:32:42',-11.82),(158,911,'2024-10-09 17:42:42',-11.72),(159,893,'2024-10-09 17:52:42',-13.47),(160,911,'2024-10-09 18:02:42',-11.72),(161,893,'2024-10-09 18:12:42',-13.47),(162,888,'2024-10-09 18:22:42',-13.95),(163,876,'2024-10-09 18:32:42',-15.12),(164,861,'2024-10-09 18:42:42',-16.57),(165,878,'2024-10-09 18:52:42',-14.92),(166,896,'2024-10-09 19:02:42',-13.18),(167,887,'2024-10-09 19:12:42',-14.05),(168,891,'2024-10-09 19:22:42',-13.66),(169,875,'2024-10-09 19:32:42',-15.21),(170,878,'2024-10-09 19:42:42',-14.92),(171,860,'2024-10-09 19:52:42',-16.67),(172,877,'2024-10-09 20:02:42',-15.02),(173,866,'2024-10-09 20:12:42',-16.09),(174,854,'2024-10-09 20:22:42',-17.25),(175,871,'2024-10-09 20:32:42',-15.6),(176,874,'2024-10-09 20:42:42',-15.31),(177,891,'2024-10-09 20:52:42',-13.66),(178,886,'2024-10-09 21:02:42',-14.15),(179,868,'2024-10-09 21:12:42',-15.89),(180,863,'2024-10-09 21:22:42',-16.38),(181,880,'2024-10-09 21:32:42',-14.73),(182,866,'2024-10-09 21:42:42',-16.09),(183,857,'2024-10-09 21:52:42',-16.96),(184,840,'2024-10-09 22:02:42',-18.6),(185,842,'2024-10-09 22:12:42',-18.41),(186,857,'2024-10-09 22:22:42',-16.96),(187,860,'2024-10-09 22:32:42',-16.67),(188,855,'2024-10-09 22:42:42',-17.15),(189,856,'2024-10-09 22:52:42',-17.05),(190,873,'2024-10-09 23:02:42',-15.41),(191,857,'2024-10-09 23:12:42',-16.96),(192,864,'2024-10-09 23:22:42',-16.28),(193,849,'2024-10-09 23:32:42',-17.73),(194,847,'2024-10-09 23:42:42',-17.93),(195,864,'2024-10-09 23:52:42',-16.28),(196,853,'2024-10-10 00:02:42',-10.4),(197,870,'2024-10-10 00:12:42',-8.61),(198,855,'2024-10-10 00:22:42',-10.19),(199,839,'2024-10-10 00:32:42',-11.87),(200,848,'2024-10-10 00:42:42',-10.92),(201,865,'2024-10-10 00:52:42',-9.14),(202,848,'2024-10-10 01:02:42',-10.92),(203,860,'2024-10-10 01:12:42',-9.66),(204,877,'2024-10-10 01:22:42',-7.88),(205,892,'2024-10-10 01:32:42',-6.3),(206,892,'2024-10-10 01:42:42',-6.3),(207,874,'2024-10-10 01:52:42',-8.19),(208,876,'2024-10-10 02:02:42',-7.98),(209,894,'2024-10-10 02:12:42',-6.09),(210,900,'2024-10-10 02:22:42',-5.46),(211,910,'2024-10-10 02:32:42',-4.41),(212,910,'2024-10-10 02:42:42',-4.41),(213,893,'2024-10-10 02:52:42',-6.2),(214,894,'2024-10-10 03:02:42',-6.09),(215,885,'2024-10-10 03:12:42',-7.04),(216,877,'2024-10-10 03:22:42',-7.88),(217,869,'2024-10-10 03:32:42',-8.72),(218,870,'2024-10-10 03:42:42',-8.61),(219,869,'2024-10-10 03:52:42',-8.72),(220,852,'2024-10-10 04:02:42',-10.5),(221,869,'2024-10-10 04:12:42',-8.72),(222,852,'2024-10-10 04:22:42',-10.5),(223,863,'2024-10-10 04:32:42',-9.35),(224,846,'2024-10-10 04:42:42',-11.13),(225,851,'2024-10-10 04:52:42',-10.61),(226,840,'2024-10-10 05:02:42',-11.76),(227,837,'2024-10-10 05:12:42',-12.08),(228,838,'2024-10-10 05:22:42',-11.97),(229,851,'2024-10-10 05:32:42',-10.61),(230,850,'2024-10-10 05:42:42',-10.71),(231,854,'2024-10-10 05:52:42',-10.29),(232,844,'2024-10-10 06:02:42',-11.34),(233,856,'2024-10-10 06:12:42',-10.08),(234,854,'2024-10-10 06:22:42',-10.29),(235,867,'2024-10-10 06:32:42',-8.93),(236,857,'2024-10-10 06:42:42',-9.98),(237,852,'2024-10-10 06:52:42',-10.5),(238,841,'2024-10-10 07:02:42',-11.66),(239,839,'2024-10-10 07:12:42',-11.87),(240,830,'2024-10-10 07:22:42',-12.82),(241,826,'2024-10-10 07:32:42',-13.24),(242,816,'2024-10-10 07:42:42',-14.29),(243,800,'2024-10-10 07:52:42',-15.97),(244,816,'2024-10-10 08:02:42',-14.29),(245,800,'2024-10-10 08:12:42',-15.97),(246,816,'2024-10-10 08:22:42',-14.29),(247,800,'2024-10-10 08:32:42',-15.97),(248,813,'2024-10-10 08:42:42',-14.6),(249,825,'2024-10-10 08:52:42',-13.34),(250,842,'2024-10-10 09:02:42',-11.55),(251,851,'2024-10-10 09:05:39',-10.61),(252,870,'2024-10-10 09:07:51',-8.61),(253,868,'2024-10-10 09:12:37',-8.82),(254,852,'2024-10-10 09:12:42',-10.5),(255,835,'2024-10-10 09:15:05',-12.29),(256,846,'2024-10-10 09:17:49',-11.13),(257,849,'2024-10-10 09:18:15',-10.82),(258,852,'2024-10-10 09:19:25',-10.5),(259,852,'2024-10-10 09:22:42',-10.5),(260,849,'2024-10-10 09:25:35',-17.73),(261,851,'2024-10-10 09:35:34',-17.54),(262,849,'2024-10-10 09:44:22',-10.82),(263,866,'2024-10-10 09:45:34',-16.09),(264,871,'2024-10-10 09:45:41',-8.51),(265,878,'2024-10-10 09:46:16',-7.77),(266,890,'2024-10-10 09:52:13',-13.76),(267,878,'2024-10-10 09:56:16',-7.77),(268,862,'2024-10-10 10:02:13',-16.47),(269,845,'2024-10-10 10:12:13',-18.12),(270,840,'2024-10-10 10:20:17',-11.76),(271,841,'2024-10-10 10:22:13',-11.66),(272,833,'2024-10-10 10:28:14',-19.28),(273,845,'2024-10-10 10:30:17',-11.24),(274,828,'2024-10-10 10:38:14',-19.77),(275,833,'2024-10-10 10:40:17',-12.5),(276,843,'2024-10-10 10:48:14',-18.31),(277,826,'2024-10-10 10:50:17',-13.24),(278,828,'2024-10-10 10:58:14',-13.03),(279,814,'2024-10-10 11:00:17',-14.5),(280,820,'2024-10-10 11:08:14',-13.87),(281,821,'2024-10-10 11:10:17',-13.76),(282,811,'2024-10-10 11:18:14',-14.81),(283,827,'2024-10-10 11:20:17',-13.13),(284,828,'2024-10-10 11:28:14',-13.03),(285,814,'2024-10-10 11:30:17',-14.5),(286,813,'2024-10-10 11:38:14',-14.6),(287,817,'2024-10-10 11:40:17',-14.18),(288,827,'2024-10-10 11:48:14',-13.13),(289,810,'2024-10-10 11:50:17',-14.92),(290,801,'2024-10-10 11:58:14',-15.86),(291,809,'2024-10-10 12:00:17',-15.02),(292,825,'2024-10-10 12:08:14',-13.34),(293,816,'2024-10-10 12:10:17',-14.29),(294,817,'2024-10-10 12:18:14',-14.18),(295,831,'2024-10-10 12:20:17',-12.71),(296,848,'2024-10-10 12:28:14',-10.92),(297,865,'2024-10-10 12:30:17',-9.14),(298,882,'2024-10-10 12:38:14',-7.35),(299,873,'2024-10-10 12:40:17',-8.3),(300,872,'2024-10-10 12:48:14',-8.4),(301,889,'2024-10-10 12:50:17',-6.62),(302,889,'2024-10-10 12:58:14',-6.62),(303,897,'2024-10-10 13:00:17',-5.78),(304,896,'2024-10-10 13:08:14',-5.88),(305,906,'2024-10-10 13:10:17',-4.83),(306,919,'2024-10-10 13:18:14',-3.47),(307,911,'2024-10-10 13:19:48',-11.72),(308,893,'2024-10-10 13:20:17',-6.2),(309,911,'2024-10-10 13:20:46',-11.72),(310,905,'2024-10-10 13:30:17',-4.94),(311,894,'2024-10-10 13:30:46',-13.37),(312,883,'2024-10-10 13:40:17',-7.25),(313,886,'2024-10-10 13:40:46',-14.15),(314,896,'2024-10-10 13:50:17',-5.88),(315,891,'2024-10-10 13:50:46',-6.41),(316,903,'2024-10-10 14:00:17',-5.15),(317,909,'2024-10-10 14:00:46',-4.52),(318,908,'2024-10-10 14:10:17',-4.62),(319,926,'2024-10-10 14:10:46',-2.73),(320,921,'2024-10-10 14:20:17',-3.26),(321,908,'2024-10-10 14:20:46',-4.62),(322,890,'2024-10-10 14:23:29',-13.76),(323,898,'2024-10-10 14:30:17',-5.67),(324,886,'2024-10-10 14:33:29',-14.15),(325,904,'2024-10-10 14:40:17',-5.04),(326,886,'2024-10-10 14:43:29',-14.15),(327,885,'2024-10-10 14:50:17',-7.04),(328,867,'2024-10-10 14:53:29',-8.93),(329,856,'2024-10-10 15:00:17',-10.08),(330,849,'2024-10-10 15:03:29',-10.82),(331,837,'2024-10-10 15:10:17',-12.08),(332,851,'2024-10-10 15:13:29',-10.61),(333,835,'2024-10-10 15:20:17',-12.29),(334,832,'2024-10-10 15:23:29',-12.61),(335,849,'2024-10-10 15:30:17',-10.82),(336,851,'2024-10-10 15:33:29',-10.61),(337,860,'2024-10-10 15:40:17',-9.66),(338,843,'2024-10-10 15:43:29',-11.45),(339,860,'2024-10-10 15:50:17',-9.66),(340,867,'2024-10-10 15:53:29',-8.93),(341,869,'2024-10-10 16:00:17',-8.72),(342,881,'2024-10-10 16:03:29',-7.46),(343,895,'2024-10-10 16:11:51',-13.28),(344,877,'2024-10-10 16:21:51',-15.02),(345,880,'2024-10-10 16:31:51',-14.73),(346,879,'2024-10-10 16:41:51',-7.67),(347,888,'2024-10-10 16:51:51',-6.72),(348,870,'2024-10-10 17:01:51',-8.61),(349,853,'2024-10-10 17:11:51',-10.4),(350,870,'2024-10-10 17:21:51',-8.61),(351,887,'2024-10-10 17:31:51',-6.83),(352,888,'2024-10-10 17:41:51',-6.72),(353,884,'2024-10-10 17:51:51',-7.14),(354,889,'2024-10-10 17:59:10',-13.86);
/*!40000 ALTER TABLE `coin_price` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coin_price_history`
--

DROP TABLE IF EXISTS `coin_price_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coin_price_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `average_price` bigint NOT NULL DEFAULT '0',
  `daily_change` double NOT NULL DEFAULT '0',
  `highest_price` bigint NOT NULL DEFAULT '0',
  `lowest_price` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coin_price_history`
--

LOCK TABLES `coin_price_history` WRITE;
/*!40000 ALTER TABLE `coin_price_history` DISABLE KEYS */;
INSERT INTO `coin_price_history` VALUES (1,'2024-10-01',1000,0,1100,900),(2,'2024-10-02',1100,10,1200,1000),(3,'2024-10-03',1050,-4.5,1100,1000),(4,'2024-10-04',1200,14,1300,1100),(5,'2024-10-05',1150,-4.1,1200,1100),(6,'2024-10-06',1000,-13,1300,700),(7,'2024-10-07',911,-8.9,1055,798),(8,'2024-10-08',1032,13.28,1104,975),(9,'2024-10-09',952,-7.75,1060,840);
/*!40000 ALTER TABLE `coin_price_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `follows`
--

DROP TABLE IF EXISTS `follows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `follows` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `from_user_id` bigint NOT NULL,
  `to_user_id` bigint NOT NULL,
  `created_at` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  KEY `subscribe_user_fk_1_idx` (`from_user_id`),
  KEY `subscribe_user_fk_2_idx` (`to_user_id`),
  CONSTRAINT `subscribe_user_fk_1` FOREIGN KEY (`from_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `subscribe_user_fk_2` FOREIGN KEY (`to_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `follows`
--

LOCK TABLES `follows` WRITE;
/*!40000 ALTER TABLE `follows` DISABLE KEYS */;
INSERT INTO `follows` VALUES (1,3,7,'2024-10-10 17:02:05'),(2,7,3,'2024-10-10 17:06:19');
/*!40000 ALTER TABLE `follows` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nft_market`
--

DROP TABLE IF EXISTS `nft_market`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nft_market` (
  `market_id` bigint NOT NULL AUTO_INCREMENT,
  `nft_id` bigint NOT NULL,
  `seller_id` bigint NOT NULL,
  `price` bigint NOT NULL,
  `like_count` int NOT NULL,
  `is_sold` tinyint(1) NOT NULL DEFAULT '0',
  `sold_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`market_id`),
  KEY `nft_market_nfts_nft_id_fk` (`nft_id`),
  KEY `nft_market_users_id_fk` (`seller_id`),
  CONSTRAINT `nft_market_nfts_nft_id_fk` FOREIGN KEY (`nft_id`) REFERENCES `nfts` (`nft_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `nft_market_users_id_fk` FOREIGN KEY (`seller_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nft_market`
--

LOCK TABLES `nft_market` WRITE;
/*!40000 ALTER TABLE `nft_market` DISABLE KEYS */;
INSERT INTO `nft_market` VALUES (1,1,1,1000,0,0,NULL,'2024-10-10 16:40:11'),(2,4,6,50,0,0,NULL,'2024-10-10 16:46:52'),(3,5,5,1000,1,1,'2024-10-10 16:55:01','2024-10-10 16:51:05'),(4,7,1,109,4,0,NULL,'2024-10-10 16:53:52'),(5,5,4,1000,0,1,'2024-10-10 16:58:22','2024-10-10 16:58:06'),(6,5,5,1500,0,0,NULL,'2024-10-10 17:00:18'),(7,2,4,3333,0,0,NULL,'2024-10-10 17:01:49'),(8,10,4,1000,0,0,NULL,'2024-10-10 17:10:47'),(9,3,4,300,0,0,NULL,'2024-10-10 17:11:27');
/*!40000 ALTER TABLE `nft_market` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nft_market_like`
--

DROP TABLE IF EXISTS `nft_market_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nft_market_like` (
  `market_like_id` bigint NOT NULL AUTO_INCREMENT,
  `market_id` bigint NOT NULL,
  `liked_user_id` bigint NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`market_like_id`),
  KEY `nft_market_like_nft_market_market_id_fk` (`market_id`),
  KEY `nft_market_like_users_id_fk` (`liked_user_id`),
  CONSTRAINT `nft_market_like_nft_market_market_id_fk` FOREIGN KEY (`market_id`) REFERENCES `nft_market` (`market_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `nft_market_like_users_id_fk` FOREIGN KEY (`liked_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nft_market_like`
--

LOCK TABLES `nft_market_like` WRITE;
/*!40000 ALTER TABLE `nft_market_like` DISABLE KEYS */;
INSERT INTO `nft_market_like` VALUES (1,3,5,'2024-10-10 16:51:43'),(2,4,1,'2024-10-10 16:53:56'),(3,4,6,'2024-10-10 16:54:48'),(4,4,5,'2024-10-10 16:57:57'),(5,4,3,'2024-10-10 17:02:29');
/*!40000 ALTER TABLE `nft_market_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nft_tag`
--

DROP TABLE IF EXISTS `nft_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nft_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ntag_id` bigint NOT NULL,
  `nft_id` bigint DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `nft_tag_ntags_id_fk` (`ntag_id`),
  KEY `nft_tag_nfts_nft_id_fk` (`nft_id`),
  CONSTRAINT `nft_tag_nfts_nft_id_fk` FOREIGN KEY (`nft_id`) REFERENCES `nfts` (`nft_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `nft_tag_ntags_id_fk` FOREIGN KEY (`ntag_id`) REFERENCES `ntags` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nft_tag`
--

LOCK TABLES `nft_tag` WRITE;
/*!40000 ALTER TABLE `nft_tag` DISABLE KEYS */;
INSERT INTO `nft_tag` VALUES (1,1,1,'2024-10-10 07:20:11'),(2,2,1,'2024-10-10 07:20:11'),(3,3,2,'2024-10-10 07:36:25'),(4,4,3,'2024-10-10 07:46:25'),(5,5,3,'2024-10-10 07:46:25'),(6,6,4,'2024-10-10 07:46:34'),(7,7,4,'2024-10-10 07:46:34'),(8,8,4,'2024-10-10 07:46:34'),(9,9,5,'2024-10-10 07:50:39'),(10,10,6,'2024-10-10 07:52:47'),(11,11,7,'2024-10-10 07:52:49'),(12,12,8,'2024-10-10 07:57:07'),(13,13,9,'2024-10-10 08:04:49'),(14,14,9,'2024-10-10 08:04:49'),(15,15,9,'2024-10-10 08:04:49'),(16,16,10,'2024-10-10 08:10:04'),(17,17,10,'2024-10-10 08:10:04'),(18,18,10,'2024-10-10 08:10:04'),(19,19,11,'2024-10-10 08:40:13'),(20,20,11,'2024-10-10 08:40:13'),(21,21,12,'2024-10-10 08:42:02'),(22,19,12,'2024-10-10 08:42:02');
/*!40000 ALTER TABLE `nft_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nfts`
--

DROP TABLE IF EXISTS `nfts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nfts` (
  `nft_id` bigint NOT NULL AUTO_INCREMENT,
  `creator_id` bigint NOT NULL,
  `owner_id` bigint NOT NULL,
  `token_id` decimal(38,0) NOT NULL,
  `is_open` tinyint(1) NOT NULL DEFAULT '1',
  `is_on_sale` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `frame_type` int NOT NULL,
  PRIMARY KEY (`nft_id`),
  KEY `nfts_users_id_fk` (`creator_id`),
  KEY `nfts_users_id_fk_2` (`owner_id`),
  CONSTRAINT `nfts_users_id_fk` FOREIGN KEY (`creator_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `nfts_users_id_fk_2` FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nfts`
--

LOCK TABLES `nfts` WRITE;
/*!40000 ALTER TABLE `nfts` DISABLE KEYS */;
INSERT INTO `nfts` VALUES (1,1,1,0,1,1,'2024-10-10 16:20:12',1),(2,4,4,1,1,1,'2024-10-10 16:36:26',1),(3,4,4,2,1,1,'2024-10-10 16:46:26',1),(4,6,6,3,1,1,'2024-10-10 16:46:35',1),(5,5,5,5,1,1,'2024-10-10 16:50:39',1),(6,7,7,6,1,0,'2024-10-10 16:52:48',1),(7,1,1,7,1,1,'2024-10-10 16:52:50',1),(8,2,2,8,1,0,'2024-10-10 16:57:07',1),(9,2,2,9,1,1,'2024-10-10 17:04:50',1),(10,4,4,10,1,1,'2024-10-10 17:10:04',1),(11,2,2,11,1,1,'2024-10-10 17:40:14',1),(12,2,2,12,1,0,'2024-10-10 17:42:02',1);
/*!40000 ALTER TABLE `nfts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ntags`
--

DROP TABLE IF EXISTS `ntags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ntags` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ntags`
--

LOCK TABLES `ntags` WRITE;
/*!40000 ALTER TABLE `ntags` DISABLE KEYS */;
INSERT INTO `ntags` VALUES (1,'흑백 요라사','2024-10-10 16:20:12'),(2,'백종원','2024-10-10 16:20:12'),(3,'아하','2024-10-10 16:36:26'),(4,'루피','2024-10-10 16:46:26'),(5,'원피스','2024-10-10 16:46:26'),(6,'제리','2024-10-10 16:46:35'),(7,'검도','2024-10-10 16:46:35'),(8,'칼','2024-10-10 16:46:35'),(9,'베트맨','2024-10-10 16:50:39'),(10,'drtaa','2024-10-10 16:52:48'),(11,'세모내모','2024-10-10 16:52:50'),(12,'고양이','2024-10-10 16:57:07'),(13,'돼지','2024-10-10 17:04:50'),(14,'피그','2024-10-10 17:04:50'),(15,'꿀꿀','2024-10-10 17:04:50'),(16,'BTS','2024-10-10 17:10:04'),(17,'봉준호','2024-10-10 17:10:04'),(18,'손흥민','2024-10-10 17:10:04'),(19,'나경','2024-10-10 17:40:14'),(20,'같이찍어요','2024-10-10 17:40:14'),(21,'이나경','2024-10-10 17:42:02');
/*!40000 ALTER TABLE `ntags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refresh_tokens`
--

DROP TABLE IF EXISTS `refresh_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refresh_tokens` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `refresh_token` varchar(255) NOT NULL,
  `user_id` bigint NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK7tdcd6ab5wsgoudnvj7xf1b7l` (`user_id`),
  CONSTRAINT `FK1lih5y2npsf8u5o3vhdb9y0os` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refresh_tokens`
--

LOCK TABLES `refresh_tokens` WRITE;
/*!40000 ALTER TABLE `refresh_tokens` DISABLE KEYS */;
INSERT INTO `refresh_tokens` VALUES (1,'Bearer eyJhbGciOiJIUzM4NCJ9.eyJpc3MiOiJTZW1vbmVtbyIsInN1YiI6IjB4YzI3MmVlNmZmMjQ2NDk3NGIxYTA1ZjIwYzhlMjg1MjZlZGNjZDlkNiIsImlhdCI6MTcyODU1MDU1MCwiZXhwIjoxNzI5MTU1MzUwfQ.wqNW_g-FCRnu-gcsDtdz1GDBI4UvscYMDQm4FSO-mbZDxtk9-B-77MHE2-jKwWxx',1,'2024-10-10 16:16:42.714052'),(2,'Bearer eyJhbGciOiJIUzM4NCJ9.eyJpc3MiOiJTZW1vbmVtbyIsInN1YiI6IjB4NGI1ZGU4NTI2YjMzMWQ4ZWY2YzdjYmEzYWRmYTUyMGY5OGY3MTBiNSIsImlhdCI6MTcyODU0OTQ3NiwiZXhwIjoxNzI5MTU0Mjc2fQ.UYoD5sXSLp6QakgUQJthDcMyo515jD7odiGwkybM_NcAI1APaZfSpQI_pGtxSI6K',2,'2024-10-10 16:19:36.638559'),(3,'Bearer eyJhbGciOiJIUzM4NCJ9.eyJpc3MiOiJTZW1vbmVtbyIsInN1YiI6IjB4OGI1ZmJkOTdjZmE0MTE3OTYzYzM0YWQ0NzM2NjZiZTBhOGI3YWQ0OCIsImlhdCI6MTcyODU0NDk0OCwiZXhwIjoxNzI5MTQ5NzQ4fQ.-2OLkrrKgMXVJBNg4qOtaZwCN2StaJJrdDAPqgA-0AXLeylaGKKajNUSBEdo8hor',3,'2024-10-10 16:22:28.307017'),(4,'Bearer eyJhbGciOiJIUzM4NCJ9.eyJpc3MiOiJTZW1vbmVtbyIsInN1YiI6IjB4MmZkYjM3YTVlZTBhZGIxNDk5OGE3MmJjNjdlN2VkNjhkY2M4YzlkMyIsImlhdCI6MTcyODU0NTI4NiwiZXhwIjoxNzI5MTUwMDg2fQ.IJDteHbOfKvaRyr4yvKnzXLXigUN2yGKfN82YSrvclwl1K8hXgFfw_GX5rNGU7qf',4,'2024-10-10 16:28:06.386219'),(5,'Bearer eyJhbGciOiJIUzM4NCJ9.eyJpc3MiOiJTZW1vbmVtbyIsInN1YiI6IjB4YzU4ODRmYTJjMzk1NjYzNWJhYjY2YjVkZGRkNDNkMGFlODcwNDY2YSIsImlhdCI6MTcyODU0NjU0OSwiZXhwIjoxNzI5MTUxMzQ5fQ.XIcn5Z4Qo-oXCT_tjOxVvgJf9VveQ-cNyBerCy86wdAK5yX434cAe3aeL25XbYcs',5,'2024-10-10 16:32:51.728440'),(6,'Bearer eyJhbGciOiJIUzM4NCJ9.eyJpc3MiOiJTZW1vbmVtbyIsInN1YiI6IjB4YjgxMDg0NzliMTkyY2RiOGZmZjMxOGIwNTZmNTdkNjM4ZjVmOTgyMCIsImlhdCI6MTcyODU0NzA3MywiZXhwIjoxNzI5MTUxODczfQ.Wzn_kYqZ11S8sz5cPugB4qx6Yv2RILZGYDxYfjbr8-8eNiWHU812PAJsKcfRflzx',6,'2024-10-10 16:39:07.987609'),(7,'Bearer eyJhbGciOiJIUzM4NCJ9.eyJpc3MiOiJTZW1vbmVtbyIsInN1YiI6IjB4ZjViMTIyYWYxNWE5NjViMTJhZmM2MDY0OTE4ZGVlYmUwZTE0ZjcwMCIsImlhdCI6MTcyODU0NzU3MCwiZXhwIjoxNzI5MTUyMzcwfQ.Xd-u7GPaMVEYLTzrO18t78NbTSloDQP2i2sawOLaroj8j2u44ZEld01Mqb_KBRoG',7,'2024-10-10 16:50:01.156602');
/*!40000 ALTER TABLE `refresh_tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trade_log`
--

DROP TABLE IF EXISTS `trade_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trade_log` (
  `log_id` bigint NOT NULL AUTO_INCREMENT,
  `trade_id` decimal(38,0) NOT NULL,
  `from_user_id` bigint DEFAULT NULL,
  `to_user_id` bigint DEFAULT NULL,
  `trade_type` varchar(20) NOT NULL,
  `amount` bigint NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`),
  KEY `trade_log_users_id_fk` (`from_user_id`),
  KEY `trade_log_users_id_fk_2` (`to_user_id`),
  CONSTRAINT `trade_log_users_id_fk` FOREIGN KEY (`from_user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `trade_log_users_id_fk_2` FOREIGN KEY (`to_user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trade_log`
--

LOCK TABLES `trade_log` WRITE;
/*!40000 ALTER TABLE `trade_log` DISABLE KEYS */;
INSERT INTO `trade_log` VALUES (1,1,NULL,2,'페이코인 전환',8000,'2024-10-10 16:23:39'),(2,2,NULL,5,'페이코인 전환',7000,'2024-10-10 16:36:09'),(3,3,NULL,4,'페이코인 전환',5000,'2024-10-10 16:37:26'),(4,4,NULL,6,'페이코인 전환',5000,'2024-10-10 16:42:46'),(5,5,6,5,'에셋 거래',20,'2024-10-10 16:43:18'),(6,6,NULL,3,'페이코인 전환',5000,'2024-10-10 16:49:44'),(7,7,NULL,6,'페이코인 전환',175940,'2024-10-10 16:53:47'),(8,8,4,5,'NFT 거래',1000,'2024-10-10 16:55:01'),(9,9,5,4,'NFT 거래',1000,'2024-10-10 16:58:22'),(10,10,NULL,2,'페이코인 전환',476530,'2024-10-10 16:59:09');
/*!40000 ALTER TABLE `trade_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(50) NOT NULL,
  `password` varchar(60) NOT NULL,
  `nickname` varchar(20) NOT NULL,
  `balance` bigint NOT NULL DEFAULT '0',
  `profile_image` varchar(500) DEFAULT NULL,
  `created_at` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `address_UNIQUE` (`address`),
  UNIQUE KEY `nickname_UNIQUE` (`nickname`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'0xc272ee6ff2464974b1a05f20c8e28526edccd9d6','$2a$10$z6CJU2d45eI69SmnZzhuUuXW6bc7jxiTmEkgVdyEI.5YrRmgLwgpu','미루지마_미루니',0,'https://semonemo.s3.amazonaws.com/picture/076c3ec4-636c-4190-b690-41e49b7a8e5c_1000032126.png','2024-10-10 16:16:36'),(2,'0x4b5de8526b331d8ef6c7cba3adfa520f98f710b5','$2a$10$BYmn7Ry5O/7lI61yP/r8bea7lW6JNtuInLiOxEHTYT1MW69EqIVWO','나갱',484530,'https://semonemo.s3.amazonaws.com/picture/88713e22-5ebc-4939-ad98-3a2f3ce268c8_1000006400.jpg','2024-10-10 16:19:29'),(3,'0x8b5fbd97cfa4117963c34ad473666be0a8b7ad48','$2a$10$gKriCNgzFgss5mVeNv7VR.xwTivWZvqXW0TabCGiwF13JIR1tl3NO','나폴리갱피아',5000,'https://semonemo.s3.amazonaws.com/picture/f5f17d6a-3975-4753-86e1-026cc4388e35_41.jpg','2024-10-10 16:22:12'),(4,'0x2fdb37a5ee0adb14998a72bc67e7ed68dcc8c9d3','$2a$10$HRtMDLs..dSyPOEzw.Dj.OEj1kgj7cH3fpZGHs00K1kBu7dC0BxYa','아하',5000,'https://semonemo.s3.amazonaws.com/picture/5c29e0ad-290a-4563-9cc2-d1608ef880e4_1000002526.webp','2024-10-10 16:27:48'),(5,'0xc5884fa2c3956635bab66b5dddd43d0ae870466a','$2a$10$RRtq.zw20tAOpe3dhb8g/uWH9gMJtOiNH.xqemjvQ4GTPxKTydSeW','타이거즈1지명',7020,'https://semonemo.s3.amazonaws.com/picture/89014a78-cdf0-4b27-beb1-0a46e6021798_images.jpg','2024-10-10 16:32:30'),(6,'0xb8108479b192cdb8fff318b056f57d638f5f9820','$2a$10$8CskIjAEnJuG4zyFyZuEnexI643FXhqBCfVrb5z88bNgaSV58BPHW','끄르뀨',180920,'https://semonemo.s3.amazonaws.com/picture/3048d55d-38a8-4ed1-8331-f318f8cce3fb_1000003092.jpg','2024-10-10 16:39:01'),(7,'0xf5b122af15a965b12afc6064918deebe0e14f700','$2a$10$/2sX5vf5nKbC6s0SdgK6OuIp3kOVzjQvrRo5hFvQwWUp5n.vZleGK','hj',0,'https://semonemo.s3.amazonaws.com/picture/1e7e571e-7902-4a6e-8e18-6ef74c1e3318_195a4f324494ad4db1a0209a41ff8b79.jpg','2024-10-10 16:49:17');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-10 17:59:55
