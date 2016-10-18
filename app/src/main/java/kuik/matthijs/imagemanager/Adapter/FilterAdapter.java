package kuik.matthijs.imagemanager.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import java.util.ArrayList;

import kuik.matthijs.imagemanager.DataTypes.FilterHolder;
import kuik.matthijs.imagemanager.R;
import kuik.matthijs.imagemanager.UserInput.Hue;
import kuik.matthijs.imagemanager.UserInput.Parts.ValueContainer;
import kuik.matthijs.imagemanager.UserInput.Saturation;
import kuik.matthijs.imagemanager.UserInput.Size;

/**
 * Created by Matthijs Kuik on 9/26/2016.
 */

public class FilterAdapter extends ArrayAdapter<FilterHolder> {

    private Context mContext;
    private int layoutResourceId;

    public FilterAdapter(Context mContext, int layoutResourceId, ArrayList<FilterHolder> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.filter = (FrameLayout) row.findViewById(R.id.filter);
            holder.button = (ImageButton) row.findViewById(R.id.button_clear);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final FilterHolder item = getItem(position);

        ValueContainer container = null;
        switch (item.type) {
            case HUE:
                container = new Hue(getContext());
                break;
            case BW:
                container = new Saturation(getContext());
                break;
            case SIZE:
                container = new Size(getContext());
                break;
        }
        container.setValue(item.A);
        container.setOtherValue(item.B);
        holder.filter.removeAllViewsInLayout();
        holder.filter.addView(container);

        container.addValueChangeListener(new ValueContainer.OnValueChangeListener() {
            @Override
            public void onValueChange(ValueContainer source) {
                item.A = source.getValue();
                item.B = source.getOtherValue();
            }
        });

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(item);
                notifyDataSetChanged();
            }
        });

        return row;
    }

    static class ViewHolder {
        FrameLayout filter;
        ImageButton button;
    }
}