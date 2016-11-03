package kuik.matthijs.imagemanager.Search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import kuik.matthijs.imagemanager.Bucket.BucketFragment;
import kuik.matthijs.imagemanager.DataTypes.FilterHolder;
import kuik.matthijs.imagemanager.DataTypes.FilterType;
import kuik.matthijs.imagemanager.DataTypes.Picture;
import kuik.matthijs.imagemanager.R;
import kuik.matthijs.imagemanager.UserInput.Hue;
import kuik.matthijs.imagemanager.UserInput.Parts.ValueContainer;
import kuik.matthijs.imagemanager.UserInput.Saturation;
import kuik.matthijs.imagemanager.UserInput.Size;
import kuik.matthijs.imagemanager.Widget.HueGraph;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Picture} and makes a call to the
 * specified {@link BucketFragment.OnImageInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private final List<FilterHolder> mValues;
    private final Context mContext;

    public SearchAdapter(Context context, List<FilterHolder> items) {
        mValues = items;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_row, parent, false);
        return new ViewHolder(view);
    }

    public void addItem(FilterHolder item) {
        mValues.add(item);
    }

    public FilterHolder getItem(int index) { return mValues.get(index); }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final FilterHolder filterHolder = mValues.get(position);
        Log.d("onBindViewHolder", filterHolder.toString());
        holder.setType(mContext, filterHolder.type);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public FrameLayout container;
        public ImageButton closeButton;
        public ValueContainer valueContainer = null;

        public ViewHolder(View view) {
            super(view);
            container = (FrameLayout) view.findViewById(R.id.filter);
            closeButton = (ImageButton) view.findViewById(R.id.button_clear);
        }

        public void setView(ValueContainer view) {
            valueContainer = view;
            container.removeAllViewsInLayout();
            container.addView(valueContainer);
        }

        public void setType(Context context, FilterType type) {
            switch (type) {
                case HUE:
                    setView(new Hue(context));
                    break;
                case BW:
                    setView(new Saturation(context));
                    break;
                case SIZE:
                    setView(new Size(context));
                    break;
            }
        }
    }
}
