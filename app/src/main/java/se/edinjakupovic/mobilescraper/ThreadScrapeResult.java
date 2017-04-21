package se.edinjakupovic.mobilescraper;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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



    /**
    * Splits up the input text to sentences and assigns them a score based on
    * relevance. The n most relevant articles are returned based on input
    *
    *   @return The most relevant sentences as a String
    *
    * */
    String Summarize(){
        ArrayList<String> sentences;
      //  int lines;
     //   double avgLength;
     //   boolean previous;
     //   String currentWord;

        sentences = split_sentences(this.text);

        for (String s: sentences) {
            ArrayList<String> words;
            words = split_words(s);
            for (String word : words) {

                if (MainActivity.IgnoreWordTrie.search(word)){
                    assert(1==1);
                }

            }

        }

        /*
        *   keyword = (text) -> Returnerar 10mest populära orden inte i blacklist
        *
        *   1. splitta alla ord i texten.
        *   2. Kolla antal ord i texten innan blacklist.
        *   3. freq = räkna antal ord i text som inte är Ignorewords(blacklistade)
        *   4. Stoppa in alla orden som inte är IgnoreWords i en hasmap, varje gång ordet hittas
        *       incrementera <key,value> value med 1. När e klar, returnera 10 mest populära ordern.
        *
        *  sbs = (sentence , keywords ) -> Tar in 10 mest populra orden + meningen, För varje hittat ord
        *  öka meningens värde med keywordets hashmap värde.
        *  sbs = number of keywords in a sentence scalled to sentence length
        *
        * dbs -> kolla github
        *
        *
        *
        *
         */




        return "b";
    }

    /**
    *   Splits text up into sentences, splitting at .!? .
    *
    * @param inputText Takes all webscraped text as a String
    * @return sentences Return an ArrayList of Strings containing sentences
    * */
    private ArrayList<String> split_sentences(String inputText){
        ArrayList<String> sentences = new ArrayList<>();
        Pattern p = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)", Pattern.MULTILINE | Pattern.COMMENTS);
        Matcher match = p.matcher(inputText);
        while (match.find()) {
            sentences.add(match.group());
        }
        return sentences;
    }


    /**
    *   Splits a sentence to words [A-Za-z1-9_]
    *
    *   @param s A sentence from the ArrayList "sentences"
    *   @return words Returns an ArrayList of words
    * */
    private ArrayList<String> split_words(String s){
        ArrayList<String> words = new ArrayList<>();

        Pattern p = Pattern.compile("\\w+");
        Matcher match = p.matcher(s);
        while (match.find()) {
            words.add(match.group());
        }

        return words;
    }



}
