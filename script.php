<?php


include 'db.php';
include 'functions.php';



  if(isset($_POST['search'])){ // Http request
    $string = $_POST['search'];
    new_search($string);
    echo $string." bazinga";
  }





 ?>
