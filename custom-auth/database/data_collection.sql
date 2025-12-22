-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: data_collection
-- ------------------------------------------------------
-- Server version	8.0.37

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
-- Table structure for table `hardware_info`
--

DROP TABLE IF EXISTS `hardware_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hardware_info` (
  `hardware_id` bigint NOT NULL AUTO_INCREMENT,
  `device_name` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `serial_number` varchar(255) DEFAULT NULL,
  `imei_primary` varchar(255) DEFAULT NULL,
  `imei_secondary` varchar(255) DEFAULT NULL,
  `cpu_architecture` varchar(255) DEFAULT NULL,
  `memory_size` varchar(255) DEFAULT NULL,
  `baseband_version` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`hardware_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hardware_info`
--

LOCK TABLES `hardware_info` WRITE;
/*!40000 ALTER TABLE `hardware_info` DISABLE KEYS */;
INSERT INTO `hardware_info` VALUES (16,'TYH232U','TYH232U','TYH232UA2B34000076','861231060026514','861231060026522','arm64-v8a','7.70 GB','5G_MODEM_V2_22C_W23.51.4|ums9620_modem,5G_MODEM_V2_22C_W23.51.4|ums9620_modem','2025-04-28 03:06:29'),(17,'TYH232U','TYH232U','TYH232UA2B34000076','861231060026514','861231060026522','arm64-v8a','7.70 GB','5G_MODEM_V2_22C_W23.51.4|ums9620_modem,5G_MODEM_V2_22C_W23.51.4|ums9620_modem','2025-04-28 03:30:23'),(18,'TYH232U','TYH232U','TYH232UA2B34000076','861231060026514','861231060026522','arm64-v8a','7.70 GB','5G_MODEM_V2_22C_W23.51.4|ums9620_modem,5G_MODEM_V2_22C_W23.51.4|ums9620_modem','2025-04-28 03:37:58');
/*!40000 ALTER TABLE `hardware_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sim_info`
--

DROP TABLE IF EXISTS `sim_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sim_info` (
  `sim_id` bigint NOT NULL AUTO_INCREMENT,
  `imsi` varchar(255) DEFAULT NULL,
  `imsi2` varchar(255) DEFAULT NULL,
  `iccid` varchar(255) DEFAULT NULL,
  `iccid2` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `phone_number2` varchar(255) DEFAULT NULL,
  `collection_time` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`sim_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sim_info`
--

LOCK TABLES `sim_info` WRITE;
/*!40000 ALTER TABLE `sim_info` DISABLE KEYS */;
INSERT INTO `sim_info` VALUES (11,'460014386207475','null','89860124801235810283','','13264381896','null','2025-04-28 19:06:42','2025-04-28 03:06:46'),(12,'460014386207475','null','89860124801235810283','','13264381896','null','2025-04-28 19:38:02','2025-04-28 03:38:06');
/*!40000 ALTER TABLE `sim_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `software_info`
--

DROP TABLE IF EXISTS `software_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `software_info` (
  `software_id` bigint NOT NULL AUTO_INCREMENT,
  `os_name` varchar(255) DEFAULT NULL,
  `os_version` varchar(255) DEFAULT NULL,
  `android_id` varchar(255) DEFAULT NULL,
  `kernel_version` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`software_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `software_info`
--

LOCK TABLES `software_info` WRITE;
/*!40000 ALTER TABLE `software_info` DISABLE KEYS */;
INSERT INTO `software_info` VALUES (8,'Android','11','c332be596eba476','4.14.199','2025-04-28 03:06:42'),(9,'Android','11','c332be596eba476','4.14.199','2025-04-28 03:38:05');
/*!40000 ALTER TABLE `software_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-19 15:52:09
