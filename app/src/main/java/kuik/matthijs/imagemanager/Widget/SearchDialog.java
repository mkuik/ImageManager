package kuik.matthijs.imagemanager.Widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import kuik.matthijs.imagemanager.Adapter.FilterAdapter;
import kuik.matthijs.imagemanager.DataTypes.FilterHolder;
import kuik.matthijs.imagemanager.DataTypes.FilterType;
import kuik.matthijs.imagemanager.R;
import kuik.matthijs.imagemanager.UserInput.Hue;
import kuik.matthijs.imagemanager.UserInput.Parts.ValueContainer;
import kuik.matthijs.imagemanager.UserInput.Saturation;
import kuik.matthijs.imagemanager.UserInput.Size;

/**
 * Created by Matthijs on 28/09/2016.
 */

public class SearchDialog extends Dialog implements View.OnClickListener {

    ListView listView;
    FilterAdapter adapter;

    public SearchDialog(Context context) {
        super(context);
        init(context);
    }

    protected SearchDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public SearchDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private void init(Context context) {
        setContentView(R.layout.search_dialog);
        listView = (ListView) findViewById(R.id.filter_listview);
        adapter = new FilterAdapter(context, R.layout.filter_row, new ArrayList<FilterHolder>());
        listView.setAdapter(adapter);

        ImageButton size = (ImageButton) findViewById(R.id.button_size);
        ImageButton hue = (ImageButton) findViewById(R.id.button_color);
        ImageButton bw = (ImageButton) findViewById(R.id.button_saturation);

        size.setOnClickListener(this);
        hue.setOnClickListener(this);
        bw.setOnClickListener(this);
    }

    public FilterHolder[] getSearchQuery() {
        FilterHolder[] query = new FilterHolder[adapter.getCount()];
        for (int i = 0; i != adapter.getCount(); ++i) {
            FilterHolder filter = adapter.getItem(i);
            query[i] = filter;
        }
        return query;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_size:
                adapter.add(new FilterHolder(FilterType.SIZE));
                adapter.notifyDataSetChanged();
                break;
            case R.id.button_color:
                adapter.add(new FilterHolder(FilterType.HUE));
                adapter.notifyDataSetChanged();
                break;
            case R.id.button_saturation:
                adapter.add(new FilterHolder(FilterType.BW));
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
