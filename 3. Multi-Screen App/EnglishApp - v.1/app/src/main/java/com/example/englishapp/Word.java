package com.example.englishapp;

/**
 *  represents a vocabulary word that user wants to learn,
 * It contains a default translation and an english translation for that word.
 */
public class Word {

    /** English translation for the word */
    private String mEnglishTranslation;

    /** Audio resource ID for the word */
    private int mAudioResourceId;

    /** Image Resource id */
    private int mReasourceId = No_image_Provided;

    private static final int No_image_Provided = -1;

    public Word(String englishTranslation, int AudioId) {
        mEnglishTranslation = englishTranslation;
        mAudioResourceId = AudioId;
    }

    public Word(String English, int Id, int AudioId) {
        mEnglishTranslation = English;
        mReasourceId = Id;
        mAudioResourceId = AudioId;
    }

    /**
     * Get the English translation of the word.
     */
    public String getEnglishTranslation() {
        return mEnglishTranslation;
    }

    /**
     * Get the Image Resource Id of Photo to display
     */
    public int getReasourceId() {
        return mReasourceId;
    }

    /**
     * Returns whether the image is there or not in the word
     * @return
     */
    public boolean hasImage() {
        return mReasourceId != No_image_Provided;
    }

    /**
     * Return the audio resource ID of the word.
     */
    public int getAudioResourceId() {
        return mAudioResourceId;
    }
}
