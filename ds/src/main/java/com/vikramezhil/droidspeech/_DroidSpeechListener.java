package com.vikramezhil.droidspeech;

/**
 * Created by Vikram Ezhil on 29/07/17
 *
 * Email: vikram.ezhil.1988@gmail.com
 *
 * Droid Speech Listener
 */

public interface _DroidSpeechListener
{
    /**
     * The droid speech rms changed result
     *
     * @param rmsChangedValue The rms changed result
     */
    void onDroidSpeechRmsChanged(float rmsChangedValue);

    /**
     * The droid speech recognizer live result
     *
     * @param liveSpeechResult The live speech result
     */
    void onDroidSpeechLiveResult(String liveSpeechResult);

    /**
     * The droid speech recognizer final result
     *
     * @param finalSpeechResult The final speech result
     *
     * @param droidSpeechWillListen The droid speech continue listening status
     */
    void onDroidSpeechFinalResult(String finalSpeechResult, boolean droidSpeechWillListen);

    /**
     * The droid speech recognizer error update
     *
     * @param errorMsg The error message
     */
    void onDroidSpeechError(String errorMsg);
}
