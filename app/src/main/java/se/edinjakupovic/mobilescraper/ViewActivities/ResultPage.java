package se.edinjakupovic.mobilescraper.ViewActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import se.edinjakupovic.mobilescraper.DTOs.RelevanceUpdateDTO;
import se.edinjakupovic.mobilescraper.DatabaseQueries.DB;
import se.edinjakupovic.mobilescraper.R;
import se.edinjakupovic.mobilescraper.ListHandling.SummaryAdapter;
import se.edinjakupovic.mobilescraper.WebScraping.ThreadScrapeResult;
import se.edinjakupovic.mobilescraper.DatabaseQueries.UppdateRelevancys;
import se.edinjakupovic.mobilescraper.WebScraping.UrlGet;
import se.edinjakupovic.mobilescraper.WebScraping.UrlRun;
import se.edinjakupovic.mobilescraper.DTOs.UrlSummaryDTO;

/**
* ResultPage.java - Page reached after a search is initiated.
* Also where most of the computations are made
*
* @author Edin Jakupovic
* @version 1.0
* */
public class ResultPage extends AppCompatActivity {
    private ListView sumList;
    private String searchTerm;
    private HashMap<String,RelevanceUpdateDTO> swipeTracker = new HashMap<>();
    String passToResult;
    SummaryAdapter listAdapter;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        Button backButton = (Button) findViewById(R.id.backButton);
        Button showResultButton = (Button) findViewById(R.id.showResultButton);

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                newSearch();
            }
        });

        showResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!swipeTracker.isEmpty()){
                    new UppdateRelevancys(swipeTracker,searchTerm).execute();
                    toResultPage();
                }

            }
        });




        searchTerm = getIntent().getStringExtra(MainActivity.MESSAGE); // SearchTerm fecteedtrough intent
        sumList = (ListView) findViewById(R.id.sumList);

        new ParseUrl().execute(searchTerm); // nonblocking

        sumList.setOnTouchListener(new View.OnTouchListener(){
            private  float x1,y1,t1;
            private String DomainUrl;
            private String Url;
            RelativeLayout clicked;
            TextView clicktex;
            TextView urlTV;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        y1 = event.getY();
                        t1 = System.currentTimeMillis();
                        return true;
                    case MotionEvent.ACTION_UP:
                        float x2 = event.getX();
                        float y2 = event.getY();
                        float t2 = System.currentTimeMillis();

                        if (x1 == x2 && y1 == y2 && (t2 - t1) < 100) {

                            int firstPosition = sumList.getFirstVisiblePosition();
                            int ListPos = sumList.pointToPosition((int)x1,(int)y1);
                            int childPosition = ListPos-firstPosition;

                            clicked = (RelativeLayout) sumList.getChildAt(childPosition);
                            if(clicked != null) {
                                clicktex =(TextView) clicked.findViewById(R.id.row_id);
                                if(clicktex.getMaxLines() == 1000){
                                    clicktex.setMaxLines(5);
                                }else {
                                    clicktex.setMaxLines(1000);
                                }
                            }

                        } else if (x1 > x2+150) {

                            int firstPosition = sumList.getFirstVisiblePosition();
                            int ListPos = sumList.pointToPosition((int)x1,(int)y1);
                            int childPosition = ListPos -firstPosition;

                            clicked = (RelativeLayout) sumList.getChildAt(childPosition); //swipe left remove
                            urlTV = (TextView) clicked.findViewById(R.id.urlsource);

                            if(clicked != null) {clicktex =(TextView) clicked.findViewById(R.id.row_id);
                                if(clicktex.getMaxLines() == 5){
                                    Url = urlTV.getText().toString().substring(8);
                                    DomainUrl = UrlGet.getSingleDomain(Url);
                                    removeFromMap(clicktex,DomainUrl,Url);
                                    clicked.setBackgroundColor(Color.parseColor("#f9bbb6"));
                                }
                            }

                        } else if (x2 > x1+150) {

                            int firstPosition = sumList.getFirstVisiblePosition();
                            int ListPos = sumList.pointToPosition((int)x1,(int)y1);
                            int childPosition = ListPos -firstPosition;

                            clicked = (RelativeLayout) sumList.getChildAt(childPosition); //swipe right add

                            if(clicked != null) {
                                clicktex =(TextView) clicked.findViewById(R.id.row_id);
                                urlTV = (TextView) clicked.findViewById(R.id.urlsource);
                                if(clicktex.getMaxLines() == 5){
                                    Url = urlTV.getText().toString().substring(8);
                                    DomainUrl = UrlGet.getSingleDomain(Url);
                                    addToMap(clicktex,DomainUrl,Url);
                                    clicked.setBackgroundColor(Color.parseColor("#b9f9b6"));
                                }
                            }
                        }
                        return true;
                }
                return false;
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




    ArrayList<UrlSummaryDTO> threadSearch(ArrayList result){
        String Temp = result.toString();
        String set[] = Temp.split("\\s+");
        int numOfTreads = set.length/2;
        ArrayList<ThreadScrapeResult> threadResult = new ArrayList<>();
        ArrayList<UrlSummaryDTO> threadSummeries = new ArrayList<>();

        List<Callable<ThreadScrapeResult>> callableTasks = new ArrayList<>();
        for(int i=0, j=0; j<set.length-1; i++,j+=2){
            callableTasks.add(new UrlRun(set[j],Double.parseDouble(set[j+1]))); // (url , relevance)
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
                threadSummeries.add(new UrlSummaryDTO(x.getText(),x.getUrl(),x.getRelevance()));

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
    private class ParseUrl extends AsyncTask<String, Void, ArrayList<UrlSummaryDTO>>{
        ProgressDialog pdLoading = new ProgressDialog(ResultPage.this);

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdLoading.setMessage("\tSearching...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected ArrayList<UrlSummaryDTO> doInBackground(String... strings){
            ArrayList<String> links;
            ArrayList<String> domains;
            ArrayList<String> result;
            ArrayList<UrlSummaryDTO> sumResult;
            String searchTerm = strings[0];

            links = UrlGet.getLinks(searchTerm);  // Returns links full url
            domains = UrlGet.getDomain(links);
            result = new DB().query(links,searchTerm,domains);

            sumResult = threadSearch(result);

            return sumResult;
        }

        @Override
        protected void onPostExecute(ArrayList result){
            pdLoading.dismiss();

            listAdapter = new SummaryAdapter(ResultPage.this, sortResult(result));
            sumList.setAdapter(listAdapter);
        }
    }



    void addToMap(TextView input,String DomainUrl,String Url){
        if(input != null){
            String text = input.getText().toString(); //contains summary
            swipeTracker.put(DomainUrl,new RelevanceUpdateDTO(Url,DomainUrl,text,1));
        }
    }

    void removeFromMap(TextView input,String DomainUrl,String Url){
        if(input != null){
            String text = input.getText().toString();
            swipeTracker.put(DomainUrl,new RelevanceUpdateDTO(Url,DomainUrl,text,0));
        }

    }

    ArrayList<UrlSummaryDTO> sortResult(ArrayList<UrlSummaryDTO> input){
        Collections.sort(input,Collections.<UrlSummaryDTO>reverseOrder());
        return input;
    }

    void newSearch(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    void toResultPage(){



        Intent intent = new Intent(this,FinalResultPage.class);
        intent.putExtra("map",passToResult);
        startActivity(intent);
    }
}
