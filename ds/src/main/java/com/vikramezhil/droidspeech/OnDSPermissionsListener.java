package com.vikramezhil.droidspeech;

/**
 * Created by Vikram Ezhil on 29/07/17
 *
 * Email: vikram.ezhil.1988@gmail.com
 *
 * Droid Speech Permissions Listener
 */

public interface OnDSPermissionsListener
{
    /**
     * Sends an update with the droid speech audio permission status
     *
     * @param audioPermissionGiven The audio permission given status
     *
     * @param errorMsgIfAny Error message if any
     */
    void onDroidSpeechAudioPermissionStatus(boolean audioPermissionGiven, String errorMsgIfAny);
}
