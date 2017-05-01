package se.edinjakupovic.mobilescraper.ViewActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import se.edinjakupovic.mobilescraper.DTOs.RelevanceUpdateDTO;
import se.edinjakupovic.mobilescraper.DatabaseQueries.QuerySearch;
import se.edinjakupovic.mobilescraper.R;
import se.edinjakupovic.mobilescraper.ListHandling.SummaryAdapter;
import se.edinjakupovic.mobilescraper.DatabaseQueries.UppdateRelevancys;
import se.edinjakupovic.mobilescraper.WebScraping.ThreadSearch;
import se.edinjakupovic.mobilescraper.WebScraping.UrlGet;
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
                    for (RelevanceUpdateDTO x: swipeTracker.values()) {
                        if(x.getRelevance() == 1 && x.getSummaryText() != null){
                            passToResult += x.getSummaryText();
                        }
                    }
                    new UppdateRelevancys(swipeTracker,searchTerm).execute();
                    toResultPage();
                }

            }
        });




        searchTerm = getIntent().getStringExtra(MainActivity.MESSAGE); // SearchTerm fecteedtrough intent
        sumList = (ListView) findViewById(R.id.sumList);

        new ParseUrl().execute(searchTerm); // nonblocking


        /*
         * Used to detect if a user is swiping left/right or clicking
         * Updates which values to update in the database when swiping
         * Changes the item which is touched on ACTION_DOWN
         */
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
                            Log.d("CLICK","Swipe DETECTED");

                            int firstPosition = sumList.getFirstVisiblePosition();
                            int ListPos = sumList.pointToPosition((int)x1,(int)y1);
                            int childPosition = ListPos-firstPosition;

                            clicked = (RelativeLayout) sumList.getChildAt(childPosition);
                            if(clicked != null) {
                                clicktex =(TextView) clicked.findViewById(R.id.row_id);
                                if(clicktex.getMaxLines() == 1000){
                                    clicktex.setMaxLines(5);
                                }else if(clicktex.getLineCount() > 5){
                                    clicktex.setMaxLines(1000);
                                }
                            }

                        } else if (x1 > x2+150) {
                            Log.d("Left","Swipe DETECTED");

                            int firstPosition = sumList.getFirstVisiblePosition();
                            int ListPos = sumList.pointToPosition((int)x1,(int)y1);
                            int childPosition = ListPos -firstPosition;


                            clicked = (RelativeLayout) sumList.getChildAt(childPosition); //swipe left remove
                            urlTV = (TextView) clicked.findViewById(R.id.urlsource);


                            if(clicked != null) {clicktex =(TextView) clicked.findViewById(R.id.row_id);
                                if(clicktex.getMaxLines() == 5 && urlTV != null){
                                    Url = urlTV.getText().toString().substring(8);
                                    DomainUrl = UrlGet.getSingleDomain(Url);
                                    removeFromMap(clicktex,DomainUrl,Url);
                                    clicked.setBackgroundColor(Color.parseColor("#f9bbb6"));
                                }
                            }

                        } else if (x2 > x1+150) {
                            Log.d("Right","Swipe DETECTED");

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

            links = UrlGet.getLinks(searchTerm);
            domains = UrlGet.getDomain(links);
            result = new QuerySearch().query(links,searchTerm,domains);

            sumResult = ThreadSearch.threadSearch(result);

            return sumResult;
        }

        @Override
        protected void onPostExecute(ArrayList result){
            pdLoading.dismiss();
            System.out.println(result.get(0).toString() + "\n"+result.size());
            listAdapter = new SummaryAdapter(ResultPage.this, sortResult(result));
            sumList.setAdapter(listAdapter);
        }
    }


    /**
     * When a user swipes right, adds or uppdates the corresponding
     * field in the hashmap to object.relevance to 1 for relevant, 0
     * for irrelevant
     *
     * @param input Textview where text is found
     * @param DomainUrl Domain of the listview item url
     * @param Url Url of the listview item
     */
    void addToMap(TextView input,String DomainUrl,String Url){
        if(input != null){
            String text = input.getText().toString();
            swipeTracker.put(DomainUrl,new RelevanceUpdateDTO(Url,DomainUrl,text,1));
        }
    }

    /**
     * When a user swipes left, adds or updates the corresponding
     * field in the hashmap to object.relevance to 0 for irrelevant, 1
     * for relevant
     * @param input Textview where text is found
     * @param DomainUrl Domain of the listview item url
     * @param Url Url of the listview item
     */

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

    /**
     * When newSearch button is pressed, goes back to newSearch
     */

    void newSearch(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Puts the text which user deems relevant and sends it
     * to the FinalResultPage activity view. If nothing is
     * deemed relevant -> does nothing(grayed out?)
     */

    void toResultPage(){
        Intent intent = new Intent(this,FinalResultPage.class);
        if(passToResult != null){
            intent.putExtra("map",passToResult);
            startActivity(intent);
        }
    }
}
