package se.edinjakupovic.mobilescraper;
//testing git
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainActivity extends AppCompatActivity {
    public static final String MESSAGE = "N";
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
                for(Element e : searchLinks){
                    buffer.append(e.text());
                    buffer.append("\n");
                    buffer.append(e.attr("href"));
                    buffer.append("\n");

                }

            }catch (Throwable e){
                e.printStackTrace();
            }
            return buffer.toString();
        }
        @Override
        protected void onPostExecute(String result){
            resultext.setText(result);
        }
    }
    public void doX(String x){
        resultext.setText(x);
    }

}
