package se.edinjakupovic.mobilescraper;

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
 * Created by edinj on 19/04/2017.
 */

class DB {

    DB(){}

    private final int READ_TIMEOUT =5000;
    private final int CONNECTION_TIMEOUT=5000;
    private final String target = "http://192.168.10.208/kandidat/script.php?search=";

    ArrayList<String> query(ArrayList<String> links, String searchTerm){ // Tar in söktermen och länkar
        ArrayList<String> Result = new ArrayList<>();
        ArrayList<String> domains;
        HttpURLConnection con;
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
                Log.d("t"," \n\n SEARCH URL "+ links.get(i) + "\n\n DOMAIN URL"+ domains.get(i));
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
            Result.add("error");
            return Result;
        }
        if(con != null){
            try{
                int response_code = con.getResponseCode();
                if(response_code == HttpURLConnection.HTTP_OK){ // Check if connection is made
                    // Read data from server
                    InputStream input = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    String line;

                    while((line = reader.readLine())!=null){ // pHP servern echoes out result and we fetch it here
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
