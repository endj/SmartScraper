package se.edinjakupovic.mobilescraper.DTOs;

import android.support.annotation.NonNull;

/**
 * UrlSummaryDTO - Data transfer Object used at the SummaryAdapter to sort and display results
 * @see se.edinjakupovic.mobilescraper.ListHandling.SummaryAdapter
 * @author Edin Jakupovic
 * @version 1.0
 * */

public class UrlSummaryDTO implements Comparable<UrlSummaryDTO> {
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

    @Override
    public int compareTo(@NonNull UrlSummaryDTO o) {
        return Double.compare(this.relevance, o.relevance);
    }
}
