package se.edinjakupovic.mobilescraper.ViewActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import se.edinjakupovic.mobilescraper.R;

/**
 * FinalResultPage.java - Final page after swiping what results are deemed relevants in the ResultPage
 * @see ResultPage
 * @author Edin Jakupovic
 * @version 1.0
 * */

public class FinalResultPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_result);

        TextView Result = (TextView) findViewById(R.id.finalText);
        Button backButton = (Button) findViewById(R.id.newsearchbtn);



        Intent intent = getIntent();
        String text = intent.getStringExtra("map");
        Result.setText(text);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinalResultPage.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }




}
