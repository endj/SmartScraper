package se.edinjakupovic.mobilescraper;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String MESSAGE = "N";
    public Trie IgnoreWordTrie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IgnoreWordTrie = initTree();
        System.out.println("Finns ordet found=? " +IgnoreWordTrie.search("found"));
        System.out.println("Finns ordet bazing=? "+IgnoreWordTrie.search("bazing"));
        setContentView(R.layout.activity_main);


        String errorMsg = fetchResult();
        TextView error = (TextView) findViewById(R.id.errormessage);
        if(errorMsg != null){
            error.setText(errorMsg);
        }

        final EditText input = (EditText) findViewById(R.id.input); // Fiends the search bar
        Button htmlSearch = (Button) findViewById(R.id.htmlBtn); // Search btn



        htmlSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String siteUrl = input.getText().toString();
                doSearch(siteUrl);
            }
        });


    }


    void doSearch(String result){
        Intent intent = new Intent(this, ResultPage.class);  // An intent is used to do something
        intent.putExtra(MESSAGE,result);  // Adds
        startActivity(intent);
    }


    String fetchResult(){ // Returns a string array with content
        return getIntent().getStringExtra(MainActivity.MESSAGE);
    }

    Trie initTree(){
        String[] IgnoreWord = {"-", " ", ",", ".", "a", "e", "i", "o", "u", "t", "about", "above",
                "above", "across", "after", "afterwards", "again", "against", "all",
                "almost", "alone", "along", "already", "also", "although", "always",
                "am", "among", "amongst", "amoungst", "amount", "an", "and",
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
                "somewhere", "still", "such", "take", "ten", "than", "that", "the",
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
                "government", "police"};

        Trie IgnoreWordTrie = new Trie();

        for (String word: IgnoreWord) {
            IgnoreWordTrie.put(word);
        }

        for (String word: IgnoreWord) {
         //   System.out.println(IgnoreWordTrie.search(word));
        }
        return IgnoreWordTrie;
    }


}
