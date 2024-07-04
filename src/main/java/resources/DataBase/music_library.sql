-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 04-07-2024 a las 14:59:47
-- Versión del servidor: 10.4.27-MariaDB
-- Versión de PHP: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `music_library`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `songs`
--

CREATE TABLE `songs` (
  `id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `artist` varchar(255) NOT NULL,
  `duration` int(11) NOT NULL,
  `file_path` varchar(255) NOT NULL,
  `play_count` int(11) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `songs`
--

INSERT INTO `songs` (`id`, `title`, `artist`, `duration`, `file_path`, `play_count`) VALUES
(6, 'La Espada Sagrada.MP3', 'Artista Desconocido', 150, 'C:\\xampp\\htdocs\\JavaAvanzado\\MyMusic\\src\\main\\java\\resources\\music\\La Espada Sagrada.MP3', 2),
(7, 'LEPROSY y KENNY LA DE LOS ELÉCTRICOS No podrán parar el tren (Video oficial) [1].MP3', 'Artista Desconocido', 198, 'C:\\xampp\\htdocs\\JavaAvanzado\\MyMusic\\src\\main\\java\\resources\\music\\LEPROSY y KENNY LA DE LOS ELÉCTRICOS No podrán parar el tren (Video oficial) [1].MP3', 3),
(8, 'music.mp3', 'Artista Desconocido', 336, 'C:\\xampp\\htdocs\\JavaAvanzado\\MyMusic\\src\\main\\java\\resources\\music\\music.mp3', 1),
(9, 'Paz en la Tormenta.MP3', 'Artista Desconocido', 358, 'C:\\xampp\\htdocs\\JavaAvanzado\\MyMusic\\src\\main\\java\\resources\\music\\Paz en la Tormenta.MP3', 2),
(10, 'Que sea rock - Que sea rock - Riff.MP3', 'Artista Desconocido', 280, 'C:\\xampp\\htdocs\\JavaAvanzado\\MyMusic\\src\\main\\java\\resources\\music\\Que sea rock - Que sea rock - Riff.MP3', 2);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `songs`
--
ALTER TABLE `songs`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `songs`
--
ALTER TABLE `songs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
