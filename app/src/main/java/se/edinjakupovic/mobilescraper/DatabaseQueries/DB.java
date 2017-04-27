package se.edinjakupovic.mobilescraper.DatabaseQueries;

import android.net.Uri;
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


/**
 * DB.java - A class that handles all database queries
 * @author Edin Jakupovic
 * @version 1.0
 *
 *
 */



public class DB {

    public DB(){}


    /**
    * Post's to a php file where the queries are performed. Recieves
    * the result as a String from the server.
    *
    * @param links An arrayList containing the links found from google
    * @param searchTerm The search performed by user
    * @return Result The result is a String with the pattern
    * " Url1 Relevance_1 Url2 Relevance2 .... UrlN RelevanceN "
    * Needs to be .split
    *
    * */

    public ArrayList<String> query(ArrayList<String> links, String searchTerm, ArrayList<String> domains){
        ArrayList<String> Result = new ArrayList<>();
        HttpURLConnection con;
        URL url;
        int CONNECTION_TIMEOUT = 5000;
        int READ_TIMEOUT = 5000;
        String target = "http://192.168.10.208/kandidat/script.php?search=";

        try{
            url = new URL(target);
            con =(HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setReadTimeout(READ_TIMEOUT);
            con.setConnectTimeout(CONNECTION_TIMEOUT);



            Uri.Builder builder = new Uri.Builder().appendQueryParameter("search",searchTerm);
            builder.appendQueryParameter("numOfLinks",links.size()+"");
            for(int i=0;i<links.size();i++){
                builder.appendQueryParameter("searchUrl"+i,links.get(i));
               // Log.d("t"," \n\n SEARCH URL "+ links.get(i) + "\n\n DOMAIN URL"+ domains.get(i));
                builder.appendQueryParameter("domainUrl"+i,domains.get(i));
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
            Result.add("error");
            return Result;
        }
        if(con != null){
            try{
                int response_code = con.getResponseCode();
                if(response_code == HttpURLConnection.HTTP_OK){

                    InputStream input = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    String line;

                    while((line = reader.readLine())!=null){
                        Result.add(line);   // Returns result as a string with whitspaces between url and result
                    }
                    Log.d("",Result.toString());
                    return Result;
                }else{
                    Result.add("failed response code not ok");
                    return Result;
                }
            }catch(IOException e2){
                e2.printStackTrace();
                Result.add("exception");
                return Result;
            } finally {
                con.disconnect();

            }
        }else{
            return Result;
        }
    }





}
