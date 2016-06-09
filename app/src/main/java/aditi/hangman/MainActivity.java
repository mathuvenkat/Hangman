package aditi.hangman;

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

    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.newGame_button:
                // open a new game dialog box
                openNewGameDialog();
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
        Log.d(TAG, "clicked on " + i);
        Intent intent = new Intent(MainActivity.this, Game.class);
        intent.putExtra(Game.KEY_CATEGORY, i);
        startActivity(intent);
    }
}