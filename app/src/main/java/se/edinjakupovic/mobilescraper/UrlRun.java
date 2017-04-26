package se.edinjakupovic.mobilescraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.Callable;

/**
 * UrlRun.java - A class implementing Callable which is
 * used when creating new threads. Recieves a link and
 * webscrapes the link for paragrahps inside divs. Formats
 * the result and creates a summary before returning.
 *
 * @author Edin Jakupovic
 * @version 1.0
 */

class UrlRun implements Callable<ThreadScrapeResult> {
    private String link;
    private double relevance;


    UrlRun(String link,double relevance) {
        this.link = link;
        this.relevance = relevance;
    }

    public String getLink() {
        return link;
    }

    /**
    *   Function ran by each thread with input this.link
    * @return result Creates and return a ThreadScrapeResult
    * object which formats the text and creates a summary
    * */
    @Override
    public ThreadScrapeResult call() throws Exception {
        StringBuilder text = new StringBuilder();
        ThreadScrapeResult result;
        try{
            Document doc = Jsoup.connect(this.link).get();
            doc.select("noscript,script,style,.hidden").remove();
            Elements ps = doc.select("div p");

            for(Element e : ps){
                text.append(e.text());
            }
            result = new ThreadScrapeResult(text.toString(),this.link,this.relevance); // website text,urllink, relevance
            result.Summarize();
            return result;

        }catch (Throwable e){
            System.out.println("## FAIL ##");
            //e.printStackTrace();
        }
            result = new ThreadScrapeResult("searchFailed","",0);
        return result;
    }
}