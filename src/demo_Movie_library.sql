-- MySQL dump 10.13  Distrib 8.0.18, for macos10.14 (x86_64)
--
-- Host: 127.0.0.1    Database: demo
-- ------------------------------------------------------
-- Server version	8.0.18

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
-- Table structure for table `Movie_library`
--

DROP TABLE IF EXISTS `Movie_library`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Movie_library` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Title` varchar(64) DEFAULT NULL,
  `Year` varchar(64) DEFAULT NULL,
  `Genre` varchar(64) DEFAULT NULL,
  `RunTime` varchar(64) DEFAULT NULL,
  `Director` varchar(64) DEFAULT NULL,
  `Country` varchar(64) DEFAULT NULL,
  `Rate` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Movie_library`
--

LOCK TABLES `Movie_library` WRITE;
/*!40000 ALTER TABLE `Movie_library` DISABLE KEYS */;
INSERT INTO `Movie_library` VALUES (1,'Ted','2012','Comedy','106 min','Seth MacFarlane','USA',7),(2,'Harry Potter and the Deathly Hallows: Part 2','2011','Adventure, Drama, Fantasy, Mystery','130 min','David Yates','USA, UK',6),(3,'Avatar','2009','Action, Adventure, Fantasy, Sci-Fi','162 min','James Cameron','USA',5),(4,'StART','2010','Animation, Short','12 min','Goran Gomaz','Croatia',5),(6,'Ted 2','2015','Comedy','115 min','Seth MacFarlane','USA',5),(7,'Stuart Little','1999','Adventure, Comedy, Family, Fantasy','84 min','Rob Minkoff','Germany, USA',10),(8,'Robot Chicken: Star Wars III','2010','Animation, Comedy, Sci-Fi','45 min','Chris McKay','USA',8),(9,'Star Wars: Episode IV - A New Hope','1977','Action, Adventure, Fantasy, Sci-Fi','121 min','George Lucas','USA',5);
/*!40000 ALTER TABLE `Movie_library` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-01-01 13:41:12
