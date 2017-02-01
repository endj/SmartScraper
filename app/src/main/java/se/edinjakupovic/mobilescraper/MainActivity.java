package se.edinjakupovic.mobilescraper;
//testing git
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    public static final String MESSAGE = "N";

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    //public static final int MAXLINKS = 5;
    //private String testpage = "http://edinjakupovic.se/";
   // private Document htmlDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText input = (EditText) findViewById(R.id.input); // Fiends the search bar
        Button htmlSearch = (Button) findViewById(R.id.htmlBtn); // Search btn

        htmlSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String siteUrl = input.getText().toString();

                new ParseUrl().execute(siteUrl);
            }
        });
    }

    private class ParseUrl extends AsyncTask<String, Void, String> {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... strings){
            HttpURLConnection con;
            URL url;
            String searchTerm = strings[0];

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
                return "error";
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
                    return(result.toString());
                }else{
                    return("failed responce code not ok");
                }
            }catch(IOException e2){
                e2.printStackTrace();
                return "exception";
            } finally {
                con.disconnect();
            }
        }


        @Override
        protected void onPostExecute(String result){
            pdLoading.dismiss();

            if(result.equalsIgnoreCase("error")){
                doSearch("Error at connection");
            }else{
                doSearch(result);
            }
        }
    }

    public void doSearch(String result){
        Intent intent = new Intent(this, ResultPage.class);  // An intent is used to do something
        intent.putExtra(MESSAGE,result);  // Adds
        startActivity(intent);
    }

}
