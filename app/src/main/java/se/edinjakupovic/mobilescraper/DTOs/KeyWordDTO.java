package se.edinjakupovic.mobilescraper.DTOs;

/**
 *
 * KeyWordDTO.java - Data transfer object for  computing keyWordDensity and sentence intersection
 * @see se.edinjakupovic.mobilescraper.WebScraping.ThreadScrapeResult
 * @author Edin Jakupovic
 * @version 1.0
 * */

public class KeyWordDTO {
    private String word;
    private int score;

    public KeyWordDTO(String word, int score){
        this.word = word;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public String getWord() {
        return word;
    }
}