package kuik.matthijs.imagemanager.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import kuik.matthijs.imagemanager.R;

/**
 * Created by Matthijs on 28/09/2016.
 */

public class FilterRow extends LinearLayout {

    Filter filter;
    ImageButton clear;

    public FilterRow(Context context) {
        super(context);
        init(null, 0);
    }

    public FilterRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FilterRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        inflate(getContext(), R.layout.filter_row, this);
        filter = (Filter) findViewById(R.id.filter);
        clear = (ImageButton) findViewById(R.id.button_clear);
    }
}
