package com.vikramezhil.droidspeech;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

/**
 * Droid Speech Permissions - Non UI Fragment
 *
 * @author Vikram Ezhil
 */

public class DroidSpeechPermissions extends Fragment
{
    private static final int REQUEST_AUDIO_PERMISSIONS = 100;

    private OnDSPermissionsListener droidSpeechPermissionsListener;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        try
        {
            // Initializing the droid speech permission listener
            droidSpeechPermissionsListener = (OnDSPermissionsListener) getActivity();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Checks for audio permissions
     *
     * @param context The application instance context
     *
     * @return The audio permission status
     */
    public boolean checkForAudioPermissions(Context context)
    {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Requests for audio permissions
     */
    public void requestForAudioPermission()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            requestPermissions(new String[] {Manifest.permission.RECORD_AUDIO}, REQUEST_AUDIO_PERMISSIONS);
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case REQUEST_AUDIO_PERMISSIONS:

                for(int result : grantResults)
                {
                    if(result != PackageManager.PERMISSION_GRANTED)
                    {
                        // Audio permission not granted
                        droidSpeechPermissionsListener.onDroidSpeechAudioPermissionStatus(false, getResources().getString(R.string.ds_mic_permissions_required));

                        return;
                    }
                }

                // Audio permission granted
                droidSpeechPermissionsListener.onDroidSpeechAudioPermissionStatus(true, null);

                break;
        }
    }
}
