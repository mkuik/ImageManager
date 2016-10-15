package kuik.matthijs.imagemanager.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kuik.matthijs.imagemanager.DataTypes.Picture;
import kuik.matthijs.imagemanager.Widget.GalleryItem;
import kuik.matthijs.imagemanager.R;

/**
 * Created by Matthijs Kuik on 9/26/2016.
 */

public class GalleryGridViewAdapter  extends ArrayAdapter<Picture> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Picture> mGridData = new ArrayList<>();

    public GalleryGridViewAdapter(Context mContext, int layoutResourceId, ArrayList<Picture> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }

    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<Picture> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final Picture item = mGridData.get(position);
        holder.titleTextView.setText(item.getSource().toString());
        Picasso.with(mContext).load(item.getSource()).into(holder.imageView);
        return row;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
    }
}