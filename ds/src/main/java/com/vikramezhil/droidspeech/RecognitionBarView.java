package com.vikramezhil.droidspeech;

import android.graphics.RectF;

/**
 * Recognition Bar View
 *
 * @author Vikram Ezhil
 */

class RecognitionBarView
{
    private int x;
    private int y;
    private int radius;
    private int height;

    private final int maxHeight;
    private final int startX;
    private final int startY;
    final private RectF rect;

    RecognitionBarView(int x, int y, int height, int maxHeight, int radius)
    {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.startX = x;
        this.startY = y;
        this.height = height;
        this.maxHeight = maxHeight;
        this.rect = new RectF(x - radius,
                y - height / 2,
                x + radius,
                y + height / 2);
    }

    void update()
    {
        rect.set(x - radius,
                y - height / 2,
                x + radius,
                y + height / 2);
    }

    int getX()
    {
        return x;
    }

    void setX(int x)
    {
        this.x = x;
    }

    int getY()
    {
        return y;
    }

    void setY(int y)
    {
        this.y = y;
    }

    int getHeight()
    {
        return height;
    }

    void setHeight(int height)
    {
        this.height = height;
    }

    int getMaxHeight()
    {
        return maxHeight;
    }

    int getStartX()
    {
        return startX;
    }

    int getStartY()
    {
        return startY;
    }

    RectF getRect()
    {
        return rect;
    }

    int getRadius()
    {
        return radius;
    }
}