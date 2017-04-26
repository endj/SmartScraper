package se.edinjakupovic.mobilescraper;

/**
 * Created by edinj on 26/04/2017.
 */

public class UrlSummaryDTO {
    private String summary;
    private String url;
    private double relevance;

    public UrlSummaryDTO(String summary, String url, double relevance) {
        this.summary = summary;
        this.url = url;
        this.relevance = relevance;
    }

    public String getSummary() {
        return summary;
    }

    public String getUrl() {
        return url;
    }

    public double getRelevance() {
        return relevance;
    }
}
