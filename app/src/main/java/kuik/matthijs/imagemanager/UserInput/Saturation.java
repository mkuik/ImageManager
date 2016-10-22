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
public class Saturation extends Seekbar {

    public Saturation(Context context) {
        super(context);
    }

    public Saturation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Saturation(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void init(AttributeSet attrs, int defStyle) {
        super.init(attrs, defStyle);
        getBar().setBackground(Bar.BW);
    }

    @Override
    public void setValue(float value) {
        super.setValue(value);
        final int saturation = (int) ((1 - getValue()) * 255);
        getCursor().setColor(Color.rgb(saturation, saturation, saturation));
    }

    @Override
    public String toString() {
        return "Saturation{" + getValue() + "}";
    }
}
