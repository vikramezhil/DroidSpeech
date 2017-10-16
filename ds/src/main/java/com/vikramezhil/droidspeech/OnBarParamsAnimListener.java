package com.vikramezhil.droidspeech;

/**
 * Created by Vikram Ezhil on 12/09/17
 *
 * Email: vikram.ezhil.1988@gmail.com
 *
 * Bar Animation Listener
 */

interface OnBarParamsAnimListener
{
    /**
     * Sends update to start animation
     */
    void start();

    /**
     * Sends update to stop animation
     */
    void stop();

    /**
     * Sends update to animate
     */
    void animate();
}