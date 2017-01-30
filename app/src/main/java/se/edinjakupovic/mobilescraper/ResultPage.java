package se.edinjakupovic.mobilescraper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ResultPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);



    //

        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.activity_listview, displayResult());
        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
        displayResult();

    }

    public String[] displayResult(){
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.MESSAGE);

        return message.split("\\%+");

    }
}
