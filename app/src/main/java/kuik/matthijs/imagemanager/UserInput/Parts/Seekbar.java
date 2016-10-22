package kuik.matthijs.imagemanager.UserInput.Parts;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;

import kuik.matthijs.imagemanager.R;

/**
 * TODO: document your custom view class.
 */
public class Seekbar extends ValueContainer implements View.OnTouchListener, View.OnLayoutChangeListener {

    private Cursor cursor;
    private Bar bar;
    private int padding = 23;
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
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        setValue(XToValue(motionEvent.getX()));
        return true;
    }

    protected float XToValue(float x) {
        final int barWidth = getBarWidth();
        return (x / barWidth) - (padding / barWidth);
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

    @Override
    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        Log.d("Seekbar", "v:"+value);
        if (value >= 0 && value <= 1) {
            this.value = value;
        } else if (value < 0) {
            this.value = 0;
        } else {
            this.value = 1;
        }
        cursor.setX(padding + this.value * getBarWidth() - cursor.getWidth() / 2);
        notifyValueChanged();
    }

    public int getBarWidth() {
        return getWidth() - padding * 2;
    }

    @Override
    public float getOtherValue() {
        return 0;
    }

    @Override
    public void setOtherValue(float otherValue) {

    }

    @Override
    public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
        setValue(getValue());
    }
}
