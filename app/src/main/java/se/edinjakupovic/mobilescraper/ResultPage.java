package se.edinjakupovic.mobilescraper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;


public class ResultPage extends AppCompatActivity {
    private TextView showInput;
    public static final String MESSAGE = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        showInput = (TextView) findViewById(R.id.textView);

        final String Result = getIntent().getStringExtra(MainActivity.MESSAGE); // SearchTerm fecteedtrough intent
        showInput.setText("");
        new ParseUrl().execute(Result); // bakrundstask som körs i trådar nonblocking
    }



    Handler handler = new Handler(new Handler.Callback() { // Recieves data from threads
        ArrayList<String> data = new ArrayList<>();
// Sorts the data and uppdates the ui
        @Override
        public boolean handleMessage(Message msg){ // Fetches data and sorts it
            Bundle bundle = msg.getData();
            String text = bundle.getString("text"); // Webscraped text
            double relevance = bundle.getDouble("relevance");

            data.add(text);
            data.add("#############################\n\n");
            showInput.append(Double.toString(relevance));
            showInput.append(text);
            Log.d("myTag", "Handler ran "+text);
            return false;
        }
    });



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



    void threadSearch(ArrayList result){ // result.get(thread-length-1) == relevance mapping
        String Temp = result.toString();
        String set[] = Temp.split("\\s+"); // splits on whitespace
        Thread[] threads = new Thread[set.length/2]; // sets number

        for(int i=0, j=0; j<set.length-1; i++,j+=2){
           threads[i] = new Thread(new UrlRun(set[j],Double.parseDouble(set[j+1]), handler));
           threads[i].start();
        }
    }




    void handleError(String result){
        Intent intent = new Intent(this, MainActivity.class);  // An intent is used to do something
        intent.putExtra(MESSAGE,result);  // Adds
        startActivity(intent);
    }

}
