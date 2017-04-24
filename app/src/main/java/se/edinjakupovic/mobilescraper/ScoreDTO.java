package se.edinjakupovic.mobilescraper;

import java.util.ArrayList;

/**
 * Created by edinj on 24/04/2017.
 */

public class ScoreDTO {
    private ArrayList<String> sentence;
    private ArrayList<String> titleWords;
    private ArrayList<KeyWord> keyWords;

    ScoreDTO(ArrayList<String> sentence, ArrayList<String> titleWords, ArrayList<KeyWord> keyWords){
        this.sentence = sentence;
        this.titleWords =titleWords;
        this.keyWords = keyWords;
    }

    public ArrayList<KeyWord> getKeyWords() {
        return keyWords;
    }

    public ArrayList<String> getSentence() {
        return sentence;
    }

    public ArrayList<String> getTitleWords() {
        return titleWords;
    }
}
