package se.edinjakupovic.mobilescraper;
//testing git
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class MainActivity extends AppCompatActivity {
    public static final String MESSAGE = "N";
    TextView resultext;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            //
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText input = (EditText) findViewById(R.id.input); // Fiends the search bar
        Button htmlSearch = (Button) findViewById(R.id.htmlBtn); // Search btn
        resultext = (TextView) findViewById(R.id.resultext);

        htmlSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String siteUrl = input.getText().toString();
                new ParseUrl().execute(siteUrl);
            }
        });
    }

    private class ParseUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings){
            StringBuilder buffer = new StringBuilder();
            try{
                String searchTerm = strings[0];
                Document doc = Jsoup.connect("https://www.google.se/search?q="+searchTerm).get();
                Elements searchLinks = doc.select("h3.r > a");

                for(Element e : searchLinks){   // For each of googles search results
                    buffer.append(e.attr("href")+"¤¤");
                }
            }catch (Throwable e){
                e.printStackTrace();
            }
            return buffer.toString();
        }
        @Override
        protected void onPostExecute(String result){
            threadSearch(result);
        }
    }

    public void threadSearch(String result){
        String[] text = result.split("\\¤¤+");
        Thread[] threads = new Thread[text.length];

        for(int i=0;i<threads.length;i++){
            threads[i] = new Thread(new UrlRun(text[i]));
            threads[i].start();
        }

    }
    public void doSearch(String result){
        Intent intent = new Intent(this, ResultPage.class);  // An intent is used to do something
        intent.putExtra(MESSAGE,result);  // Adds
        startActivity(intent);
    }


    public class UrlRun implements Runnable { //constructor, svartmagi för att passa data till runnablen
        private String link;
        public UrlRun(String _link) {
            this.link = _link;
        }

        @Override
        public void run() {
           resultext.setText(link);
        }
    }





}
