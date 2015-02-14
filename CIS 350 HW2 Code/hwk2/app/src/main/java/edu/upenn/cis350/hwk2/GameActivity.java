package edu.upenn.cis350.hwk2;

/**
 * Created by shaurya on 2/11/15.
 */
import android.app.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class GameActivity extends Activity {

    private final int ID_UNDO = 1;
    private final int ID_CLEAR = 2;
    private final int ID_QUIT = 3;
    private final int MainActivity_ID = 10;
    private GameView gv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //populates entire activity with custom view
        gv = new GameView(this);
        setContentView(gv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {x
//            case R.id.new_game:
//                newGame();
//                return true;
//            case R.id.help:
//                showHelp();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
            switch (item.getItemId()) {
            case R.id.action_undo:
                // undo(); handle
                getIntent().putExtra("undo_state", "1");
                gv.postInvalidate();

                return true;
            case R.id.action_clear:
                // clear(); handle
                getIntent().putExtra("clear_state", "1");
                gv.postInvalidate();
                return true;
            case R.id.action_quit:
                // quit(); handle
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

//        return super.onOptionsItemSelected(item);
    }
}
