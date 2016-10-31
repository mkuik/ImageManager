package kuik.matthijs.imagemanager.Bucket;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.Space;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

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
    private LinearLayout searchButtonLayout;

    private OnSearchInteractionListener mListener;

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

    public void setExpand(boolean expand) {
        this.expand = expand;
        if (expand) {
            expand();
        } else {
            collapse();
        }
    }

    private void expand() {
        FloatingActionButton button = new FloatingActionButton(getContext()) {
            @Override
            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
                TranslateAnimation anim = new TranslateAnimation(searchButton.getX(), 0, 0, 0);
                anim.setDuration(500);
                this.startAnimation(anim);
            }
        };
        Space space = new Space(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        space.setLayoutParams(lp);
        searchButtonLayout.addView(space);
        searchButtonLayout.addView(button);
    }

    private void collapse() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        if (view != null) {
            searchButton = (FloatingActionButton) view.findViewById(R.id.search_button);
            searchButtonLayout = (LinearLayout) view.findViewById(R.id.button_layout);

            searchButton.setOnClickListener(this);
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSearch(uri);
        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_button:
                setExpand(!expand);
                break;
        }
    }

    public interface OnSearchInteractionListener {
        // TODO: Update argument type and name
        void onSearch(Uri uri);
    }
}
