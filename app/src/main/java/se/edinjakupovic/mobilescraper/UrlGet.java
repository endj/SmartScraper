package se.edinjakupovic.mobilescraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by edinj on 16/04/2017.
 */

public class UrlGet {

    static ArrayList<String> getLinks(String searchTerm){
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

    static ArrayList<String> getDomain(ArrayList input){
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
}
