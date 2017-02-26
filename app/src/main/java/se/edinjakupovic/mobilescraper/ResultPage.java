package se.edinjakupovic.mobilescraper;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

//import android.widget.ListView;
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

public class ResultPage extends AppCompatActivity {
    //private ListView listView;
    private TextView showInput;
    final String target = "http://192.168.0.3/kandidat/script.php?search=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        final String Result = fetchResult(); // Our data
        new ParseUrl().execute(Result);
        //listView = (ListView) findViewById(R.id.linkListView); // Our listview object
        showInput = (TextView) findViewById(R.id.textView);
        showInput.setText(Result);




















        /*
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.listview_item, Result);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
                TextView t = (TextView) view;

                int lines = t.getMaxLines();
                if(lines == 1000){
                    t.setMaxLines(5);
                }else{
                    //t.setBackgroundColor(0xFF00FF00);
                    t.setMaxLines(1000);
                }
            }
        }); */





    }



    public String fetchResult(){ // Returns a string array with content
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
            

            links = getLinks(searchTerm);
            result = query(links,searchTerm);
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList result){
            pdLoading.dismiss();
            if(result.toString().equalsIgnoreCase("error")){
                Log.d("meme2","Error");
                //doSearch("Error at connection");
            }else{
                //doSearch(result);
                Log.d("meme","sucess "+result.toString());
               threadSearch(result);
            }
        }

    }

    ArrayList<String> query(ArrayList<String> links,String searchTerm){
        ArrayList<String> error = new ArrayList<>();
        ArrayList<String> domains;
        HttpURLConnection con = null;
        URL url;

        try{
            url = new URL(target);
            con =(HttpURLConnection) url.openConnection(); // Opens connection
            con.setDoOutput(true);
            con.setDoInput(true);

            domains = getDomain(links);  // Returns just the domains without /links

            Uri.Builder builder = new Uri.Builder().appendQueryParameter("search",searchTerm);// set parameter
            builder.appendQueryParameter("numOfLinks",links.size()+"");
            for(int i=0;i<links.size();i++){
                builder.appendQueryParameter("searchUrl"+i,links.get(i)); // full url
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
            // Do something here Query failed
        }
        if(con != null){
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
        }else{
            return error;
        }

        
    }

    public void threadSearch(ArrayList result){
        Thread[] threads = new Thread[result.size()];

        for(int i=0;i<threads.length;i++){
            threads[i] = new Thread(new ResultPage.UrlRun(result.get(i).toString()));
            threads[i].start();
            Log.d(i+"", "Thread created"+i);
        }
    }
    
    ArrayList<String> getLinks(String searchTerm){
        ArrayList<String> links = new ArrayList<>();
        try{
            Document doc = Jsoup.connect("https://www.google.se/search?q="+searchTerm).get();
            Elements searchLinks = doc.select("h3.r > a");
            for(Element e : searchLinks){
                links.add(e.attr("href"));
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
        return links;
    }

    ArrayList<String> getDomain(ArrayList input){
        ArrayList<String> matches = new ArrayList<>();

        for(int i=0;i < input.size();i++){
            try {
                URL url = new URL(input.get(i).toString());
                matches.add(url.getHost());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return matches;
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



    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg){
            Bundle bundle = msg.getData();
            String string = bundle.getString("test");

            showInput.append("\n"+string);
            Log.d("myTag", "Handler ran "+string);
            return false;
        }
    });
}
