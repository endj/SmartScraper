package se.edinjakupovic.mobilescraper.DTOs;

/**
 * Created by edinj on 24/04/2017.
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