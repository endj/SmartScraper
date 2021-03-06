package se.edinjakupovic.mobilescraper.WebScraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * UrlGet.java - Class that contains function for fetching links
 * and formating the domain representation
 *
 * @author Edin Jakupovic
 * @version 1.0
 */

public class UrlGet {


    /**
    *   Returns the frontpage links from a google search with the term searchTerm
    *
    *   @param searchTerm Input is gotten from the search screen
    *   @return Returns the most popular links as an ArrayList
    * */
    public static ArrayList<String> getLinks(String searchTerm){
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


    /**
    *   Returns only the domain part of an URL
    *   example www.example.com/dir1/dir2/file.html
    *   returns www.example.com
    *
    *   @param input An ArrayList containing urls from getURl as String
    *   @return matches  Returns domains as ArrayList
    *
    * */
    public static ArrayList<String> getDomain(ArrayList input){
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

    /**
     * Returns the domain of a single Url
     * @param input Single url
     * @return domain of the url  www.example.com/abc/file.abc -> www.example.com
     */
    public static String getSingleDomain(String input){
        try{
            URL url = new URL(input);
            input = url.getHost();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return input;
    }
}
