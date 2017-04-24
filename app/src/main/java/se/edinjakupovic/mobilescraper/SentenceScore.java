package se.edinjakupovic.mobilescraper;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by edinj on 24/04/2017.
 */

class SentenceScore implements Comparable<SentenceScore>{
    private String sentence;
    private double score;

    SentenceScore(String sentence,double score){
        this.sentence=sentence;
        this.score=score;
    }



    String getSentence() {
        return sentence;
    }

    @Override
    public int compareTo(@NonNull SentenceScore o) {
        return Double.compare(this.score, o.score);
    }
}
