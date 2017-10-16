package com.vikramezhil.droidspeech;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

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
    private AlertDialog speechProgressAlertDialog;
    private RecognitionProgressView recognitionProgressView;
    private TextView recognitionProgressMsg;
    private DroidSpeechPermissions droidSpeechPermissions;
    private SpeechRecognizer droidSpeechRecognizer;
    private Intent speechIntent;
    private AudioManager audioManager;
    private Handler restartDroidSpeech = new Handler();
    private Handler droidSpeechPartialResult = new Handler();
    private long startListeningTime, pauseAndSpeakTime;
    private boolean continuousSpeechRecognition = true, showRecognitionProgressView = false, onReadyForSpeech = false, speechResultFound = false, closedByUser = false;
    private OnDSListener droidSpeechListener;

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

        // Initializing the recognition progress view
        initRecognitionProgressView();
    }

    // MARK: Droid Speech Listener

    /**
     * Sets the droid speech listener
     *
     * @param droidSpeechListener The class instance to initialize droid speech listener
     */
    public void setOnDroidSpeechListener(OnDSListener droidSpeechListener)
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
        speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, Extensions.MAX_VOICE_RESULTS);

        // Initializing the audio Manager
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * Initializes the recognition progress view
     */
    @SuppressWarnings("ConstantConditions")
    private void initRecognitionProgressView()
    {
        if(speechProgressAlertDialog == null)
        {
            try
            {
                // Initializing speech progress builder
                AlertDialog.Builder speechProgressBuilder = new AlertDialog.Builder(context);
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                @SuppressLint("InflateParams") View speechProgressView = layoutInflater.inflate(R.layout.speech_progress_layout, null);
                speechProgressBuilder.setView(speechProgressView);

                recognitionProgressView = speechProgressView.findViewById(R.id.recognitionProgressView);
                recognitionProgressView.setColors(Extensions.PV_COLORS);
                recognitionProgressView.setBarMaxHeightsInDp(Extensions.PV_BARS_HEIGHT);
                recognitionProgressView.setCircleRadiusInDp(Extensions.PV_CIRCLE_RADIUS);
                recognitionProgressView.setSpacingInDp(Extensions.PV_CIRCLE_SPACING);
                recognitionProgressView.setIdleStateAmplitudeInDp(Extensions.PV_IDLE_STATE);
                recognitionProgressView.setRotationRadiusInDp(Extensions.PV_ROTATION_RADIUS);
                recognitionProgressView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, Extensions.PV_HEIGHT));

                recognitionProgressMsg = speechProgressView.findViewById(R.id.recognitionProgressMsg);

                speechProgressAlertDialog = speechProgressBuilder.create();
                speechProgressAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                speechProgressAlertDialog.setCancelable(true);

                speechProgressAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                        closedByUser = true;

                        // Closing droid speech operations
                        closeDroidSpeechOperations();

                        if(droidSpeechListener != null)
                        {
                            // Sending an update that the droid speech was closed by user
                            droidSpeechListener.onDroidSpeechClosedByUser();
                        }
                        else
                        {
                            Log.i(TAG, "Droid speech closed");
                        }
                    }
                });
            }
            catch (Exception e)
            {
                if(droidSpeechListener == null)
                {
                    Log.e(TAG, context.getResources().getString(R.string.ds_progress_layout_error));
                }
                else
                {
                    // Sending an update that there was an error
                    droidSpeechListener.onDroidSpeechError(context.getResources().getString(R.string.ds_progress_layout_error));
                }
            }
        }
    }

    /**
     * Gets the continuous speech recognition status
     *
     * @return The continuous speech recognition status
     */
    public boolean getContinuousSpeechRecognition()
    {
        return continuousSpeechRecognition;
    }

    /**
     * Sets the continuous speech recognition status
     *
     * @param continuousSpeechRecognition The continuous speech recognition status
     */
    public void setContinuousSpeechRecognition(boolean continuousSpeechRecognition)
    {
        this.continuousSpeechRecognition = continuousSpeechRecognition;
    }

    /**
     * Sets the show recognition progress view status
     *
     * @param showRecognitionProgressView The show recognition progress view status
     */
    public void setShowRecognitionProgressView(boolean showRecognitionProgressView)
    {
        this.showRecognitionProgressView = showRecognitionProgressView;
    }

    /**
     * Sets the recognition progress view colors
     * Note: Should be of length 5
     *
     * @param colors The recognition progress view colors
     */
    public void setRecognitionProgressViewColors(int[] colors)
    {
        if(recognitionProgressView != null)
        {
            recognitionProgressView.setColors(colors);
        }
    }

    /**
     * Sets the recognition progress message
     *
     * @param msg The recognition progress message
     */
    private void setRecognitionProgressMsg(String msg)
    {
        if(recognitionProgressMsg != null && msg != null)
        {
            recognitionProgressMsg.setText(msg);
        }
    }

    /**
     * Sets the recognition progress message visibility
     *
     * NOTE: Default is true
     *
     * @param visible The recognition progress message visibility status
     */
    private void setRecognitionProgressMsgVisibility(boolean visible)
    {
        if(recognitionProgressMsg != null)
        {
            recognitionProgressMsg.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Sets the recognition progress message color
     *
     * @param color The recognition progress message color
     */
    public void setRecognitionProgressMsgColor(int color)
    {
        if(recognitionProgressMsg != null)
        {
            recognitionProgressMsg.setTextColor(color);
        }
    }

    /**
     * Starts the droid speech recognition
     *
     * Trigger Listeners - onDroidSpeechError(int errorType)
     */
    public void startDroidSpeechRecognition()
    {
        closedByUser = false;

        if(Extensions.isInternetEnabled(context))
        {
            if(droidSpeechPermissions == null || droidSpeechPermissions.checkForAudioPermissions(context))
            {
                playRecognitionProgressView(true);

                // Setting the progress message to listening
                setRecognitionProgressMsg(context.getResources().getString(R.string.ds_listening));

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
            playRecognitionProgressView(false);

            if(droidSpeechListener == null)
            {
                Log.e(TAG, context.getResources().getString(R.string.ds_internet_not_enabled));
            }
            else
            {
                // Sending an update that there was a network error
                droidSpeechListener.onDroidSpeechError(context.getResources().getString(R.string.ds_internet_not_enabled));
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
            public void run()
            {
                if(closedByUser)
                {
                    closedByUser = false;

                    // If audio beep was muted, enabling it again
                    muteAudio(false);
                }
                else
                {
                    startDroidSpeechRecognition();
                }
            }

        }, Extensions.MAX_PAUSE_TIME);
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
    private void closeDroidSpeech()
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
     * Closes the entire droid speech operations
     */
    public void closeDroidSpeechOperations()
    {
        playRecognitionProgressView(false);

        setRecognitionProgressMsg("");

        closeDroidSpeech();
    }

    /**
     * Plays or Stops the recognition progress view
     *
     * @param play The play recognition progress view status
     */
    private void playRecognitionProgressView(boolean play)
    {
        if(speechProgressAlertDialog == null || recognitionProgressView == null) return;

        if(showRecognitionProgressView)
        {
            if(play)
            {
                recognitionProgressView.play();
                speechProgressAlertDialog.show();
            }
            else
            {
                recognitionProgressView.stop();
                speechProgressAlertDialog.cancel();
            }
        }
        else if(speechProgressAlertDialog.isShowing())
        {
            recognitionProgressView.stop();
            speechProgressAlertDialog.cancel();
        }
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
        if(showRecognitionProgressView && speechProgressAlertDialog != null && recognitionProgressView != null)
        {
            recognitionProgressView.rmsValue(rmsdB);
        }

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
        if(closedByUser)
        {
            closedByUser = false;

            return;
        }

        long duration = System.currentTimeMillis() - startListeningTime;

        // If duration is less than the "error timeout" as the system didn't try listening to the user speech so ignoring
        if(duration < Extensions.ERROR_TIMEOUT && error == SpeechRecognizer.ERROR_NO_MATCH && !onReadyForSpeech) return;

        if (onReadyForSpeech && duration < Extensions.AUDIO_BEEP_DISABLED_TIMEOUT)
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
                droidSpeechListener.onDroidSpeechError(context.getResources().getString(R.string.ds_unknown_error));
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
                droidSpeechListener.onDroidSpeechFinalResult(droidSpeechFinalResult);

                if(continuousSpeechRecognition)
                {
                    // Start droid speech recognition again
                    startDroidSpeechRecognition();
                }
                else
                {
                    // Closing the droid speech operations
                    closeDroidSpeechOperations();
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
                // Setting the progress message
                setRecognitionProgressMsg(droidLiveSpeechResult);

                // Sending an update with the droid speech live result
                droidSpeechListener.onDroidSpeechLiveResult(droidLiveSpeechResult);
            }

            if((System.currentTimeMillis() - pauseAndSpeakTime) > Extensions.MAX_PAUSE_TIME)
            {
                speechResultFound = true;

                droidSpeechPartialResult.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        // Closing droid speech operations
                        closeDroidSpeech();

                        if(droidSpeechListener == null)
                        {
                            Log.i(TAG, "Droid speech final result = " + droidLiveSpeechResult);
                        }
                        else
                        {
                            // Sending an update with the droid speech final result (Partial live result
                            // is taken as the final result in this case)
                            droidSpeechListener.onDroidSpeechFinalResult(droidLiveSpeechResult);

                            if(continuousSpeechRecognition)
                            {
                                // Start droid speech recognition again
                                startDroidSpeechRecognition();
                            }
                            else
                            {
                                // Closing the droid speech operations
                                closeDroidSpeechOperations();
                            }
                        }
                    }

                }, Extensions.PARTIAL_DELAY_TIME);
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
