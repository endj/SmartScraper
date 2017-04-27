package se.edinjakupovic.mobilescraper.DTOs;

/**
 * KeyScoreDTO.java - Data transfer object for dbs fucntion
 * @see se.edinjakupovic.mobilescraper.WebScraping.ThreadScrapeResult
 * @author Edin Jakupovic
 * @version 1.0
 */

public class KeyScoreDTO {
    private int position;
    private int score;
    public KeyScoreDTO(int position, int score){
        this.position = position;
        this.score = score;
    }

    public int getPosition() {
        return position;
    }

    public int getScore() {
        return score;
    }
}