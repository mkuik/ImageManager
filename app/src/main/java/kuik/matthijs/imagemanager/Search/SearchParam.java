package kuik.matthijs.imagemanager.Search;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import kuik.matthijs.imagemanager.DataTypes.FilterType;
import kuik.matthijs.imagemanager.R;
import kuik.matthijs.imagemanager.UserInput.Parts.ValueContainer;

/**
 * Created by Matthijs Kuik on 11/4/2016.
 */

public class SearchParam extends LinearLayout {

    private FilterType type;
    private ValueContainer valueView;
    private ImageButton closeView;
    private ImageButton iconView;

    public SearchParam(Context context, FilterType type) {
        super(context);
        this.type = type;
        init();
    }

    private void init() {
        inflate(getContext(), getLayoutResource(), this);
        valueView = (ValueContainer) findViewById(R.id.value);
        closeView = (ImageButton) findViewById(R.id.close);
        iconView = (ImageButton) findViewById(R.id.icon);
    }

    public int getLayoutResource() {
        switch (type) {
            case HUE:
                return R.layout.search_param_hue;
            case BW:
                return R.layout.search_param_saturation;
            case SIZE:
                return R.layout.search_param_size;
            default:
                return 0;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        setBackgroundResource(R.drawable.search_param_background);
        GradientDrawable drawable = (GradientDrawable) getBackground();
        if (drawable != null) {
            drawable.setCornerRadius(h / 2f);
            setBackground(drawable);
        }
        Log.d("size changed", "height:" + h);

        int iconWidth = iconView.getMeasuredWidth();
        if (w < iconWidth * 2) {
            closeView.setVisibility(GONE);
            valueView.setVisibility(GONE);
        } else {
            closeView.setVisibility(VISIBLE);
            valueView.setVisibility(VISIBLE);
        }
        invalidate();
        Log.d("size changed", "width:" + w + " icon:" + iconWidth);

    }


//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int width = getMeasuredWidth();
//        int height = getMeasuredHeight();
//        if (width < height) {
//            setMeasuredDimension(width, width);
//            onSizeChanged(width, height, 0, 0);
//        }
//    }
}
