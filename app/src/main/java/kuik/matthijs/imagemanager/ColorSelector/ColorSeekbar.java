package kuik.matthijs.imagemanager.ColorSelector;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.SeekBar;

import kuik.matthijs.imagemanager.R;

/**
 * Created by Matthijs Kuik on 9/27/2016.
 */

public class ColorSeekbar extends SeekBar {

    public ColorSeekbar(Context context) {
        super(context);
        init();
    }

    public ColorSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }
}
