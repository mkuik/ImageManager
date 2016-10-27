package kuik.matthijs.imagemanager.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import kuik.matthijs.imagemanager.DataTypes.FilterHolder;
import kuik.matthijs.imagemanager.DataTypes.Picture;
import kuik.matthijs.imagemanager.Fragment.GalleryActivityFragment;
import kuik.matthijs.imagemanager.R;
import kuik.matthijs.imagemanager.Widget.SearchDialog;

public class GalleryActivity extends AppCompatActivity {

    public static final int IMAGE_FROM_STORAGE = 400;
    final String FILENAME = "gallery.db";
    GalleryActivityFragment gallery = null;
    ProgressBar progressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Gallery", "onCreate");
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gallery = (GalleryActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        new AsyncTask<Void, Void, ArrayList<Picture>>() {
            @Override
            protected ArrayList<Picture> doInBackground(Void... voids) {
                ArrayList<Picture> images = null;
                try {
                    images = load();
                } catch (IOException | ClassNotFoundException e) {
                    Log.e("LOAD", e.toString());
                }
                return images;
            }

            @Override
            protected void onPostExecute(ArrayList<Picture> pictures) {
                super.onPostExecute(pictures);
                gallery.setImages(pictures);
            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
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
            case R.id.menu_action_scan:
                scanForImages();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            if (requestCode == IMAGE_FROM_STORAGE && null != data && gallery != null) {
                gallery.addImage(new Picture(data.getData(), getID(data.getData())));
                if (gallery != null) try {
                    save(gallery.getImages());
                } catch (IOException e) {
                    Log.e("save", e.toString());
                }
            }
        }
    }

    private int getID(Uri uri) {
        // which image properties are we querying
        String[] projection = new String[] {
                MediaStore.Images.Media._ID,
        };

        // Make the query.
        final Cursor cur = getContentResolver().query(uri,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                null        // Ordering
        );

        int id = -1;
        if (cur != null && cur.moveToFirst()) {
            int idColum = cur.getColumnIndex(
                    MediaStore.Images.Media._ID);
            id = cur.getInt(idColum);
        }
        return id;
    }

    private void scanForImages() {
        // which image properties are we querying
        final String[] projection = new String[] {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DATA
        };

        // content:// style URI for the "primary" external storage volume
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // Make the query.
        final Cursor cur = getContentResolver().query(images,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME        // Ordering
        );

        Log.i("ListingImages"," query count=" + cur.getCount());

        new AsyncTask<Void, Picture, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                if (cur.moveToFirst()) {
                    String bucket;
                    String date;
                    String path;
                    int id;
                    Uri uri;
                    int bucketColumn = cur.getColumnIndex(
                            MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                    int dateColumn = cur.getColumnIndex(
                            MediaStore.Images.Media.DATE_TAKEN);
                    int pathColumn = cur.getColumnIndex(
                            MediaStore.Images.Media.DATA);
                    int idColum = cur.getColumnIndex(
                            MediaStore.Images.Media._ID);

                    do {
                        // Get the field values
                        bucket = cur.getString(bucketColumn);
                        date = cur.getString(dateColumn);
                        path = cur.getString(pathColumn);
                        uri = Uri.fromFile(new File(path));
                        id = cur.getInt(idColum);

                        // Do something with the values.
                        Log.i("ListingImages", " bucket=" + bucket
                                + " date_taken=" + date + " uri=" + uri);
                        Picture picture = new Picture(uri, id);
                        if (!gallery.hasImage(picture)) {
                            picture.setDetails(bucket);
                            try {
                                picture.init(GalleryActivity.this);
                                publishProgress(picture);
                            } catch (IOException | NullPointerException e) {
                                Log.e("init", e.toString());
                            }
                        }
                    } while (cur.moveToNext() && !isCancelled());
                    cur.close();
                    if (gallery != null) {
                        try {
                            save(gallery.getImages());
                        } catch (IOException e) {
                            Log.e("save", e.toString());
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Picture... values) {
                super.onProgressUpdate(values);
                if (gallery != null) {
                    gallery.addImage(values[0]);
                } else {
                    cancel(true);
                }
            }
        }.execute();

    }

    private void save(ArrayList<Picture> data) throws IOException {
        FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(data);
        out.close();
        fos.close();
        Log.d("Gallery", "save " + gallery.getImages().size() + " pictures");
    }

    private ArrayList<Picture> load() throws IOException, ClassNotFoundException {
        FileInputStream fis = openFileInput(FILENAME);
        ObjectInputStream in = new ObjectInputStream(fis);
        ArrayList<Picture> data = (ArrayList<Picture>) in.readObject();
        in.close();
        fis.close();
        Log.d("Gallery", "load " + data.size() + " pictures");
        return data;
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
        if (gallery != null) {
            gallery.search(query);
            try {
                save(gallery.getImages());
            } catch (IOException e) {
                Log.e("save", e.toString());
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
