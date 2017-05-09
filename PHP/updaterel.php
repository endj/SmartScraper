<?php
include 'db.php';
include 'functions.php';


  $upvotes = $_POST['upvoteSize'];
  $downvotes = $_POST['downvoteSize'];


  updateRelevancy($conn,$upvotes,$downvotes);









 ?>
