package kuik.matthijs.imagemanager.Search;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import kuik.matthijs.imagemanager.DataTypes.FilterType;
import kuik.matthijs.imagemanager.R;
import kuik.matthijs.imagemanager.UserInput.Hue;
import kuik.matthijs.imagemanager.UserInput.Parts.ValueContainer;
import kuik.matthijs.imagemanager.UserInput.Saturation;
import kuik.matthijs.imagemanager.UserInput.Size;

/**
 * Created by Matthijs on 01/11/2016.
 */

public class SearchParamView extends View {

    FrameLayout container;
    ImageButton closeButton;
    ValueContainer valueContainer = null;

    public SearchParamView(Context context) {
        super(context);
        init();
    }

    public SearchParamView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchParamView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.filter_row, null);
        container = (FrameLayout) findViewById(R.id.filter);
        closeButton = (ImageButton) findViewById(R.id.button_clear);
    }

    public void setView(ValueContainer view) {
        valueContainer = view;
        container.removeAllViewsInLayout();
        container.addView(valueContainer);
    }

    public void setType(FilterType type) {
        switch (type) {
            case HUE:
                setView(new Hue(getContext()));
                break;
            case BW:
                setView(new Saturation(getContext()));
                break;
            case SIZE:
                setView(new Size(getContext()));
                break;
        }
    }

    public ImageButton getCloseButton() {
        return closeButton;
    }

    public FrameLayout getContainer() {
        return container;
    }

    public ValueContainer getValueContainer() {
        return valueContainer;
    }
}
