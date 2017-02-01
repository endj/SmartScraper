package se.edinjakupovic.mobilescraper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class ResultPage extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        final String[] Result = fetchResult(); // Our data
        listView = (ListView) findViewById(R.id.ListView); // Our listview object

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.listview_item, Result);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();

                TextView t = (TextView) view;

                int lines = t.getMaxLines();
                if(lines == 1000){
                    t.setMaxLines(5);
                }else{
                    //t.setBackgroundColor(0xFF00FF00);
                    t.setMaxLines(1000);
                }
            }
        });





    }



    public String[] fetchResult(){ // Returns a string array with content
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.MESSAGE);

        return message.split("\\¤¤+");

    }
}
