package se.edinjakupovic.mobilescraper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;


/**
 * ThreadScrapeResult.java - Class responsible for creating a short and
 * informative summary from a larger section of text. Achives this trough
 * ranking each sentence based on different criteries
 *
 * @author Edin Jakupovic
 * @version 1.0
 *
 */
class ThreadScrapeResult {
    private double relevance; // relevance of the URL
    private String searchTerm;  // SearchTerm of the Search.
    private String text;        // All the text from the site



    ThreadScrapeResult(String text, double relevance, String searchTerm){
        this.relevance = relevance;
        this.text = text;
        this.searchTerm = searchTerm;
    }

    String getText() {
        return text;
    }

    private ArrayList<String> score(ScoreDTO S){
        ArrayList<String> words;
        ArrayList<SentenceScore> summaries = new ArrayList<>();

        double sbs;
        double dbs;
        double frequency;
        double searchFeatureS;
        double sentenceLengthS;
        double sentencePositionS;
        double totalScore;
        int sentencesLength = S.getSentence().size();
        int counter =1;

        for (String sentence: S.getSentence()) {
            counter++;
            words = TF.split_words(sentence);
            searchFeatureS = titleScore(S.getTitleWords(),words);
            sentenceLengthS = lengthScore(sentence);
            sentencePositionS = positionScore(sentencesLength,counter);
            sbs = keyWordDensity(sentence,S.getKeyWords());
            dbs = dbs(sentence,S.getKeyWords());
            frequency = (sbs+dbs)/2 * 10;

            totalScore = (searchFeatureS*1.5+ frequency*2 +sentenceLengthS+
            sentencePositionS*1)/4;

            summaries.add(new SentenceScore(sentence,totalScore));

        }
        Collections.sort(summaries);
        ArrayList<String> test = new ArrayList<>();
        for (int i=0;i<summaries.size();i++){
            if(i == summaries.size()){
                return test;
            }else if(i < 5){
                test.add(summaries.get(i).getSentence());

            }
        }
        /*
        int j=0;
        while (!summaries.isEmpty() || j < 5){
            j++;
            test.add(summaries.get(j).getSentence());
        }*/

        return test;
    }



    /**
    * Splits up the input text to sentences and assigns them a score based on
    * relevance. The n most relevant articles are returned based on input
    *
    *
    *
    * */


    void Summarize(){
        ArrayList<String> sentences;
        ArrayList<KeyWord> keywords;
        ArrayList<String> titleWords;
        ArrayList<String> ranks;

        sentences = TF.split_sentences(this.text);

        if(sentences.size() <= 5){
            this.text = sentences.toString();
        }

        keywords = getkeyWords(this.text);
        titleWords = getTitleWords(this.searchTerm);

        ScoreDTO summary = new ScoreDTO(sentences,titleWords,keywords);
        ranks = score(summary);
        this.text = ranks.toString();
    }

    private double titleScore(ArrayList<String> titlewords,ArrayList<String> sentence){
        int count = 0;
        int titleWords =titlewords.size();
        for (String titleword: titlewords) {
            if(sentence.contains(titleword) &&
                    !MainActivity.IgnoreWordSet.contains(titleword)){
                count++;
            }
        }
        if(titleWords == 0 || count == 0){
            return 0;
        }
            return count/titleWords;
    }

    private double positionScore(int sentenceSize,int count){
        double normalized = count/sentenceSize;
        double score=0;

        if(isBetween(normalized,0,0.1)){ score = 0.17; }
        if(isBetween(normalized,0.1,0.2)){ score = 0.23; }
        if(isBetween(normalized,0.2,0.3)){ score = 0.14; }
        if(isBetween(normalized,0.3,0.4)){ score = 0.08; }
        if(isBetween(normalized,0.4,0.5)){ score = 0.05; }
        if(isBetween(normalized,0.5,0.6)){ score = 0.04; }
        if(isBetween(normalized,0.6,0.7)){ score = 0.06; }
        if(isBetween(normalized,0.7,0.8)){ score = 0.04; }
        if(isBetween(normalized,0.8,0.9)){ score = 0.04; }
        if(isBetween(normalized,0.9,1)){ score = 0.15; }

        return score;
    }

    private boolean isBetween(double num,double x, double y){
        return num > x && num <= y;
    }

    private double lengthScore(String sentence){
        return 1-Math.abs(15-sentence.length())/15;
    }

    private double keyWordDensity(String sentence, ArrayList<KeyWord> keywords){
        int score = 0;
        if(sentence.length() == 0){
            return 0;
        }
        ArrayList<String> words = TF.split_words(sentence);

        for(int i=0;i<keywords.size();i++){
            KeyWord current = keywords.get(i);
            if(words.contains(current.word)){
                keywords.remove(keywords.indexOf(current));
                i--;
            }

        }
        /*
        for (KeyWord key: keywords) {
            if(words.contains(key.word)){
                score = score+1;
                keywords.remove(key);
            }
        }*/


        return (1/Math.abs(words.size()) * score)/10;
    }

    private int getKeyWordScore(ArrayList<KeyWord> keywords, String word){
        for (KeyWord key: keywords) {
            if(key.word.equals(word)){
                return  key.score;
            }
        }
        return 0;
    }

    private double dbs(String sentence, ArrayList<KeyWord> keywords){
        ArrayList<KeyScore> first = new ArrayList<>();
        ArrayList<KeyScore> second;
        double sum=0;

        if(sentence.length() == 0){
            return 0;
        }
        int score;
        ArrayList<String> words = TF.split_words(sentence);

        int i=0;
        for (String word: words) {
            i++;
            if(!MainActivity.IgnoreWordSet.contains(word)){
                score = getKeyWordScore(keywords,word);
                if(first.isEmpty()){
                    first.add(new KeyScore(score,i));
                }else{
                    second = first;
                    first.add(new KeyScore(score,i));
                    int dif = first.get(0).score - second.get(0).score;
                    sum += (first.get(1).score*second.get(1).score)/(Math.pow(dif,2));
                }
            }

        }
        int k = SentenceKeyIntersection(keywords,words)+1;

        return (1/(k*(k+1))*sum);
    }


    private int SentenceKeyIntersection(ArrayList<KeyWord> keys,ArrayList<String> words){
        int intersections=0;
        for(KeyWord key : keys){
            if(words.contains(key.word)){
               intersections++;
                keys.remove(key);
            }
        }
        return intersections;
    }




    /**
     *
     * @param text Text from article
     * @return keyWords Returns all word
     */
    private ArrayList<KeyWord> getkeyWords(String text){
        HashMap<String,Integer> keyWords = new HashMap<>();
        ArrayList<KeyWord> topKeyWord;
        ArrayList<String> textWords = TF.split_words(text);

        for (String word : textWords) {
           if (!MainActivity.IgnoreWordSet.contains(word)){
               if(!keyWords.containsKey(word)){
                   keyWords.put(word,1);
                   // if the current word is not ignored
               }else{
                   keyWords.put(word, keyWords.get(word)+1); // add it if exist replace+1
               }
           }
        }

        topKeyWord = sortMap(keyWords);
        return topKeyWord;
    }

    private ArrayList<String> getTitleWords(String searchTerm){
        return TF.split_words(searchTerm);
    }

    private ArrayList<KeyWord> sortMap(final HashMap<String,Integer> allKeywords){
        PriorityQueue<Word> p = new PriorityQueue<>();

        for(Map.Entry<String,Integer> entry : allKeywords.entrySet()){
            if(p.size() < 10){
                    p.add(new Word(entry.getKey(), entry.getValue()));
                }else if(entry.getValue() > p.peek().freq){
                    p.remove();
                p.add(new Word(entry.getKey(),entry.getValue()));
            }
        }

        ArrayList<KeyWord> result = new ArrayList<>();
        while(p.size() > 0){
            result.add(new KeyWord(p.remove().word,p.remove().freq));
        }

        return result;
    }


}







