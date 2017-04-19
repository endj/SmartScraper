package se.edinjakupovic.mobilescraper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Crea ted by edinj on 19/04/2017.
 */

class UrlRun implements Runnable { //constructor, svartmagi för att passa data till runnablen
    private String link;
    private double relevance;
    private Handler handler;
    UrlRun(String _link,double _relevance,Handler _handler) {
        this.link = _link;
        this.relevance = _relevance;
        this.handler = _handler;
    }

    @Override
    public void run() { // Webscrapa länken
        StringBuilder text = new StringBuilder(); // använder vi för appenda text
        try{
            Document doc = Jsoup.connect(this.link).get();
            Elements ps = doc.select("div p");

            for(Element e : ps){
                text.append(e.text());
            }
        }catch (Throwable e){
            e.printStackTrace();
        }


        Log.d("abc", "LINE216 URL RUNNABLE RUNNING" + this.link);
        Message msg = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putDouble("relevance",this.relevance);
        bundle.putString("text",this.link+ text.toString());
        msg.setData(bundle);

        handler.sendMessage(msg);
    }     // call handler to update ui
}