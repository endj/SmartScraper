package se.edinjakupovic.mobilescraper.DTOs;

import java.util.ArrayList;

/**
 * Created by edinj on 24/04/2017.
 */

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
