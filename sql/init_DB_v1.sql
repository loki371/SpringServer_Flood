-- MySQL dump 10.13  Distrib 8.0.23, for macos10.15 (x86_64)
--
-- Host: localhost    Database: cuu_nan
-- ------------------------------------------------------
-- Server version	8.0.23

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
-- Table structure for table `authority_location_registration`
--

DROP TABLE IF EXISTS `authority_location_registration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `authority_location_registration` (
  `username` varchar(20) NOT NULL,
  `is_rejected` bit(1) NOT NULL,
  `location_id` char(6) DEFAULT NULL,
  `location_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authority_location_registration`
--

LOCK TABLES `authority_location_registration` WRITE;
/*!40000 ALTER TABLE `authority_location_registration` DISABLE KEYS */;
/*!40000 ALTER TABLE `authority_location_registration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment_types`
--

DROP TABLE IF EXISTS `comment_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment_types` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `e_comment_type` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_types`
--

LOCK TABLES `comment_types` WRITE;
/*!40000 ALTER TABLE `comment_types` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `registration_id` bigint DEFAULT NULL,
  `type_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcroelhj11ae6sgle7mug0l9m9` (`registration_id`),
  KEY `FKilx7llae0o5a1ph5vwqkmicej` (`type_id`),
  CONSTRAINT `FKcroelhj11ae6sgle7mug0l9m9` FOREIGN KEY (`registration_id`) REFERENCES `registrations` (`id`),
  CONSTRAINT `FKilx7llae0o5a1ph5vwqkmicej` FOREIGN KEY (`type_id`) REFERENCES `comment_types` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `districts`
--

DROP TABLE IF EXISTS `districts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `districts` (
  `id` char(6) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  `province_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK82doq1t64jhly7a546lpvnu2c` (`province_id`),
  CONSTRAINT `FK82doq1t64jhly7a546lpvnu2c` FOREIGN KEY (`province_id`) REFERENCES `provinces` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `districts`
--

LOCK TABLES `districts` WRITE;
/*!40000 ALTER TABLE `districts` DISABLE KEYS */;
/*!40000 ALTER TABLE `districts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorite_district`
--

DROP TABLE IF EXISTS `favorite_district`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorite_district` (
  `user_id` varchar(20) NOT NULL,
  `district_id` char(6) NOT NULL,
  KEY `FKkxfvl1pu5tbnw1yfc0idug47s` (`district_id`),
  KEY `FKcff8d1cxn31oacuj65csl1ck0` (`user_id`),
  CONSTRAINT `FKcff8d1cxn31oacuj65csl1ck0` FOREIGN KEY (`user_id`) REFERENCES `role_user` (`username`),
  CONSTRAINT `FKkxfvl1pu5tbnw1yfc0idug47s` FOREIGN KEY (`district_id`) REFERENCES `districts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorite_district`
--

LOCK TABLES `favorite_district` WRITE;
/*!40000 ALTER TABLE `favorite_district` DISABLE KEYS */;
/*!40000 ALTER TABLE `favorite_district` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorite_province`
--

DROP TABLE IF EXISTS `favorite_province`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorite_province` (
  `user_id` varchar(20) NOT NULL,
  `province_id` char(6) NOT NULL,
  KEY `FKbrspacl7xkaa7gflqphggsl54` (`province_id`),
  KEY `FKrcfxeucjcqo5i1uc01k0qol5j` (`user_id`),
  CONSTRAINT `FKbrspacl7xkaa7gflqphggsl54` FOREIGN KEY (`province_id`) REFERENCES `provinces` (`id`),
  CONSTRAINT `FKrcfxeucjcqo5i1uc01k0qol5j` FOREIGN KEY (`user_id`) REFERENCES `role_user` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorite_province`
--

LOCK TABLES `favorite_province` WRITE;
/*!40000 ALTER TABLE `favorite_province` DISABLE KEYS */;
/*!40000 ALTER TABLE `favorite_province` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorite_ward`
--

DROP TABLE IF EXISTS `favorite_ward`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorite_ward` (
  `user_id` varchar(20) NOT NULL,
  `ward_id` char(6) NOT NULL,
  KEY `FKge2xl1kmpkameyaarp2wen3qy` (`ward_id`),
  KEY `FKdr40x3lewqskxaajs871vblkq` (`user_id`),
  CONSTRAINT `FKdr40x3lewqskxaajs871vblkq` FOREIGN KEY (`user_id`) REFERENCES `role_user` (`username`),
  CONSTRAINT `FKge2xl1kmpkameyaarp2wen3qy` FOREIGN KEY (`ward_id`) REFERENCES `wards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorite_ward`
--

LOCK TABLES `favorite_ward` WRITE;
/*!40000 ALTER TABLE `favorite_ward` DISABLE KEYS */;
/*!40000 ALTER TABLE `favorite_ward` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flood_location`
--

DROP TABLE IF EXISTS `flood_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flood_location` (
  `ward_id` char(6) NOT NULL,
  PRIMARY KEY (`ward_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flood_location`
--

LOCK TABLES `flood_location` WRITE;
/*!40000 ALTER TABLE `flood_location` DISABLE KEYS */;
/*!40000 ALTER TABLE `flood_location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `provinces`
--

DROP TABLE IF EXISTS `provinces`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `provinces` (
  `id` char(6) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `provinces`
--

LOCK TABLES `provinces` WRITE;
/*!40000 ALTER TABLE `provinces` DISABLE KEYS */;
/*!40000 ALTER TABLE `provinces` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `registrations`
--

DROP TABLE IF EXISTS `registrations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `registrations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `e_state` int DEFAULT NULL,
  `latitude` float DEFAULT NULL,
  `longitude` float DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `num_person` int NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `create_by_username` varchar(20) DEFAULT NULL,
  `saved_by_username` varchar(20) DEFAULT NULL,
  `ward_id` char(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcw1ksvifbw02xoghtfjr7de9p` (`create_by_username`),
  KEY `FKkqfhlnayyfabhlotcibjb3y7h` (`saved_by_username`),
  KEY `FKqbx0pvr83a4mujvgobb55k3qj` (`ward_id`),
  CONSTRAINT `FKcw1ksvifbw02xoghtfjr7de9p` FOREIGN KEY (`create_by_username`) REFERENCES `users` (`username`),
  CONSTRAINT `FKkqfhlnayyfabhlotcibjb3y7h` FOREIGN KEY (`saved_by_username`) REFERENCES `users` (`username`),
  CONSTRAINT `FKqbx0pvr83a4mujvgobb55k3qj` FOREIGN KEY (`ward_id`) REFERENCES `wards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `registrations`
--

LOCK TABLES `registrations` WRITE;
/*!40000 ALTER TABLE `registrations` DISABLE KEYS */;
/*!40000 ALTER TABLE `registrations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `registrations_comment_list`
--

DROP TABLE IF EXISTS `registrations_comment_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `registrations_comment_list` (
  `registration_id` bigint NOT NULL,
  `comment_list_id` bigint NOT NULL,
  UNIQUE KEY `UK_pcis02hamq71wa9chlgxydmsh` (`comment_list_id`),
  KEY `FKrd2ucud3ik0hupc5bq9vfu9hh` (`registration_id`),
  CONSTRAINT `FK9mhn3ogp81dec1lnfwemnxawn` FOREIGN KEY (`comment_list_id`) REFERENCES `comments` (`id`),
  CONSTRAINT `FKrd2ucud3ik0hupc5bq9vfu9hh` FOREIGN KEY (`registration_id`) REFERENCES `registrations` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `registrations_comment_list`
--

LOCK TABLES `registrations_comment_list` WRITE;
/*!40000 ALTER TABLE `registrations_comment_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `registrations_comment_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rescuer_location_registration`
--

DROP TABLE IF EXISTS `rescuer_location_registration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rescuer_location_registration` (
  `username` varchar(20) NOT NULL,
  `is_rejected` bit(1) NOT NULL,
  `location_id` char(6) DEFAULT NULL,
  `location_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rescuer_location_registration`
--

LOCK TABLES `rescuer_location_registration` WRITE;
/*!40000 ALTER TABLE `rescuer_location_registration` DISABLE KEYS */;
/*!40000 ALTER TABLE `rescuer_location_registration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `road_points`
--

DROP TABLE IF EXISTS `road_points`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `road_points` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `latitude` float DEFAULT NULL,
  `longitude` float DEFAULT NULL,
  `serial` int NOT NULL,
  `road_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnd75mj0vika9ka77ysvitc9au` (`road_id`),
  CONSTRAINT `FKnd75mj0vika9ka77ysvitc9au` FOREIGN KEY (`road_id`) REFERENCES `roads` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `road_points`
--

LOCK TABLES `road_points` WRITE;
/*!40000 ALTER TABLE `road_points` DISABLE KEYS */;
/*!40000 ALTER TABLE `road_points` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roads`
--

DROP TABLE IF EXISTS `roads`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roads` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ward_id` char(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkemwar1wnjuhhbxgrlcy7k30j` (`ward_id`),
  CONSTRAINT `FKkemwar1wnjuhhbxgrlcy7k30j` FOREIGN KEY (`ward_id`) REFERENCES `wards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roads`
--

LOCK TABLES `roads` WRITE;
/*!40000 ALTER TABLE `roads` DISABLE KEYS */;
/*!40000 ALTER TABLE `roads` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_authority`
--

DROP TABLE IF EXISTS `role_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_authority` (
  `username` varchar(20) NOT NULL,
  `farther_username` varchar(20) DEFAULT NULL,
  `ward_id` char(6) DEFAULT NULL,
  PRIMARY KEY (`username`),
  KEY `FKt03aii6jya9r4hi5ddxxj4xin` (`farther_username`),
  KEY `FK4xtyye9ml3xp6hkm7pjmgkbdq` (`ward_id`),
  CONSTRAINT `FK4xtyye9ml3xp6hkm7pjmgkbdq` FOREIGN KEY (`ward_id`) REFERENCES `wards` (`id`),
  CONSTRAINT `FKt03aii6jya9r4hi5ddxxj4xin` FOREIGN KEY (`farther_username`) REFERENCES `role_authority` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_authority`
--

LOCK TABLES `role_authority` WRITE;
/*!40000 ALTER TABLE `role_authority` DISABLE KEYS */;
/*!40000 ALTER TABLE `role_authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_rescuer`
--

DROP TABLE IF EXISTS `role_rescuer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_rescuer` (
  `username` varchar(20) NOT NULL,
  `score` float DEFAULT NULL,
  `in_saving_id` bigint DEFAULT NULL,
  `ward_id` char(6) DEFAULT NULL,
  PRIMARY KEY (`username`),
  KEY `FK5tmkh3kc45k7slkkotj57jddv` (`in_saving_id`),
  KEY `FK5shrm3hwc99gfhths2bmum32r` (`ward_id`),
  CONSTRAINT `FK5shrm3hwc99gfhths2bmum32r` FOREIGN KEY (`ward_id`) REFERENCES `wards` (`id`),
  CONSTRAINT `FK5tmkh3kc45k7slkkotj57jddv` FOREIGN KEY (`in_saving_id`) REFERENCES `registrations` (`id`),
  CONSTRAINT `FKo5fv8wyncxcqwn3fdcdu6321v` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_rescuer`
--

LOCK TABLES `role_rescuer` WRITE;
/*!40000 ALTER TABLE `role_rescuer` DISABLE KEYS */;
/*!40000 ALTER TABLE `role_rescuer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_user`
--

DROP TABLE IF EXISTS `role_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_user` (
  `username` varchar(20) NOT NULL,
  PRIMARY KEY (`username`),
  CONSTRAINT `FK9uoybf6aoobaem8e7ulimn28l` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_user`
--

LOCK TABLES `role_user` WRITE;
/*!40000 ALTER TABLE `role_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `role_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_volunteer`
--

DROP TABLE IF EXISTS `role_volunteer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_volunteer` (
  `username` varchar(20) NOT NULL,
  `score` float DEFAULT NULL,
  `ward_id` char(6) DEFAULT NULL,
  PRIMARY KEY (`username`),
  KEY `FKhwimufhm63bm1vikpqyk7drum` (`ward_id`),
  CONSTRAINT `FKhwimufhm63bm1vikpqyk7drum` FOREIGN KEY (`ward_id`) REFERENCES `wards` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_volunteer`
--

LOCK TABLES `role_volunteer` WRITE;
/*!40000 ALTER TABLE `role_volunteer` DISABLE KEYS */;
/*!40000 ALTER TABLE `role_volunteer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscriber_registration`
--

DROP TABLE IF EXISTS `subscriber_registration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subscriber_registration` (
  `user_id` varchar(20) NOT NULL,
  `registration_id` bigint NOT NULL,
  KEY `FK3v63ugqxe6skh2n9oa2vxarmt` (`registration_id`),
  KEY `FK4r03hs067j8tacedmrxu4jxly` (`user_id`),
  CONSTRAINT `FK3v63ugqxe6skh2n9oa2vxarmt` FOREIGN KEY (`registration_id`) REFERENCES `registrations` (`id`),
  CONSTRAINT `FK4r03hs067j8tacedmrxu4jxly` FOREIGN KEY (`user_id`) REFERENCES `role_user` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscriber_registration`
--

LOCK TABLES `subscriber_registration` WRITE;
/*!40000 ALTER TABLE `subscriber_registration` DISABLE KEYS */;
/*!40000 ALTER TABLE `subscriber_registration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `user_username` varchar(20) NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`user_username`,`role_id`),
  KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`),
  CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FKs9rxtuttxq2ln7mtp37s4clce` FOREIGN KEY (`user_username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `username` varchar(20) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `firstname` varchar(50) DEFAULT NULL,
  `lastname` varchar(50) DEFAULT NULL,
  `password` varchar(120) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`username`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `volunteer_location_registration`
--

DROP TABLE IF EXISTS `volunteer_location_registration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `volunteer_location_registration` (
  `username` varchar(20) NOT NULL,
  `is_rejected` bit(1) NOT NULL,
  `location_id` char(6) DEFAULT NULL,
  `location_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `volunteer_location_registration`
--

LOCK TABLES `volunteer_location_registration` WRITE;
/*!40000 ALTER TABLE `volunteer_location_registration` DISABLE KEYS */;
/*!40000 ALTER TABLE `volunteer_location_registration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wards`
--

DROP TABLE IF EXISTS `wards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wards` (
  `id` char(6) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  `district_id` char(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfjqt744bo800mb5uax74lav8k` (`district_id`),
  CONSTRAINT `FKfjqt744bo800mb5uax74lav8k` FOREIGN KEY (`district_id`) REFERENCES `districts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wards`
--

LOCK TABLES `wards` WRITE;
/*!40000 ALTER TABLE `wards` DISABLE KEYS */;
/*!40000 ALTER TABLE `wards` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-04-07 22:39:21
