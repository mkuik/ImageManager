package kuik.matthijs.imagemanager.ColorSelector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * TODO: document your custom view class.
 */
public class Cursor extends View {

    int color = Color.RED;
    Paint paint;

    public Cursor(Context context) {
        super(context);
        init(null, 0);
    }

    public Cursor(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public Cursor(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(255);
        canvas.drawCircle(contentWidth / 2, contentHeight / 2,
                Math.min(contentWidth / 2, contentHeight / 2) - 2, paint);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAlpha(255);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        canvas.drawCircle(contentWidth / 2, contentHeight / 2,
                Math.min(contentWidth / 2, contentHeight / 2) - 4, paint);
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }
}
