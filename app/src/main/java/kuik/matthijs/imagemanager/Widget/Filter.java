package kuik.matthijs.imagemanager.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import kuik.matthijs.imagemanager.DataTypes.FilterType;
import kuik.matthijs.imagemanager.UserInput.Hue;
import kuik.matthijs.imagemanager.UserInput.Saturation;
import kuik.matthijs.imagemanager.UserInput.Size;

/**
 * Created by Matthijs on 28/09/2016.
 */

public class Filter extends FrameLayout {

    FilterType property;
    View view;

    public Filter(Context context) {
        super(context);
        init(null, 0);
    }

    public Filter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public Filter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {

    }

    public void setFilterType(FilterType property) {
        this.property = property;
        switch (property) {
            case HUE:
                setupColorView();
                break;
            case BW:
                setupSaturationView();
                break;
            case SIZE:
                setupSizeView();
                break;
        }
    }

    public void setValues(float A, float B) {
        if (view instanceof Hue) {
            Hue hue = (Hue) view;
            hue.setHue(A);
        } else if (view instanceof Saturation) {
            Saturation saturation = (Saturation) view;
            saturation.setSaturation(A);
        } else if (view instanceof Size) {
            Size size = (Size) view;
            size.setWidthInput((int) A);
            size.setHeightInput((int) B);
        }
    }

    private void setupSizeView() {
        removeAllViewsInLayout();
        view = new Size(getContext());
        addView(view);
    }

    private void setupColorView() {
        removeAllViewsInLayout();
        view = new Hue(getContext());
        addView(view);
    }

    private void setupSaturationView() {
        removeAllViewsInLayout();
        view = new Saturation(getContext());
        addView(view);
    }
}
