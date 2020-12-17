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

public class PhrasesActivity extends AppCompatActivity {

    private MediaPlayer player;

    /** Handles audio focus when playing a sound file */
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
        setContentView(R.layout.activity_phrases);

        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Hello",R.raw.st_one));
        words.add(new Word("I am fine.",R.raw.st_two));
        words.add(new Word("Where are you from?",R.raw.st_three));
        words.add(new Word("Who are you?",R.raw.st_four));
        words.add(new Word("What is this?",R.raw.st_five));
        words.add(new Word("My name is ..",R.raw.st_six));
        words.add(new Word("I am from ..",R.raw.st_seven));
        words.add(new Word("I study in class ..",R.raw.st_eight));
        words.add(new Word("Thank you so much.",R.raw.st_nine));
        words.add(new Word("I am really sorry!!",R.raw.st_ten));
        words.add(new Word("Excuse me.",R.raw.st_eleven));
        words.add(new Word("I really appreciate.",R.raw.st_twelve));
        words.add(new Word("I do not understand.",R.raw.st_thirteen));
        words.add(new Word("Could you repeat that please?",R.raw.st_forteen));
        words.add(new Word("What does this mean?",R.raw.st_fifteen));
        words.add(new Word("That helps a lot!",R.raw.st_sixteen));
        words.add(new Word("What do you mean?",R.raw.st_seventeen));
        words.add(new Word("How are you?",R.raw.st_eighteen));
        words.add(new Word("Nice to meet you.",R.raw.st_ninteen));
        words.add(new Word("What do you do?",R.raw.st_twenty));
        words.add(new Word("What do you like to do?",R.raw.st_twentyone));
        words.add(new Word("Can I help you?",R.raw.st_twentytwo));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdaptor adapter = new WordAdaptor(this, words,R.color.category_phrases);

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

                // Release the media player if it currently exists because we are about to play a different file.
                releaseMediaPlayer();

                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnChangeListener,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // mAudioManager.registerMediaButtonEventReceiver(RemoteControlReceive);
                    // we have audio focus now.

                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word
                    player = MediaPlayer.create(PhrasesActivity.this, word.getAudioResourceId());

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