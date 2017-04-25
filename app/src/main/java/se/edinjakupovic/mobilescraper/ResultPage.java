package se.edinjakupovic.mobilescraper;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    private ListView sumList;
    private ArrayList resultSummaries;
    SummaryAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        final String Result = getIntent().getStringExtra(MainActivity.MESSAGE); // SearchTerm fecteedtrough intent
        sumList = (ListView) findViewById(R.id.sumList);

        new ParseUrl().execute(Result); // nonblocking



        sumList.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout t = (LinearLayout) view;
                TextView text = (TextView) t.findViewById(R.id.row_id);

                int lines = text.getMaxLines();
                if(lines == 1000){
                    text.setBackgroundColor(Color.parseColor("#ffffff"));
                    text.setMaxLines(5);
                }else{
                    text.setBackgroundColor(Color.parseColor("#b9f9b6"));
                    text.setMaxLines(1000);
                }
            }
        });
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

    ArrayList<String> threadSearch(ArrayList result){
        String Temp = result.toString();
        String set[] = Temp.split("\\s+");
        int numOfTreads = set.length/2;
        ArrayList<ThreadScrapeResult> threadResult = new ArrayList<>();
        ArrayList<String> threadSummeries = new ArrayList<>();

        List<Callable<ThreadScrapeResult>> callableTasks = new ArrayList<>();
        for(int i=0, j=0; j<set.length-1; i++,j+=2){
            callableTasks.add(new UrlRun(set[j],Double.parseDouble(set[j+1])));
        }

        ExecutorService executor = Executors.newFixedThreadPool(numOfTreads); // #of threads
        try {
            List<Future<ThreadScrapeResult>> futures = executor.invokeAll(callableTasks);
          //  System.out.println("Antal futures " +futures.size());
            for (Future futurex:futures) {
                threadResult.add((ThreadScrapeResult) futurex.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        for (ThreadScrapeResult x: threadResult) {
            if(x.getText().length() > 100){
                threadSummeries.add(x.getText());

            }
        }
        return threadSummeries;
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
            result = threadSearch(result);

            return result;
        }

        @Override
        protected void onPostExecute(ArrayList result){
            pdLoading.dismiss();

            System.out.println("AT POSTEXECUTE" + result.toString());
            resultSummaries = result;

            listAdapter = new SummaryAdapter(ResultPage.this, resultSummaries);
            sumList.setAdapter(listAdapter);


        }



    }



    /*
     * Recivies and displayes a error on the MainActivity
    * @param result Gets an error message as a string
    * */
    /*
    void handleError(String result){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MESSAGE,result);
        startActivity(intent);
    }*/

}
