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
    TextView resultext;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText input = (EditText) findViewById(R.id.input); // Fiends the search bar
        Button htmlSearch = (Button) findViewById(R.id.htmlBtn); // Search btn
        resultext = (TextView) findViewById(R.id.resultext);

        htmlSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String siteUrl = input.getText().toString();
                resultext.setText("");
                doSearch(siteUrl);
             //   new ParseUrl().execute(siteUrl);
            }
        });
    }






    void doSearch(String result){
        Intent intent = new Intent(this, ResultPage.class);  // An intent is used to do something
        intent.putExtra(MESSAGE,result);  // Adds
        startActivity(intent);
    }

}
