package se.edinjakupovic.mobilescraper;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by edinj on 20/04/2017.
 *
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


    String Summarize(){
        ArrayList<String> sentences;
      //  int lines;
     //   double avgLength;
     //   boolean previous;
     //   String currentWord;

        sentences = split_sentences(this.text); // Hämtar meningar

        for (String s: sentences) {  // För varje sentence
            ArrayList<String> words;
            words = split_words(s); // get words of a sentence
            for (String word : words) {

                if (MainActivity.IgnoreWordTrie.search(word)){
                    // kys
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


    private ArrayList<String> split_sentences(String inputText){
        ArrayList<String> sentences = new ArrayList<>();
        Pattern p = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)", Pattern.MULTILINE | Pattern.COMMENTS);
        Matcher match = p.matcher(inputText);
        while (match.find()) {
            sentences.add(match.group());
        }
        return sentences;
    }

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
