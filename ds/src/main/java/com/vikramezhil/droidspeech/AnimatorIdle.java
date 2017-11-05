package com.vikramezhil.droidspeech;

import java.util.List;

/**
 * AnimatorIdle
 *
 * @author Vikram Ezhil
 */

class AnimatorIdle implements OnBarParamsAnimListener
{
    private static final long IDLE_DURATION = 1500;

    private long startTimestamp;
    private boolean isPlaying;

    private final int floatingAmplitude;
    private final List<RecognitionBarView> bars;

    AnimatorIdle(List<RecognitionBarView> bars, int floatingAmplitude)
    {
        this.floatingAmplitude = floatingAmplitude;
        this.bars = bars;
    }

    @Override
    public void start()
    {
        isPlaying = true;
        startTimestamp = System.currentTimeMillis();
    }

    @Override
    public void stop()
    {
        isPlaying = false;
    }

    @Override
    public void animate()
    {
        if (isPlaying)
        {
            update(bars);
        }
    }

    void update(List<RecognitionBarView> bars)
    {

        long currTimestamp = System.currentTimeMillis();
        if (currTimestamp - startTimestamp > IDLE_DURATION)
        {
            startTimestamp += IDLE_DURATION;
        }

        long delta = currTimestamp - startTimestamp;
        int i = 0;
        for (RecognitionBarView bar : bars)
        {
            updateCirclePosition(bar, delta, i);
            i++;
        }
    }

    private void updateCirclePosition(RecognitionBarView bar, long delta, int num)
    {
        float angle = ((float) delta / IDLE_DURATION) * 360f + 120f * num;
        int y = (int) (Math.sin(Math.toRadians(angle)) * floatingAmplitude) + bar.getStartY();
        bar.setY(y);
        bar.update();
    }
}