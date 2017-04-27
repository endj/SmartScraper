package se.edinjakupovic.mobilescraper.WebScraping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import se.edinjakupovic.mobilescraper.DTOs.KeyScoreDTO;
import se.edinjakupovic.mobilescraper.DTOs.KeyWordDTO;
import se.edinjakupovic.mobilescraper.DTOs.ScoreDTO;
import se.edinjakupovic.mobilescraper.DTOs.SentenceScoreDTO;
import se.edinjakupovic.mobilescraper.DTOs.WordDTO;
import se.edinjakupovic.mobilescraper.ViewActivities.MainActivity;


/**
 * ThreadScrapeResult.java - Class responsible for creating a short and
 * informative summary from a larger section of text. Achives this trough
 * ranking each sentence based on different criteries
 *
 * @author Edin Jakupovic
 * @version 1.0
 *
 */
public class ThreadScrapeResult {
    private String text;// All the text from the site before scraped
    private String url;  // SearchTerm of the Search.
    private double relevance; // relevance of the URL




    ThreadScrapeResult(String text,String url, double relevance){
        this.relevance = relevance;
        this.text = text;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public double getRelevance() {
        return relevance;
    }

    public String getText() {
        return text;
    }

    private ArrayList<String> score(ScoreDTO S){
        ArrayList<String> words;
        ArrayList<SentenceScoreDTO> summaries = new ArrayList<>();

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
            words = RegexFunctions.split_words(sentence);
            searchFeatureS = titleScore(S.getTitleWords(),words);
            sentenceLengthS = lengthScore(sentence);
            sentencePositionS = positionScore(sentencesLength,counter);
            sbs = keyWordDensity(sentence,S.getKeyWords());
            dbs = dbs(sentence,S.getKeyWords());
            frequency = (sbs+dbs)/2 * 10;

            totalScore = (searchFeatureS*1.5+ frequency*2 +sentenceLengthS+
            sentencePositionS*1)/4;

            summaries.add(new SentenceScoreDTO(sentence,totalScore));

        }
        Collections.sort(summaries);
        ArrayList<String> summaryText = new ArrayList<>();
        for (int i=0;i<summaries.size();i++){
            if(i == summaries.size()){
                return summaryText;
            }else if(i < 5){
                summaryText.add(summaries.get(i).getSentence()+" ");

            }
        }
        return summaryText;
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
        ArrayList<KeyWordDTO> keywords;
        ArrayList<String> titleWords;
        ArrayList<String> ranks;

        sentences = RegexFunctions.split_sentences(this.text);

        if(sentences.size() <= 5){
            this.text = sentences.toString();
        }

        keywords = getkeyWords(this.text);
        titleWords = getTitleWords(this.url);

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

    private double keyWordDensity(String sentence, ArrayList<KeyWordDTO> keywords){
        int score = 0;
        if(sentence.length() == 0){
            return 0;
        }
        ArrayList<String> words = RegexFunctions.split_words(sentence);

        for(int i=0;i<keywords.size();i++){
            KeyWordDTO current = keywords.get(i);
            if(words.contains(current.getWord())){
                keywords.remove(keywords.indexOf(current));
                i--;
            }

        }



        return (1/Math.abs(words.size()) * score)/10;
    }

    private int getKeyWordScore(ArrayList<KeyWordDTO> keywords, String word){
        for (KeyWordDTO key: keywords) {
            if(key.getWord().equals(word)){
                return  key.getScore();
            }
        }
        return 0;
    }

    private double dbs(String sentence, ArrayList<KeyWordDTO> keywords){
        ArrayList<KeyScoreDTO> first = new ArrayList<>();
        ArrayList<KeyScoreDTO> second;
        double sum=0;

        if(sentence.length() == 0){
            return 0;
        }
        int score;
        ArrayList<String> words = RegexFunctions.split_words(sentence);

        int i=0;
        for (String word: words) {
            i++;
            if(!MainActivity.IgnoreWordSet.contains(word)){
                score = getKeyWordScore(keywords,word);
                if(first.isEmpty()){
                    first.add(new KeyScoreDTO(score,i));
                }else{
                    second = first;
                    first.add(new KeyScoreDTO(score,i));
                    int dif = first.get(0).getScore() - second.get(0).getScore();
                    sum += (first.get(1).getScore()*second.get(1).getScore())/(Math.pow(dif,2));
                }
            }

        }
        int k = SentenceKeyIntersection(keywords,words)+1;

        return (1/(k*(k+1))*sum);
    }


    private int SentenceKeyIntersection(ArrayList<KeyWordDTO> keys, ArrayList<String> words){
        int intersections=0;
        for(KeyWordDTO key : keys){
            if(words.contains(key.getWord())){
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
    private ArrayList<KeyWordDTO> getkeyWords(String text){
        HashMap<String,Integer> keyWords = new HashMap<>();
        ArrayList<KeyWordDTO> topKeyWord;
        ArrayList<String> textWords = RegexFunctions.split_words(text);

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
        return RegexFunctions.split_words(searchTerm);
    }

    private ArrayList<KeyWordDTO> sortMap(final HashMap<String,Integer> allKeywords){
        PriorityQueue<WordDTO> p = new PriorityQueue<>();

        for(Map.Entry<String,Integer> entry : allKeywords.entrySet()){
            if(p.size() < 10){
                    p.add(new WordDTO(entry.getKey(), entry.getValue()));
                }else if(entry.getValue() > p.peek().getFreq()){
                    p.remove();
                p.add(new WordDTO(entry.getKey(),entry.getValue()));
            }
        }

        ArrayList<KeyWordDTO> result = new ArrayList<>();
        while(p.size() > 0){
            result.add(new KeyWordDTO(p.remove().getWord(),p.remove().getFreq()));
        }

        return result;
    }


}







