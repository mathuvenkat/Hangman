package aditi.hangman;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Hangman";
    SharedPreferences sharedPreferences;

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

        sharedPreferences = getSharedPreferences("Game", Context.MODE_PRIVATE);
        Boolean canContinue = sharedPreferences.getBoolean(Game.PREF_ENABLE_CONTINUE, false);
        Log.d(TAG, "Cont" + canContinue.toString());
        if (canContinue == null || !canContinue) {
            continueButton.setEnabled(false);
        } else {
            continueButton.setEnabled(true);
        }


    }

    @Override
    protected void onResume(){
        super.onResume();
        sharedPreferences = getSharedPreferences("Game", Context.MODE_PRIVATE);
        Button continueButton = (Button) this.findViewById(R.id.continue_button);
        Boolean canContinue = sharedPreferences.getBoolean(Game.PREF_ENABLE_CONTINUE, false);
        Log.d("TAG","Cont"+canContinue.toString());
        if (canContinue == null || !canContinue) {
            continueButton.setEnabled(false);
        } else {
            continueButton.setEnabled(true);
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.newGame_button:
                // open a new game dialog box
                openNewGameDialog();
                break;

            case R.id.continue_button:
                sharedPreferences = getSharedPreferences("Game", Context.MODE_PRIVATE);
                int cat = sharedPreferences.getInt(Game.PREF_CATEGORY, Context.MODE_PRIVATE);
                startGame(cat, 1);
                break;


            case R.id.vocab_button:
                Intent intent = new Intent(MainActivity.this, VocabGame.class);
                startActivity(intent);
                break;

            case R.id.exit_button:
                SharedPreferences perfs = getApplicationContext().getSharedPreferences("Game", MODE_PRIVATE);
                perfs.edit().clear().commit();
                sharedPreferences.edit().clear().commit();
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
                                startGame(i, 0);
                            }
                        }).show();

    }

    /**
     * Start a new game with the given category
     */
    private void startGame(int i, int toContinue) {
        //Log.d(TAG, "clicked on " + i);
        Intent intent = new Intent(MainActivity.this, Game.class);
        intent.putExtra(Game.KEY_CATEGORY_INTENT, i);

        if (toContinue == 1) {
            intent.putExtra(Game.KEY_CONTINUE_INTENT, toContinue);
        }
        startActivity(intent);
    }


    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"Onpause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
        Log.d(TAG,"stop");
        SharedPreferences perfs = getApplicationContext().getSharedPreferences("Game", MODE_PRIVATE);
        perfs.edit().clear().commit();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
        SharedPreferences perfs = getApplicationContext().getSharedPreferences("Game", MODE_PRIVATE);
        perfs.edit().clear().commit();
    }
}