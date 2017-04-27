package se.edinjakupovic.mobilescraper.DTOs;

import android.support.annotation.NonNull;

/**
 * SentenceScoreDTO - Data transfer object used when computing most relevant summaires
 * @see se.edinjakupovic.mobilescraper.WebScraping.ThreadScrapeResult
 * @author Edin Jakupovic
 * @version 1.0
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
