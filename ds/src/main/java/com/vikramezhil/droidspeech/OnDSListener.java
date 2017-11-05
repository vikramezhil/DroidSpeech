package com.vikramezhil.droidspeech;

import java.util.List;

/**
 * Droid Speech Listener
 *
 * @author Vikram Ezhil
 */

public interface OnDSListener
{
    /**
     * The droid speech supported languages
     *
     * @param currentSpeechLanguage The current speech language
     *
     * @param supportedSpeechLanguages The supported speech languages
     */
    void onDroidSpeechSupportedLanguages(String currentSpeechLanguage, List<String> supportedSpeechLanguages);

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
     */
    void onDroidSpeechFinalResult(String finalSpeechResult);

    /**
     * The droid speech recognition was closed by user
     */
    void onDroidSpeechClosedByUser();

    /**
     * The droid speech recognizer error update
     *
     * @param errorMsg The error message
     */
    void onDroidSpeechError(String errorMsg);
}
