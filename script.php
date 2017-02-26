<?php
include 'db.php';
include 'functions.php';

  if(isset($_POST['search'])){ // Http request
    //echo $_POST['domainUrl0'];
    $links = $_POST['numOfLinks'];
    new_search($conn,$links);


    $x = 0;
    foreach ($_POST as $key => $value) {
    //  echo $x;
      $x++;
    }


    //new_search($conn);
  //  new_domain($conn);
  }






 ?>
