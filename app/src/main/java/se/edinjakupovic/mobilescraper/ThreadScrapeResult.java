package se.edinjakupovic.mobilescraper;

/**
 * Created by edinj on 20/04/2017.
 */

public class ThreadScrapeResult {
    private double relevance;
    private String text;

    ThreadScrapeResult(String text, double relevance){
        this.relevance = relevance;
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
