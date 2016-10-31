package kuik.matthijs.imagemanager.BucketOverview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import kuik.matthijs.imagemanager.BucketOverview.BucketOverviewFragment.OnBucketInteractionListener;
import kuik.matthijs.imagemanager.R;
import kuik.matthijs.imagemanager.Widget.HueGraph;

import java.io.File;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BucketItem} and makes a call to the
 * specified {@link OnBucketInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BucketOverviewAdapter extends RecyclerView.Adapter<BucketOverviewAdapter.ViewHolder> {

    private final List<BucketItem> mValues;
    private final OnBucketInteractionListener mListener;
    private final Context mContext;

    public BucketOverviewAdapter(Context context, List<BucketItem> items, OnBucketInteractionListener listener) {
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

    public void addItem(BucketItem item) {
        mValues.add(item);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mSubtitleView.setText("position " + position);
        holder.mSubtitleView.setVisibility(View.GONE);
        holder.mGraph.setVisibility(View.GONE);

        Picasso.with(mContext).load(new File(mValues.get(position).getPath())).fit().centerCrop().into(holder.mImageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onBucketClick(holder.mItem);
                }
            }
        });
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mListener) {
                     return mListener.onBucketLongClick(holder.mItem);
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
        public BucketItem mItem;

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
