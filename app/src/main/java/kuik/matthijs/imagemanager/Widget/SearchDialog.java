package kuik.matthijs.imagemanager.Widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import kuik.matthijs.imagemanager.Adapter.FilterAdapter;
import kuik.matthijs.imagemanager.DataTypes.FilterType;
import kuik.matthijs.imagemanager.R;

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
        adapter = new FilterAdapter(context, R.layout.filter_row, new ArrayList<FilterItem>());
        listView.setAdapter(adapter);

        ImageButton size = (ImageButton) findViewById(R.id.button_size);
        ImageButton hue = (ImageButton) findViewById(R.id.button_color);
        ImageButton bw = (ImageButton) findViewById(R.id.button_saturation);

        size.setOnClickListener(this);
        hue.setOnClickListener(this);
        bw.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_size:
                adapter.add(new FilterItem(FilterType.SIZE, 0, 0));
                adapter.notifyDataSetChanged();
                break;
            case R.id.button_color:
                adapter.add(new FilterItem(FilterType.HUE, 0, 0));
                adapter.notifyDataSetChanged();
                break;
            case R.id.button_saturation:
                adapter.add(new FilterItem(FilterType.BW, 0, 0));
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
