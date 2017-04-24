package se.edinjakupovic.mobilescraper;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by edinj on 24/04/2017.
 */

class TF{

    /**
     *   Splits a sentence to words [A-Za-z1-9_]
     *
     *   @param s A sentence from the ArrayList "sentences"
     *   @return words Returns an ArrayList of words
     * */

    static ArrayList<String> split_words(String s){
        ArrayList<String> words = new ArrayList<>();

        Pattern p = Pattern.compile("\\w+");
        Matcher match = p.matcher(s);
        while (match.find()) {
            words.add(match.group());
        }
        return words;
    }


    /**
     *   Splits text up into sentences, splitting at .!? .
     *
     * @param inputText Takes all webscraped text as a String
     * @return sentences Return an ArrayList of Strings containing sentences
     * */
    static ArrayList<String> split_sentences(String inputText){
        ArrayList<String> sentences = new ArrayList<>();
        Pattern p = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)", Pattern.MULTILINE | Pattern.COMMENTS);
        Matcher match = p.matcher(inputText);
        while (match.find()) {
            sentences.add(match.group());
        }
        return sentences;
    }


}
