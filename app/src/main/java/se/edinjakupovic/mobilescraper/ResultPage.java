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

/**
* ResultPage.java - Page reached after a search is initiated.
* Also where most of the computations are made
*
* @author Edin Jakupovic
* @version 1.0
* */
public class ResultPage extends AppCompatActivity {
    private TextView showInput;
    private ProgressDialog progressDialog;

    private static final String MESSAGE = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        showInput = (TextView) findViewById(R.id.textView);
        final String Result = getIntent().getStringExtra(MainActivity.MESSAGE); // SearchTerm fecteedtrough intent
        showInput.setText("");
        new ParseUrl().execute(Result); // nonblocking
    }

    /**
     * Creates a thread for each searchresult from google. Up
     * to 10 threads. An ExecutorService collects the ThreadScrapeResult
     * from each thread. The result is sorted by relevance and displayed to ui
     *
     * @param result Contains the string returned from the DB @see DB
     *
     *
     */

    void threadSearch(ArrayList result){
        progressDialog = ProgressDialog.show(ResultPage.this, "", "Loading...");

        String Temp = result.toString();
        String set[] = Temp.split("\\s+");
        int numOfTreads = set.length/2;
        ArrayList<ThreadScrapeResult> summaries = new ArrayList<>();


        List<Callable<ThreadScrapeResult>> callableTasks = new ArrayList<>();
        for(int i=0, j=0; j<set.length-1; i++,j+=2){
            callableTasks.add(new UrlRun(set[j],Double.parseDouble(set[j+1])));
        }

        ExecutorService executor = Executors.newFixedThreadPool(numOfTreads); // #of threads
        try {
            List<Future<ThreadScrapeResult>> futures = executor.invokeAll(callableTasks);
          //  System.out.println("Antal futures " +futures.size());
            for (Future futurex:futures) {
                summaries.add((ThreadScrapeResult) futurex.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        for (ThreadScrapeResult x: summaries) {
            if(x.getText().length() > 20){
                showInput.append(x.getText() + "\n\n");

            }
            System.out.println("After tread: "+x.getText());
        }
        progressDialog.dismiss();

    }


    /**
     * Performs the initial search to google and starts the
     * threads which scrape the results.
     * Input in doInBackground(String... string) is from user search
     *
     */
    private class ParseUrl extends AsyncTask<String, Void, ArrayList<String>>{
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
            ArrayList<String> links;
            ArrayList<String> result;
            String searchTerm = strings[0];

            links = UrlGet.getLinks(searchTerm);
            result = new DB().query(links,searchTerm);
            return result; // echo from php
        }

        @Override
        protected void onPostExecute(ArrayList result){
            pdLoading.dismiss();
            if(result.toString().equalsIgnoreCase("error")){
                    handleError("Search failed");
            }else{
               threadSearch(result);
            }
        }
    }


    /**
     * Recivies and displayes a error on the MainActivity
    * @param result Gets an error message as a string
    * */
    void handleError(String result){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MESSAGE,result);
        startActivity(intent);
    }

}
