package com.vikramezhil.droidspeech;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Droid Speech Extensions
 *
 * @author Vikram Ezhil
 */

class Extensions
{
    final static int[] PV_COLORS = new int[] {Color.BLUE, Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN};
    final static int[] PV_BARS_HEIGHT = new int[] {24, 28, 22, 27, 20};
    final static int PV_HEIGHT = 100;
    final static int PV_CIRCLE_RADIUS = 5;
    final static int PV_CIRCLE_SPACING = 2;
    final static int PV_IDLE_STATE = 2;
    final static int PV_ROTATION_RADIUS = 10;
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
