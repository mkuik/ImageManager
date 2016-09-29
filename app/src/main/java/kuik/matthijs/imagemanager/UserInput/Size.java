package kuik.matthijs.imagemanager.UserInput;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import kuik.matthijs.imagemanager.R;

/**
 * Created by Matthijs on 28/09/2016.
 */

public class Size extends LinearLayout {

    private EditText width;
    private EditText height;

    public Size(Context context) {
        super(context);
        init(null, 0);
    }

    public Size(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public Size(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.photo_size_form, this);
        width = (EditText) findViewById(R.id.dimension_form_width);
        height = (EditText) findViewById(R.id.dimension_form_height);
    }

    public int getWidthInput() {
        return Integer.parseInt(width.getText().toString());
    }

    public int getHeightInput() {
        return Integer.parseInt(height.getText().toString());
    }

    public void setWidthInput(int width) {
        this.width.setText(width);
    }

    public void setHeightInput(int height) {
        this.height.setText(height);
    }
}
