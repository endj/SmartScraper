package se.edinjakupovic.mobilescraper;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Create d by edinj on 26/04/2017.
 */

class UppdateRelevancys extends AsyncTask<String, Void, Void> {

    private ArrayList<String> upvote = new ArrayList<>();
    ArrayList<String> downvote = new ArrayList<>();

    private HashMap<String ,KeyWord>  map;

    UppdateRelevancys(HashMap<String, KeyWord> map){
        this.map = map ;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params) {
        setRelevanceArray(map);   // for each item that swiped left or right assign to correct list
        System.out.println("upvote"+upvote.toString());
        System.out.println("downvote"+downvote.toString());
       // queryResults(map);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    private void queryResults(HashMap<String, KeyWord> map){
        HttpURLConnection con;
        URL url;
        final int READ_TIMEOUT =5000;
        final int CONNECTION_TIMEOUT=5000;
        final String target = "http://192.168.10.208/kandidat/script.php?search=";


        try{
            url = new URL(target);
            con =(HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setReadTimeout(READ_TIMEOUT);
            con.setConnectTimeout(CONNECTION_TIMEOUT);


            Uri.Builder builder = new Uri.Builder();




            if(!upvote.isEmpty()){  // if somethings been upvoted
                int upvoteSize = upvote.size();
                builder.appendQueryParameter("upvotesize",upvoteSize+""); // pass ammount to query
                for(int i=0; i<upvoteSize;i++){
                    builder.appendQueryParameter("upvote"+i,upvote.get(i)); // how many upvotes
                }
            }else{
                builder.appendQueryParameter("upvotesize",0+""); // no upvotes
            }


            if(!downvote.isEmpty()){ // somehting downvoted
                int downvoteSize = downvote.size();
                builder.appendQueryParameter("downvotesize",downvoteSize+""); // how many
                for (int i=0;i<downvoteSize;i++){
                    builder.appendQueryParameter("downvote"+i,downvote.get(i));
                }
            }else{
                builder.appendQueryParameter("downvotesize",0+""); // no downvotes
            }

            String query = builder.build().getEncodedQuery();

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            wr.close();
            con.connect();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setRelevanceArray(HashMap<String, KeyWord> map) {
        for (KeyWord word : map.values()) {
            if(word.score == 1){
                upvote.add(word.word);
            }else{
                downvote.add(word.word);
            }
        }
    }

}
