package kuik.matthijs.imagemanager.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kuik.matthijs.imagemanager.R;
import kuik.matthijs.imagemanager.Widget.Filter;
import kuik.matthijs.imagemanager.Widget.FilterItem;
import kuik.matthijs.imagemanager.Widget.FilterRow;
import kuik.matthijs.imagemanager.Widget.GalleryItem;

/**
 * Created by Matthijs Kuik on 9/26/2016.
 */

public class FilterAdapter extends ArrayAdapter<FilterItem> {

    private Context mContext;
    private int layoutResourceId;

    public FilterAdapter(Context mContext, int layoutResourceId, ArrayList<FilterItem> mGridData) {
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
            holder.filter = (Filter) row.findViewById(R.id.filter);
            holder.button = (ImageButton) row.findViewById(R.id.button_clear);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final FilterItem item = getItem(position);
        holder.filter.setFilterType(item.getType());
        holder.filter.setValues(item.getValueA(), item.getValueB());
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
        Filter filter;
        ImageButton button;
    }
}