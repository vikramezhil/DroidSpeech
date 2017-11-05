package com.vikramezhil.droidspeech;

/**
 * Bar Animation Listener
 *
 * @author Vikram Ezhil
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