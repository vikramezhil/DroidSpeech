package com.vikramezhil.droidspeech;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vikram Ezhil on 12/09/17
 *
 * Email: vikram.ezhil.1988@gmail.com
 *
 * AnimatorRms
 */

class AnimatorRms implements OnBarParamsAnimListener
{
    final private List<AnimatorBarRms> barAnimators;

    AnimatorRms(List<RecognitionBarView> recognitionBars)
    {
        this.barAnimators = new ArrayList<>();

        for(RecognitionBarView bar : recognitionBars)
        {
            barAnimators.add(new AnimatorBarRms(bar));
        }
    }

    @Override
    public void start()
    {
        for(AnimatorBarRms barAnimator : barAnimators)
        {
            barAnimator.start();
        }
    }

    @Override
    public void stop()
    {
        for(AnimatorBarRms barAnimator : barAnimators)
        {
            barAnimator.stop();
        }
    }

    @Override
    public void animate()
    {
        for(AnimatorBarRms barAnimator : barAnimators)
        {
            barAnimator.animate();
        }
    }

    public void onRmsChanged(float rmsDB)
    {
        for (AnimatorBarRms barAnimator : barAnimators)
        {
            barAnimator.onRmsChanged(rmsDB);
        }
    }
}