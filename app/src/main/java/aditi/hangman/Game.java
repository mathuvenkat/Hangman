package aditi.hangman;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by administrator on 6/8/16.
 */
public class Game extends Activity implements View.OnClickListener {

    private static final String TAG = "Game";

    /**
     * The View of the mystery word that is to be guessed
     */
    private TextView mystword;
    /**
     * The View of the string of letters guessed incorrectly
     */
    private TextView wrongletters;

    private TextView clue;

    /**
     * The View of the displayed Hangman image
     */
    private ImageView hangmanimg;

    /**
     * The Dialog currently in the foreground during the game
     */
    private Dialog dialog;

    /**
     * The id of the Dialog currently in the foreground
     */
    private int currentDialogId;
    private int numWrongGuesses;
    private String mysteryWord;
    private int cat;

    static final String KEY_CATEGORY =
            "aditi.hangman.category";

    public static final int CATEGORY_ADJECTIVES = 0;
    public static final int CATEGORY_VERBS = 1;
    public static final int CATEGORY_COUNTRIES = 2;

    static final int DIALOG_WIN_ID = 10;
    static final int DIALOG_LOSE_ID = 11;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * sets the number of wrong guesses to zero wrongGuesses string
     * to empty
     */
    private void initWrongGuesses() {
        numWrongGuesses = 0;
        wrongletters.setText("");
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
                openNewGameDialog();
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
        Log.d(TAG, "clicked on " + i);
        Intent intent = new Intent(Game.this, Game.class);
        intent.putExtra(Game.KEY_CATEGORY, i);
        startActivity(intent);
        finish();
    }

    private void validateGuess(char guess) {
        if (mysteryWord.indexOf(guess) == -1) {
            // Check if letter already exists
            String wrongletters_t =
                    wrongletters.getText().toString();
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
        if (mystword.getText().toString().indexOf("_ ") == -1) {
            showDialog(DIALOG_WIN_ID);
        }
    }

    private void updateMystWord(char ch) {
        char[] updatedWord =
                mystword.getText().toString().toCharArray();
        for (int i = 0; i < mysteryWord.length(); i++) {
            if (ch == mysteryWord.charAt(i)) {
                updatedWord[i * 2] = mysteryWord.charAt(i);
            }
        }
        mystword.setText(new String(updatedWord));
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
        wrongletters.setText(wrongletters.getText() +
                Character.toString(ch));
    }


    /**
     * Sets the mystery word based on the current category
     */
    private void setWordByCategory() {
        cat = getIntent().getIntExtra(KEY_CATEGORY,
                CATEGORY_ADJECTIVES);
        mysteryWord = getWord(cat);
    }

    /**
     * sets the View of Mystery Word to a text view with underscores
     * and spaces
     */
    private void initMystWord() {
        mystword.setText(underscore());
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
//            case 0:
//                hangmanimg.setImageResource(R.drawable.hang_1);
//                break;
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
        mystword = (TextView) this.findViewById(R.id.mysteryword);
        wrongletters = (TextView)
                this.findViewById(R.id.wrongletters);
        clue = (TextView) this.findViewById(R.id.clue);
        hangmanimg = (ImageView) this.findViewById(R.id.hangman_img);
    }

    /**
     * gets the word from the word bank based on the category
     *
     * @param cat
     * @return temp
     */
    private String getWord(int cat) {
        String temp;

        Map<Integer, String[]> map = new HashMap<Integer, String[]>();

        map.put(CATEGORY_ADJECTIVES, new String[]{"beautiful", "charming", "active", "lazy", "huge",
                "happy", "angry", "embarrassed", "scary", "loud", "hot", "heavy", "empty", "humongous",
                "wrong", "thankful", "nice", "purple", "gentle"});
        map.put(CATEGORY_VERBS, new String[]{"swimming", "exercise", "climbing", "dance", "paint"});
        map.put(CATEGORY_COUNTRIES, new String[]{"india", "russia", "china", "japan", "france", "egypt", "norway",
                "thailand"});

        Random gen = new Random();
        String[] res = map.get(cat);
        temp = res[gen.nextInt(res.length)];


        return temp;
    }

    private void useHint() {
        Map<String, String> hintMap = new HashMap<String, String>();
        hintMap.put("beautiful", "used to describe a person");
        hintMap.put("charming", "used to describe a person");
        hintMap.put("active", "opposite of how a snail is");
        hintMap.put("lazy", "another word for not active");
        hintMap.put("huge", "another word for big");

        hintMap.put("happy", "describes positive emotion");
        hintMap.put("angry", "describes negative emotion");
        hintMap.put("embarrassed", "describes negative emotion");
        hintMap.put("scary", "describes negative emotion");
        hintMap.put("loud", "describes sound");
        hintMap.put("hot", "describes touch");
        hintMap.put("heavy", "describes quantity");
        hintMap.put("empty", "describes quantity");
        hintMap.put("humongous", "describes quantity - means large");

        hintMap.put("purple", "describes a color");

        hintMap.put("wrong", "describes a conditional adjective");
        hintMap.put("thankful", "describes positive emotion");
        hintMap.put("nice", "describes positive personality");
        hintMap.put("gentle", "describes positive personality");

        hintMap.put("swimming", "an activity you enjoy");
        hintMap.put("exercise", "healthy for the body");
        hintMap.put("climbing", "what do we do on stairs");
        hintMap.put("dance", "you do this while watching go noodles");
        hintMap.put("paint", "we both do this together sometimes");


        hintMap.put("india", "biggest democracy");
        hintMap.put("russia", "largest country");
        hintMap.put("china", "most populated country");
        hintMap.put("japan", "its in asia - land of rising sun");
        hintMap.put("france", "eifel tower is here");
        hintMap.put("egypt", "gift of Nile - Africa");
        hintMap.put("norway", "land of midnight sun - Europe");
        hintMap.put("thailand", "land of white elephants - Asia");


        Log.d(TAG, "hint for word \n" + mysteryWord);
        Log.d(TAG, "res \n" + hintMap.get(mysteryWord));
        clue.setText(hintMap.get(mysteryWord));

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // views for end game dialog
        TextView endmessege;
        Button endgame1;
        Button endgame2;
        switch (id) {
            case DIALOG_WIN_ID:
                currentDialogId = id;
                // do the work to define the WIN Dialog
                dialog = new Dialog(Game.this);
                dialog.setContentView(R.layout.endgame_dialog);
                dialog.setTitle("You have won! Game Over");
                endmessege = (TextView)
                        dialog.findViewById(R.id.endmessage);
                endgame1 = (Button)
                        dialog.findViewById(R.id.endgame1);
                endgame2 = (Button)
                        dialog.findViewById(R.id.endgame2);
                endmessege.setText("You Win!");
                endgame1.setText("Play Again");
                endgame2.setText("Back To Main");
                endgame1.setOnClickListener(this);
                endgame2.setOnClickListener(this);
                break;
            case DIALOG_LOSE_ID:
                currentDialogId = id;
                // do the work to define the LOSE Dialog
                dialog = new Dialog(Game.this);
                dialog.setContentView(R.layout.endgame_dialog);
                dialog.setTitle("Lost - Game Over");
                endmessege = (TextView)
                        dialog.findViewById(R.id.endmessage);
                endgame1 = (Button)
                        dialog.findViewById(R.id.endgame1);
                endgame2 = (Button)
                        dialog.findViewById(R.id.endgame2);
                endmessege.setText("You did not guess!");
                endgame1.setText("Try Again");
                endgame2.setText("Back To Main");
                endgame1.setOnClickListener(this);
                endgame2.setOnClickListener(this);
                break;
            default:
                dialog = null;
        }
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
