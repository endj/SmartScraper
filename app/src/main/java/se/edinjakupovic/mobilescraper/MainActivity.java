package se.edinjakupovic.mobilescraper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static final String MESSAGE = "N";
    TextView resultext;

    Handler handler = new Handler(){ //Används bara om man behöver ändra uit
        @Override
        public void handleMessage(Message msg){
            Bundle bundle = msg.getData();
            String string = bundle.getString("test");

            resultext.append("\n"+string);
            Log.d("myTag", "Handler ran "+string);
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
                resultext.setText("");
                new ParseUrl().execute(siteUrl);
            }
        });
    }

    private class ParseUrl extends AsyncTask<String, Void, ArrayList<String>> {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            pdLoading.setMessage("\tSearching...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }


        @Override
        protected ArrayList<String> doInBackground(String... strings){
            HttpURLConnection con;
            URL url;
            String searchTerm = strings[0];
            ArrayList<String> links = new ArrayList<>();
            ArrayList<String> error = new ArrayList<>();


            try{
                Document doc = Jsoup.connect("https://www.google.se/search?q="+searchTerm).get();
                Elements searchLinks = doc.select("h3.r > a");
                //Find popular results

                for(Element e : searchLinks){   // For each of googles search results
                    links.add(e.attr("href"));
                }
            }catch (Throwable e){
                e.printStackTrace();
            }

            Log.d("abc",links.toString());

            // 192.168.1.74 jonas
            // 192.168.0.3 edin
            final String target = "http://192.168.0.3/kandidat/script.php?search=";
            try{
                url = new URL(target); // Target php file
                con =(HttpURLConnection) url.openConnection(); // Opens connection
                // con.setReadTimeout(READ_TIMEOUT);
                //con.setConnectTimeout(CONNECTION_TIMEOUT);
                con.setDoOutput(true);
                con.setDoInput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("search",searchTerm); // set parameter
                String query = builder.build().getEncodedQuery();


                DataOutputStream wr = new DataOutputStream(con.getOutputStream()); // Connection for sending data
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                wr.close();
                con.connect();

            }catch (Exception e){
                e.printStackTrace();
                return error;
            }
            try{
                int response_code = con.getResponseCode();
                if(response_code == HttpURLConnection.HTTP_OK){ // Check if connection is made


                    // Read data from server
                    InputStream input = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;


                    while((line = reader.readLine())!=null){
                        result.append(line);
                    }
                    links.add(result.toString());
                    return links;
                    //return(result.toString());
                }else{
                    error.add("failed responce code not ok");
                    return error;
                }
            }catch(IOException e2){
                e2.printStackTrace();
                error.add("exception");
                return error;
            } finally {
                con.disconnect();
            }
        }







        @Override
        protected void onPostExecute(ArrayList result){
            pdLoading.dismiss();

            if(result.toString().equalsIgnoreCase("error")){
                doSearch("Error at connection");
            }else{
                //doSearch(result);
                threadSearch(result);
            }
        }
    }

    public void threadSearch(ArrayList result){
        Thread[] threads = new Thread[result.size()];

        for(int i=0;i<threads.length;i++){
            threads[i] = new Thread(new UrlRun(result.get(i).toString()));
            threads[i].start();
            Log.d(i+"", "Thread created"+i);
        }

    }
    public void doSearch(String result){
        Intent intent = new Intent(this, ResultPage.class);  // An intent is used to do something
        intent.putExtra(MESSAGE,result);  // Adds
        startActivity(intent);
    }


    public class UrlRun implements Runnable { //constructor, svartmagi för att passa data till runnablen
        private String link;
        UrlRun(String _link) {
            this.link = _link;
        }

        @Override
        public void run() { // Webscrapa länken
            StringBuilder text = new StringBuilder(); // använder vi för appenda text
            try{
                Document doc = Jsoup.connect(this.link).get();
                Elements ps = doc.select("p");

                for(Element e : ps){
                    text.append(e.text());
                }
            }catch (Throwable e){
                e.printStackTrace();
            }


            Log.d("abc", "URL RUNNABLE RUNNING" + this.link);
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("test",this.link+ text.toString());
            msg.setData(bundle);
            handler.sendMessage(msg);
        }     // call handler to update ui
    }





}
