<?php
include 'db.php';

function new_search($conn,$links){

  $stmt = $conn->prepare("INSERT INTO searchterm(term,noOfSearches) VALUES (:term, 1)
                          ON DUPLICATE KEY UPDATE noOfSearches = noOfSearches+1");
      $stmt->execute([':term' => $_POST['search']]);

  $stmt = $conn->prepare("INSERT INTO domain(url,numOfHits) VALUES (:url,1)
                          ON DUPLICATE KEY UPDATE numOfHits = numOfHits+1");
      for($x=0;  $x < $links; $x++){
          $stmt->execute([':url' => $_POST['domainUrl'.$x]]);
      }


}

 ?>
