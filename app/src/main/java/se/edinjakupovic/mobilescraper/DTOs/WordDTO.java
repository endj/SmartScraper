package se.edinjakupovic.mobilescraper.DTOs;

import android.support.annotation.NonNull;

/**
 * Created by edinj on 24/04/2017.
 */

public class WordDTO implements Comparable<WordDTO>{
    private String word;
    private int freq;

    public WordDTO(String w, int f){
        this.word = w;
        this.freq = f;
    }


    public String getWord() {
        return word;
    }

    public int getFreq() {
        return freq;
    }

    @Override
    public int compareTo(@NonNull WordDTO o) {
        return this.freq - o.freq;
    }
}