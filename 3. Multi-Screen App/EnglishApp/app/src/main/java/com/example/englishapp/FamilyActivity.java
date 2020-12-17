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

public class FamilyActivity extends AppCompatActivity {

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

    private MediaPlayer.OnCompletionListener OnComplete =new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);

        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Mother",R.drawable.family_mother,R.raw.mother));
        words.add(new Word("Father",R.drawable.family_father,R.raw.father));
        words.add(new Word("Grandmother",R.drawable.family_grandmother,R.raw.grandmother));
        words.add(new Word("Grandfather",R.drawable.family_grandfather,R.raw.grandfather));
        words.add(new Word("Great-Grandmother",R.drawable.family_grandmother,R.raw.great_grandmother));
        words.add(new Word("Great-Grandfather",R.drawable.family_grandfather,R.raw.great_grandfather));
        words.add(new Word("Daughter",R.drawable.family_daughter,R.raw.daughter));
        words.add(new Word("Son",R.drawable.family_son,R.raw.son));
        words.add(new Word("Younger Brother",R.drawable.family_younger_brother,R.raw.younger_brother));
        words.add(new Word("Younger Sister",R.drawable.family_younger_sister,R.raw.younger_sister));
        words.add(new Word("Older Brother",R.drawable.family_older_brother,R.raw.older_brother));
        words.add(new Word("Older Sister",R.drawable.family_older_sister,R.raw.older_sister));
        words.add(new Word("Aunt",R.drawable.family_mother,R.raw.aunt));
        words.add(new Word("Uncle",R.drawable.family_father,R.raw.uncle));
        words.add(new Word("Niece",R.drawable.family_younger_brother,R.raw.neice));
        words.add(new Word("Nephew",R.drawable.family_younger_sister,R.raw.nephew));
        words.add(new Word("Cousin",R.drawable.family_older_sister,R.raw.cousin));
        words.add(new Word("Mother-in-law",R.drawable.family_grandmother,R.raw.mother_in_law));
        words.add(new Word("Father-in-law",R.drawable.family_grandfather,R.raw.father_in_law));
        words.add(new Word("daughter-in-law",R.drawable.family_daughter,R.raw.daughter_in_law));
        words.add(new Word("Son-in-law",R.drawable.family_son,R.raw.son_in_law));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdaptor adapter = new WordAdaptor(this, words,R.color.category_family);

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
                    player = MediaPlayer.create(FamilyActivity.this, word.getAudioResourceId());

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
