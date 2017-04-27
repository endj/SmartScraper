package se.edinjakupovic.mobilescraper.DTOs;

/**
 * RelevanceUpdateDTO.java - Data transfer object for passing to UppdateRelevancys class
 * @see se.edinjakupovic.mobilescraper.DatabaseQueries.UppdateRelevancys
 * @author Edin Jakupovic
 * @version 1.0
 * */

public class RelevanceUpdateDTO {
    private String url;
    private String domain;
    private String summaryText;
    private int relevance;

    public RelevanceUpdateDTO(String url, String domain, String summaryText, int relevance) {
        this.url = url;
        this.domain = domain;
        this.summaryText = summaryText;
        this.relevance = relevance;
    }

    public String getUrl() {
        return url;
    }

    public String getDomain() {
        return domain;
    }

    public String getSummaryText() {
        return summaryText;
    }

    public int getRelevance() {
        return relevance;
    }
}
