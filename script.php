<?php


include 'db.php';
include 'functions.php';

 // ['söktermen','länk1','länk2'..'länkn']
  if(isset($_POST['search'])){ // Http request


    $urls = array_slice($_POST,2); // urls
    new_search($urls);  // insert

  }

  // TODO
  // Stoppa in domän
  // stoppa in varje unika sökning
  // Ta fram emst relevant länkar för den sökninge
 // echoa ut dom mest relevanta




 ?>
