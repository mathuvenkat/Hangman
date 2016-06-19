package aditi.hangman;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
/**
 * Created by administrator on 6/9/16.
 */
public class Music {

    private static MediaPlayer mp = null;
    private static final String TAG = "Music";


    public static void play(Context context, int resource) {
        stop(context);
        //Log.v(TAG , "playing music");
        mp = MediaPlayer.create(context, resource);
        mp.start();
    }


    /**
     * Stop the music
     */
    public static void stop(Context context) {
        if (mp != null) {
            //Log.v(TAG , "stopping music");
            mp.stop();
            mp.release();
            mp = null;
        }
    }
}
