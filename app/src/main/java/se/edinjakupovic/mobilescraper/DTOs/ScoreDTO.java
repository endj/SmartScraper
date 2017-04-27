package se.edinjakupovic.mobilescraper.DTOs;

import java.util.ArrayList;

/**
 * ScoreDTO.java - Class for computing most relevant summaries
 * @see se.edinjakupovic.mobilescraper.WebScraping.ThreadScrapeResult
 * @author Edin Jakupovic
 * @version 1.0
 * */

public class ScoreDTO {
    private ArrayList<String> sentence;
    private ArrayList<String> titleWords;
    private ArrayList<KeyWordDTO> keyWords;

    public ScoreDTO(ArrayList<String> sentence, ArrayList<String> titleWords, ArrayList<KeyWordDTO> keyWords){
        this.sentence = sentence;
        this.titleWords =titleWords;
        this.keyWords = keyWords;
    }

    public ArrayList<KeyWordDTO> getKeyWords() {
        return keyWords;
    }

    public ArrayList<String> getSentence() {
        return sentence;
    }

    public ArrayList<String> getTitleWords() {
        return titleWords;
    }
}
