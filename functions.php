<?php
include 'db.php';

function new_search($data){
  global $mysqli;


//  $urls = array_slice($data,2); // urls
//  $searchTerm = $data['search']; // The user search
  //$numOfLinks = $data['urls']; // Number of urls found


//  $term = $mysqli->real_escape_string($search);

//  $sql_searchterm =  ("INSERT INTO searchterm(term,noOfSearches) VALUES ('$term',1) ON DUPLICATE KEY UPDATE noOfSearches = noOfSearches+1");
  $sql_domain = ("INSERT IGNORE INTO domain(url,numOfHits) VALUES ('$data[2]',2)");

//  $mysqli->query($sql_searchterm);
  $mysqli->query($sql_domain);
}


 ?>
