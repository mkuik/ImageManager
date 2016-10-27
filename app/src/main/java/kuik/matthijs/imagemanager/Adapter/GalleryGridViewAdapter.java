package kuik.matthijs.imagemanager.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kuik.matthijs.imagemanager.DataTypes.Picture;
import kuik.matthijs.imagemanager.R;
import kuik.matthijs.imagemanager.Widget.HueGraph;

public class GalleryGridViewAdapter  extends ArrayAdapter<Picture> {

    private Context mContext;
    private int layoutResourceId;

    public GalleryGridViewAdapter(Context mContext, int layoutResourceId, ArrayList<Picture> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
    }

    public boolean hasPicture(Picture picture) {
        for (int i = 0; i != getCount(); ++i) {
            if (getItem(i).getId() == picture.getId()) {
                return true;
            }
        }
        return false;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
            holder.details = (TextView) row.findViewById(R.id.details);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            holder.graph = (HueGraph) row.findViewById(R.id.graph);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final Picture item = getItem(position);
        if (item != null) {
            holder.titleTextView.setText(String.format(Locale.ENGLISH, "%s", item.getDetails()));
            Picasso.with(mContext).load(item.getSource()).fit().centerCrop().into(holder.imageView);
            holder.graph.setData(item.getColors());
        }
        return row;
    }

    private static class SetGraphTask extends AsyncTask<Void, Void, Void> {

        private ViewHolder holder;
        private Picture picture;
        private Context context;

        SetGraphTask(Context context, ViewHolder holder, Picture picture) {
            this.holder = holder;
            this.picture = picture;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            holder.graph.setData(null);
            holder.titleTextView.setText(String.format(Locale.ENGLISH, "%s",
                    picture.getSource().getLastPathSegment()));
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            try {
//                picture.init(context);
//            } catch (IOException e) {
//                Log.d("pic init", e.toString());
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            holder.graph.setData(picture.getColors());
            holder.titleTextView.setText(String.format(Locale.ENGLISH, "%s %dx%d",
                    picture.getSource().getLastPathSegment(),
                    picture.getWidth(), picture.getHeight()));
        }
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView details;
        ImageView imageView;
        HueGraph graph;
    }
}