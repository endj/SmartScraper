package se.edinjakupovic.mobilescraper.DatabaseQueries;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
import java.util.HashMap;

import se.edinjakupovic.mobilescraper.DTOs.RelevanceUpdateDTO;

/**
 * UppdateRelevancys.java - AsyncTask that handles uppdating relevances after user swipes
 * @author Edin Jakupovic
 * @version 1.0
 */

public class UppdateRelevancys extends AsyncTask<String, Void, Void> {

    private ArrayList<RelevanceUpdateDTO> upvote = new ArrayList<>();
    private ArrayList<RelevanceUpdateDTO> downvote = new ArrayList<>();

    private HashMap<String ,RelevanceUpdateDTO>  map;
    private String searchTerm;

    public UppdateRelevancys(HashMap<String, RelevanceUpdateDTO> map,String searchTerm){
        this.map = map ;
        this.searchTerm = searchTerm;
    }




    @Override
    protected Void doInBackground(String... params) {
        setRelevanceArray(map);   // for each item that swiped left or right assign to correct list
        queryResults();
        return null;
    }


    /**
     *
     * Sends a POST request to the server with information
     * that is interpreted on the server to update the
     * relevancy in the database
     *
     */
    private void queryResults(){
        HttpURLConnection con = null;
        URL url;
        final int READ_TIMEOUT =5000;
        final int CONNECTION_TIMEOUT=5000;
        final String target = "http://edinjakupovic.se/updaterel.php";
        

        try{
            url = new URL(target);
            con =(HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setReadTimeout(READ_TIMEOUT);
            con.setConnectTimeout(CONNECTION_TIMEOUT);



            Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchTerm",this.searchTerm);

            if(!upvote.isEmpty()){
                int upvoteSize = upvote.size();
                builder.appendQueryParameter("upvoteSize",upvoteSize+"");
                for(int i=0; i<upvoteSize;i++){
                    builder.appendQueryParameter("upvoteURL"+i,upvote.get(i).getUrl());
                    builder.appendQueryParameter("upvoteDomain"+i,upvote.get(i).getDomain());
                }
            }else{
                builder.appendQueryParameter("upvoteSize",0+"");
            }


            if(!downvote.isEmpty()){
                int downvoteSize = downvote.size();
                builder.appendQueryParameter("downvoteSize",downvoteSize+"");
                for (int i=0;i<downvoteSize;i++){
                    builder.appendQueryParameter("downvoteURL"+i,downvote.get(i).getUrl());
                    builder.appendQueryParameter("downvoteDomain"+i,downvote.get(i).getDomain());
                }
            }else{
                builder.appendQueryParameter("downvoteSize",0+""); // no downvotes
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

        if(con != null){
            try{
                ArrayList<String> Result = new ArrayList<>();

                int response_code = con.getResponseCode();
                if(response_code == HttpURLConnection.HTTP_OK){

                    InputStream input = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    String line;
                    while((line = reader.readLine())!=null){
                        Result.add(line);
                    }

                }else{
                }
            }catch(IOException e2){
                e2.printStackTrace();

            } finally {
                con.disconnect();

            }
        }else{
            System.out.println("Fail");

        }

    }

    /**
     * Splits upp results into two arraylist based
     * on wherever the user finds the result relevant
     * or irelevant
     *
     * @param map
     *
     */

    private void setRelevanceArray(HashMap<String, RelevanceUpdateDTO> map) {
        for (RelevanceUpdateDTO word : map.values()) {
            if(word.getRelevance() == 1){
                upvote.add(word);
            }else{
                downvote.add(word);
            }
        }
    }

}
