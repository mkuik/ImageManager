package kuik.matthijs.imagemanager.ColorSelector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import kuik.matthijs.imagemanager.R;

/**
 * TODO: document your custom view class.
 */
public class Hue extends Selector {

    public Hue(Context context) {
        super(context);
        init(null, 0);
    }

    public Hue(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public Hue(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.hue_selector, this);
        getBar().setBackground(Bar.RAINBOW);
    }

    public float getHue() {
        return (getValue() - getPadding()) / (getWidth() - getPadding() * 2) * 360;
    }

    @Override
    public void setValue(float value) {
        super.setValue(value);
        getCursor().setColor(Color.HSVToColor(new float[]{(int)getHue(), 255, 255}));
    }
}
