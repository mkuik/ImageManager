package kuik.matthijs.imagemanager.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

import kuik.matthijs.imagemanager.Adapter.GalleryGridViewAdapter;
import kuik.matthijs.imagemanager.DataTypes.FilterHolder;
import kuik.matthijs.imagemanager.DataTypes.Picture;
import kuik.matthijs.imagemanager.R;
import kuik.matthijs.imagemanager.UserInput.Parts.ValueContainer;
import kuik.matthijs.imagemanager.Widget.SearchDialog;

public class GalleryActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    public static final int IMAGE_FROM_STORAGE = 400;
    private GalleryGridViewAdapter adapter;
    private ArrayList<Picture> data = new ArrayList<>();
    private GridView galleryGrid;
    final String FILENAME = "gallery.db";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Gallery", "onDestroy");
        // TODO Save data in internal memory using serialisation

        try {
            save();
        } catch (IOException e) {
            Log.d("Gallery", e.toString());
        }
    }

    private void save() throws IOException {
        FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(data);
        out.close();
        fos.close();

        Log.d("Gallery", "save " + data.size() + " pictures");
    }

    private void load() throws IOException, ClassNotFoundException {
        FileInputStream fis = openFileInput(FILENAME);
        ObjectInputStream in = new ObjectInputStream(fis);
        data = (ArrayList<Picture>) in.readObject();
        in.close();
        fis.close();

        Log.d("Gallery", "load " + data.size() + " pictures");
        for (Picture picture : data) {
            Log.d("Gallery", "load " + picture.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Gallery", "onCreate");
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        galleryGrid = (GridView) findViewById(R.id.gridView);
        galleryGrid.setOnItemLongClickListener(this);

        try {
            load();
        } catch (IOException | ClassNotFoundException e) {
            Log.d("Gallery", e.toString());
        }

        adapter = new GalleryGridViewAdapter(this, R.layout.gallery_item, data);
        galleryGrid.setAdapter(adapter);
    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
        final Picture picture = adapter.getItem(i);

        CharSequence colors[] = new CharSequence[] {"Remove"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }

    public void showSearchDialog() {
        final SearchDialog dialog = new SearchDialog(this);
        Button buttonOk = (Button) dialog.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                search(dialog.getSearchQuery());
            }
        });
        Button buttonCancel = (Button) dialog.findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void search(FilterHolder[] query) {
        final HashMap<Picture, Float> results = new HashMap<>();
        for (Picture picture : data) {
            float score = 0;
            for (FilterHolder filterItem : query) {
                Log.i("search", filterItem.toString());
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
            picture.setDetails(String.format(Locale.ENGLISH, "P:%.3f", score));
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
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_action_search:
                showSearchDialog();
                return true;
            case R.id.menu_action_add:
                getImageFromGallery();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addImage(final Uri uri) {
        final Picture picture = new Picture(uri);
        data.add(picture);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case IMAGE_FROM_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImageFromGallery();
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_FROM_STORAGE && null != data) {
                addImage(data.getData());
            }
        }
    }

    protected void getImageFromGallery() {
        if (hasStoragePermissions()) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMAGE_FROM_STORAGE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    IMAGE_FROM_STORAGE);
        }
    }

    protected boolean hasStoragePermissions() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

}
