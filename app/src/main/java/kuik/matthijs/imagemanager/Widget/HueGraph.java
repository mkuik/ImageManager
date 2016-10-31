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
import kuik.matthijs.imagemanager.DataTypes.HueHistogramData;

/**
 * Created by Matthijs on 15/10/2016.
 */

public class HueGraph extends View {

    private HueHistogramData data;
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

    public void setData(HueHistogramData data) {
        this.data = data;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (data != null && !data.isEmpty()) {
            final int width = getWidth();
            final int height = getHeight();
            final float xRatio = width / 360F;
            final float yRatio = height / data.getMax().getCount();
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
