package kuik.matthijs.imagemanager.Bucket;

import android.Manifest;
import android.app.Fragment;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import kuik.matthijs.imagemanager.DataTypes.Picture;
import kuik.matthijs.imagemanager.GridSpacingItemDecoration;
import kuik.matthijs.imagemanager.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnImageInteractionListener}
 * interface.
 */
public class BucketFragment extends Fragment {

    public static final int BUCKETS = 402;
    public static final String ARG_COLUMN_COUNT = "column-count";
    public static final String ARG_BUCKET_NAME = "bucket-name";
    private int mColumnCount = 2;
    private String bucketName = "";
    private OnImageInteractionListener mListener;
    private BucketAdapter adapter;

    public BucketFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BucketFragment newInstance(int columnCount) {
        BucketFragment fragment = new BucketFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnImageInteractionListener) {
            mListener = (OnImageInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnImageInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        bucketName = getActivity().getIntent().getExtras().getString(ARG_BUCKET_NAME);
        getActivity().setTitle(bucketName);
        Log.i("BucketFragment", bucketName);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new BucketAdapter(getActivity(), new ArrayList<Picture>(), mListener);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(mColumnCount, 2, false));
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        retrieveContent();
        new AnalyseImages().execute();
    }

    public class AnalyseImages extends AsyncTask<Void, Picture, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i != adapter.getItemCount() && !isDetached(); ++i) {
                try {
                    adapter.getItem(i).init(getActivity());
                    publishProgress();
                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Picture... values) {
            super.onProgressUpdate(values);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnImageInteractionListener {
        void onImageClick(Picture item);
        boolean onImageLongClick(Picture item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case BUCKETS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    retrieveContent();
                }
                break;
            }
        }
    }



    public void retrieveContent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    BUCKETS);
            return;
        }

        // which image properties are we querying
        String[] PROJECTION_BUCKET = {
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT
        };

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String clause = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?";
        String [] args = {bucketName};
        String orderBy = MediaStore.Images.Media.DATE_TAKEN + " DESC";

        Cursor cur = getActivity().getContentResolver().query(
                images, PROJECTION_BUCKET, clause, args, orderBy);

        Log.i("ListingImages"," query count=" + cur.getCount());

        if (cur.moveToFirst()) {
            String bucket;
            String date;
            String data;
            int id;
            int width;
            int height;
            int bucketColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int dateColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATE_TAKEN);
            int dataColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATA);
            int idColumn = cur.getColumnIndex(
                    MediaStore.Images.Media._ID);
            int widthColum = cur.getColumnIndex(
                    MediaStore.Images.Media.WIDTH);
            int heightColum = cur.getColumnIndex(
                    MediaStore.Images.Media.HEIGHT);

            do {
                // Get the field values
                bucket = cur.getString(bucketColumn);
                date = cur.getString(dateColumn);
                data = cur.getString(dataColumn);
                id = cur.getInt(idColumn);
                width = cur.getInt(widthColum);
                height = cur.getInt(heightColum);

                // Do something with the values.
                Log.i("ListingImages", " bucket=" + bucket
                        + "  date_taken=" + date
                        + "  _data=" + data);

                adapter.addItem(new Picture(Uri.fromFile(new File(data)), id, width, height));

            } while (cur.moveToNext());

            adapter.notifyDataSetChanged();
        }

        cur.close();
    }
}
