package com.vikramezhil.droidspeech;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.app.FragmentManager;
import android.util.Log;

/**
 * Created by Vikram Ezhil on 29/07/17
 *
 * Email: vikram.ezhil.1988@gmail.com
 *
 * Droid Speech
 */

public class DroidSpeech implements RecognitionListener
{
    private final String TAG = "DroidSpeech";

    private Context context;

    private DroidSpeechPermissions droidSpeechPermissions;

    private SpeechRecognizer droidSpeechRecognizer;

    private Intent speechIntent;

    private AudioManager audioManager;

    private Handler restartDroidSpeech = new Handler();

    private Handler droidSpeechPartialResult = new Handler();

    private long startListeningTime, pauseAndSpeakTime;

    private boolean continuousVoiceRecognition = true, onReadyForSpeech = false, speechResultFound = false;

    private _DroidSpeechListener droidSpeechListener;

    // MARK: Constructor

    /**
     * Droid Speech Constructor
     *
     * @param context The application context instance
     *
     * @param fragmentManager The fragment manager instance (Pass this "null" if Droid Speech doesn't
     *                        want to handle permissions)
     */
    public DroidSpeech(Context context, FragmentManager fragmentManager)
    {
        this.context = context;

        if(fragmentManager != null)
        {
            // Initializing the Non-UI droid speech fragment and beginning transaction
            droidSpeechPermissions = new DroidSpeechPermissions();
            fragmentManager.beginTransaction().add(droidSpeechPermissions, TAG).commit();
        }

        // Initializing the droid speech properties
        initDroidSpeechProperties();
    }

    // MARK: Droid Speech Listener

    /**
     * Sets the droid speech listener
     *
     * @param droidSpeechListener The class instance to initialize droid speech listener
     */
    public void setOnDroidSpeechListener(_DroidSpeechListener droidSpeechListener)
    {
        this.droidSpeechListener = droidSpeechListener;
    }

    // MARK: Droid Speech Methods

    /**
     * Initializes the droid speech properties
     */
    private void initDroidSpeechProperties()
    {
        // Initializing the droid speech recognizer
        droidSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);

        // Initializing the speech intent
        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
        speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, ExtensionDroidSpeech.MAX_VOICE_RESULTS);

        // Initializing the audio Manager
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * Gets the continuous voice recognition status
     *
     * @return The continuous voice recognition status
     */
    public boolean getContinuousVoiceRecognition()
    {
        return continuousVoiceRecognition;
    }

    /**
     * Sets the continuous voice recognition status
     *
     * @param continuousVoiceRecognition The continuous voice recognition status
     */
    public void setContinuousVoiceRecognition(boolean continuousVoiceRecognition)
    {
        this.continuousVoiceRecognition = continuousVoiceRecognition;
    }

    /**
     * Starts the droid speech recognition
     *
     * Trigger Listeners - onDroidSpeechError(int errorType)
     */
    public void startDroidSpeechRecognition()
    {
        if(ExtensionDroidSpeech.isInternetEnabled(context))
        {
            if(droidSpeechPermissions == null || droidSpeechPermissions.checkForAudioPermissions(context))
            {
                startListeningTime = System.currentTimeMillis();
                pauseAndSpeakTime = startListeningTime;
                speechResultFound = false;

                if(droidSpeechRecognizer == null || speechIntent == null || audioManager == null)
                {
                    // Initializing the droid speech properties if found not initialized
                    initDroidSpeechProperties();
                }

                // Setting the droid speech recognizer listener
                droidSpeechRecognizer.setRecognitionListener(this);

                // Canceling any running droid speech operations, before listening
                cancelDroidSpeechOperations();

                // Start Listening
                droidSpeechRecognizer.startListening(speechIntent);
            }
            else
            {
                // Requesting audio permissions
                droidSpeechPermissions.requestForAudioPermission();
            }
        }
        else
        {
            if(droidSpeechListener == null)
            {
                Log.e(TAG, context.getResources().getString(R.string.internet_not_enabled));
            }
            else
            {
                // Sending an update that there was a network error
                droidSpeechListener.onDroidSpeechError(context.getResources().getString(R.string.internet_not_enabled));
            }
        }
    }

    /**
     * Restarts droid speech recognition after a small delay
     */
    private void restartDroidSpeechRecognition()
    {
        restartDroidSpeech.postDelayed(new Runnable() {

            @Override
            public void run() {

                startDroidSpeechRecognition();
            }

        }, ExtensionDroidSpeech.MAX_PAUSE_TIME);
    }

    /**
     * Cancels the droid speech operations
     */
    private void cancelDroidSpeechOperations()
    {
        if (droidSpeechRecognizer != null)
        {
            droidSpeechRecognizer.cancel();
        }
    }

    /**
     * Closes the droid speech operations
     */
    public void closeDroidSpeechOperations()
    {
        if (droidSpeechRecognizer != null)
        {
            droidSpeechRecognizer.destroy();
        }

        // Removing the partial result callback handler if applicable
        droidSpeechPartialResult.removeCallbacksAndMessages(null);

        // If audio beep was muted, enabling it again
        muteAudio(false);
    }

    /**
     * Mutes (or) un mutes the audio
     *
     * @param mute The mute audio status
     */
    @SuppressWarnings("deprecation")
    private void muteAudio(Boolean mute)
    {
        try
        {
            // mute (or) un mute audio based on status
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, mute ? AudioManager.ADJUST_MUTE : AudioManager.ADJUST_UNMUTE, 0);
            }
            else
            {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, mute);
            }
        }
        catch (Exception e)
        {
            if(audioManager == null) return;

            // un mute the audio if there is an exception
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
            }
            else
            {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            }
        }
    }

    // MARK: RecognitionListener Methods

    @Override
    public void onReadyForSpeech(Bundle bundle)
    {
        // If audio beep was muted, enabling it again
        muteAudio(false);

        onReadyForSpeech = true;
    }

    @Override
    public void onBeginningOfSpeech()
    {
        // NA
    }

    @Override
    public void onRmsChanged(float rmsdB)
    {
        if(droidSpeechListener != null)
        {
            // Sending an update with the rms changed value
            droidSpeechListener.onDroidSpeechRmsChanged(rmsdB);
        }
    }

    @Override
    public void onBufferReceived(byte[] bytes)
    {
        // NA
    }

    @Override
    public void onEndOfSpeech()
    {
        // NA
    }

    @Override
    public void onError(int error)
    {
        long duration = System.currentTimeMillis() - startListeningTime;

        // If duration is less than the "error timeout" as the system didn't try listening to the user voice so ignoring
        if(duration < ExtensionDroidSpeech.ERROR_TIMEOUT && error == SpeechRecognizer.ERROR_NO_MATCH && !onReadyForSpeech) return;

        if (onReadyForSpeech && duration < ExtensionDroidSpeech.AUDIO_BEEP_DISABLED_TIMEOUT)
        {
            // Disabling audio beep if less than "audio beep disabled timeout", as it will be
            // irritating for the user to hear the beep sound again and again
            muteAudio(true);
        }
        else
        {
            // If audio beep was muted, enabling it again
            muteAudio(false);
        }

        if(error == SpeechRecognizer.ERROR_NO_MATCH || error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT || error == SpeechRecognizer.ERROR_AUDIO)
        {
            // Restart droid speech recognition
            restartDroidSpeechRecognition();
        }
        else if(droidSpeechListener == null)
        {
           Log.e(TAG, "Droid speech error, code = " + error);
        }
        else
        {
            if(error <= context.getResources().getStringArray(R.array.droid_speech_errors).length)
            {
                // Sending an update with the droid speech error
                droidSpeechListener.onDroidSpeechError(context.getResources().getStringArray(R.array.droid_speech_errors)[error-1]);
            }
            else
            {
                // Sending an update that there was an unknown error
                droidSpeechListener.onDroidSpeechError(context.getResources().getString(R.string.unknown_error));
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onResults(Bundle results)
    {
        if(speechResultFound) return;

        speechResultFound = true;

        // If audio beep was muted, enabling it again
        muteAudio(false);

        Boolean valid = (results != null && results.containsKey(SpeechRecognizer.RESULTS_RECOGNITION) &&
                results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) != null &&
                results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).size() > 0 &&
                !results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0).trim().isEmpty());

        if(valid)
        {
            // Getting the droid speech final result
            String droidSpeechFinalResult = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);

            if(droidSpeechListener == null)
            {
                Log.i(TAG, "Droid speech final result = " + droidSpeechFinalResult);
            }
            else
            {
                // Sending an update with the droid speech final result
                droidSpeechListener.onDroidSpeechFinalResult(droidSpeechFinalResult, continuousVoiceRecognition);

                if(continuousVoiceRecognition)
                {
                    // Start droid speech recognition again
                    startDroidSpeechRecognition();
                }
            }
        }
        else
        {
            // No match found, restart droid speech recognition
            restartDroidSpeechRecognition();
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onPartialResults(Bundle partialResults)
    {
        if(speechResultFound) return;

        Boolean valid = (partialResults != null && partialResults.containsKey(SpeechRecognizer.RESULTS_RECOGNITION) &&
                partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) != null &&
                partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).size() > 0 &&
                !partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0).trim().isEmpty());

        if(valid)
        {
            final String droidLiveSpeechResult = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);

            if(droidSpeechListener == null)
            {
                Log.i(TAG, "Droid speech live result = " + droidLiveSpeechResult);
            }
            else
            {
                // Sending an update with the droid speech live result
                droidSpeechListener.onDroidSpeechLiveResult(droidLiveSpeechResult);
            }

            if((System.currentTimeMillis() - pauseAndSpeakTime) > ExtensionDroidSpeech.MAX_PAUSE_TIME)
            {
                speechResultFound = true;

                droidSpeechPartialResult.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        // Closing droid speech operations
                        closeDroidSpeechOperations();

                        if(droidSpeechListener == null)
                        {
                            Log.i(TAG, "Droid speech final result = " + droidLiveSpeechResult);
                        }
                        else
                        {
                            // Sending an update with the droid speech final result (Partial live result
                            // is taken as the final result in this case)
                            droidSpeechListener.onDroidSpeechFinalResult(droidLiveSpeechResult, continuousVoiceRecognition);

                            if(continuousVoiceRecognition)
                            {
                                // Start droid speech recognition again
                                startDroidSpeechRecognition();
                            }
                        }
                    }

                }, ExtensionDroidSpeech.PARTIAL_DELAY_TIME);
            }
            else
            {
                pauseAndSpeakTime = System.currentTimeMillis();
            }
        }
        else
        {
            pauseAndSpeakTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onEvent(int i, Bundle bundle)
    {
        // NA
    }
}
