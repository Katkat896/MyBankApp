-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 08, 2025 at 09:29 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bankappdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `id` int(11) NOT NULL,
  `user_number` varchar(20) NOT NULL,
  `type` varchar(100) NOT NULL,
  `amount` decimal(12,2) NOT NULL,
  `date` datetime DEFAULT current_timestamp(),
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`id`, `user_number`, `type`, `amount`, `date`, `user_id`) VALUES
(1, '09350047189', 'Cash In', 500.00, '2025-08-05 00:00:00', 2),
(2, '09350047189', 'Transfer to 09111111111', 100.00, '2025-08-05 00:38:07', 2),
(3, '09111111111', 'Received from 09350047189', 100.00, '2025-08-05 00:38:07', 3),
(4, '09350047189', 'Cash In', 1000.00, '2025-08-05 00:00:00', 2),
(5, '09350047189', 'Transfer to 09111111111', 650.00, '2025-08-05 00:55:05', 2),
(6, '09111111111', 'Received from 09350047189', 650.00, '2025-08-05 00:55:05', 3),
(7, '09111111111', 'Cash In', 5000.00, '2025-08-05 00:00:00', 3),
(8, '09350047189', 'Transfer to 09111111111', 700.00, '2025-08-05 01:13:34', 2),
(9, '09111111111', 'Received from 09350047189', 700.00, '2025-08-05 01:13:34', 3),
(10, '09350047189', 'Cash In', 1000.00, '2025-08-05 00:00:00', 2),
(11, '09333333333', 'Cash In', 5000.00, '2025-08-05 00:00:00', 5),
(12, '09333333333', 'Transfer to 09111111111', 250.00, '2025-08-05 18:32:53', 5),
(13, '09111111111', 'Received from 09333333333', 250.00, '2025-08-05 18:32:53', 3),
(14, '09222222222', 'Cash In', 20000.00, '2025-08-05 00:00:00', 4),
(15, '09222222222', 'Transfer to 09350047189', 150.00, '2025-08-05 18:39:31', 4),
(16, '09350047189', 'Received from 09222222222', 150.00, '2025-08-05 18:39:31', 2),
(17, '09222222222', 'Cash In', 500.00, '2025-08-05 00:00:00', 4),
(18, '09350047189', 'Transfer to 09555555555', 2000.00, '2025-08-05 18:57:38', 2),
(19, '09555555555', 'Received from 09350047189', 2000.00, '2025-08-05 18:57:38', 7),
(20, '09111111111', 'Cash In', 5000.00, '2025-08-05 00:00:00', 3),
(21, '09222222222', 'Cash In', 2000.00, '2025-08-05 00:00:00', 4),
(22, '09333333333', 'Cash In', 5000.00, '2025-08-05 00:00:00', 5),
(23, '09333333333', 'Transfer to 09350047189', 500.00, '2025-08-05 19:09:07', 5),
(24, '09350047189', 'Received from 09333333333', 500.00, '2025-08-05 19:09:07', 2),
(25, '09555555555', 'Cash In', 20000.00, '2025-08-05 00:00:00', 7),
(26, '09111111111', 'Cash In', 5000.00, '2025-08-05 00:00:00', 3),
(27, '09444444444', 'Cash In', 20000.00, '2025-08-05 00:00:00', 6),
(28, '09444444444', 'Transfer to 09555555555', 500.00, '2025-08-05 19:17:50', 6),
(29, '09555555555', 'Received from 09444444444', 500.00, '2025-08-05 19:17:50', 7),
(30, '09333333333', 'Cash In', 5000.00, '2025-08-05 19:21:03', 5),
(31, '09333333333', 'Cash In', 500.00, '2025-08-05 19:52:30', 5),
(32, '09350047189', 'Cash In', 2000.00, '2025-08-05 23:40:04', 2),
(33, '09350047189', 'Transfer to 09555555555', 5000.00, '2025-08-05 23:40:16', 2),
(34, '09555555555', 'Received from 09350047189', 5000.00, '2025-08-05 23:40:16', 7),
(35, '09666666666', 'Cash In', 20000.00, '2025-08-06 01:28:29', 8),
(36, '09666666666', 'Transfer to 09555555555', 500.00, '2025-08-06 01:28:58', 8),
(37, '09555555555', 'Received from 09666666666', 500.00, '2025-08-06 01:28:58', 7),
(38, '09350047189', 'Cash In', 1000.00, '2025-08-07 01:03:58', 2),
(39, '09350047189', 'Transfer to 09666666666', 2000.00, '2025-08-07 01:04:12', 2),
(40, '09666666666', 'Received from 09350047189', 2000.00, '2025-08-07 01:04:12', 8),
(41, '09350047189', 'Cash In', 1500.00, '2025-08-07 01:34:52', 2),
(42, '09350047189', 'Transfer to 09222222222', 200.00, '2025-08-07 01:35:07', 2),
(43, '09222222222', 'Received from 09350047189', 200.00, '2025-08-07 01:35:07', 4),
(44, '09111111111', 'Cash In', 500.00, '2025-08-07 04:16:00', 3),
(45, '09111111111', 'Transfer to 09666666666', 250.00, '2025-08-07 04:16:17', 3),
(46, '09666666666', 'Received from 09111111111', 250.00, '2025-08-07 04:16:17', 8),
(47, '09666666666', 'Cash In', 5000.00, '2025-08-07 20:07:54', 8),
(48, '09666666666', 'Transfer to 09350047189', 500.00, '2025-08-07 20:08:05', 8),
(49, '09350047189', 'Received from 09666666666', 500.00, '2025-08-07 20:08:05', 2),
(50, '09666666666', 'Cash In', 500.00, '2025-08-07 20:47:52', 8),
(51, '09666666666', 'Transfer to 09111111111', 2000.00, '2025-08-07 20:48:00', 8),
(52, '09111111111', 'Received from 09666666666', 2000.00, '2025-08-07 20:48:00', 3),
(53, '09555555555', 'Cash In', 250.00, '2025-08-07 21:03:17', 7),
(54, '09555555555', 'Transfer to 09666666666', 560.00, '2025-08-07 21:03:34', 7),
(55, '09666666666', 'Received from 09555555555', 560.00, '2025-08-07 21:03:34', 8),
(56, '09444444444', 'Cash In', 5000.00, '2025-08-07 21:13:48', 6),
(57, '09444444444', 'Transfer to 09666666666', 600.00, '2025-08-07 21:14:21', 6),
(58, '09666666666', 'Received from 09444444444', 600.00, '2025-08-07 21:14:21', 8),
(59, '09333333333', 'Transfer to 09350047189', 250.00, '2025-08-07 21:18:14', 5),
(60, '09350047189', 'Received from 09333333333', 250.00, '2025-08-07 21:18:14', 2),
(61, '09111111111', 'Transfer to 09444444444', 400.00, '2025-08-07 21:33:42', 3),
(62, '09444444444', 'Received from 09111111111', 400.00, '2025-08-07 21:33:42', 6),
(63, '09350047189', 'Cash In', 500.00, '2025-08-07 22:06:19', 2),
(64, '09350047189', 'Transfer to 09111111111', 100.00, '2025-08-07 22:06:34', 2),
(65, '09111111111', 'Received from 09350047189', 100.00, '2025-08-07 22:06:34', 3),
(66, '09111111111', 'Cash In', 100.00, '2025-08-07 22:52:20', 3);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `number` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `pin` varchar(255) NOT NULL,
  `balance` decimal(12,2) NOT NULL DEFAULT 0.00,
  `role` enum('user','admin') NOT NULL DEFAULT 'user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `number`, `email`, `pin`, `balance`, `role`) VALUES
(1, 'Admin', '09999999999', 'admin@mlbb.com\r\n', '1234', 0.00, 'admin'),
(2, 'Kath Manaloto', '09350047189', 'kathmanaloto@mlbb.com', '0000', 48150.00, 'user'),
(3, 'Hanabi', '09111111111', 'fireflowerhanabi@mlbb.com', '1111', 39250.00, 'user'),
(4, 'Miya', '09222222222', 'moonlightarchermiya@mlbb.com', '2222', 22550.00, 'user'),
(5, 'Layla', '09333333333', 'energygunnerlayla@mlbb.com', '3333', 14500.00, 'user'),
(6, 'Badang', '09444444444', 'tribalwarriorbadang@mlbb.com', '4444', 24300.00, 'user'),
(7, 'Franco', '09555555555', 'frozenwarriorfranco@mlbb.com', '5555', 27690.00, 'user'),
(8, 'Hayabusa', '09666666666', 'crimsonshadowhayabusa@mlbb.com', '6666', 25910.00, 'user'),
(9, 'Balmond', '09777777777', 'bloodybeastbalmond@mlbb.com', '7777', 0.00, 'user');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_number` (`user_number`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD UNIQUE KEY `number` (`number`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=67;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`user_number`) REFERENCES `users` (`number`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
