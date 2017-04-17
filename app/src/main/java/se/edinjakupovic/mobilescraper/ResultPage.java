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
import java.util.Arrays;

public class ResultPage extends AppCompatActivity {
    private TextView showInput;
    public static final String MESSAGE = "";
    final int READ_TIMEOUT =5000;
    final int CONNECTION_TIMEOUT=5000;
    final String target = "http://192.168.10.208/kandidat/script.php?search=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        showInput = (TextView) findViewById(R.id.textView);

        final String Result = fetchResult(); // SearchTerm fecteedtrough intent
        showInput.setText("");
        new ParseUrl().execute(Result);



    }

    Handler handler = new Handler(new Handler.Callback() {
        ArrayList<String> data = new ArrayList<>();


        @Override
        public boolean handleMessage(Message msg){
            Bundle bundle = msg.getData();
            String string = bundle.getString("text");
            double relevance = bundle.getDouble("relevance");

            data.add(string);
            data.add("#############################\n\n");
            showInput.append(Double.toString(relevance));
            //showInput.append(string);
            // data.sort
           // data.update();
            // Sortera datan efter relevans och displaya den,
            //showInput.append("\n"+string);
            Log.d("myTag", "Handler ran "+string);
            return false;
        }
    });


    String fetchResult(){ // Returns a string array with content
        return getIntent().getStringExtra(MainActivity.MESSAGE);
    }

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
            ArrayList<String> links; // Urls
            ArrayList<String> result;
            String searchTerm = strings[0];
            

            links = UrlGet.getLinks(searchTerm);  // Returns links as arraylist
            result = query(links,searchTerm);

            return result; // echo from php
        }

        @Override
        protected void onPostExecute(ArrayList result){ // Returns Url links + URL-Relevance-Mapping from database
            Log.d("# OnPostEXECUTE #","line 108"+result.toString());

            pdLoading.dismiss();
            if(result.toString().equalsIgnoreCase("error")){
                handleError("Search failed");
                Log.d("meme2",result.toString());
            }else{
               threadSearch(result);
            }
        }
    }

    ArrayList<String> query(ArrayList<String> links,String searchTerm){
        ArrayList<String> Result = new ArrayList<>();
        ArrayList<String> error = new ArrayList<>();
        ArrayList<String> domains;
        HttpURLConnection con = null;
        URL url;

        try{
            url = new URL(target);
            con =(HttpURLConnection) url.openConnection(); // Opens connection
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setReadTimeout(READ_TIMEOUT);
            con.setConnectTimeout(CONNECTION_TIMEOUT);

            domains = UrlGet.getDomain(links);  // Returns just the domains without /links

            Uri.Builder builder = new Uri.Builder().appendQueryParameter("search",searchTerm);// set parameter
            builder.appendQueryParameter("numOfLinks",links.size()+"");
            for(int i=0;i<links.size();i++){
                builder.appendQueryParameter("searchUrl"+i,links.get(i)); // full url
                //Log.d("t"," \n\n SEARCH URL "+ links.get(i) + "\n\n DOMAIN URL"+ domains.get(i));
                builder.appendQueryParameter("domainUrl"+i,domains.get(i)); // just domains
            }
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
            handleError("Connection error");
        }
        if(con != null){
            try{
                int response_code = con.getResponseCode();
                if(response_code == HttpURLConnection.HTTP_OK){ // Check if connection is made
                    // Read data from server
                    InputStream input = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    String line;

                    while((line = reader.readLine())!=null){
                        Result.add(line);
                    }
                    Log.d("",Result.toString());
                    return Result;
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
        }else{
            return error;
        }
    }

    void threadSearch(ArrayList result){ // result.get(thread-length-1) == relevance mapping
        String Temp = result.toString();
        String set[] = Temp.split("\\s+");
        // set contains url to relevance mappings -> 0:url 1:R , 2:url 3:R
        Thread[] threads = new Thread[set.length/2]; // sets number

        for(int i=0, j=0; j<set.length-1; i++,j+=2){
            Log.d("A",  j+"j "+set[j]+" j+1:"+set[j+1]);
            Log.d("thread",i+" i");

           threads[i] = new Thread(new ResultPage.UrlRun(set[j], Double.parseDouble(set[j+1])));
           threads[i].start();
        }
    }

    public class UrlRun implements Runnable { //constructor, svartmagi för att passa data till runnablen
        private String link;
        private double relevance;
        UrlRun(String _link,double _relevance) {
            this.link = _link;
            this.relevance = _relevance;
        }

        @Override
        public void run() { // Webscrapa länken
            StringBuilder text = new StringBuilder(); // använder vi för appenda text
            try{
                Document doc = Jsoup.connect(this.link).get();
                Elements ps = doc.select("div p");

                for(Element e : ps){
                    text.append(e.text());
                }
            }catch (Throwable e){
                e.printStackTrace();
            }


            Log.d("abc", "LINE216 URL RUNNABLE RUNNING" + this.link);
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putDouble("relevance",this.relevance);
            bundle.putString("text",this.link+ text.toString());
            msg.setData(bundle);

            handler.sendMessage(msg);
        }     // call handler to update ui
    }


    void handleError(String result){
        Intent intent = new Intent(this, MainActivity.class);  // An intent is used to do something
        intent.putExtra(MESSAGE,result);  // Adds
        startActivity(intent);
    }

}
