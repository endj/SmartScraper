package se.edinjakupovic.mobilescraper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.Callable;

/**
 * Crea ted by edinj on 19/04/2017.
 */

class UrlRun implements Callable<ThreadScrapeResult> { //constructor, svartmagi för att passa data till runnablen
    private String link;
    private double relevance;


    UrlRun(String _link,double _relevance) {
        this.link = _link;
        this.relevance = _relevance;

    }

    @Override
    public ThreadScrapeResult call() throws Exception { // Webscrapa länken
        StringBuilder text = new StringBuilder(); // använder vi för appenda text
        try{
            Document doc = Jsoup.connect(this.link).get();
            doc.select("noscript,script,style,.hidden").remove();
            Elements ps = doc.select("div p");

            for(Element e : ps){
                text.append(e.text());
            }
        }catch (Throwable e){
            e.printStackTrace();
        }

        ThreadScrapeResult result = new ThreadScrapeResult(text.toString(), this.relevance, this.link);
        result.Summarize();
        return result;
    }     // call handler to update ui
}