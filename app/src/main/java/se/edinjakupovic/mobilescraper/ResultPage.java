package se.edinjakupovic.mobilescraper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ResultPage extends AppCompatActivity {
    private TextView showInput;
    private static final String MESSAGE = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        showInput = (TextView) findViewById(R.id.textView);

        final String Result = getIntent().getStringExtra(MainActivity.MESSAGE); // SearchTerm fecteedtrough intent
        showInput.setText("");
        new ParseUrl().execute(Result); // bakrundstask som körs i trådar nonblocking
    }


    void threadSearch(ArrayList result){ // result.get(thread-length-1) == relevance mapping
        String Temp = result.toString();
        String set[] = Temp.split("\\s+");
        int numOfTreads = set.length/2;// splits on whitespace
        ArrayList<ThreadScrapeResult> xd = new ArrayList<>();


        List<Callable<ThreadScrapeResult>> callableTasks = new ArrayList<>();
        for(int i=0, j=0; j<set.length-1; i++,j+=2){
            callableTasks.add(new UrlRun(set[j],Double.parseDouble(set[j+1])));
        }

        ExecutorService executor = Executors.newFixedThreadPool(numOfTreads); // #of threads
        try {
            List<Future<ThreadScrapeResult>> futures = executor.invokeAll(callableTasks);
            for (Future futurex:futures) {
                xd.add((ThreadScrapeResult) futurex.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


       // for(int i=0; i< numOfTreads ; i++){
          //  ThreadScrapeResult xxd = xd.get(i)
        //}

        System.out.println("AAAAAAAAAAAAAAAAAAAAAA"+xd.get(3).getText());
        showInput.setText(xd.get(3).getText());


    }




    private class ParseUrl extends AsyncTask<String, Void, ArrayList<String>>{ // får in sökningen
        ProgressDialog pdLoading = new ProgressDialog(ResultPage.this);


        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdLoading.setMessage("\tSearching...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings){
            ArrayList<String> links; // Urls
            ArrayList<String> result;
            String searchTerm = strings[0];

            links = UrlGet.getLinks(searchTerm);  // Returns links as arraylist
            result = new DB().query(links,searchTerm); // Put search into database and returns most relavent links
            return result; // echo from php
        }

        @Override
        protected void onPostExecute(ArrayList result){ // Returns Url links + URL-Relevance-Mapping from database
            pdLoading.dismiss();
            if(result.toString().equalsIgnoreCase("error")){
                    handleError("Search failed"); // IF error send user back to search and display error message
            }else{
               threadSearch(result); // Create treads and webscrape links
            }
        }
    }








    void handleError(String result){
        Intent intent = new Intent(this, MainActivity.class);  // An intent is used to do something
        intent.putExtra(MESSAGE,result);  // Adds
        startActivity(intent);
    }

}
