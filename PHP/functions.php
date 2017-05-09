<?php
include 'db.php';


function return_UrlRelevanceMapping($conn){
    $stmt = $conn->prepare("SELECT url,relevance
                            FROM search
                            WHERE s_id = (SELECT searchterm.id FROM searchterm WHERE
                                          term = :term) ORDER BY 'relevance' LIMIT 10");
    $stmt->execute([':term' => $_POST['search']]);
    while($row = $stmt->fetch(PDO::FETCH_ASSOC)){
      echo $row['url']." ".$row['relevance']." ";

    }


}

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

        $stmt->bindParam(':term',$_POST['search']); // stops
          for($x=0; $x < $links; $x++){
            $stmt->bindParam(':search', $_POST['searchUrl'.$x]);
            $stmt->bindParam(':url', $_POST['domainUrl'.$x]);
            $stmt->execute();
          }
}


function test($conn){
  $stmt = $conn->prepare("UPDATE search SET relevance = 1
                          WHERE d_id = '3637'");
  $stmt->execute();
}

function updateRelevancy($conn,$upvotes,$downvotes){

  $stmt = $conn->prepare("UPDATE search SET relevance = relevance+0.01
                        WHERE s_id = (SELECT searchterm.id FROM searchterm WHERE term = :s_id)
                        AND d_id = (SELECT domain.id FROM domain WHERE url = :d_id)
                        AND url = :url");
            for($x=0; $x < $upvotes; $x++){
              $stmt->bindParam(':url',$_POST['upvoteURL'.$x]);
              $stmt->bindParam(':d_id',$_POST['upvoteDomain'.$x]);
              $stmt->bindParam(':s_id',$_POST['searchTerm']);
              $stmt->execute();
            }

  $stmt = $conn->prepare("UPDATE search SET relevance = relevance-0.01
                        WHERE s_id = (SELECT searchterm.id FROM searchterm WHERE term = :s_id)
                        AND d_id = (SELECT domain.id FROM domain WHERE url = :d_id)
                        AND url = :url");
            for($x=0; $x < $downvotes; $x++){
              $stmt->bindParam(':url',$_POST['downvoteURL'.$x]);
              $stmt->bindParam(':d_id',$_POST['downvoteDomain'.$x]);
              $stmt->bindParam(':s_id',$_POST['searchTerm']);
              $stmt->execute();
            }


}

 ?>
