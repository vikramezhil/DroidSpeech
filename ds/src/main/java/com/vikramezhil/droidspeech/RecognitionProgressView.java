package com.vikramezhil.droidspeech;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Recognition Progress View
 *
 * @author Vikram Ezhil
 */

class RecognitionProgressView extends View
{
    public static final int BARS_COUNT = 5;

    private static final int CIRCLE_RADIUS_DP = 5;
    private static final int CIRCLE_SPACING_DP = 11;
    private static final int ROTATION_RADIUS_DP = 25;
    private static final int IDLE_FLOATING_AMPLITUDE_DP = 3;

    private static final int[] DEFAULT_BARS_HEIGHT_DP = { 60, 46, 70, 54, 64 };

    private static final float MDPI_DENSITY = 1.5f;

    private final List<RecognitionBarView> recognitionBars = new ArrayList<>();
    private Paint paint;
    private OnBarParamsAnimListener animator;

    private int radius;
    private int spacing;
    private int rotationRadius;
    private int amplitude;

    private float density;

    private boolean animating;

    private int barColor = -1;
    private int[] barColors;
    private int[] barMaxHeights;

    // MARK: SpeechProgressView Constructors

    public RecognitionProgressView(Context context)
    {
        super(context);

        init();
    }

    public RecognitionProgressView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init();
    }

    public RecognitionProgressView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        init();
    }

    // MARK: View Methods

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);

        if(recognitionBars.isEmpty())
        {
            initBars();
        }
        else if(changed)
        {
            recognitionBars.clear();

            initBars();
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if(recognitionBars.isEmpty())
        {
            return;
        }

        if(animating)
        {
            animator.animate();
        }

        for(int i = 0; i < recognitionBars.size(); i++)
        {
            RecognitionBarView bar = recognitionBars.get(i);

            if(barColors != null)
            {
                paint.setColor(barColors[i]);
            }
            else if (barColor != -1)
            {
                paint.setColor(barColor);
            }

            canvas.drawRoundRect(bar.getRect(), radius, radius, paint);
        }

        if(animating)
        {
            invalidate();
        }
    }

    /**
     * Starts animating view
     */
    public void play() {
        startIdleInterpolation();
        animating = true;
    }

    /**
     * Stops animating view
     */
    public void stop() {
        if (animator != null) {
            animator.stop();
            animator = null;
        }
        animating = false;
        resetBars();
    }

    /**
     * Set one color to all bars in view
     */
    public void setSingleColor(int color) {
        barColor = color;
    }

    /**
     * Set different colors to bars in view
     *
     * @param colors - array with size = {@link #BARS_COUNT}
     */
    public void setColors(int[] colors) {
        if (colors == null) return;

        barColors = new int[BARS_COUNT];
        if (colors.length < BARS_COUNT) {
            System.arraycopy(colors, 0, barColors, 0, colors.length);
            for (int i = colors.length; i < BARS_COUNT; i++) {
                barColors[i] = colors[0];
            }
        } else {
            System.arraycopy(colors, 0, barColors, 0, BARS_COUNT);
        }
    }

    /**
     * Set sizes of bars in view
     *
     * @param heights - array with size = {@link #BARS_COUNT},
     * if not set uses default bars heights
     */
    public void setBarMaxHeightsInDp(int[] heights) {
        if (heights == null) return;

        barMaxHeights = new int[BARS_COUNT];
        if (heights.length < BARS_COUNT) {
            System.arraycopy(heights, 0, barMaxHeights, 0, heights.length);
            for (int i = heights.length; i < BARS_COUNT; i++) {
                barMaxHeights[i] = heights[0];
            }
        } else {
            System.arraycopy(heights, 0, barMaxHeights, 0, BARS_COUNT);
        }
    }

    /**
     * Set radius of circle
     *
     * @param radius - Default value = {@link #CIRCLE_RADIUS_DP}
     */
    public void setCircleRadiusInDp(int radius) {
        this.radius = (int) (radius * density);
    }

    /**
     * Set spacing between circles
     *
     * @param spacing - Default value = {@link #CIRCLE_SPACING_DP}
     */
    public void setSpacingInDp(int spacing) {
        this.spacing = (int) (spacing * density);
    }

    /**
     * Set idle animation amplitude
     *
     * @param amplitude - Default value = {@link #IDLE_FLOATING_AMPLITUDE_DP}
     */
    public void setIdleStateAmplitudeInDp(int amplitude) {
        this.amplitude = (int) (amplitude * density);
    }

    /**
     * Set rotation animation radius
     *
     * @param radius - Default value = {@link #ROTATION_RADIUS_DP}
     */
    public void setRotationRadiusInDp(int radius) {
        this.rotationRadius = (int) (radius * density);
    }

    private void init()
    {
        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);

        density = getResources().getDisplayMetrics().density;

        radius = (int) (CIRCLE_RADIUS_DP * density);
        spacing = (int) (CIRCLE_SPACING_DP * density);
        rotationRadius = (int) (ROTATION_RADIUS_DP * density);
        amplitude = (int) (IDLE_FLOATING_AMPLITUDE_DP * density);

        if (density <= MDPI_DENSITY) {
            amplitude *= 2;
        }
    }

    private void initBars()
    {
        final List<Integer> heights = initBarHeights();

        int firstCirclePosition = getMeasuredWidth() / 2 -
                2 * spacing -
                4 * radius;

        for (int i = 0; i < BARS_COUNT; i++)
        {
            int x = firstCirclePosition + (2 * radius + spacing) * i;

            RecognitionBarView bar = new RecognitionBarView(x, getMeasuredHeight() / 2, 2 * radius, heights.get(i), radius);

            recognitionBars.add(bar);
        }
    }

    private List<Integer> initBarHeights()
    {
        final List<Integer> barHeights = new ArrayList<>();

        if(barMaxHeights == null)
        {
            for(int i = 0; i < BARS_COUNT; i++)
            {
                barHeights.add((int) (DEFAULT_BARS_HEIGHT_DP[i] * density));
            }
        }
        else
        {
            for(int i = 0; i < BARS_COUNT; i++)
            {
                barHeights.add((int) (barMaxHeights[i] * density));
            }
        }

        return barHeights;
    }

    private void resetBars()
    {
        for (RecognitionBarView bar : recognitionBars)
        {
            bar.setX(bar.getStartX());
            bar.setY(bar.getStartY());
            bar.setHeight(radius * 2);
            bar.update();
        }
    }

    private void startIdleInterpolation()
    {
        animator = new AnimatorIdle(recognitionBars, amplitude);
        animator.start();
    }

    private void startRmsInterpolation()
    {
        resetBars();

        animator = new AnimatorRms(recognitionBars);
        animator.start();
    }

    private void startTransformInterpolation()
    {
        resetBars();

        animator = new AnimatorTransform(recognitionBars, getWidth() / 2, getHeight() / 2, rotationRadius);

        animator.start();

        ((AnimatorTransform) animator).setOnInterpolationFinishedListener(new AnimatorTransform.OnInterpolationFinishedListener()
        {
            @Override
            public void onFinished() {
                startRotateInterpolation();
            }
        });
    }

    private void startRotateInterpolation()
    {
        animator = new AnimatorRotating(recognitionBars, getWidth() / 2, getHeight() / 2);

        animator.start();
    }

    /**
     * Updates the view with the rmsDB Value
     *
     * @param rmsdB The rmsdb value
     */
    public void rmsValue(float rmsdB)
    {
        if (animator == null || rmsdB < 1f)
        {
            return;
        }

        if (!(animator instanceof AnimatorRms))
        {
            startRmsInterpolation();
        }

        if (animator instanceof AnimatorRms)
        {
            ((AnimatorRms) animator).onRmsChanged(rmsdB);
        }
    }
}
