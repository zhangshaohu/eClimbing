-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Dec 07, 2016 at 02:30 AM
-- Server version: 10.1.19-MariaDB
-- PHP Version: 5.6.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `iclimbing`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `employee_id` varchar(6) NOT NULL,
  `first_name` varchar(30) NOT NULL,
  `last_name` varchar(30) NOT NULL,
  `dob` date NOT NULL,
  `sex` varchar(10) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `email` varchar(30) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`employee_id`, `first_name`, `last_name`, `dob`, `sex`, `username`, `password`, `email`, `phone`, `start_date`) VALUES
('', 'Appala', 'Chekuri', '2016-11-01', 'male', 'anchekuri', '12345678', 'narasimharaju386@gmail.com', '3213128431', '2016-12-07 01:22:49');

-- --------------------------------------------------------

--
-- Table structure for table `checkin`
--

CREATE TABLE `checkin` (
  `checkin_id` int(11) NOT NULL,
  `checkin_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_type` varchar(20) NOT NULL,
  `patron_id` int(11) DEFAULT NULL,
  `first_name` varchar(30) DEFAULT NULL,
  `last_name` varchar(30) DEFAULT NULL,
  `walvor` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `checkin`
--

INSERT INTO `checkin` (`checkin_id`, `checkin_timestamp`, `user_type`, `patron_id`, `first_name`, `last_name`, `walvor`) VALUES
(1, '2016-11-27 09:21:09', 'Existing', 123456, NULL, NULL, NULL),
(3, '2016-11-27 09:34:22', 'Demographic', NULL, 'chekuri', 'appala', 'No'),
(4, '2016-11-27 09:38:43', 'Existing', 123456, NULL, NULL, NULL),
(6, '2016-11-27 12:11:24', 'Existing', 234235, NULL, NULL, NULL),
(8, '2016-11-27 12:23:43', 'Existing', 234236, NULL, NULL, NULL),
(9, '2016-11-27 12:27:22', 'New', 234237, NULL, NULL, NULL),
(10, '2016-11-27 13:09:03', 'Demographic', NULL, 'jo marzi', 'apko kia', ''),
(11, '2016-12-04 11:53:16', 'Existing', 234241, NULL, NULL, NULL),
(12, '2016-12-05 00:22:40', 'Demographic', NULL, 'haha', 'no', ''),
(14, '2016-12-05 01:54:27', 'Existing', 234240, NULL, NULL, NULL),
(15, '2016-12-06 00:35:41', 'Existing', 234243, NULL, NULL, NULL),
(16, '2016-12-07 00:32:17', 'Existing', 760782, NULL, NULL, NULL),
(17, '2016-12-07 00:33:04', 'Demographic', NULL, 'Appala', 'chekuri', 'Yes'),
(18, '2016-12-07 00:33:11', 'Demographic', NULL, 'Appala', 'chekuri', 'No'),
(19, '2016-12-07 01:25:02', 'Demographic', NULL, 'appala', 'chekuri', 'Yes');

-- --------------------------------------------------------

--
-- Table structure for table `class`
--

CREATE TABLE `class` (
  `class_id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `class_timing_start` time NOT NULL,
  `class_timing_end` time NOT NULL,
  `duration_start` date NOT NULL,
  `duration_end` date NOT NULL,
  `instructor` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `class`
--

INSERT INTO `class` (`class_id`, `name`, `class_timing_start`, `class_timing_end`, `duration_start`, `duration_end`, `instructor`) VALUES
(1, 'class', '12:00:00', '12:00:00', '2016-11-27', '2016-11-28', 'chattha'),
(2, 'class 2', '12:00:00', '02:00:00', '2016-11-11', '2016-12-12', 'Appala');

-- --------------------------------------------------------

--
-- Table structure for table `class_registrations`
--

CREATE TABLE `class_registrations` (
  `cr_id` int(11) NOT NULL,
  `patron_id` double NOT NULL,
  `name` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `instructor` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `class_registrations`
--

INSERT INTO `class_registrations` (`cr_id`, `patron_id`, `name`, `email`, `instructor`) VALUES
(2, 234235, 'test user', 'sourcecode777@gmail.com', 'Dr. Zhang');

-- --------------------------------------------------------

--
-- Table structure for table `employee`
--

CREATE TABLE `employee` (
  `employee_id` int(6) NOT NULL,
  `first_name` varchar(30) NOT NULL,
  `last_name` varchar(30) NOT NULL,
  `dob` date NOT NULL,
  `sex` varchar(10) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `email` varchar(30) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `employee`
--

INSERT INTO `employee` (`employee_id`, `first_name`, `last_name`, `dob`, `sex`, `username`, `password`, `email`, `phone`, `start_date`) VALUES
(123457, 'shaohu', 'zhang', '1992-08-14', 'male', 'tigersdsu', 'shaohuzhang', 'shaohu.zhang@sdstate.edu', '6055920499', '2016-12-07 00:23:29');

-- --------------------------------------------------------

--
-- Table structure for table `inventory`
--

CREATE TABLE `inventory` (
  `inventory_id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `inventory`
--

INSERT INTO `inventory` (`inventory_id`, `name`, `start_date`, `end_date`) VALUES
(1, 'Computer Table', '2016-12-04', '2016-12-28'),
(4, 'item 2', '2016-11-11', '2016-11-12');

-- --------------------------------------------------------

--
-- Table structure for table `patron`
--

CREATE TABLE `patron` (
  `patron_id` int(6) NOT NULL,
  `first_name` varchar(30) NOT NULL,
  `last_name` varchar(30) NOT NULL,
  `dob` datetime NOT NULL,
  `sex` varchar(10) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `email` varchar(30) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `payment` varchar(10) NOT NULL DEFAULT 'Not Done',
  `walvor` varchar(10) NOT NULL DEFAULT 'Not Done',
  `start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `end_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `semester` varchar(10) NOT NULL,
  `access` varchar(10) NOT NULL DEFAULT 'Yes',
  `notification` int(11) NOT NULL DEFAULT '1',
  `suspension_date` date DEFAULT NULL,
  `suspension_count` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `patron`
--

INSERT INTO `patron` (`patron_id`, `first_name`, `last_name`, `dob`, `sex`, `username`, `password`, `email`, `phone`, `payment`, `walvor`, `start_date`, `end_date`, `semester`, `access`, `notification`, `suspension_date`, `suspension_count`) VALUES
(113428, 'qwer', 'asdf', '2016-11-11 00:00:00', 'Male', 'qwerasdf', '12345678', 'qwer@asdf.com', '1234567890', 'Done', 'Not Done', '2016-12-05 23:31:23', '2016-12-05 23:31:23', 'Summer', 'Yes', 1, NULL, 0),
(234234, 'test', 'user', '2016-12-12 00:00:00', 'male', 'testuser2', '12345678', 'ainosoft@gmail.com', '12345678900', 'Done', 'Not Done', '2016-12-05 23:19:37', '2016-11-24 06:52:18', 'fall', 'yes', 1, NULL, 0),
(234235, 'test', 'user', '2016-12-12 00:00:00', 'Male', 'testuser2', '12345678', 'sourcecode777@gmail.com', '12345678900', 'Not Done', 'Not Done', '2016-11-30 20:43:49', '2016-11-27 12:07:26', 'Fall', 'Yes', 1, NULL, 0),
(234236, 'test', 'user', '2016-12-12 00:00:00', 'Male', 'testuser2', '12345678', 'mnouman2356@gmail.com', '12345678900', 'Not Done', 'Not Done', '2016-11-30 20:43:49', '2016-11-27 12:23:32', 'Fall', 'Yes', 1, NULL, 0),
(234237, 'test', 'user', '2016-12-12 00:00:00', 'Male', 'testuser2', '12345678', 'test@gmail.com', '12345678900', 'Not Done', 'Not Done', '2016-11-30 20:43:49', '2016-11-27 12:27:17', 'Fall', 'Yes', 1, NULL, 0),
(234238, 'test', 'user', '2016-12-12 00:00:00', 'Male', 'testuser2', '12345678', 'qwer@gmail.com', '12345678900', 'Not Done', 'Not Done', '2016-11-30 20:43:49', '2016-11-27 12:28:05', 'Fall', 'Yes', 1, NULL, 0),
(234239, 'test', 'user', '2016-12-12 00:00:00', 'Male', 'testuser2', '12345678', 'qwer@gmail.com', '12345678900', 'Not Done', 'Not Done', '2016-11-30 20:43:49', '2016-11-27 12:28:24', 'Fall', 'Yes', 1, NULL, 0),
(234240, 'test', 'user', '2016-12-12 00:00:00', 'Male', 'testuser2', '12345678', 'muajksd@gmail.com', '1234567890', 'Done', 'Done', '2016-12-05 01:54:05', '2016-11-27 12:46:29', 'Fall', 'Yes', 0, NULL, 0),
(234241, 'testing', 'users', '2016-12-13 00:00:00', 'Male', 'testuser22', '12345678', 'test2@gmail.com', '12345678900', 'Not Done', 'Done', '2016-12-05 01:43:12', '2016-11-27 12:51:20', 'Fall', 'denied', 0, '2016-12-05', 1),
(234242, 'hahaha', 'hahaha', '2016-10-10 00:00:00', 'Male', 'hahahaha', '12345678', 'hahaha@hahaha.com', '12345678900', 'Not Done', 'Done', '2016-12-04 22:30:22', '2016-12-04 17:01:56', 'Summer', 'denied', 1, '2016-12-05', 2),
(234243, 'abcd', 'efgh', '2016-11-11 00:00:00', 'Male', 'abcedffd', '12345678', 'abcdf@gmail.com', '1234567890', 'Done', 'Done', '2016-12-06 00:35:19', '2016-12-05 23:20:30', 'Fall', 'Yes', 1, NULL, 0),
(378073, 'qwer', 'asdf', '2016-11-11 00:00:00', 'Male', 'qwerasdf', '12345678', 'qwer@asdfm.com', '1234567890', 'Done', 'Not Done', '2016-12-05 23:31:45', '2016-12-05 23:31:45', 'Summer', 'Yes', 1, NULL, 0),
(408865, 'Hussain', 'Otudi', '1989-04-18 00:00:00', 'Male', 'hotudi2016', '12345678', 'Otudi.2015@gmail.com', '6055920069', 'Not Done', 'Done', '2016-12-07 00:51:10', '2016-12-07 00:48:42', 'Fall', 'Yes', 1, NULL, 0),
(760782, 'appala', 'chekuri', '1992-08-14 00:00:00', 'Male', 'anchekuri', 'appala2015', 'narasimharaju386@gmail.com', '3213128431', 'Done', 'Done', '2016-12-07 00:31:58', '2016-12-07 00:14:56', 'Fall', 'Yes', 1, NULL, 0);

-- --------------------------------------------------------

--
-- Table structure for table `reservation`
--

CREATE TABLE `reservation` (
  `reservation_id` int(11) NOT NULL,
  `type` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `from_date` datetime NOT NULL,
  `to_date` datetime NOT NULL,
  `duration` varchar(20) NOT NULL,
  `description` text NOT NULL,
  `status` varchar(10) NOT NULL DEFAULT 'Pending'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `reservation`
--

INSERT INTO `reservation` (`reservation_id`, `type`, `name`, `email`, `from_date`, `to_date`, `duration`, `description`, `status`) VALUES
(1, 'Patron', 'appala', 'appala@gmail.com', '2016-12-04 00:00:00', '2016-12-05 00:00:00', '1 Day', 'hi i need you to reserve a room for me.', 'Rejected'),
(2, 'Patron', 'abcd', 'abc@gmail.com', '2016-11-11 00:00:00', '2016-12-12 00:00:00', '2 days', 'hi i need a seperate room ', 'Accepted'),
(3, 'Patron', 'abcd', 'any@email.com', '2016-11-11 00:00:00', '2016-12-13 00:00:00', '2 days', 'this is test description', 'Rejected'),
(4, 'Patron', 'appala', 'narasimharaju386@gmail.com', '2016-12-25 00:00:00', '2016-12-26 00:00:00', 'chekuri', 'We want to register the wall climbing center for one day', 'Rejected'),
(5, 'Patron', 'Hussain', 'Otudi.2015@gmail.com', '2016-12-27 00:00:00', '2016-12-29 00:00:00', '3days', 'I want to reserve wall', 'Pending');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`employee_id`);

--
-- Indexes for table `checkin`
--
ALTER TABLE `checkin`
  ADD PRIMARY KEY (`checkin_id`);

--
-- Indexes for table `class`
--
ALTER TABLE `class`
  ADD PRIMARY KEY (`class_id`);

--
-- Indexes for table `class_registrations`
--
ALTER TABLE `class_registrations`
  ADD PRIMARY KEY (`cr_id`);

--
-- Indexes for table `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`employee_id`);

--
-- Indexes for table `inventory`
--
ALTER TABLE `inventory`
  ADD PRIMARY KEY (`inventory_id`);

--
-- Indexes for table `patron`
--
ALTER TABLE `patron`
  ADD PRIMARY KEY (`patron_id`);

--
-- Indexes for table `reservation`
--
ALTER TABLE `reservation`
  ADD PRIMARY KEY (`reservation_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `checkin`
--
ALTER TABLE `checkin`
  MODIFY `checkin_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;
--
-- AUTO_INCREMENT for table `class`
--
ALTER TABLE `class`
  MODIFY `class_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `class_registrations`
--
ALTER TABLE `class_registrations`
  MODIFY `cr_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `employee`
--
ALTER TABLE `employee`
  MODIFY `employee_id` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=123458;
--
-- AUTO_INCREMENT for table `inventory`
--
ALTER TABLE `inventory`
  MODIFY `inventory_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `reservation`
--
ALTER TABLE `reservation`
  MODIFY `reservation_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
