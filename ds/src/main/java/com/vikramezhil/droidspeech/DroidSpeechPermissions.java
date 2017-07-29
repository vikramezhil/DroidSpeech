package com.vikramezhil.droidspeech;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Vikram Ezhil on 29/07/17
 *
 * Email: vikram.ezhil.1988@gmail.com
 *
 * Droid Speech Permissions - Non UI Fragment
 */

public class DroidSpeechPermissions extends Fragment
{
    private static final int REQUEST_AUDIO_PERMISSIONS = 100;

    private _DroidSpeechPermissionsListener droidSpeechPermissionsListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        try
        {
            // Initializing the droid speech permission listener
            droidSpeechPermissionsListener = (_DroidSpeechPermissionsListener) getActivity();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        super.onActivityCreated(savedInstanceState);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_AUDIO_PERMISSIONS:

                for(int result : grantResults)
                {
                    if(result != PackageManager.PERMISSION_GRANTED)
                    {
                        // Audio permission not granted
                        droidSpeechPermissionsListener.onDroidSpeechAudioPermissionStatus(false, getResources().getString(R.string.microphone_permissions_required));

                        return;
                    }
                }

                // Audio permission granted
                droidSpeechPermissionsListener.onDroidSpeechAudioPermissionStatus(true, null);

                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
