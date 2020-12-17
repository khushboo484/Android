package com.example.englishapp;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class NumbersActivity extends AppCompatActivity {

    /** Handles playback of all the sound files */
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
        setContentView(R.layout.activity_numbers);

        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("One",R.drawable.number_one,R.raw.one));
        words.add(new Word("Two",R.drawable.number_two,R.raw.two));
        words.add(new Word("Three",R.drawable.number_three,R.raw.three));
        words.add(new Word("Four",R.drawable.number_four,R.raw.four));
        words.add(new Word("Five",R.drawable.number_five,R.raw.five));
        words.add(new Word("Six",R.drawable.number_six,R.raw.six));
        words.add(new Word("Seven",R.drawable.number_seven,R.raw.seven));
        words.add(new Word("Eight",R.drawable.number_eight,R.raw.eight));
        words.add(new Word("Nine",R.drawable.number_nine,R.raw.nine));
        words.add(new Word("ten",R.drawable.number_ten,R.raw.ten));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdaptor adapter = new WordAdaptor(this, words,R.color.category_numbers);

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

                //Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnChangeListener,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // mAudioManager.registerMediaButtonEventReceiver(RemoteControlReceive);
                    // we have audio focus now.

                        // Create and setup the {@link MediaPlayer} for the audio resource associated
                        // with the current word
                        player = MediaPlayer.create(NumbersActivity.this, word.getAudioResourceId());

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