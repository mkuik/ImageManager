package kuik.matthijs.imagemanager.Bucket;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import kuik.matthijs.imagemanager.DataTypes.Picture;
import kuik.matthijs.imagemanager.DataTypes.Size;
import kuik.matthijs.imagemanager.R;
import kuik.matthijs.imagemanager.Widget.HueGraph;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Picture} and makes a call to the
 * specified {@link BucketFragment.OnImageInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BucketAdapter extends RecyclerView.Adapter<BucketAdapter.ViewHolder> {

    private final List<Picture> mValues;
    private final BucketFragment.OnImageInteractionListener mListener;
    private final Context mContext;

    public BucketAdapter(Context context, List<Picture> items, BucketFragment.OnImageInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    public void addItem(Picture item) {
        mValues.add(item);
    }

    public Picture getItem(int index) { return mValues.get(index); }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Picture picture = mValues.get(position);
        Log.d("onBindViewHolder", picture.toString());
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(String.valueOf(picture.getId()));
        holder.mSubtitleView.setText(String.format(Locale.ENGLISH, "%dx%d", picture.getWidth(), picture.getHeight()));
        holder.mGraph.setData(mValues.get(position).getColors());

        Picasso.with(mContext).load(mValues.get(position).getSource()).fit().centerCrop().into(holder.mImageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onImageClick(holder.mItem);
                }
            }
        });
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mListener) {
                     return mListener.onImageLongClick(holder.mItem);
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mSubtitleView;
        public final ImageView mImageView;
        public final HueGraph mGraph;
        public Picture mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mSubtitleView = (TextView) view.findViewById(R.id.subtitle);
            mImageView = (ImageView) view.findViewById(R.id.image);
            mGraph = (HueGraph) view.findViewById(R.id.graph);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
