package aditi.hangman;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Created by administrator on 6/8/16.
 */
public class Game extends Activity implements View.OnClickListener {

    private static final String TAG = "Game";

    public static final int CATEGORY_ADJECTIVES = 0;
    public static final int CATEGORY_VERBS = 1;
    public static final int CATEGORY_COUNTRIES = 2;

    private TextView guessedLettersTextView;
    private TextView wronglettersTextView;
    private TextView clueTextView;
    private ImageView hangmanimg;
    private ImageView resultImg;


    private Dialog dialog;
    private int currentDialogId;

    private String mysteryWord;
    private int numWrongGuesses;
    private int categorySelected = 0;

    private boolean guessCompleted = false;

    private int isContinue = 0;
    //protected static final int CATEGORY_CONTINUE = -1;

    SharedPreferences sharedPreferences;

    static final int DIALOG_WIN_ID = 10;
    static final int DIALOG_LOSE_ID = 11;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    static final String PREF_MYSTWORD = "aditi.hangman.guessedLettersTextView";
    static final String PREF_MYSTERYWORD = "aditi.hangman.mysterytword";
    static final String PREF_WRONGLETTERS = "aditi.hangman.wronglettersTextView";
    static final String PREF_NUMWRONGGUESSES = "aditi.hangman.numWrongGuesses";
    static final String PREF_CLUE = "aditi.hangman.clueTextView";
    static final String PREF_CATEGORY = "aditi.hangman.category";
    static final String PREF_ENABLE_CONTINUE = "aditi.hangman.isContinueEnabled";

    static final String KEY_CATEGORY_INTENT =
            "aditi.hangman.category";
    static final String KEY_CONTINUE_INTENT = "aditi.hangman.continue";

    private int score = 0;
    private int total = 0;

    static final String PREF_score = "score";
    static final String PREF_total = "total";

    Random gen = new Random();


    Properties prop_0 = new Properties();
    Properties prop_1 = new Properties();
    Properties prop_2 = new Properties();


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("Game", Context.MODE_PRIVATE);
        isContinue = getIntent().getIntExtra(KEY_CONTINUE_INTENT, 0);
        initProperties();

        score = getPreferences(MODE_PRIVATE).getInt(
                PREF_score, 0);

        total = getPreferences(MODE_PRIVATE).getInt(
                PREF_total, 0);

        super.onCreate(savedInstanceState);
        // set content view to game layout
        setContentView(R.layout.game);
        // bind the resource views to textview and imageview holders
        bindViews();
        // get selected category and assign mystery word
        setWordByCategory();
        // initialize the Mystery word view with underscores
        initMystWord();
        // initialize the number of wrong guesses and wrong guesses view string
        initWrongGuesses();
        // set OnClick Listeners for each button in the view
        setClickListeners();

        loadGameIfSaved();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {

        //Log.d(TAG, "onPause");
        super.onPause();
        Music.stop(this);

        // Save the current mystery word
        getPreferences(MODE_PRIVATE).edit().putString(PREF_MYSTERYWORD,
                mysteryWord).commit();
        getPreferences(MODE_PRIVATE).edit().putString(PREF_MYSTWORD,
                guessedLettersTextView.getText().toString()).commit();
        getPreferences(MODE_PRIVATE).edit().putString(PREF_WRONGLETTERS,
                wronglettersTextView.getText().toString()).commit();
        getPreferences(MODE_PRIVATE).edit().putInt(PREF_NUMWRONGGUESSES,
                numWrongGuesses).commit();
        getPreferences(MODE_PRIVATE).edit().putString(PREF_CLUE,
                clueTextView.getText().toString()).commit();


        if (!guessCompleted) {
            sharedPreferences.edit().putBoolean(PREF_ENABLE_CONTINUE, true).commit();
        }

    }

    /**
     * Loads the old Game if game was saved
     */
    private void loadGameIfSaved() {
        if (isContinue == 1) {
            mysteryWord = getPreferences(MODE_PRIVATE).getString(
                    PREF_MYSTERYWORD, getWord(CATEGORY_ADJECTIVES));

            guessedLettersTextView.setText(getPreferences(MODE_PRIVATE).getString(
                    PREF_MYSTWORD, underscore()));

            String wrongLetters = getPreferences(MODE_PRIVATE).getString(
                    PREF_WRONGLETTERS, "");
            wronglettersTextView.setText(wrongLetters);
            numWrongGuesses =
                    getPreferences(MODE_PRIVATE).getInt(
                            PREF_NUMWRONGGUESSES, 0);
            String clue = getPreferences(MODE_PRIVATE).getString(PREF_CLUE, "");
            clueTextView.setText(clue);
            if (clue.length() > 0) {
                Button hint = (Button) this.findViewById(R.id.hint_button);
                hint.setEnabled(false);
            }

            Button tmp;
            String alreadyGuessed = (String) guessedLettersTextView.getText();
            alreadyGuessed = alreadyGuessed.replaceAll("[^a-zA-Z]", "");

            for (int i = 0; i < alreadyGuessed.length(); i++) {
                int buttonId = getResources().getIdentifier(
                        Character.toString(alreadyGuessed.charAt(i)) + "_button", "id", getPackageName());
                tmp = (Button) this.findViewById(buttonId);
                tmp.setEnabled(false);
            }

            for (int i = 0; i < wrongLetters.length(); i++) {
                int buttonId = getResources().getIdentifier(
                        Character.toString(wrongLetters.charAt(i)) + "_button", "id", getPackageName());
                tmp = (Button) this.findViewById(buttonId);
                tmp.setEnabled(false);
            }

            updateImg();
        } else {
            guessedLettersTextView.setText(underscore());
        }
    }

    /**
     * sets the number of wrong guesses to zero wrongGuesses string
     * to empty
     */
    private void initWrongGuesses() {
        numWrongGuesses = 0;
        wronglettersTextView.setText("");
    }


    private void initProperties() {

        Map<String, Properties> map = new HashMap<>();
        map.put("config_0.properties", prop_0);
        map.put("config_1.properties", prop_1);
        map.put("config_2.properties", prop_2);


        InputStream is;
        for (String file : map.keySet()) {
            is = getClass().getClassLoader().getResourceAsStream(file);

            try {
                if (is != null) {
                    map.get(file).load(is);
                }
            } catch (Exception e) {
                //Log.e(TAG, "Unable to load properties...");

            }
        }

    }

    private void setClickListeners() {

        this.findViewById(R.id.a_button).setOnClickListener(this);
        this.findViewById(R.id.b_button).setOnClickListener(this);
        this.findViewById(R.id.c_button).setOnClickListener(this);
        this.findViewById(R.id.d_button).setOnClickListener(this);
        this.findViewById(R.id.e_button).setOnClickListener(this);
        this.findViewById(R.id.f_button).setOnClickListener(this);
        this.findViewById(R.id.g_button).setOnClickListener(this);
        this.findViewById(R.id.h_button).setOnClickListener(this);
        this.findViewById(R.id.i_button).setOnClickListener(this);
        this.findViewById(R.id.j_button).setOnClickListener(this);
        this.findViewById(R.id.k_button).setOnClickListener(this);
        this.findViewById(R.id.l_button).setOnClickListener(this);
        this.findViewById(R.id.m_button).setOnClickListener(this);
        this.findViewById(R.id.n_button).setOnClickListener(this);
        this.findViewById(R.id.o_button).setOnClickListener(this);
        this.findViewById(R.id.p_button).setOnClickListener(this);
        this.findViewById(R.id.q_button).setOnClickListener(this);
        this.findViewById(R.id.r_button).setOnClickListener(this);
        this.findViewById(R.id.s_button).setOnClickListener(this);
        this.findViewById(R.id.t_button).setOnClickListener(this);
        this.findViewById(R.id.u_button).setOnClickListener(this);
        this.findViewById(R.id.v_button).setOnClickListener(this);
        this.findViewById(R.id.w_button).setOnClickListener(this);
        this.findViewById(R.id.x_button).setOnClickListener(this);
        this.findViewById(R.id.y_button).setOnClickListener(this);
        this.findViewById(R.id.z_button).setOnClickListener(this);
        this.findViewById(R.id.hint_button).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Button temp = (Button) this.findViewById(v.getId());

        if (temp != null) {
            temp.setEnabled(false);
        }
        switch (v.getId()) {
            case R.id.a_button:
                validateGuess('a');
                break;
            case R.id.b_button:
                validateGuess('b');
                break;
            case R.id.c_button:
                validateGuess('c');
                break;
            case R.id.d_button:
                validateGuess('d');
                break;
            case R.id.e_button:
                validateGuess('e');
                break;
            case R.id.f_button:
                validateGuess('f');
                break;
            case R.id.g_button:
                validateGuess('g');
                break;
            case R.id.h_button:
                validateGuess('h');
                break;
            case R.id.i_button:
                validateGuess('i');
                break;
            case R.id.j_button:
                validateGuess('j');
                break;
            case R.id.k_button:
                validateGuess('k');
                break;
            case R.id.l_button:
                validateGuess('l');
                break;
            case R.id.m_button:
                validateGuess('m');
                break;
            case R.id.n_button:
                validateGuess('n');
                break;
            case R.id.o_button:
                validateGuess('o');
                break;
            case R.id.p_button:
                validateGuess('p');
                break;
            case R.id.q_button:
                validateGuess('q');
                break;
            case R.id.r_button:
                validateGuess('r');
                break;
            case R.id.s_button:
                validateGuess('s');
                break;
            case R.id.t_button:
                validateGuess('t');
                break;
            case R.id.u_button:
                validateGuess('u');
                break;
            case R.id.v_button:
                validateGuess('v');
                break;
            case R.id.w_button:
                validateGuess('w');
                break;
            case R.id.x_button:
                validateGuess('x');
                break;
            case R.id.y_button:
                validateGuess('y');
                break;
            case R.id.z_button:
                validateGuess('z');
                break;
            case R.id.endgame1:
                categorySelected = getPreferences(MODE_PRIVATE).getInt(
                        PREF_CATEGORY, 0);
                getIntent().putExtra(KEY_CATEGORY_INTENT, categorySelected);
                sharedPreferences.edit().putBoolean(PREF_ENABLE_CONTINUE, false).commit();
                startGame(categorySelected);
                break;
            case R.id.endgame2:
                finish();
                sharedPreferences.edit().putBoolean(PREF_ENABLE_CONTINUE, false).commit();
                break;
            case R.id.hint_button:
                useHint();
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

    /**
     * Start a new game with the given category
     */
    private void startGame(int i) {
        ////Log.d(TAG, "game clicked on " + i);
        Intent intent = new Intent(Game.this, Game.class);
        intent.putExtra(Game.KEY_CATEGORY_INTENT, i);
        startActivity(intent);
        finish();
    }

    private void validateGuess(char guess) {
        if (mysteryWord.indexOf(guess) == -1) {
            Music.play(this, R.raw.wrong);
            // Check if letter already exists
            String wrongletters_t =
                    wronglettersTextView.getText().toString();
            if (wrongletters_t.indexOf(guess) == -1) {
                // Letter not found in word
                if (numWrongGuesses < 10) {
                    numWrongGuesses++;
                    updateWrongGuesses(guess);
                    updateImg();
                }
                checkLose();
            }
        } else {
            // Update word with guessed letter
            Music.play(this, R.raw.correct);
            if (numWrongGuesses < 10) {
                updateMystWord(guess);
                checkWin();
            } else {
                checkLose();
            }
        }
    }

    /**
     * checks to see if player has won the game
     */
    private void checkWin() {
        if (guessedLettersTextView.getText().toString().indexOf("_ ") == -1) {
            showDialog(DIALOG_WIN_ID);
        }
    }

    private void updateMystWord(char ch) {
        char[] updatedWord =
                guessedLettersTextView.getText().toString().toCharArray();
        for (int i = 0; i < mysteryWord.length(); i++) {
            if (ch == mysteryWord.charAt(i)) {
                updatedWord[i * 2] = mysteryWord.charAt(i);
            }
        }
        guessedLettersTextView.setText(new String(updatedWord));
    }

    /**
     * checks to see if the player has lost the game
     */
    private void checkLose() {
        if (numWrongGuesses == 10) {
            showDialog(DIALOG_LOSE_ID);
        }
    }

    /**
     * updates the View of wrong guesses with the recent wrong guess
     */
    private void updateWrongGuesses(char ch) {
        wronglettersTextView.setText(wronglettersTextView.getText() +
                Character.toString(ch));
    }


    /**
     * Sets the mystery word based on the current category
     */
    private void setWordByCategory() {
        categorySelected = getIntent().getIntExtra(KEY_CATEGORY_INTENT,
                CATEGORY_ADJECTIVES);
        if (isContinue == 1) {
            mysteryWord = getPreferences(MODE_PRIVATE).getString(PREF_MYSTERYWORD, getWord(CATEGORY_ADJECTIVES));
            return;
        }
        mysteryWord = getWord(categorySelected).toLowerCase();
    }

    /**
     * sets the View of Mystery Word to a text view with underscores
     * and spaces
     */
    private void initMystWord() {
        guessedLettersTextView.setText(underscore());
    }

    /**
     * sets the Hangman image to the starting image
     */
    private void initImg() {
        hangmanimg.setImageResource(R.drawable.hang_1);
    }

    /**
     * updates the Hangman image based on the number of wrong guesses
     */
    private void updateImg() {
        switch (numWrongGuesses) {
            case 0:
                hangmanimg.setImageResource(R.drawable.hang_0);
                break;
            case 1:
                hangmanimg.setImageResource(R.drawable.hang_1);
                break;
            case 2:
                hangmanimg.setImageResource(R.drawable.hang_2);
                break;
            case 3:
                hangmanimg.setImageResource(R.drawable.hang_3);
                break;
            case 4:
                hangmanimg.setImageResource(R.drawable.hang_4);
                break;
            case 5:
                hangmanimg.setImageResource(R.drawable.hang_5);
                break;
            case 6:
                hangmanimg.setImageResource(R.drawable.hang_6);
                break;
            case 7:
                hangmanimg.setImageResource(R.drawable.hang_7);
                break;
            case 8:
                hangmanimg.setImageResource(R.drawable.hang_8);
                break;
            case 9:
                hangmanimg.setImageResource(R.drawable.hang_9);
                break;
            case 10:
                hangmanimg.setImageResource(R.drawable.hang_10);
                break;
        }
    }

    /**
     * converts the textview to a view with underscores and spaces
     *
     * @return
     */
    private String underscore() {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < mysteryWord.length(); i++) {
            result.append("_ ");
        }
        return result.toString();
    }

    /**
     * binds each manipulated view to a private variable
     */
    private void bindViews() {
        guessedLettersTextView = (TextView) this.findViewById(R.id.mysteryword);
        wronglettersTextView = (TextView)
                this.findViewById(R.id.wrongletters);
        clueTextView = (TextView) this.findViewById(R.id.clue);
        hangmanimg = (ImageView) this.findViewById(R.id.hangman_img);
        resultImg = (ImageView) this.findViewById(R.id.congrats);
    }

    /**
     * gets the word from the word bank based on the category
     *
     * @param cat
     * @return temp
     */
    private String getWord(int cat) {
        String temp;

        Set<Object> keys = new HashSet<>();
        switch (cat) {
            case 0:
                keys = prop_0.keySet();
                break;
            case 1:
                keys = prop_1.keySet();
                break;
            case 2:
                keys = prop_2.keySet();
                break;

        }
        List<Object> list = new ArrayList<Object>(keys);
        temp = (String) list.get(gen.nextInt(keys.size()));
        return temp;
    }

    private void useHint() {

        Properties temp = new Properties();
        switch (categorySelected) {
            case 0:
                temp = prop_0;
                break;
            case 1:
                temp = prop_1;
                break;
            case 2:
                temp = prop_2;
                break;

        }
        clueTextView.setText((String) temp.get(mysteryWord));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // views for end game dialog
        TextView endmessage;
        Button endgame1;
        Button endgame2;
        ImageView result = null;

        switch (id) {
            case DIALOG_WIN_ID:
                currentDialogId = id;
                score++;
                total++;
                // do the work to define the WIN Dialog
                dialog = new Dialog(Game.this);

                dialog.setContentView(R.layout.endgame_dialog);
                dialog.setTitle("You have won!");
                endmessage = (TextView)
                        dialog.findViewById(R.id.endmessage);
                endgame1 = (Button)
                        dialog.findViewById(R.id.endgame1);
                endgame2 = (Button)
                        dialog.findViewById(R.id.endgame2);
                endmessage.setText("Correct! Scored " + score + " out of " + total);
                endgame1.setText("Play Again");
                endgame2.setText("Back To Main");
                endgame1.setOnClickListener(this);
                endgame2.setOnClickListener(this);
                Music.play(this, R.raw.cheering);

                break;
            case DIALOG_LOSE_ID:
                currentDialogId = id;

                // do the work to define the LOSE Dialog
                dialog = new Dialog(Game.this);
                dialog.setContentView(R.layout.endgame_dialog);
                dialog.setTitle("Lost - Game Over");
                endmessage = (TextView)
                        dialog.findViewById(R.id.endmessage);
                endgame1 = (Button)
                        dialog.findViewById(R.id.endgame1);
                endgame2 = (Button)
                        dialog.findViewById(R.id.endgame2);
                result = (ImageView) dialog.findViewById(R.id.congrats);
                endmessage.setText("You did not guess. Word is " + mysteryWord);
                endgame1.setText("Try Again");
                endgame2.setText("Back To Main");
                endgame1.setOnClickListener(this);
                endgame2.setOnClickListener(this);
                if (result != null) {
                    result.setImageResource(R.drawable.lost);
                }
                total++;
                break;
            default:
                dialog = null;
        }

        getPreferences(MODE_PRIVATE).edit().putInt(PREF_score,
                score).commit();
        getPreferences(MODE_PRIVATE).edit().putInt(PREF_total,
                total).commit();
        getPreferences(MODE_PRIVATE).edit().putInt(PREF_CATEGORY,
                categorySelected).commit();
        sharedPreferences.edit().putBoolean(PREF_ENABLE_CONTINUE, false).commit();
        guessCompleted = true;

        return dialog;
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Game Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://aditi.hangman/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {

        ////Log.d(TAG, "onStop");
        super.onPause();
        ////Log.d(TAG, "stopping music");
        Music.stop(this);

        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Game Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://aditi.hangman/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
