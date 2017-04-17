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


  $stmt = $conn->prepare("INSERT IGNORE INTO search(relevance,d_id,s_id,url)
          SELECT 0.5,domain.id, searchterm.id,:search FROM domain,searchterm
          WHERE domain.id = (SELECT domain.id FROM domain WHERE url = :url) AND
          searchterm.id = (SELECT searchterm.id FROM searchterm where term = :term)");

        $stmt->bindParam(':term',$_POST['search']);
          for($x=0; $x < $links; $x++){
            $stmt->bindParam(':search', $_POST['searchUrl'.$x]);
            $stmt->bindParam(':url', $_POST['domainUrl'.$x]);
            $stmt->execute();
          }


}

 ?>
