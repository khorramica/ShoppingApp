<?php

include 'connection.php';

$query = " SELECT * FROM  category LIMIT 5 ";

$stmt = $connection->prepare($query);
$stmt->execute();

$slider = array();

while($row = $stmt-> fetch(PDO::FETCH_ASSOC)){
	
	$banner = array();
	$banner['id'] = $row['id'];
	$banner['title'] = $row['title'];
	$banner['link_img'] = $row['link_img'];
	$slider[] = $banner;
}

echo json_encode($slider);

?>