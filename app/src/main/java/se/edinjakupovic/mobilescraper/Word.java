package se.edinjakupovic.mobilescraper;

import android.support.annotation.NonNull;

/**
 * Created by edinj on 24/04/2017.
 */

class Word implements Comparable<Word>{
    String word;
    int freq;

    Word(String w, int f){
        this.word = w;
        this.freq = f;
    }


    @Override
    public int compareTo(@NonNull Word o) {
        return this.freq - o.freq;
    }
}