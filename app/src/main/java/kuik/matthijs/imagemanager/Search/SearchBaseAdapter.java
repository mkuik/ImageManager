package kuik.matthijs.imagemanager.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import kuik.matthijs.imagemanager.DataTypes.FilterHolder;
import kuik.matthijs.imagemanager.R;
import kuik.matthijs.imagemanager.UserInput.Parts.ValueContainer;

/**
 * Created by Matthijs Kuik on 11/3/2016.
 */

public class SearchBaseAdapter extends BaseAdapter {

    private List<FilterHolder> items;
    private Context mContext;

    public SearchBaseAdapter(Context context) {
        items = new ArrayList<>();
        mContext = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public FilterHolder getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final FilterHolder item = getItem(i);
        view = new SearchParam(mContext, item.type);

        final ViewHolder holder = new ViewHolder(view);
        holder.value.setValue(item.A);
        holder.value.setOtherValue(item.B);
        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(item);
            }
        });
        holder.value.addValueChangeListener(new ValueContainer.OnValueChangeListener() {
            @Override
            public void onValueChange(ValueContainer source) {
                item.A = source.getValue();
                item.B = source.getOtherValue();
            }
        });
        return view;
    }

    public void addItem(final FilterHolder item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(final FilterHolder item) {
        items.remove(item);
        notifyDataSetChanged();
    }

    static class ViewHolder {

        ImageButton icon;
        ValueContainer value;
        ImageButton close;

        public ViewHolder(View view) {
            icon = (ImageButton) view.findViewById(R.id.icon);
            value = (ValueContainer) view.findViewById(R.id.value);
            close = (ImageButton) view.findViewById(R.id.close);
        }
    }
}
