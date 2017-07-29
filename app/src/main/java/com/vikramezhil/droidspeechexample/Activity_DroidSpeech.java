package com.vikramezhil.droidspeechexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vikramezhil.droidspeech.DroidSpeech;
import com.vikramezhil.droidspeech._DroidSpeechListener;
import com.vikramezhil.droidspeech._DroidSpeechPermissionsListener;

public class Activity_DroidSpeech extends AppCompatActivity implements OnClickListener, _DroidSpeechListener, _DroidSpeechPermissionsListener
{
    private DroidSpeech droidSpeech;

    private TextView finalSpeechResult, liveInfo;

    private Button start, stop;

    // MARK: Activity methods

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Setting the layout
        setContentView(R.layout.activity_droid_speech);

        // Initializing the droid speech and setting the listener
        droidSpeech = new DroidSpeech(this, getFragmentManager());
        droidSpeech.setOnDroidSpeechListener(this);

        finalSpeechResult = (TextView) findViewById(R.id.finalSpeechResult);
        liveInfo = (TextView) findViewById(R.id.liveInfo);

        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(this);

        stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(this);
    }

    @Override
    protected void onPause()
    {
        if(stop.getVisibility() == View.VISIBLE)
        {
            stop.performClick();
        }

        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        if(stop.getVisibility() == View.VISIBLE)
        {
            stop.performClick();
        }

        super.onDestroy();
    }

    // MARK: OnClickListener method

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.start:

                // Starting droid speech
                droidSpeech.startDroidSpeechRecognition();

                // Setting the view visibilities when droid speech is running
                liveInfo.setVisibility(View.VISIBLE);
                start.setVisibility(View.GONE);
                stop.setVisibility(View.VISIBLE);

                break;

            case R.id.stop:

                // Closing droid speech
                droidSpeech.closeDroidSpeechOperations();

                // Setting the views back to default
                liveInfo.setText(getResources().getString(R.string.listening));

                liveInfo.setVisibility(View.GONE);
                stop.setVisibility(View.GONE);
                start.setVisibility(View.VISIBLE);

                break;
        }
    }

    // MARK: DroidSpeechListener methods

    @Override
    public void onDroidSpeechRmsChanged(float rmsChangedValue)
    {
        // Log.wtf("Rms changed value = ", "" + rmsChangedValue);
    }

    @Override
    public void onDroidSpeechLiveResult(String liveSpeechResult)
    {
        // Setting the live speech result
        liveInfo.setText(liveInfo.getText().toString().contains(getResources().getString(R.string.listening)) ? liveSpeechResult : liveInfo.getText() + " , " + liveSpeechResult);
    }

    @Override
    public void onDroidSpeechFinalResult(String finalSpeechResult, boolean droidSpeechWillListen)
    {
        // Setting the final speech result
        this.finalSpeechResult.setText(finalSpeechResult);

        if(droidSpeechWillListen)
        {
            // Setting the live info back to listening
            liveInfo.setText(getResources().getString(R.string.listening));
        }
        else
        {
            stop.post(new Runnable()
            {
                @Override
                public void run()
                {
                    // Stop listening
                    stop.performClick();
                }
            });
        }
    }

    @Override
    public void onDroidSpeechError(String errorMsg)
    {
        // Speech error
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();

        stop.post(new Runnable()
        {
            @Override
            public void run()
            {
                // Stop listening
                stop.performClick();
            }
        });
    }

    // MARK: DroidSpeechPermissionsListener method

    @Override
    public void onDroidSpeechAudioPermissionStatus(boolean audioPermissionGiven, String errorMsgIfAny)
    {
        if(audioPermissionGiven)
        {
            start.post(new Runnable()
            {
                @Override
                public void run()
                {
                    // Start listening
                    start.performClick();
                }
            });
        }
        else
        {
            if(errorMsgIfAny != null)
            {
                // Permissions error
                Toast.makeText(this, errorMsgIfAny, Toast.LENGTH_LONG).show();
            }

            stop.post(new Runnable()
            {
                @Override
                public void run()
                {
                    // Stop listening
                    stop.performClick();
                }
            });
        }
    }
}
