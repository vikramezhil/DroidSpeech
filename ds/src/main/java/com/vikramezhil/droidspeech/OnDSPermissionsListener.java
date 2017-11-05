package com.vikramezhil.droidspeech;

/**
 * Droid Speech Permissions Listener
 *
 * @author Vikram Ezhil
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
