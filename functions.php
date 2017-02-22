<?php
include 'db.php';

function new_search($search){
  global $mysqli;
  $term = $mysqli->real_escape_string($search);

  $sql =  ("INSERT INTO searchterm(term,noOfSearches) VALUES ('$term',1) ON DUPLICATE KEY UPDATE noOfSearches = noOfSearches+1");
  $mysqli->query($sql);
}



 ?>
