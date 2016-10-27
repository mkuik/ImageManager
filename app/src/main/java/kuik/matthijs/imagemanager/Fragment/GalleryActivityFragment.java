package kuik.matthijs.imagemanager.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

import kuik.matthijs.imagemanager.Activity.GalleryActivity;
import kuik.matthijs.imagemanager.Adapter.GalleryGridViewAdapter;
import kuik.matthijs.imagemanager.DataTypes.FilterHolder;
import kuik.matthijs.imagemanager.DataTypes.Picture;
import kuik.matthijs.imagemanager.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class GalleryActivityFragment extends Fragment implements AdapterView.OnItemLongClickListener {

    private GalleryGridViewAdapter adapter;
    private ArrayList<Picture> data = new ArrayList<>();
    private GridView galleryGrid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        if (view != null) {
            galleryGrid = (GridView) view.findViewById(R.id.gridView);
            galleryGrid.setOnItemLongClickListener(this);
        }
        setImages(data);
        return view;
    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
        final Picture picture = adapter.getItem(i);

        CharSequence colors[] = new CharSequence[] {"Remove"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(picture.getSource().getLastPathSegment());
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    adapter.remove(picture);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        builder.show();
        return true;
    }

    public void setImages(ArrayList<Picture> data) {
        this.data = data;
        adapter = new GalleryGridViewAdapter(getContext(), R.layout.gallery_item, this.data);
        galleryGrid.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    public ArrayList<Picture> getImages() {
        return data;
    }

    public boolean hasImage(Picture picture) {
        return adapter.hasPicture(picture);
    }

    public void search(final FilterHolder[] query) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                final HashMap<Picture, Float> results = new HashMap<>();

                for (Picture picture : data) {
                    float score = 0;
                    for (FilterHolder filterItem : query) {
//                Log.i("search", filterItem.toString());
                        switch (filterItem.type) {
                            case HUE:
                                score += picture.getHueDistance((short) (filterItem.A * 360));
                                break;
                            case BW:
                                break;
                            case SIZE:
                                break;
                        }
                    }
                    results.put(picture, score);
                    picture.setDetails(String.format(Locale.ENGLISH, "P:%.1f", score));
                }
                Collections.sort(data, new Comparator<Picture>() {
                    @Override
                    public int compare(Picture p0, Picture p1) {
                        float s0 = results.get(p0);
                        float s1 = results.get(p1);
                        if (s0 == s1) {
                            return 0;
                        } else if (s0 < s1) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    public void addImage(final Picture picture) {
        Log.i("GalleryFragment", "add "+ picture);
        data.add(picture);
        adapter.notifyDataSetChanged();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
//                try {
//                    picture.init(getContext());
//                } catch (IOException e) {
//                    Log.d("init", e.toString());
//                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
