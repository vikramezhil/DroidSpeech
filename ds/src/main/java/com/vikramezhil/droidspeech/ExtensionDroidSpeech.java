package com.vikramezhil.droidspeech;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Vikram Ezhil on 29/07/17
 *
 * Email: vikram.ezhil.1988@gmail.com
 *
 * Droid Speech Extension
 */

class ExtensionDroidSpeech
{
    final static int MAX_VOICE_RESULTS = 5;
    final static int MAX_PAUSE_TIME = 500;
    final static int PARTIAL_DELAY_TIME = 500;
    final static int ERROR_TIMEOUT = 5000;
    final static int AUDIO_BEEP_DISABLED_TIMEOUT = 30000;

    /**
     * Checks if the internet is enabled
     *
     * @param context The application context instance
     *
     * @return The internet enabled status
     */
    static boolean isInternetEnabled(Context context)
    {
        // Initializing the connectivity Manager
        ConnectivityManager activeConnection = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Getting the network information
        NetworkInfo networkInfo = activeConnection.getActiveNetworkInfo();

        return networkInfo != null && (networkInfo.getType() == ConnectivityManager.TYPE_WIFI || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }
}
