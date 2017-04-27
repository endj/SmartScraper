package se.edinjakupovic.mobilescraper.DTOs;

/**
 * Created by edinj on 24/04/2017.
 */

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