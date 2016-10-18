package kuik.matthijs.imagemanager.UserInput;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import kuik.matthijs.imagemanager.R;
import kuik.matthijs.imagemanager.UserInput.Parts.ValueContainer;

/**
 * Created by Matthijs on 28/09/2016.
 */

public class Size extends ValueContainer implements TextWatcher {

    private EditText width;
    private EditText height;

    public Size(Context context) {
        super(context);
    }

    public Size(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Size(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void init(AttributeSet attrs, int defStyle) {
        super.init(attrs, defStyle);
        inflate(getContext(), R.layout.photo_size_form, this);
        width = (EditText) findViewById(R.id.dimension_form_width);
        height = (EditText) findViewById(R.id.dimension_form_height);

        width.addTextChangedListener(this);
        height.addTextChangedListener(this);
    }

    @Override
    public float getOtherValue() {
        return getHeightInput();
    }

    @Override
    public float getValue() {
        return getWidthInput();
    }

    @Override
    public void setOtherValue(float otherValue) {
        setHeightInput((int) otherValue);
    }

    @Override
    public void setValue(float value) {
        setWidthInput((int) value);
    }

    public int getWidthInput() {
        return Integer.parseInt(width.getText().toString());
    }

    public int getHeightInput() {
        return Integer.parseInt(height.getText().toString());
    }

    public void setWidthInput(int width) {
        this.width.setText("" + width);
        notifyValueChanged();
    }

    public void setHeightInput(int height) {
        this.height.setText("" + height);
        notifyValueChanged();
    }

    @Override
    public String toString() {
        return "Size{" +
                "height=" + height +
                ", width=" + width +
                '}';
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        notifyValueChanged();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
