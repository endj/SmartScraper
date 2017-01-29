package se.edinjakupovic.mobilescraper;
//what up edin
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String MESSAGE = "N";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void doSearch(View view){
        Intent intent = new Intent(this, ResultPage.class);  // An intent is used to do something
        EditText input = (EditText) findViewById(R.id.input); // Fiends the search bar
        String intext = input.getText().toString(); // Gets the search message
        intent.putExtra(MESSAGE,intext);  // Adds
        startActivity(intent);
    }
    //
}
