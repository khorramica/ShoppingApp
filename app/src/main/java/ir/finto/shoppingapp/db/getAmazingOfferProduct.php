<?php

include 'connection.php';

$query = " SELECT * FROM  product WHERE off=1 LIMIT 5  ";

$stmt = $connection->prepare($query);
$stmt->execute();

$product = array();

while($row = $stmt-> fetch(PDO::FETCH_ASSOC)){
	
	$rec = array();
	
	$value_off = $row['value_off'];
	$price = $row['price'];
	
	$totalofprice = $price * $value_off / 100;
	$finalprice = $price - $totalofprice;
	$rec['offprice'] = $finalprice;
	
	
	$rec['id'] = $row['id'];
	$rec['category_id'] = $row['category_id'];
	$rec['name'] = $row['name'];
	$rec['link_img'] = $row['link_img'];
	$rec['price'] = $row['price'];
	$rec['value_off'] = $row['value_off'];
	$rec['brand'] = $row['brand'];
	$product[] = $rec;
}

echo json_encode($product);

?>