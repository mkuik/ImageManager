package kuik.matthijs.imagemanager.UserInput.Parts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.List;

import kuik.matthijs.imagemanager.R;

/**
 * Created by Matthijs on 17/10/2016.
 */

public abstract class ValueContainer extends LinearLayout implements ValuePair {

    private List<OnValueChangeListener> listeners = new ArrayList<>();

    public ValueContainer(Context context) {
        super(context);
        init(null, 0);
    }

    public ValueContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ValueContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    protected void init(AttributeSet attrs, int defStyle) {
    }

    public void addValueChangeListener(OnValueChangeListener listener) {
        listeners.add(listener);
    }

    public void removeValueChangeListener(OnValueChangeListener listener) {
        listeners.remove(listener);
    }

    protected void notifyValueChanged() {
        for (OnValueChangeListener listener : listeners) {
            listener.onValueChange(this);
        }
    }

    @Override
    public String toString() {
        return "ValueContainer{}";
    }

    public interface OnValueChangeListener {
        void onValueChange(ValueContainer source);
    }
}
