package kuik.matthijs.imagemanager.Search;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import kuik.matthijs.imagemanager.Adapter.FilterAdapter;
import kuik.matthijs.imagemanager.DataTypes.FilterHolder;
import kuik.matthijs.imagemanager.DataTypes.FilterType;
import kuik.matthijs.imagemanager.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSearchInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements View.OnClickListener {

    private boolean expand = false;

    private FloatingActionButton searchButton;
    private FloatingActionButton colorButton;
    private FloatingActionButton saturationButton;
    private FloatingActionButton sizeButton;
    private LinearLayout buttonLayout;
    private SearchAdapter adapter;
    private OnSearchInteractionListener mListener;
    private RecyclerView recyclerView;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        if (view != null) {
            searchButton = (FloatingActionButton) view.findViewById(R.id.search_button);
            colorButton = (FloatingActionButton) view.findViewById(R.id.color_button);
            saturationButton = (FloatingActionButton) view.findViewById(R.id.saturation_button);
            sizeButton = (FloatingActionButton) view.findViewById(R.id.size_button);
            buttonLayout = (LinearLayout) view.findViewById(R.id.button_container);

            searchButton.setOnClickListener(this);
            colorButton.setOnClickListener(this);
            saturationButton.setOnClickListener(this);
            sizeButton.setOnClickListener(this);

            recyclerView = (RecyclerView) view.findViewById(R.id.search_param_list);
            adapter = new SearchAdapter(getContext(), new ArrayList<FilterHolder>());
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_button:
                setExpand(!expand);
                break;
            case R.id.color_button:
                adapter.addItem(new FilterHolder(FilterType.HUE));
                break;
            case R.id.saturation_button:
                adapter.addItem(new FilterHolder(FilterType.BW));
                break;
            case R.id.size_button:
                adapter.addItem(new FilterHolder(FilterType.SIZE));
                break;
        }
        adapter.notifyDataSetChanged();
    }

    public void setExpand(boolean expand) {
        if (expand) {
            expand();
        } else  {
            collapse();
        }
        this.expand = expand;
    }

    private void expand() {
        buttonLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

        final int resource = R.anim.zoom_in;
        animate(colorButton, 100, null, resource);
        animate(saturationButton, 50, null, resource);
        animate(sizeButton, 0, null, resource);
    }

    private void collapse() {
        final int resource = R.anim.zoom_out;
        animate(colorButton, 0, null, resource);
        animate(saturationButton, 50, null, resource);
        animate(sizeButton, 100, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                buttonLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        }, resource);
    }

    private void animate(final View view, int delay, Animation.AnimationListener listener, int resource) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), resource);
        animation.setAnimationListener(listener);
        animation.setStartOffset(delay);
        view.startAnimation(animation);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchInteractionListener) {
            mListener = (OnSearchInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSearchInteractionListener {
        void onSearch(Uri uri);
    }
}
