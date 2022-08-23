<?php $file_path = "/home/local/ASUAD/akamzin/cse535/files/";

$asu_id = $_POST['id'] . "/";
$accept = $_POST['accept'];

$pass = isset($_POST['id']) && isset($_POST['accept']) && !empty($_POST['id']) && !empty($_POST['accept']);

if(!$pass){
    echo "fail";
    return;
}

if($accept == 1){
    $file_path = $file_path . $asu_id . "accept/";
} else {
    $file_path = $file_path . $asu_id . "reject/";
}

if(!file_exists($file_path)){
    mkdir($file_path, 0777, true);
}

$file = $file_path . basename($_FILES['uploaded_file']['name']);

if (move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file)) {
	echo "Uploaded";
} else {
    echo "fail";
}