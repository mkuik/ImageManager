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
        getBar().setBackground(Bar.RAINBOW);
    }

    public float getHue() {
        return (getValue() - getPadding()) / (getWidth() - getPadding() * 2) * 360;
    }

    public void setHue(float hue) {

    }

    @Override
    protected void setValue(float value) {
        super.setValue(value);
        getCursor().setColor(Color.HSVToColor(new float[]{(int)getHue(), 255, 255}));
    }
}
