package com.example.englishapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColoursActivity extends AppCompatActivity {

    private MediaPlayer player;

    private AudioManager mAudioManager;

    private AudioManager.OnAudioFocusChangeListener mOnChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // The AudioFOCUS_LOSS_TRANSIENT case means that we've lost audio focus
                        // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK casev means
                        // our app is allowed to continue playing sound but at a lower volume
                        // both cases the same way because our app is playing short sound files.

                        // Pause playback and reset player to the start of the file. Thta way,
                        // play the word from the beginning when we resume playback.
                        player.pause();
                        player.seekTo(0);
                    } else if(focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // The AudioFOCUS_gain case means we have regained focus and can resume playback.
                        player.start();
                    }
                    else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // the AUDIOFOCUS_LOSS case means we've lost audio focus and
                        // stop playback and cleanup resources.
                        releaseMediaPlayer();
                    }
                }
            };

    private MediaPlayer.OnCompletionListener OnComplete = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colours);

        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("White",R.color.White,R.raw.white));
        words.add(new Word("Yellow",R.color.Yellow,R.raw.yellow));
        words.add(new Word("Black",R.color.Black,R.raw.black));
        words.add(new Word("Brown",R.color.Brown,R.raw.brown));
        words.add(new Word("Dark Blue",R.color.DarkBlue,R.raw.dark_blue));
        words.add(new Word("Dark Gray",R.color.DarkGray,R.raw.dark_grey));
        words.add(new Word("Dark Green",R.color.DarkGreen,R.raw.dark_green));
        words.add(new Word("Dark Orange",R.color.DarkOrange,R.raw.dark_orange));
        words.add(new Word("Dark Red",R.color.DarkRed,R.raw.dark_red));
        words.add(new Word("Dark Violet",R.color.DarkViolet,R.raw.dark_violet));
        words.add(new Word("Dark Pink",R.color.DeepPink,R.raw.dark_pink));
        words.add(new Word("Gray",R.color.Gray,R.raw.gray));
        words.add(new Word("Green",R.color.Green,R.raw.green));
        words.add(new Word("Hot Pink",R.color.HotPink,R.raw.hot_pink));
        words.add(new Word("Indigo",R.color.Indigo,R.raw.indigo));
        words.add(new Word("Light Blue",R.color.LightBlue,R.raw.light_blue));
        words.add(new Word("Magenta",R.color.Magenta,R.raw.magenta));
        words.add(new Word("Maroon",R.color.Maroon,R.raw.maroon));
        words.add(new Word("Light Green",R.color.LightGreen,R.raw.light_green));
        words.add(new Word("Navy Blue",R.color.Navy,R.raw.navy_blue));
        words.add(new Word("Orange",R.color.Orange,R.raw.orange));
        words.add(new Word("Pink",R.color.Pink,R.raw.pink));
        words.add(new Word("Purple",R.color.Purple,R.raw.purple));
        words.add(new Word("Red",R.color.Red,R.raw.dark_red));
        words.add(new Word("Royal Blue",R.color.RoyalBlue,R.raw.royal_blue));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdaptor adapter = new WordAdaptor(this, words,R.color.category_colors);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // activity_numbers.xml layout file.
        ListView listView = (ListView) findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Get the {@link Word} object at the given position the user clicked on
                Word word = words.get(position);

                releaseMediaPlayer();

                int result = mAudioManager.requestAudioFocus(mOnChangeListener,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//                    mAudioManager.registerMediaButtonEventReceiver(RemoteControlReceive);
                    // we have audio focus now.

                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word
                    player = MediaPlayer.create(ColoursActivity.this, word.getAudioResourceId());

                    // Start the audio file
                    player.start();
                    player.setOnCompletionListener(OnComplete);
                }

            }
        });
    }

    protected void onStop() {
        super.onStop();
        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (player != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            player.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            player = null;
            // Abondon audio focus when playback complete
            mAudioManager.abandonAudioFocus(mOnChangeListener);
        }
    }
}