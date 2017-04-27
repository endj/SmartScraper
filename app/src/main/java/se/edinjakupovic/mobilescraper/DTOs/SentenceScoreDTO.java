package se.edinjakupovic.mobilescraper.DTOs;

import android.support.annotation.NonNull;

/**
 * Created by edinj on 24/04/2017.
 */

public class SentenceScoreDTO implements Comparable<SentenceScoreDTO>{
    private String sentence;
    private double score;

    public SentenceScoreDTO(String sentence, double score){
        this.sentence=sentence;
        this.score=score;
    }



    public String getSentence() {
        return sentence;
    }

    @Override
    public int compareTo(@NonNull SentenceScoreDTO o) {
        return Double.compare(this.score, o.score);
    }
}
