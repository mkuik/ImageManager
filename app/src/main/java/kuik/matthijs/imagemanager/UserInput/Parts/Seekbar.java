package kuik.matthijs.imagemanager.UserInput.Parts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import kuik.matthijs.imagemanager.R;

/**
 * TODO: document your custom view class.
 */
public class Seekbar extends ValueContainer implements View.OnTouchListener, View.OnLayoutChangeListener {

    private Cursor cursor;
    private Bar bar;
    private int padding = 25;
    private float value = 0;

    public Seekbar(Context context) {
        super(context);
    }

    public Seekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Seekbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void init(AttributeSet attrs, int defStyle) {
        super.init(attrs, defStyle);
        inflate(getContext(), R.layout.seekbar, this);

        bar = (Bar) findViewById(R.id.hue_bar);
        cursor = (Cursor) findViewById(R.id.hue_cursor);

        bar.setOnTouchListener(this);
        setOnTouchListener(this);

        setPadding(padding);
        addOnLayoutChangeListener(this);


        setValue(padding);
    }

    protected void setPadding(int padding) {
        this.padding = padding;
        if (bar.getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams p = (MarginLayoutParams) bar.getLayoutParams();
            p.setMargins(padding, 0, padding, 0);
            bar.requestLayout();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        if (x >= padding && x <= getWidth() - padding) {
            setValue(x);
            return true;
        } else {
            return false;
        }
    }

    protected Cursor getCursor() {
        return cursor;
    }

    protected Bar getBar() {
        return bar;
    }

    protected int getPadding() {
        return padding;
    }

    public void setValue(float value) {
        this.value = value;
        cursor.setX(value - cursor.getWidth() / 2);
        notifyValueChanged();
    }

    @Override
    public float getOtherValue() {
        return 0;
    }

    @Override
    public float getValue() {
        return value;
    }

    @Override
    public void setOtherValue(float otherValue) {

    }

    @Override
    public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
        setValue(getValue());
    }
}
