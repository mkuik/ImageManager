package kuik.matthijs.imagemanager.ColorSelector;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import kuik.matthijs.imagemanager.R;

/**
 * TODO: document your custom view class.
 */
public class Saturation extends Selector {

    public Saturation(Context context) {
        super(context);
        init(null, 0);
    }

    public Saturation(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public Saturation(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.hue_selector, this);
        getBar().setBackground(Bar.BW);
    }

    public float getSaturation() {
        return (getValue() - getPadding()) / (getWidth() - getPadding() * 2) * 255;
    }

    @Override
    public void setValue(float value) {
        super.setValue(value);
        final int saturation = 255 - (int) getSaturation();
        getCursor().setColor(Color.rgb(saturation, saturation, saturation));
    }
}
