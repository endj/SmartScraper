<?php
include 'db.php';
include 'functions.php';

  if(isset($_POST['search'])){

    $links = $_POST['numOfLinks'];
    new_search($conn,$links);

    return_UrlRelevanceMapping($conn); // Return most relevant searches for searchterm

  }






 ?>
