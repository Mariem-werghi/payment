-- MySQL dump 10.13  Distrib 8.4.7, for Linux (x86_64)
--
-- Host: localhost    Database: artera_dev
-- ------------------------------------------------------
-- Server version	8.4.7-0ubuntu0.25.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `artwork`
--

DROP TABLE IF EXISTS `artwork`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `artwork` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `title` varchar(255) NOT NULL,
                           `description` longtext,
                           `type` varchar(50) NOT NULL,
                           `file_path` varchar(255) NOT NULL,
                           `thumbnail_path` varchar(255) DEFAULT NULL,
                           `verification_status` varchar(20) NOT NULL DEFAULT 'pending',
                           `creation_process` longtext,
                           `creation_hours` int DEFAULT NULL,
                           `created_at` datetime NOT NULL,
                           `updated_at` datetime DEFAULT NULL,
                           `artist_id` int NOT NULL,
                           PRIMARY KEY (`id`),
                           KEY `IDX_881FC576B7970CF8` (`artist_id`),
                           CONSTRAINT `FK_881FC576B7970CF8` FOREIGN KEY (`artist_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `artwork`
--

LOCK TABLES `artwork` WRITE;
/*!40000 ALTER TABLE `artwork` DISABLE KEYS */;
INSERT INTO `artwork` VALUES (4,'Mona Lisaaaaaa','Mona Lisaaa','painting','6988eace669a8.jpg',NULL,'pending','Mona Lisa m',1000,'2026-02-08 20:58:06','2026-02-09 11:59:21',3),(5,'loup','good','painting','698cce4e7d776.jpg',NULL,'pending','zaaz',5,'2026-02-11 19:45:34','2026-02-11 19:45:34',11),(7,'wilson','losiento','poetery','6999331805f0d.jpg',NULL,'pending','mlwsdkdldqs',NULL,'2026-02-21 05:22:48','2026-02-21 05:24:00',14),(8,'speed','speed photo shoot','painting','699b515b43c12.jpg',NULL,'pending','hard to make',10,'2026-02-22 19:56:27','2026-02-22 20:00:32',41),(9,'pinture','pinture animal pickok','painting','699b6f5b7d053.png',NULL,'pending','sdskldkdlmDMQLDKQDQ',100,'2026-02-22 22:04:27','2026-02-22 22:04:27',41),(10,'graphic','lmsfkdqd!lkdmlqk:cwxc','digital','699b6f8b2f064.png',NULL,'pending','mlwsdkdldqs',10,'2026-02-22 22:05:15','2026-02-22 22:05:15',41),(11,'adem',NULL,'painting','699be02a7d4361.08867178.jpg',NULL,'pending','mlwsdkdldqs',NULL,'2026-02-23 06:05:46','2026-03-01 12:10:03',32),(12,'wilson',NULL,'photography','699be1df64f2d4.04812483.jpg',NULL,'pending','mlwsdkdldqs',NULL,'2026-02-23 06:13:03','2026-02-23 06:13:03',32);
/*!40000 ALTER TABLE `artwork` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categorie_reclamation`
--

DROP TABLE IF EXISTS `categorie_reclamation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorie_reclamation` (
                                         `id` int NOT NULL AUTO_INCREMENT,
                                         `name` varchar(100) NOT NULL,
                                         `description` longtext,
                                         `active` tinyint NOT NULL,
                                         `created_at` datetime NOT NULL,
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorie_reclamation`
--

LOCK TABLES `categorie_reclamation` WRITE;
/*!40000 ALTER TABLE `categorie_reclamation` DISABLE KEYS */;
INSERT INTO `categorie_reclamation` VALUES (34,'Racisme','Comportement discriminatoire envers une personne en raison de son origine ethnique.',1,'2026-02-23 10:52:35'),(35,'Information trompeuse','Informations inexactes ou fausses fournies au client.',1,'2026-03-01 13:34:15'),(36,'aaaaaaaaaaaaaaaa','aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa',1,'2026-03-01 13:47:03'),(37,'Sexisme','Comportement discriminatoire fondé sur le sexe ou le genre.',1,'2026-03-01 13:48:16');
/*!40000 ALTER TABLE `categorie_reclamation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `certificat`
--

DROP TABLE IF EXISTS `certificat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `certificat` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `ownership_confirmed` tinyint NOT NULL,
                              `is_ai_generated` tinyint NOT NULL,
                              `issued_at` datetime NOT NULL,
                              `artwork_id` int NOT NULL,
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `UNIQ_27448F77DB8FFA4` (`artwork_id`),
                              CONSTRAINT `FK_27448F77DB8FFA4` FOREIGN KEY (`artwork_id`) REFERENCES `artwork` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `certificat`
--

LOCK TABLES `certificat` WRITE;
/*!40000 ALTER TABLE `certificat` DISABLE KEYS */;
/*!40000 ALTER TABLE `certificat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `content` longtext NOT NULL,
                           `created_at` datetime NOT NULL,
                           `post_id` int NOT NULL,
                           `author_id` int NOT NULL,
                           PRIMARY KEY (`id`),
                           KEY `IDX_9474526C4B89032C` (`post_id`),
                           KEY `IDX_9474526CF675F31B` (`author_id`),
                           CONSTRAINT `FK_9474526C4B89032C` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE,
                           CONSTRAINT `FK_9474526CF675F31B` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (37,'test','2026-02-22 01:24:24',42,4),(38,'nice','2026-02-23 09:52:59',92,16),(39,'test validation','2026-02-23 10:01:10',93,17),(40,'niceu','2026-02-25 14:57:54',93,17),(41,'tres bien','2026-03-01 02:13:31',93,47);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctrine_migration_versions`
--

DROP TABLE IF EXISTS `doctrine_migration_versions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctrine_migration_versions` (
                                               `version` varchar(191) NOT NULL,
                                               `executed_at` datetime DEFAULT NULL,
                                               `execution_time` int DEFAULT NULL,
                                               PRIMARY KEY (`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctrine_migration_versions`
--

LOCK TABLES `doctrine_migration_versions` WRITE;
/*!40000 ALTER TABLE `doctrine_migration_versions` DISABLE KEYS */;
INSERT INTO `doctrine_migration_versions` VALUES ('DoctrineMigrations\\Version20260204114631','2026-02-04 12:46:37',3292),('DoctrineMigrations\\Version20260204115007','2026-02-04 12:50:21',589),('DoctrineMigrations\\Version20260207204221','2026-02-07 21:42:49',591),('DoctrineMigrations\\Version20260208105637','2026-02-08 23:57:04',231),('DoctrineMigrations\\Version20260208123102','2026-02-08 13:31:14',266),('DoctrineMigrations\\Version20260208133929','2026-02-08 14:39:46',370),('DoctrineMigrations\\Version20260208225628','2026-02-08 23:59:41',80),('DoctrineMigrations\\Version20260209031842','2026-02-09 04:18:58',179),('DoctrineMigrations\\Version20260212105456','2026-02-12 11:55:14',146),('DoctrineMigrations\\Version20260215212533','2026-02-15 22:26:06',171),('DoctrineMigrations\\Version20260219231554','2026-02-20 00:16:38',419),('DoctrineMigrations\\Version20260220223816','2026-02-20 23:39:06',915),('DoctrineMigrations\\Version20260220224359','2026-02-20 23:44:18',402),('DoctrineMigrations\\Version20260220235000','2026-02-20 23:54:42',652),('DoctrineMigrations\\Version20260221002422','2026-02-21 01:24:31',938),('DoctrineMigrations\\Version20260221004357','2026-02-21 01:45:03',1097),('DoctrineMigrations\\Version20260221215659','2026-02-21 22:57:45',750),('DoctrineMigrations\\Version20260222010033','2026-02-22 02:01:29',1081),('DoctrineMigrations\\Version20260223010000','2026-02-23 00:27:02',571);
/*!40000 ALTER TABLE `doctrine_migration_versions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `events`
--

DROP TABLE IF EXISTS `events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `events` (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `title` varchar(255) NOT NULL,
                          `description` longtext,
                          `start_date` datetime NOT NULL,
                          `end_date` datetime NOT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `events`
--

LOCK TABLES `events` WRITE;
/*!40000 ALTER TABLE `events` DISABLE KEYS */;
INSERT INTO `events` VALUES (2,'Festival Créatif','festival de l\'art et de l\'inspiration \njour 1: decouverte et initiation \njour 2: exposition et performance \nrejoignez-nous !','2026-02-14 10:00:00','2026-02-15 17:00:00'),(4,'eventTest',NULL,'2026-02-15 08:00:00','2026-02-20 11:30:00'),(5,'gjjk','description','2026-03-01 10:24:00','2026-03-07 10:24:00'),(7,'l\'art moderne',NULL,'2026-02-25 09:30:00','2026-02-28 19:30:00'),(9,'l\'art urbain','d','2026-03-02 09:53:00','2026-03-05 16:53:00'),(10,'genartive Art','s','2026-02-22 23:41:00','2026-02-26 11:41:00'),(11,'genartive Art','s','2026-02-22 23:41:00','2026-02-26 11:41:00');
/*!40000 ALTER TABLE `events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `facture`
--

DROP TABLE IF EXISTS `facture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `facture` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `reference` varchar(50) NOT NULL,
                           `total_amount` decimal(10,2) NOT NULL,
                           `status` varchar(20) NOT NULL,
                           `issued_at` datetime NOT NULL,
                           `user_id` int NOT NULL,
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `UNIQ_FE866410AEA34913` (`reference`),
                           KEY `IDX_FE866410A76ED395` (`user_id`),
                           CONSTRAINT `FK_FE866410A76ED395` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `facture`
--

LOCK TABLES `facture` WRITE;
/*!40000 ALTER TABLE `facture` DISABLE KEYS */;
/*!40000 ALTER TABLE `facture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `facture_orders`
--

DROP TABLE IF EXISTS `facture_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `facture_orders` (
                                  `facture_id` int NOT NULL,
                                  `order_id` int NOT NULL,
                                  PRIMARY KEY (`facture_id`,`order_id`),
                                  KEY `IDX_B0B8EF497F2DEE08` (`facture_id`),
                                  KEY `IDX_B0B8EF498D9F6D38` (`order_id`),
                                  CONSTRAINT `FK_B0B8EF497F2DEE08` FOREIGN KEY (`facture_id`) REFERENCES `facture` (`id`) ON DELETE CASCADE,
                                  CONSTRAINT `FK_B0B8EF498D9F6D38` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `facture_orders`
--

LOCK TABLES `facture_orders` WRITE;
/*!40000 ALTER TABLE `facture_orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `facture_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `facture_reservations`
--

DROP TABLE IF EXISTS `facture_reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `facture_reservations` (
                                        `facture_id` int NOT NULL,
                                        `reservation_id` int NOT NULL,
                                        PRIMARY KEY (`facture_id`,`reservation_id`),
                                        KEY `IDX_549472CB7F2DEE08` (`facture_id`),
                                        KEY `IDX_549472CBB83297E7` (`reservation_id`),
                                        CONSTRAINT `FK_549472CB7F2DEE08` FOREIGN KEY (`facture_id`) REFERENCES `facture` (`id`) ON DELETE CASCADE,
                                        CONSTRAINT `FK_549472CBB83297E7` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `facture_reservations`
--

LOCK TABLES `facture_reservations` WRITE;
/*!40000 ALTER TABLE `facture_reservations` DISABLE KEYS */;
/*!40000 ALTER TABLE `facture_reservations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `follow`
--

DROP TABLE IF EXISTS `follow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `follow` (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `created_at` datetime NOT NULL,
                          `follower_id` int NOT NULL,
                          `following_id` int NOT NULL,
                          PRIMARY KEY (`id`),
                          KEY `IDX_68344470AC24F853` (`follower_id`),
                          KEY `IDX_683444701816E3A3` (`following_id`),
                          CONSTRAINT `FK_683444701816E3A3` FOREIGN KEY (`following_id`) REFERENCES `users` (`id`),
                          CONSTRAINT `FK_68344470AC24F853` FOREIGN KEY (`follower_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `follow`
--

LOCK TABLES `follow` WRITE;
/*!40000 ALTER TABLE `follow` DISABLE KEYS */;
INSERT INTO `follow` VALUES (1,'2026-02-21 01:47:16',4,3),(2,'2026-02-21 21:00:50',4,11),(3,'2026-02-21 21:01:49',4,17),(4,'2026-02-23 03:02:03',32,17),(5,'2026-02-23 05:01:17',32,16),(6,'2026-02-23 06:29:27',16,17),(9,'2026-02-23 07:10:56',16,41),(10,'2026-02-23 10:03:07',17,41);
/*!40000 ALTER TABLE `follow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ligne_order`
--

DROP TABLE IF EXISTS `ligne_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ligne_order` (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `price_at_purchase` decimal(10,2) NOT NULL,
                               `quantity` int NOT NULL,
                               `order_id` int NOT NULL,
                               `marketplace_post_id` int NOT NULL,
                               PRIMARY KEY (`id`),
                               KEY `IDX_F68579A88D9F6D38` (`order_id`),
                               KEY `IDX_F68579A8AF6FD030` (`marketplace_post_id`),
                               CONSTRAINT `FK_F68579A88D9F6D38` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
                               CONSTRAINT `FK_F68579A8AF6FD030` FOREIGN KEY (`marketplace_post_id`) REFERENCES `marketplace_post` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ligne_order`
--

LOCK TABLES `ligne_order` WRITE;
/*!40000 ALTER TABLE `ligne_order` DISABLE KEYS */;
INSERT INTO `ligne_order` VALUES (1,1200.00,3,4,6),(2,1200.00,1,5,6),(3,200.00,1,6,7),(6,200.00,1,10,7),(8,1200.00,1,12,6),(9,1100.00,1,13,9),(10,200.00,1,14,7),(11,200.00,1,15,7);
/*!40000 ALTER TABLE `ligne_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `marketplace_post`
--

DROP TABLE IF EXISTS `marketplace_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `marketplace_post` (
                                    `id` int NOT NULL AUTO_INCREMENT,
                                    `price` decimal(10,2) NOT NULL,
                                    `status` varchar(20) NOT NULL,
                                    `available_quantity` int NOT NULL DEFAULT '1',
                                    `sold_quantity` int NOT NULL DEFAULT '0',
                                    `product_type` varchar(20) NOT NULL DEFAULT 'original',
                                    `unlimited_stock` tinyint NOT NULL DEFAULT '0',
                                    `created_at` datetime NOT NULL,
                                    `updated_at` datetime DEFAULT NULL,
                                    `artwork_id` int NOT NULL,
                                    `seller_id` int NOT NULL,
                                    PRIMARY KEY (`id`),
                                    KEY `IDX_48F146EB8DE820D9` (`seller_id`),
                                    KEY `IDX_48F146EBDB8FFA4` (`artwork_id`),
                                    CONSTRAINT `FK_48F146EB8DE820D9` FOREIGN KEY (`seller_id`) REFERENCES `users` (`id`),
                                    CONSTRAINT `FK_48F146EBDB8FFA4` FOREIGN KEY (`artwork_id`) REFERENCES `artwork` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `marketplace_post`
--

LOCK TABLES `marketplace_post` WRITE;
/*!40000 ALTER TABLE `marketplace_post` DISABLE KEYS */;
INSERT INTO `marketplace_post` VALUES (6,1200.00,'active',4,0,'original',1,'2026-02-22 05:46:16',NULL,5,17),(7,200.00,'active',1,0,'original',1,'2026-02-22 06:46:01',NULL,4,3),(9,1100.00,'active',0,1,'original',0,'2026-02-22 21:05:43',NULL,10,41);
/*!40000 ALTER TABLE `marketplace_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messenger_messages`
--

DROP TABLE IF EXISTS `messenger_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messenger_messages` (
                                      `id` bigint NOT NULL AUTO_INCREMENT,
                                      `body` longtext NOT NULL,
                                      `headers` longtext NOT NULL,
                                      `queue_name` varchar(190) NOT NULL,
                                      `created_at` datetime NOT NULL,
                                      `available_at` datetime NOT NULL,
                                      `delivered_at` datetime DEFAULT NULL,
                                      PRIMARY KEY (`id`),
                                      KEY `IDX_75EA56E0FB7336F0E3BD61CE16BA31DBBF396750` (`queue_name`,`available_at`,`delivered_at`,`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messenger_messages`
--

LOCK TABLES `messenger_messages` WRITE;
/*!40000 ALTER TABLE `messenger_messages` DISABLE KEYS */;
/*!40000 ALTER TABLE `messenger_messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
                                `id` int NOT NULL AUTO_INCREMENT,
                                `type` varchar(50) NOT NULL,
                                `is_read` tinyint NOT NULL,
                                `created_at` datetime NOT NULL,
                                `user_id` int NOT NULL,
                                `source_user_id` int DEFAULT NULL,
                                `post_id` int DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                KEY `IDX_BF5476CAA76ED395` (`user_id`),
                                KEY `IDX_BF5476CAEEB16BFD` (`source_user_id`),
                                KEY `IDX_BF5476CA4B89032C` (`post_id`),
                                CONSTRAINT `FK_BF5476CAA76ED395` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                                CONSTRAINT `FK_BF5476CAEEB16BFD` FOREIGN KEY (`source_user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (20,'comment',0,'2026-02-22 01:24:24',38,4,42),(21,'like',0,'2026-02-22 01:50:42',38,4,42),(22,'like',1,'2026-02-22 14:11:12',4,17,48),(23,'follow',1,'2026-02-23 03:02:04',17,32,NULL),(24,'follow',1,'2026-02-23 05:01:17',16,32,NULL),(25,'follow',1,'2026-02-23 06:29:27',17,16,NULL),(26,'follow',0,'2026-02-23 07:03:08',14,16,NULL),(27,'follow',0,'2026-02-23 07:07:29',41,16,NULL),(28,'follow',0,'2026-02-23 07:10:56',41,16,NULL),(29,'like',0,'2026-02-23 07:54:37',4,47,84),(30,'follow',0,'2026-02-23 10:03:07',41,17,NULL),(31,'like',1,'2026-02-23 10:07:19',17,47,93);
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `total_amount` decimal(10,2) NOT NULL,
                          `created_at` datetime NOT NULL,
                          `buyer_id` int NOT NULL,
                          PRIMARY KEY (`id`),
                          KEY `IDX_E52FFDEE6C755722` (`buyer_id`),
                          CONSTRAINT `FK_E52FFDEE6C755722` FOREIGN KEY (`buyer_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (4,3600.00,'2026-02-22 06:01:06',40),(5,1200.00,'2026-02-22 06:43:47',40),(6,200.00,'2026-02-22 06:46:38',40),(8,1001.00,'2026-02-22 20:38:52',40),(9,1001.00,'2026-02-22 20:42:10',40),(10,200.00,'2026-02-22 20:46:35',40),(12,1200.00,'2026-02-22 21:32:13',40),(13,1100.00,'2026-02-23 10:31:47',47),(14,200.00,'2026-02-28 23:52:06',17),(15,200.00,'2026-03-01 02:22:17',17);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `planning`
--

DROP TABLE IF EXISTS `planning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `planning` (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `start_time` datetime NOT NULL,
                            `end_time` datetime NOT NULL,
                            `capacity` int NOT NULL,
                            `event_id` int NOT NULL,
                            `price` double NOT NULL,
                            PRIMARY KEY (`id`),
                            KEY `IDX_D499BFF671F7E88B` (`event_id`),
                            CONSTRAINT `FK_D499BFF671F7E88B` FOREIGN KEY (`event_id`) REFERENCES `events` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `planning`
--

LOCK TABLES `planning` WRITE;
/*!40000 ALTER TABLE `planning` DISABLE KEYS */;
INSERT INTO `planning` VALUES (3,'2026-02-14 10:00:00','2026-02-14 15:00:00',20,2,10),(4,'2026-02-15 00:00:00','2026-02-15 17:00:00',30,2,5),(8,'2026-02-17 22:00:00','2026-02-19 17:49:00',10,4,0),(9,'2026-02-18 10:07:00','2026-02-18 19:07:00',20,4,30),(12,'2026-02-14 16:30:00','2026-02-14 20:30:00',15,2,5);
/*!40000 ALTER TABLE `planning` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `content` longtext NOT NULL,
                        `created_at` datetime NOT NULL,
                        `updated_at` datetime DEFAULT NULL,
                        `author_id` int NOT NULL,
                        `image_url` varchar(255) DEFAULT NULL,
                        `status` varchar(20) NOT NULL DEFAULT 'published',
                        PRIMARY KEY (`id`),
                        KEY `IDX_5A8A6C8DF675F31B` (`author_id`),
                        CONSTRAINT `FK_5A8A6C8DF675F31B` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (42,'test test 123','2026-02-22 01:09:45',NULL,38,'/uploads/posts/699a494943d25.jpg','published'),(84,'aaaaaaaaaaaaa','2026-02-22 22:07:45',NULL,4,'/uploads/posts/699b7022887c0.jpg','published'),(92,'aaaaaaaaaaaaaaaaa','2026-02-23 09:52:45',NULL,16,'/uploads/posts/699c155de7770.jpg','published'),(93,'test validation','2026-02-23 10:00:42',NULL,17,'/uploads/posts/699c173b21fd7.jpg','published');
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_likes`
--

DROP TABLE IF EXISTS `post_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_likes` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `created_at` datetime NOT NULL,
                              `user_id` int NOT NULL,
                              `post_id` int NOT NULL,
                              PRIMARY KEY (`id`),
                              KEY `IDX_DED1C292A76ED395` (`user_id`),
                              KEY `IDX_DED1C2924B89032C` (`post_id`),
                              CONSTRAINT `FK_DED1C2924B89032C` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
                              CONSTRAINT `FK_DED1C292A76ED395` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_likes`
--

LOCK TABLES `post_likes` WRITE;
/*!40000 ALTER TABLE `post_likes` DISABLE KEYS */;
INSERT INTO `post_likes` VALUES (21,'2026-02-22 01:50:42',4,42),(25,'2026-02-22 22:08:03',4,84),(26,'2026-02-23 07:54:37',47,84),(27,'2026-02-23 09:53:03',16,92),(28,'2026-02-23 10:01:02',17,93),(29,'2026-02-23 10:07:19',47,93),(30,'2026-03-01 02:12:14',17,92);
/*!40000 ALTER TABLE `post_likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preference`
--

DROP TABLE IF EXISTS `preference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `preference` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `preference_key` varchar(100) NOT NULL,
                              `preference_value` longtext,
                              `created_at` datetime NOT NULL,
                              `updated_at` datetime DEFAULT NULL,
                              `user_id` int NOT NULL,
                              PRIMARY KEY (`id`),
                              KEY `IDX_5D69B053A76ED395` (`user_id`),
                              CONSTRAINT `FK_5D69B053A76ED395` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preference`
--

LOCK TABLES `preference` WRITE;
/*!40000 ALTER TABLE `preference` DISABLE KEYS */;
/*!40000 ALTER TABLE `preference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reclamations`
--

DROP TABLE IF EXISTS `reclamations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reclamations` (
                                `id` int NOT NULL AUTO_INCREMENT,
                                `subject` varchar(255) NOT NULL,
                                `description` longtext NOT NULL,
                                `status` varchar(20) NOT NULL,
                                `created_at` datetime NOT NULL,
                                `updated_at` datetime DEFAULT NULL,
                                `email` varchar(180) NOT NULL,
                                `categorie_id` int NOT NULL,
                                `reported_user_id` int DEFAULT NULL,
                                `reported_marketplace_post_id` int DEFAULT NULL,
                                `reported_artwork_id` int DEFAULT NULL,
                                `reported_post_id` int DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                KEY `IDX_1CAD6B76BCF5E72D` (`categorie_id`),
                                KEY `IDX_1CAD6B76E7566E` (`reported_user_id`),
                                KEY `IDX_1CAD6B76613CE19` (`reported_marketplace_post_id`),
                                KEY `IDX_1CAD6B7666B934B8` (`reported_artwork_id`),
                                KEY `IDX_POST_MODERATION` (`reported_post_id`),
                                CONSTRAINT `FK_1CAD6B76613CE19` FOREIGN KEY (`reported_marketplace_post_id`) REFERENCES `marketplace_post` (`id`) ON DELETE SET NULL,
                                CONSTRAINT `FK_1CAD6B7666B934B8` FOREIGN KEY (`reported_artwork_id`) REFERENCES `artwork` (`id`) ON DELETE SET NULL,
                                CONSTRAINT `FK_1CAD6B76BCF5E72D` FOREIGN KEY (`categorie_id`) REFERENCES `categorie_reclamation` (`id`),
                                CONSTRAINT `FK_1CAD6B76E7566E` FOREIGN KEY (`reported_user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL,
                                CONSTRAINT `FK_POST_MODERATION` FOREIGN KEY (`reported_post_id`) REFERENCES `post` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reclamations`
--

LOCK TABLES `reclamations` WRITE;
/*!40000 ALTER TABLE `reclamations` DISABLE KEYS */;
INSERT INTO `reclamations` VALUES (105,'Contenu Restreint (IA) - Nouveau Post #107','alpha123 cccccccccccccccccc','rejected','2026-03-01 15:52:15',NULL,'balkiss.youssfi@esprit.tn',34,38,NULL,NULL,NULL),(106,'Contenu Restreint (IA) - Nouveau Post #108','alpha123 aaaaaa','rejected','2026-03-01 15:53:16',NULL,'balkiss.youssfi@esprit.tn',34,38,NULL,NULL,NULL);
/*!40000 ALTER TABLE `reclamations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `reserved_at` datetime NOT NULL,
                               `status` varchar(20) NOT NULL,
                               `user_id` int NOT NULL,
                               PRIMARY KEY (`id`),
                               KEY `IDX_42C84955A76ED395` (`user_id`),
                               CONSTRAINT `FK_42C84955A76ED395` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation_planning`
--

DROP TABLE IF EXISTS `reservation_planning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation_planning` (
                                        `reservation_id` int NOT NULL,
                                        `planning_id` int NOT NULL,
                                        PRIMARY KEY (`reservation_id`,`planning_id`),
                                        KEY `IDX_492C5DE2B83297E7` (`reservation_id`),
                                        KEY `IDX_492C5DE23D865311` (`planning_id`),
                                        CONSTRAINT `FK_492C5DE23D865311` FOREIGN KEY (`planning_id`) REFERENCES `planning` (`id`) ON DELETE CASCADE,
                                        CONSTRAINT `FK_492C5DE2B83297E7` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation_planning`
--

LOCK TABLES `reservation_planning` WRITE;
/*!40000 ALTER TABLE `reservation_planning` DISABLE KEYS */;
/*!40000 ALTER TABLE `reservation_planning` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reset_password_request`
--

DROP TABLE IF EXISTS `reset_password_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reset_password_request` (
                                          `id` int NOT NULL AUTO_INCREMENT,
                                          `selector` varchar(20) NOT NULL,
                                          `hashed_token` varchar(100) NOT NULL,
                                          `requested_at` datetime NOT NULL,
                                          `expires_at` datetime NOT NULL,
                                          `user_id` int NOT NULL,
                                          PRIMARY KEY (`id`),
                                          KEY `IDX_7CE748AA76ED395` (`user_id`),
                                          CONSTRAINT `FK_7CE748AA76ED395` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reset_password_request`
--

LOCK TABLES `reset_password_request` WRITE;
/*!40000 ALTER TABLE `reset_password_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `reset_password_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `responses`
--

DROP TABLE IF EXISTS `responses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `responses` (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `message` longtext NOT NULL,
                             `created_at` datetime NOT NULL,
                             `reclamation_id` int NOT NULL,
                             `email_sent` tinyint NOT NULL DEFAULT '0',
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `UNIQ_315F9F942D6BA2D9` (`reclamation_id`),
                             CONSTRAINT `FK_315F9F942D6BA2D9` FOREIGN KEY (`reclamation_id`) REFERENCES `reclamations` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `responses`
--

LOCK TABLES `responses` WRITE;
/*!40000 ALTER TABLE `responses` DISABLE KEYS */;
/*!40000 ALTER TABLE `responses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `saved_post`
--

DROP TABLE IF EXISTS `saved_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `saved_post` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `created_at` datetime NOT NULL,
                              `user_id` int NOT NULL,
                              `post_id` int NOT NULL,
                              PRIMARY KEY (`id`),
                              KEY `IDX_54B59E98A76ED395` (`user_id`),
                              KEY `IDX_54B59E984B89032C` (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `saved_post`
--

LOCK TABLES `saved_post` WRITE;
/*!40000 ALTER TABLE `saved_post` DISABLE KEYS */;
INSERT INTO `saved_post` VALUES (5,'2026-02-22 03:15:42',4,43),(6,'2026-02-23 10:01:28',17,93);
/*!40000 ALTER TABLE `saved_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `email` varchar(180) DEFAULT NULL,
                         `roles` json NOT NULL,
                         `password` varchar(255) DEFAULT NULL,
                         `oauth_provider` varchar(255) DEFAULT NULL,
                         `oauth_id` varchar(255) DEFAULT NULL,
                         `oauth_access_token` varchar(255) DEFAULT NULL,
                         `oauth_refresh_token` varchar(255) DEFAULT NULL,
                         `first_name` varchar(100) NOT NULL,
                         `last_name` varchar(100) NOT NULL,
                         `username` varchar(50) NOT NULL,
                         `avatar` longtext,
                         `is_verified` tinyint NOT NULL,
                         `is_active` tinyint NOT NULL,
                         `created_at` datetime NOT NULL,
                         `updated_at` datetime DEFAULT NULL,
                         `user_type` varchar(255) NOT NULL,
                         `artist_name` varchar(255) DEFAULT NULL,
                         `profile_image` varchar(255) DEFAULT NULL,
                         `bio` longtext,
                         `specialty` varchar(100) DEFAULT NULL,
                         `years_of_experience` int DEFAULT '0',
                         `instagram` varchar(255) DEFAULT NULL,
                         `facebook` varchar(255) DEFAULT NULL,
                         `youtube` varchar(255) DEFAULT NULL,
                         `is_verified_artist` tinyint DEFAULT '0',
                         `rating` decimal(5,2) DEFAULT NULL,
                         `total_sales` int DEFAULT '0',
                         `face_descriptor` json DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `UNIQ_1483A5E9F85E0677` (`username`),
                         UNIQUE KEY `UNIQ_1483A5E9E7927C74` (`email`),
                         UNIQUE KEY `UNIQ_1483A5E97107339C` (`oauth_id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (2,'oumaima@gmail.com','[\"ROLE_USER\"]','yassine',NULL,NULL,NULL,NULL,'oumaima','oumaima','oumaima',NULL,0,1,'2021-01-01 00:00:00',NULL,'user',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,0,NULL),(3,NULL,'[\"ROLE_USER\", \"ROLE_ARTIST\"]',NULL,NULL,NULL,NULL,NULL,'malekra','lzk','malekghaze',NULL,0,1,'2021-01-01 00:00:00','2026-02-09 11:55:38','artist','malekgh',NULL,NULL,'Painting',0,NULL,NULL,NULL,0,NULL,0,NULL),(4,'ghabrimalek19@gmail.com','[\"ROLE_USER\"]','$2y$13$.PVDiYFs74364gM69DJhtOLBpkaY2VIo6rpWyYVBmFPI36Yg6dAsO',NULL,NULL,NULL,NULL,'yassine','kechida','yasski',NULL,1,1,'2026-02-07 19:30:00','2026-02-23 03:48:06','user',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,0,NULL),(6,'hamrounihmer@gmail.com','[\"ROLE_USER\", \"ROLE_ARTIST\"]','$2y$13$7cuu3E7Gwtxb0LM1ce.GoOqf0AhODODNPEcI9A/i/aQOdfHoRHoJa',NULL,NULL,NULL,NULL,'hamrouni','hmer','hmer',NULL,1,1,'2026-02-08 14:28:28',NULL,'user',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,0,NULL),(7,'malekellafi@gmail.com','[\"ROLE_USER\", \"ROLE_ARTIST\"]','$2y$13$4VEGSdcnUR0XutRlDCaRyuyZp5O08MI.VB10NZs2Rpo0.SfbPt1Wm',NULL,NULL,NULL,NULL,'ellafi','malek','elaffi',NULL,1,1,'2026-02-08 15:59:31',NULL,'user',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,0,NULL),(8,'mahadijaniour@gmail.com','[\"ROLE_USER\", \"ROLE_ARTIST\"]','$2y$13$uYXBtnpgTlzHe5TtVEBaUuHCN8vV08PJCl4XrXLO6Ww0ZHvMBObmu',NULL,NULL,NULL,NULL,'mahadi','janiour','mahadi',NULL,1,1,'2026-02-08 16:02:26',NULL,'user',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,0,NULL),(9,'malekghabri@gmail.com','[\"ROLE_USER\"]','$2y$13$IDRxj19yZnoBN6hhr5I3uuJkbva9JW.JCg9ZYKRE5uRHh5MuuVMWm',NULL,NULL,NULL,NULL,'malek','ghabri','mlk',NULL,0,1,'2026-02-09 10:08:16',NULL,'user',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,0,NULL),(10,'oumaima1@gmail.com','[\"ROLE_USER\"]','$2y$13$xrlDbbfWr6Jb4REzrmKoluLevdSyi0wXGstmR0nMFwSjNjBUWNgc.',NULL,NULL,NULL,NULL,'oumaima','hahaha','oumaimazh',NULL,0,0,'2026-02-09 11:13:13',NULL,'user',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,0,NULL),(11,'aem@gmail.com','[\"ROLE_USER\", \"ROLE_ARTIST\"]','$2y$13$DTw0Ggg/NE5AHAgTJuMCZOnzQVhU5ZHHDRQRL3Bsj8yymFsVSRfNa',NULL,NULL,NULL,NULL,'aem','aer','aer',NULL,0,1,'2026-02-09 11:57:08',NULL,'artist','azerty',NULL,NULL,'Painting',0,NULL,NULL,NULL,0,NULL,0,NULL),(14,'adembouafif77@gmail.com','[\"ROLE_USER\", \"ROLE_ARTIST\"]','$2y$13$5TAIqN8R2M0RL7zOJVLdpu5T1R2wlrAevTRnvC7l1B28oWt30XJrm',NULL,NULL,NULL,NULL,'ahmed','bouafif','ahmedb',NULL,0,1,'2026-02-11 19:22:29',NULL,'artist','ahmed','698cc8e53895a.jpg',NULL,'Poetry',2,NULL,NULL,NULL,0,2.00,0,NULL),(15,'adembouafif55@gmail.com','[\"ROLE_USER\"]','$2y$13$JIfvJ/yRM2zL.1QKIjMPaegkScBv.HdWSVAv7Jdmv37YG.kQ6WwJ2',NULL,NULL,NULL,NULL,'adem','bouafif','adem1',NULL,0,1,'2026-02-11 19:27:59',NULL,'user',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,0,NULL),(16,'adembouafif@gmail.com','[\"ROLE_USER\", \"ROLE_ARTIST\"]','$2y$13$B.o9vupbQjj.7p9Y.F/preFcVFJQgXIUZr9Zt02K6/dSn1dKejy3O',NULL,NULL,NULL,NULL,'adem','bouafif','adembh77',NULL,0,1,'2026-02-11 19:35:30','2026-02-23 06:31:33','artist','ademos77','699bf29dbe2b4.jpg',NULL,'Painting',5,NULL,NULL,NULL,1,2.00,0,'[-0.0937870666384697, 0.08024635910987854, 0.028153259307146072, -0.02303542196750641, -0.04293947294354439, -0.05822226032614708, -0.09661684185266496, -0.08532930910587311, 0.10852015763521194, -0.051382049918174744, 0.22941508889198303, -0.05565936490893364, -0.21376846730709076, -0.03050142340362072, -0.030514869838953015, 0.07890364527702332, -0.14229370653629303, -0.13333548605442047, -0.006637353450059891, -0.06881988048553467, 0.054385360330343246, -0.0158340223133564, 0.0580810084939003, 0.1388167142868042, -0.17663298547267914, -0.3111260235309601, -0.1198774054646492, -0.08526036888360977, 0.016009047627449036, -0.12000322341918944, -0.07180369645357132, 0.009567664004862309, -0.1544836163520813, -0.005160348489880562, 0.03083481267094612, 0.0812404602766037, -0.10806678980588912, -0.1394675225019455, 0.2350480854511261, 0.08624428510665894, -0.09479627758264542, -0.0065989140421152115, 0.05185940861701965, 0.2917570173740387, 0.1651136428117752, 0.10949595272541046, 0.051530808210372925, -0.030368151143193245, 0.11323095858097076, -0.21878798305988312, 0.06713713705539703, 0.18168482184410095, 0.125195249915123, 0.058233700692653656, 0.1055472493171692, -0.2074631303548813, 0.03289968520402908, 0.12587016820907593, -0.11873814463615416, 0.052710458636283875, -0.02419094368815422, 0.006698057055473328, 0.07176782935857773, -0.0585232712328434, 0.26177167892456055, 0.10114262998104095, -0.1358094960451126, -0.025009281933307648, 0.07210967689752579, -0.11187270283699036, -0.0528954342007637, 0.09928682446479796, -0.11545142531394958, -0.2296636700630188, -0.2175474315881729, 0.06911027431488037, 0.425653338432312, 0.15999120473861694, -0.10024292021989822, 0.022557945922017097, -0.014388205483555794, -0.08336131274700165, 0.041685596108436584, 0.06134717911481857, -0.19970333576202393, 0.0028857672587037086, -0.13104891777038574, 0.018589714542031288, 0.2095172107219696, 0.050385981798172, -0.04183715954422951, 0.16585950553417206, -0.031038781628012657, 0.08924366533756256, 0.03060660883784294, -0.013936631381511688, -0.1542384922504425, 0.03055460751056671, -0.05608003214001656, -0.00837930291891098, -0.026025064289569855, -0.07920985668897629, -0.01855413056910038, 0.0735262781381607, -0.1904679536819458, 0.05522448569536209, 0.009193464182317255, -0.08390720933675766, -0.10182436555624008, 0.05123189464211464, -0.1912059634923935, 0.0037461291067302223, 0.06937183439731598, -0.34594181180000305, 0.1084277555346489, 0.1687636822462082, 0.10909026116132736, 0.16827185451984406, 0.11601222306489944, 0.005666167475283146, 0.010033078491687776, -0.01379062421619892, -0.10242827981710434, -0.04608147218823433, 0.08490163087844849, -0.0011546919122338295, 0.11131042242050172, -0.0015389490872621536]'),(17,'yassine2@gmail.com','[\"ROLE_USER\", \"ROLE_ARTIST\", \"ROLE_ADMIN\"]','$2y$13$I/x25emnavtla5uUv6m/1.zr6f9JfV5ZdjpvH27T8JNsQHi38XZmS',NULL,NULL,NULL,NULL,'yassiner','yassine','yassine',NULL,1,1,'2026-02-12 15:41:43','2026-02-23 11:10:44','artist','yassinea','699c2783d4570.jpg',NULL,'Painting',10,NULL,NULL,NULL,0,5.00,0,'[-0.1927020251750946, 0.179635152220726, 0.052194152027368546, -0.11948076635599136, -0.1678931713104248, 0.06299314647912979, -0.07152344286441803, 0.020107507705688477, 0.13905905187129974, 0.04551133140921593, 0.15840476751327515, 0.037217091768980026, -0.248530313372612, 0.04421321675181389, -0.1124175861477852, 0.11997725069522858, -0.2432336658239365, -0.13319183886051178, -0.10620772838592528, -0.1571122258901596, 0.02491120807826519, 0.032090820372104645, 0.0010090582072734833, -0.016784347593784332, -0.18856094777584076, -0.2664989233016968, 0.0027733328752219677, -0.16532032191753387, 0.13837550580501556, -0.14729389548301697, 0.06848353892564774, -0.04127904400229454, -0.21149562299251556, -0.040528032928705215, 0.03849077969789505, 0.05927557870745659, -0.0020920108072459698, -0.05743686854839325, 0.1876632571220398, -0.011166369542479517, -0.1085592955350876, 0.08012281358242035, 0.0827157199382782, 0.33991438150405884, 0.14452826976776123, 0.051570337265729904, 0.034761834889650345, -0.03601445257663727, 0.1332249939441681, -0.2148749977350235, 0.1046467199921608, 0.1814042180776596, 0.10668444633483888, 0.05577722936868667, 0.10334591567516328, -0.18897347152233124, 0.059401948004961014, 0.10926531255245207, -0.1921636015176773, 0.15482258796691897, 0.05855584144592285, 0.09471888095140456, -0.008480887860059738, -0.04487825930118561, 0.1554706245660782, 0.05012611299753189, -0.11050456017255784, -0.17048326134681702, 0.11544004082679749, -0.11179780215024948, 0.014159612357616425, 0.09274894744157793, -0.13556380569934845, -0.13106012344360352, -0.22210845351219177, 0.05002552270889282, 0.41174858808517456, 0.13015075027942655, -0.17765483260154724, -0.020119065418839455, -0.039811570197343826, -0.09743352234363556, 0.0012849696213379502, 0.026518352329730988, -0.18876178562641144, 0.007678266149014235, -0.08868448436260223, 0.07351268082857132, 0.2270377278327942, 0.014117940329015257, -0.03398475795984268, 0.22084423899650577, 0.07548505812883377, -0.09398575127124786, -0.0012962146429345012, 0.08167847990989685, -0.19644814729690552, -0.06620179116725922, -0.10953596979379654, -0.08818154036998749, 0.048740070313215256, -0.13132300972938538, -0.0405430905520916, 0.13989795744419098, -0.18787042796611783, 0.09065866470336914, -0.017903560772538185, -0.020528972148895264, -0.1314060091972351, 0.08918172866106033, -0.1115475744009018, -0.05830978602170944, 0.2272091805934906, -0.15225790441036224, 0.21179072558879852, 0.2679561376571655, 0.044737499207258224, 0.042054273188114166, 0.11960940808057784, -0.032289497554302216, 0.048433348536491394, 0.08555067330598831, -0.041021108627319336, -0.11957640945911407, -0.04030243307352066, -0.09294768422842026, 0.03332239016890526, 0.04891045764088631]'),(32,'aminekadri1211@gmail.com','[\"ROLE_USER\", \"ROLE_ARTIST\"]','$2y$13$fzxXfPtJCTUp74d58RMEiOXKHd5BcDxwv4uqGL8jxUbyTTSXBkC8e',NULL,NULL,NULL,NULL,'kadriii','amine','gadour',NULL,0,1,'2026-02-19 23:39:55','2026-02-23 03:06:05','artist','amine','699bb60dba3a8.jpg',NULL,'Poetry',2,NULL,NULL,NULL,1,5.00,0,'[-0.16060751676559448, 0.1541600227355957, 0.00472781527787447, -0.02902553416788578, -0.05967903882265091, 0.021845269948244095, 0.035159606486558914, -0.06126345321536064, 0.18267112970352173, -0.028465066105127335, 0.2617293894290924, -0.024364836513996124, -0.19551469385623932, -0.047795649617910385, -0.014593251049518583, 0.0970441997051239, -0.12480729073286057, -0.1035422682762146, -0.07092996686697006, -0.06061685085296631, 0.11768777668476103, 0.060152579098939896, -0.03358891233801842, 0.11948823928833008, -0.182917982339859, -0.3310951888561249, -0.06048404052853584, -0.1451144814491272, -0.02515571191906929, -0.10707882046699524, 0.01030795555561781, 0.02606627717614174, -0.14238789677619934, -0.057296231389045715, 0.03807304799556732, 0.0567970871925354, -0.001805491279810667, 0.014924836345016956, 0.13330963253974917, 0.013487006537616251, -0.13792158663272858, 0.1406775563955307, 0.08979842066764832, 0.3254653215408325, 0.10662159323692322, 0.03789887577295303, 0.04381042346358299, -0.09386715292930604, 0.051736216992139816, -0.22873035073280337, 0.08036511391401291, 0.11011376231908798, 0.07285334169864655, 0.03579622134566307, 0.08638177812099457, -0.1885683238506317, -0.04302885755896568, 0.0929066464304924, -0.20529575645923617, 0.16701215505599976, 0.07436396926641464, -0.05243411660194397, -0.05464678257703781, -0.04126080870628357, 0.20750200748443604, 0.16856776177883148, -0.13205260038375854, -0.1468060463666916, 0.09204272925853728, -0.14342474937438965, -0.0641152411699295, 0.0466683954000473, -0.11169642955064774, -0.182855486869812, -0.26662278175354004, 0.1340290904045105, 0.4676048159599304, 0.13859397172927856, -0.17260682582855225, 0.07051046192646027, -0.11899923533201218, 0.04340727999806404, 0.037611883133649826, 0.030532047152519223, -0.13176098465919495, -0.01764177903532982, -0.09026003628969193, 0.08059820532798767, 0.1673697680234909, 0.08709385991096497, -0.03101353161036968, 0.2589245140552521, -0.011403342708945274, 0.004508425481617451, 0.05472170561552048, 0.023907167837023735, -0.143482506275177, -0.05310525745153427, -0.10138995945453644, -0.0421227365732193, -0.016614701598882675, -0.13743126392364502, 0.0430976040661335, 0.10749446600675584, -0.2632668614387512, 0.12244614958763124, -0.02872089482843876, -0.017307616770267487, -0.0022529875859618187, 0.14344240725040436, -0.11146966367959976, 0.022370606660842896, 0.12983909249305725, -0.27633360028266907, 0.223215714097023, 0.12330079078674316, 0.11661995947360992, 0.15878358483314514, 0.11251748353242874, 0.030730271711945537, 0.009621080942451954, 0.026567403227090836, -0.13385632634162903, -0.01231431495398283, 0.052473604679107666, -0.04244890436530113, 0.08410751819610596, 0.042270299047231674]'),(33,'testapi@gmail.com','[\"ROLE_USER\", \"ROLE_ARTIST\"]','$2y$13$D3WM5M1qaZUlvQnykd5yIexSMHijXLeFCQo8YMDRgKdlERI79QTVm',NULL,NULL,NULL,NULL,'test api','test api','testapi',NULL,0,1,'2026-02-20 00:23:24','2026-02-20 01:32:27','artist','Mohamed Test','6997ab9b7dba5.jpg',NULL,'Painting',0,NULL,NULL,NULL,1,5.00,0,NULL),(34,'pati@gmail.com','[\"ROLE_USER\", \"ROLE_ARTIST\"]','$2y$13$.C2KjGsyG/I9hMW4hoS52u.QA2EyDmSGtuKENvi1zEabcySlUwG6W',NULL,NULL,NULL,NULL,'pati','pati','pati',NULL,0,1,'2026-02-20 01:36:35','2026-02-20 01:38:28','artist','pati','6997ad03f384e.jpg',NULL,'Painting',0,NULL,NULL,NULL,1,5.00,0,NULL),(36,NULL,'[\"ROLE_USER\", \"ROLE_ARTIST\"]','$2y$13$FaVx2/cafGZo1DUMFSaxQ.YYwxJTlcMWJcEu6l52SgAkBk8vCobLS',NULL,NULL,NULL,NULL,'test3','test3@gmail.com','test3',NULL,0,1,'2026-02-21 04:48:31',NULL,'artist','test3','69992b0f11320.jpg',NULL,'Painting',0,NULL,NULL,NULL,0,NULL,0,NULL),(37,'test4@gmail.com','[\"ROLE_USER\", \"ROLE_ARTIST\"]','$2y$13$kufcjICjlbpI4z1oeBuliuopuNNErIluD4O2DRl/HoleOZNNQRgIG',NULL,NULL,NULL,NULL,'test4','test4','test4',NULL,0,1,'2026-02-21 04:50:21','2026-02-21 17:24:58','artist','test4','69992c2698bd6.jpg',NULL,'Painting',0,NULL,NULL,NULL,1,NULL,0,'[-0.1927020251750946, 0.179635152220726, 0.052194152027368546, -0.11948076635599136, -0.1678931713104248, 0.06299314647912979, -0.07152344286441803, 0.020107507705688477, 0.13905905187129974, 0.04551133140921593, 0.15840476751327515, 0.037217091768980026, -0.248530313372612, 0.04421321675181389, -0.1124175861477852, 0.11997725069522858, -0.2432336658239365, -0.13319183886051178, -0.10620772838592528, -0.1571122258901596, 0.02491120807826519, 0.032090820372104645, 0.0010090582072734833, -0.016784347593784332, -0.18856094777584076, -0.2664989233016968, 0.0027733328752219677, -0.16532032191753387, 0.13837550580501556, -0.14729389548301697, 0.06848353892564774, -0.04127904400229454, -0.21149562299251556, -0.040528032928705215, 0.03849077969789505, 0.05927557870745659, -0.0020920108072459698, -0.05743686854839325, 0.1876632571220398, -0.011166369542479517, -0.1085592955350876, 0.08012281358242035, 0.0827157199382782, 0.33991438150405884, 0.14452826976776123, 0.051570337265729904, 0.034761834889650345, -0.03601445257663727, 0.1332249939441681, -0.2148749977350235, 0.1046467199921608, 0.1814042180776596, 0.10668444633483888, 0.05577722936868667, 0.10334591567516328, -0.18897347152233124, 0.059401948004961014, 0.10926531255245207, -0.1921636015176773, 0.15482258796691897, 0.05855584144592285, 0.09471888095140456, -0.008480887860059738, -0.04487825930118561, 0.1554706245660782, 0.05012611299753189, -0.11050456017255784, -0.17048326134681702, 0.11544004082679749, -0.11179780215024948, 0.014159612357616425, 0.09274894744157793, -0.13556380569934845, -0.13106012344360352, -0.22210845351219177, 0.05002552270889282, 0.41174858808517456, 0.13015075027942655, -0.17765483260154724, -0.020119065418839455, -0.039811570197343826, -0.09743352234363556, 0.0012849696213379502, 0.026518352329730988, -0.18876178562641144, 0.007678266149014235, -0.08868448436260223, 0.07351268082857132, 0.2270377278327942, 0.014117940329015257, -0.03398475795984268, 0.22084423899650577, 0.07548505812883377, -0.09398575127124786, -0.0012962146429345012, 0.08167847990989685, -0.19644814729690552, -0.06620179116725922, -0.10953596979379654, -0.08818154036998749, 0.048740070313215256, -0.13132300972938538, -0.0405430905520916, 0.13989795744419098, -0.18787042796611783, 0.09065866470336914, -0.017903560772538185, -0.020528972148895264, -0.1314060091972351, 0.08918172866106033, -0.1115475744009018, -0.05830978602170944, 0.2272091805934906, -0.15225790441036224, 0.21179072558879852, 0.2679561376571655, 0.044737499207258224, 0.042054273188114166, 0.11960940808057784, -0.032289497554302216, 0.048433348536491394, 0.08555067330598831, -0.041021108627319336, -0.11957640945911407, -0.04030243307352066, -0.09294768422842026, 0.03332239016890526, 0.04891045764088631]'),(38,'balkiss.youssfi@esprit.tn','[\"ROLE_USER\", \"ROLE_ADMIN\"]','$2y$13$qWUG13PN3bFFh01GhjUctOmq/11K0s9J3VG06Ag6ys623LMxLyExq',NULL,NULL,NULL,NULL,'Balkiss','youssfi','balkiss',NULL,0,1,'2026-02-22 01:07:20',NULL,'user',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,0,NULL),(39,'zaghouani.oumaima19@gmail.com','[\"ROLE_USER\"]','$2y$13$H8odSXo4m0bNsvgSdJN2V.bkdmvNhWum8bpT5F8ncLG6JkfX9LAnG',NULL,NULL,NULL,NULL,'Oumaima','Zaghouani','oumaima19',NULL,0,1,'2026-02-22 03:58:28',NULL,'user',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,0,NULL),(40,'testuser@example.com','[\"ROLE_USER\"]','$2y$13$sGtPVcCkmq9ePOYVyyt9LOmgruAlzIc0gboxVtiNnEeNCpZ1x1dEG',NULL,NULL,NULL,NULL,'aaaa','aaaaa','testuser@example.com',NULL,0,1,'2026-02-22 05:35:25',NULL,'user',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,0,NULL),(41,'testuser12@example.com','[\"ROLE_USER\", \"ROLE_ARTIST\"]','$2y$13$ifecuUp.0HDZJgHjAKKcOurGyxzX0kriFprt4hNzqZXrq9MsS3JYq',NULL,NULL,NULL,NULL,'test','test','test',NULL,0,1,'2026-02-22 16:01:21','2026-02-22 16:01:46','artist','aaaa aaaaa','699b1a4146884.jpg',NULL,'Painting',4,NULL,NULL,NULL,1,5.00,0,NULL),(43,'admin@artera.local','[\"ROLE_USER\", \"ROLE_ADMIN\"]','$2y$13$e3hRxJD7V.bExA0ZOu/PQu.hOC5GUwVVkOuopBzwsAs/jl42A5x5O',NULL,NULL,NULL,NULL,'admin','Artera','admin',NULL,1,1,'2026-02-23 03:52:05','2026-02-23 04:01:13','user',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,0,NULL),(44,'yassinekechida974@gmail.com','[\"ROLE_USER\"]','$2y$13$bbU81Rw.W9J84amseCUxwOHzVFDXagW74HycrGnv/N3LRyOcmVeIe',NULL,NULL,NULL,NULL,'yassine','kechida','yassinek',NULL,0,1,'2026-02-23 03:54:15',NULL,'user',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,0,NULL),(45,'firas@test.com','[\"ROLE_USER\"]','$2y$13$72/RNkcJaJDGy/sG7/Rl5O9Y0PMGonXbR/W9V25boSXNQvUOjA.T2',NULL,NULL,NULL,NULL,'fyras','nyx','hackerman',NULL,0,1,'2026-02-23 04:02:54',NULL,'user',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,0,NULL),(46,'firaszx232@gmail.com','[\"ROLE_USER\"]','$2y$13$Aym.rPMduqREKPdz9VvdrO4f3.iKlZRWRSoywRH0viBp5fIkYhH5m',NULL,NULL,NULL,NULL,'fyras','firas','nyxfirras',NULL,1,1,'2026-02-23 04:04:39','2026-02-23 09:29:42','user',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,0,NULL),(47,'yassinekechida5@gmail.com','[\"ROLE_USER\", \"ROLE_ADMIN\"]','$2y$13$a2KZFNJvOxPGcQ1dVsKqL.WyGRfHS0H6HATN.1.cXesGB69G/N35K',NULL,NULL,NULL,NULL,'yassine','kechida','yassinek1',NULL,1,1,'2026-02-23 04:13:58','2026-02-23 10:22:41','user',NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,0,NULL);
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

-- Dump completed on 2026-04-01 11:43:16
