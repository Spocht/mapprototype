package dev.spocht.spocht.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by highway on 31.08.2015.
 * Source: https://www.youtube.com/watch?v=xbl5cxfA1n4
 *
 */
public class FractionalLinearLayout extends LinearLayout {

    private float yFraction;
    private  int screenHeight;

    public FractionalLinearLayout(Context context) {
        super(context);
    }

    public FractionalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public float getYFraction() {
        return yFraction;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        screenHeight = h;
        setY(screenHeight);
    }

    public void setYFraction(float yFraction) {
        this.yFraction = yFraction;

        setY((screenHeight > 0) ? (screenHeight - yFraction * screenHeight) : 0);
    }
}
