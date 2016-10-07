package kuik.matthijs.imagemanager.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import kuik.matthijs.imagemanager.DataTypes.Picture;
import kuik.matthijs.imagemanager.R;
import kuik.matthijs.imagemanager.Widget.SearchDialog;

public class GalleryActivity extends AppCompatActivity {

    public static final int IMAGE_FROM_STORAGE = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }

    public void showSearchDialog() {

        // custom dialog
        final Dialog dialog = new SearchDialog(this);

        Button buttonOk = (Button) dialog.findViewById(R.id.button_ok);
        // if button is clicked, close the custom dialog
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button buttonCancel = (Button) dialog.findViewById(R.id.button_cancel);
        // if button is clicked, close the custom dialog
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
        AsyncTask<Uri, Void, Picture> task = new AsyncTask<Uri, Void, Picture>() {
            @Override
            protected Picture doInBackground(Uri... uris) {
                try {
                    return new Picture(GalleryActivity.this, uris[0]);
                } catch (IOException e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Picture picture) {
                super.onPostExecute(picture);
                if (picture != null) {
                    Toast.makeText(GalleryActivity.this, picture.toString(), Toast.LENGTH_LONG).show();
                    Log.d("Colors", "" + picture.getColors());
                }
                else
                    Toast.makeText(GalleryActivity.this, "file not found", Toast.LENGTH_LONG).show();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(GalleryActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        task.execute(uri);
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
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, IMAGE_FROM_STORAGE);
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
