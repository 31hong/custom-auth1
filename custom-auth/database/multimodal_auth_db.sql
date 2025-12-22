-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: multimodal_auth_db
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
-- Table structure for table `identity_proof`
--

DROP TABLE IF EXISTS `identity_proof`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `identity_proof` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `phone_number_hash` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '电话号码哈希（根节点）',
  `device_fingerprint_hash` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '设备指纹哈希（第一层融合）',
  `final_credential_hash` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '最终凭证哈希（最终结果）',
  `creation_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '凭证创建时间',
  `last_update_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '凭证最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_phone_number_hash` (`phone_number_hash`) COMMENT '确保每个电话号码哈希的唯一性'
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='移动码号为中心的身份凭证证据链';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `identity_proof`
--

LOCK TABLES `identity_proof` WRITE;
/*!40000 ALTER TABLE `identity_proof` DISABLE KEYS */;
INSERT INTO `identity_proof` VALUES (1,'a3411b8b6d17398c3c56b8ea15a75a81c081447909be70fd5618f42f1b77f044','e4d339e2bffbb787e693a6ef2cb4a649c27fd684a74d20f171b9c674d9c93a1a','966c4778b4ccddf14bd7e8ab30b77e51073dd2f22aa60c8ee07db6890827ac97','2025-10-13 09:00:42','2025-10-13 09:00:42'),(2,'bbc3ee1a9e7cc555504a469887a8f94b5e5f4f8ce130cb3aa4085f22878cb227','e4d339e2bffbb787e693a6ef2cb4a649c27fd684a74d20f171b9c674d9c93a1a','52b16d17c103ef291c64f997190fd7d3b62c4107a6f930b6c25cba18a784a5df','2025-10-13 09:10:27','2025-10-13 09:10:27'),(3,'fff724dd3417f6aae435d614e97b52ddd6ad23b0fbc0c143a229ab9c260d175b','e4d339e2bffbb787e693a6ef2cb4a649c27fd684a74d20f171b9c674d9c93a1a','1034733ac0df5684e5128bff0da95c7b8cf648f186f9eb2cc041ae689f4173bf','2025-10-13 09:24:00','2025-10-13 09:24:00'),(4,'29850e5ae718cd074775ae60c62f7744ad0f2d03e41130111f2a8d125eaa6036','e4d339e2bffbb787e693a6ef2cb4a649c27fd684a74d20f171b9c674d9c93a1a','fd8a904b5098999790d795cbc8b0c320bcf0ce0941ccc1827f7657f214c28b20','2025-10-13 09:33:35','2025-10-13 09:33:35'),(5,'fb9cc5d04285f53d10f1928fd414b67498ecd4252d337278d9d78987d3bd9962','e4d339e2bffbb787e693a6ef2cb4a649c27fd684a74d20f171b9c674d9c93a1a','a081caebcdb8c1629fbf598ee62a940af654e998f9fe2ba4dc27beb478323cfc','2025-10-13 09:37:18','2025-10-13 09:37:18');
/*!40000 ALTER TABLE `identity_proof` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-19 15:52:55
