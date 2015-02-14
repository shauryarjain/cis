package edu.upenn.cis350.hwk2;

import android.app.Activity;
import java.util.List;
import java.util.ArrayList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Intent;


public class MainActivity extends Activity {
    private Spinner spinner1;
    private Button btnPlay;
    public static final int GameActivity_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addItemsOnSpinner();
        addListenerOnButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        List<String> list = new ArrayList<String>();
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);
    }

       public void onLaunchButtonClick(View v) {
             // create an Intent using the current Activity
             // and the class to be created
             Intent i;
             i = new Intent(this, GameActivity.class);

        	     // you can pass arguments to the new Activity
             // using key/value pairs in the Intent
//             i.putExtra(“MESSAGE", “Hey, buddy!");

        	     // pass the Intent to the Activity,
             // using the specified request code
           i.putExtra("SpinnerValueString", String.valueOf(spinner1.getSelectedItem()));

         	 startActivityForResult(i, GameActivity_ID);
        }



    public void addListenerOnButton() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        btnPlay = (Button) findViewById(R.id.btnPlay);

        btnPlay.setOnClickListener(new OnClickListener() {

            //for debugging purposes
            @Override
            public void onClick(View v) {onLaunchButtonClick(v);}

        });
    }

}
