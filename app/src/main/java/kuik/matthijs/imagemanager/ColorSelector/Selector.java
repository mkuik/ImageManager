package kuik.matthijs.imagemanager.ColorSelector;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import kuik.matthijs.imagemanager.R;

/**
 * TODO: document your custom view class.
 */
public class Selector extends LinearLayout implements View.OnTouchListener, View.OnLayoutChangeListener {

    private Cursor cursor;
    private Bar bar;
    private int padding = 25;
    private float value = padding;

    public Selector(Context context) {
        super(context);
        init(null, 0);
    }

    public Selector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public Selector(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.hue_selector, this);

        bar = (Bar) findViewById(R.id.hue_bar);
        cursor = (Cursor) findViewById(R.id.hue_cursor);

        bar.setOnTouchListener(this);
        setOnTouchListener(this);

        setPadding(padding);
        addOnLayoutChangeListener(this);
    }

    public void setPadding(int padding) {
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

    public Cursor getCursor() {
        return cursor;
    }

    public Bar getBar() {
        return bar;
    }

    public float getValue() {
        return value;
    }

    public int getPadding() {
        return padding;
    }

    public void setValue(float value) {
        this.value = value;
        cursor.setX(value - cursor.getWidth() / 2);
    }

    @Override
    public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
        setValue(value);
    }
}
