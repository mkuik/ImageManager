package kuik.matthijs.imagemanager.Widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

import kuik.matthijs.imagemanager.DataTypes.Hue;

/**
 * Created by Matthijs on 15/10/2016.
 */

public class HueGraph extends View {

    private List<Hue> data;
    private float max;
    private Paint paint = new Paint();
    private float[] hsv = {0, 120, 120};

    public HueGraph(Context context) {
        super(context);
        init();
    }

    public HueGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HueGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.FILL);
    }

    public void setData(List<Hue> data) {
        this.data = data;
        max = 0;
        if (data != null) {
            for (Hue hue : data) {
                if (hue.getCount() > max) {
                    max = hue.getCount();
                }
            }
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (data != null) {
            final int width = getWidth();
            final int height = getHeight();
            final float xRatio = width / 360F;
            final float yRatio = height / (float)max;
            for (Hue hue : data) {
                final float x = hue.getHue() * xRatio;
                final float y = hue.getCount() * yRatio;
                hsv[0] = hue.getHue();
                paint.setColor(Color.HSVToColor(hsv));
                canvas.drawLine(x, height, x, height - y, paint);
            }
        }
    }
}
