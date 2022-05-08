<?php
$servername = "127.0.0.1";
$username= "root";
$password = "123456";
$dbname = "tankers";
//创建连接
$conn = new mysqli($servername,$username,$password,$dbname);
// @mysqli_set_charset($conn,utf8);
//检测连接
if($conn->connect_errno){
	die("连接失败：" . $conn->connect_errno);
}