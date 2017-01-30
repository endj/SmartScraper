package se.edinjakupovic.mobilescraper;
//testing git
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.nodes.Document;

public class MainActivity extends AppCompatActivity {
    public static final String MESSAGE = "N";
    private String testpage = "http://edinjakupovic.se/";
    private Document htmlDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText input = (EditText) findViewById(R.id.input); // Fiends the search bar
        Button htmlSearch = (Button) findViewById(R.id.htmlBtn); // Search btn
        TextView resultext = (TextView) findViewById(R.id.resultext);

        htmlSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String siteUrl = input.getText().toString();
              //  new ParseURL().execute(siteUrl);
            }
        });
    }






    private class ParseUrl extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings){
            String x = "test";
            return x;
        }
        @Override
        protected void onPreExecute(){

        }
        @Override
        protected void onPostExecute(String s){

        }
    }


}
