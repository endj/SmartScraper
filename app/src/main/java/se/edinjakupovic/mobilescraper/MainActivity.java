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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String MESSAGE = "N";
    public static final int MAXLINKS = 5;
    //private String testpage = "http://edinjakupovic.se/";
   // private Document htmlDocument;
    TextView resultext;

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
            StringBuffer buffer = new StringBuffer();
            try{
                String searchTerm = strings[0];
                Document doc = Jsoup.connect("https://www.google.se/search?q="+searchTerm).get();
                Elements searchLinks = doc.select("h3.r > a");


                for(Element e : searchLinks){   // For each of googles search results

                    Document temp = Jsoup.connect(e.attr("href")).get(); // Get the current page
                    Elements para = temp.select("div > p");  // Fetch all links
                    Elements listItems = temp.select("div > ul");

                    buffer.append("TEXT ============================");
                    for(Element p : para){
                        if(p.text().length() > 20){
                            buffer.append("-\n-"+p.text()+"-\n-");
                        }
                    }
                    buffer.append("=======================================");
/*
                    for(Element li : listItems){
                        buffer.append(li.text());
                    }*/
                        buffer.append(e.text()+"\n"+e.attr("href")+"¤¤");

                }

            }catch (Throwable e){
                e.printStackTrace();
            }
            return buffer.toString();
        }
        @Override
        protected void onPostExecute(String result){
            doSearch(result);
        }
    }
    public void doSearch(String result){
        Intent intent = new Intent(this, ResultPage.class);  // An intent is used to do something
        intent.putExtra(MESSAGE,result);  // Adds
        startActivity(intent);
    }

}
