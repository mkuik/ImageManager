package kuik.matthijs.imagemanager.ColorSelector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class Bar extends View {

    static final GradientDrawable RAINBOW = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT,
            new int[] {Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED});
    static final GradientDrawable BW = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT,
            new int[] {Color.BLACK, Color.WHITE});

    public Bar(Context context) {
        super(context);
        init(null, 0);
    }

    public Bar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public Bar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

    }

}
