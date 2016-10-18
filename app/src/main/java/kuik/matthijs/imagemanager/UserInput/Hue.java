package kuik.matthijs.imagemanager.UserInput;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import kuik.matthijs.imagemanager.R;
import kuik.matthijs.imagemanager.UserInput.Parts.Bar;
import kuik.matthijs.imagemanager.UserInput.Parts.Seekbar;

/**
 * TODO: document your custom view class.
 */
public class Hue extends Seekbar {

    public Hue(Context context) {
        super(context);
    }

    public Hue(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Hue(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void init(AttributeSet attrs, int defStyle) {
        super.init(attrs, defStyle);
        getBar().setBackground(Bar.RAINBOW);
    }

    public float getValue() {
        return (super.getValue() - getPadding()) / (getWidth() - getPadding() * 2) * 360;
    }

    @Override
    public void setValue(float value) {
        super.setValue(value);
        getCursor().setColor(Color.HSVToColor(new float[]{(int)getValue(), 255, 255}));
    }

    @Override
    public String toString() {
        return "Hue{" + getValue() + "}";
    }
}
