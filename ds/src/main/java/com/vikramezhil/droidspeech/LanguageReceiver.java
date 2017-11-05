package com.vikramezhil.droidspeech;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import java.util.ArrayList;
import java.util.List;

/**
 * Droid Speech Language Receiver
 *
 * @author Vikram Ezhil
 */

class LanguageReceiver extends BroadcastReceiver
{
    private OnLanguageDetailsListener onLanguageDetailsListener;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        List<String> supportedLanguages = new ArrayList<>();
        String defaultLanguagePreference = null;

        if(getResultCode() == Activity.RESULT_OK)
        {
            Bundle results = getResultExtras(true);
            if(results != null)
            {
                if(results.containsKey(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE))
                {
                    defaultLanguagePreference = results.getString(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE);
                }

                if(results.containsKey(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES))
                {
                    if(results.getStringArrayList(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES) != null)
                    {
                        supportedLanguages = results.getStringArrayList(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES);
                    }
                }
            }
        }

        if(onLanguageDetailsListener != null)
        {
            // Sending an update with the language details information
            onLanguageDetailsListener.onLanguageDetailsInfo(defaultLanguagePreference, supportedLanguages);
        }
    }

    /**
     * Sets the language details listener
     *
     * @param onLanguageDetailsListener The language details listener
     */
    void setOnLanguageDetailsListener(OnLanguageDetailsListener onLanguageDetailsListener)
    {
        this.onLanguageDetailsListener = onLanguageDetailsListener;
    }
}
