package se.edinjakupovic.mobilescraper.ViewActivities;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashSet;

import se.edinjakupovic.mobilescraper.R;
import se.edinjakupovic.mobilescraper.WebScraping.ThreadScrapeResult;

/**
* MainActivity.java - Start activity for the class.
* User inputs their search in the search field and clicks
* on the search button.
*
* Sets up the Trie which hold words to ignore during the textformating
* @see ThreadScrapeResult
*
* @author Edin Jakupovic
* @version 1.0
*
*
* */

public class MainActivity extends AppCompatActivity {
    public static final String MESSAGE = "N";
    public static HashSet IgnoreWordSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IgnoreWordSet = initIgnoreSet();
        setContentView(R.layout.activity_main);


        String errorMsg = fetchResult();
        TextView error = (TextView) findViewById(R.id.errormessage);
        if(errorMsg != null){
            error.setText(errorMsg);
        }

        final EditText input = (EditText) findViewById(R.id.input);
        Button htmlSearch = (Button) findViewById(R.id.htmlBtn);



        htmlSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if( validSearch(input.getText()) ){
                    String siteUrl = input.getText().toString();
                    doSearch(siteUrl);
                }
            }
        });


    }

    private boolean validSearch(Editable text) {
        String input = String.valueOf(text);
        return !input.trim().isEmpty();
    }




    /**
    * Changes intent to the ResultPage and passes
    * the search term
    *
    * @param result Search term of the user
    * */
    void doSearch(String result){
        Intent intent = new Intent(this, ResultPage.class);
        intent.putExtra(MESSAGE,result);
        startActivity(intent);
    }

    /**
    * Used mostly to fetch error message due to
    * failed connection in the ResultPage activity
    * or similar
    * @return Intent Returns a message
    * */
    String fetchResult(){ // Returns a string array with content
        return getIntent().getStringExtra(MainActivity.MESSAGE);
    }


    /**
    * Initiates the IngoreWordTree @see ThreadScrapeResult
    * with words to ignore
    *
    * @return IgnoreWordTrie Returns a Trie object filled with words
    * */
    HashSet<String> initIgnoreSet(){
        String[] IgnoreWord = {"-", " ", ",", ".", "a", "e", "i", "o", "u", "t", "about", "above",
                "above", "across", "after", "afterwards", "again", "against", "all",
                "almost", "alone", "along", "already", "also", "although", "always",
                "am", "among", "amongst", "amoungst", "amount", "an", "and","s",
                "another", "any", "anyhow", "anyone", "anything", "anyway",
                "anywhere", "are", "around", "as", "at", "back", "be", "became",
                "because", "become", "becomes", "becoming", "been", "before",
                "beforehand", "behind", "being", "below", "beside", "besides",
                "between", "beyond", "both", "bottom", "but", "by", "call", "can",
                "cannot", "can't", "co", "con", "could", "couldn't", "de",
                "describe", "detail", "did", "do", "done", "down", "due", "during",
                "each", "eg", "eight", "either", "eleven", "else", "elsewhere",
                "empty", "enough", "etc", "even", "ever", "every", "everyone",
                "everything", "everywhere", "except", "few", "fifteen", "fifty",
                "fill", "find", "fire", "first", "five", "for", "former",
                "formerly", "forty", "found", "four", "from", "front", "full",
                "further", "get", "give", "go", "got", "had", "has", "hasnt",
                "have", "he", "hence", "her", "here", "hereafter", "hereby",
                "herein", "hereupon", "hers", "herself", "him", "himself", "his",
                "how", "however", "hundred", "i", "ie", "if", "in", "inc", "indeed",
                "into", "is", "it", "its", "it's", "itself", "just", "keep", "last",
                "latter", "latterly", "least", "less", "like", "ltd", "made", "make",
                "many", "may", "me", "meanwhile", "might", "mill", "mine", "more",
                "moreover", "most", "mostly", "move", "much", "must", "my", "myself",
                "name", "namely", "neither", "never", "nevertheless", "new", "next",
                "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing",
                "now", "nowhere", "of", "off", "often", "on", "once", "one", "only",
                "onto", "or", "other", "others", "otherwise", "our", "ours",
                "ourselves", "out", "over", "own", "part", "people", "per",
                "perhaps", "please", "put", "rather", "re", "said", "same", "see",
                "seem", "seemed", "seeming", "seems", "several", "she", "should",
                "show", "side", "since", "sincere", "six", "sixty", "so", "some",
                "somehow", "someone", "something", "sometime", "sometimes",
                "somewhere", "still", "such", "take", "ten", "than", "that", "the","The",
                "their", "them", "themselves", "then", "thence", "there",
                "thereafter", "thereby", "therefore", "therein", "thereupon",
                "these", "they", "thickv", "thin", "third", "this", "those",
                "though", "three", "through", "throughout", "thru", "thus", "to",
                "together", "too", "top", "toward", "towards", "twelve", "twenty",
                "two", "un", "under", "until", "up", "upon", "us", "use", "very",
                "via", "want", "was", "we", "well", "were", "what", "whatever",
                "when", "whence", "whenever", "where", "whereafter", "whereas",
                "whereby", "wherein", "whereupon", "wherever", "whether", "which",
                "while", "whither", "who", "whoever", "whole", "whom", "whose",
                "why", "will", "with", "within", "without", "would", "yet", "you",
                "your", "yours", "yourself", "yourselves", "the", "reuters", "news",
                "monday", "tuesday", "wednesday", "thursday", "friday", "saturday",
                "sunday", "mon", "tue", "wed", "thu", "fri", "sat", "sun",
                "rappler", "rapplercom", "inquirer", "yahoo", "home", "sports",
                "1", "10", "2012", "sa", "says", "tweet", "pm", "home", "homepage",
                "sports", "section", "newsinfo", "stories", "story", "photo",
                "2013", "na", "ng", "ang", "year", "years", "percent", "ko", "ako",
                "yung", "yun", "2", "3", "4", "5", "6", "7", "8", "9", "0", "time",
                "january", "february", "march", "april", "may", "june", "july",
                "august", "september", "october", "november", "december",
                "government", "police","alla","allt","alltså","andra","att",
                "bara","bli","blir","borde","bra","mitt","ser","dem","den","denna",
                "det","detta","dig","din","dock","dom","där","edit","efter","eftersom",
                "eller","ett","fast","fel","fick","finns","fram","från","får","fått",
                "för","första","genom","ger","gör","går","göra","hade","han","har","hela",
                "helt","honom","hur","hör","iaf","igen","ingen","inget","inte","jag","kan",
                "kanske","kommer","lika","lite","man","med","men","mer","mig","min","mot",
                "mycket","många","måste","nog","når","någon","något","några","nån","nåt","och",
                "också","rött","samma","sedan","sen","sig","sin","själv","ska","skulle","som","sött"
                ,"tar","till","tror","tycker","typ","upp","utan","vad","var","vara","vet"
                ,"vid","vilket","vill","väl","även","över","våra","egen","är","på",
                "en","du","ha","av","Det","så","om","mest","Här","samt","Så",
                "här","nu","Har","Jag","jag","De","de","Nu","sitt","Och","hon","han",
                "Vi","vi","träffar","berättar","se","än","på","På","ut","ta","En","en",
                "få","när","För","ju","oss","cookies"};

        HashSet<String> Ignore = new HashSet<>(IgnoreWord.length);

        Collections.addAll(Ignore, IgnoreWord);

        return Ignore;
    }


}
