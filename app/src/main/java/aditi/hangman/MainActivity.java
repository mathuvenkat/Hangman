package aditi.hangman;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Hangman";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newGameButton = (Button) this.findViewById(R.id.newGame_button);
        newGameButton.setOnClickListener(this);

        Button continueButton = (Button) this.findViewById(R.id.continue_button);
        continueButton.setOnClickListener(this);

        Button exitButton = (Button) this.findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);

        Button vocabButton = (Button) this.findViewById(R.id.vocab_button);
        vocabButton.setOnClickListener(this);



    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.newGame_button:
                // open a new game dialog box
                openNewGameDialog();
                break;

            case R.id.continue_button:
                startGame(Game.CATEGORY_CONTINUE);
                break;


            case R.id.vocab_button:
                Intent intent = new Intent(MainActivity.this, VocabGame.class);
                startActivity(intent);
                break;

            case R.id.exit_button:
                SharedPreferences perfs = getApplicationContext().getSharedPreferences("Game" , MODE_PRIVATE);
                perfs.edit().clear().commit();
                Music.stop(this);
                finish();
                break;

        }
    }


    /**
     * Ask the user what category they want
     */
    private void openNewGameDialog() {
        new AlertDialog.Builder(this).setTitle("Choose a Category").setItems(
                R.array.category, new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface
                                                        dialoginterface, int i) {
                                startGame(i);
                            }
                        }).show();

    }

    /** Start a new game with the given category */
    private void startGame(int i) {
        //Log.d(TAG, "clicked on " + i);
        Intent intent = new Intent(MainActivity.this, Game.class);
        intent.putExtra(Game.KEY_CATEGORY, i);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.d(TAG, "on resume");

    }
    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        //Log.d(TAG, "on Pause");

    }
}