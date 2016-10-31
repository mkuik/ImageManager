package kuik.matthijs.imagemanager.Bucket;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import kuik.matthijs.imagemanager.BucketOverview.BucketItem;
import kuik.matthijs.imagemanager.DataTypes.Picture;
import kuik.matthijs.imagemanager.R;

public class BucketActivity extends AppCompatActivity implements BucketFragment.OnImageInteractionListener, SearchFragment.OnSearchInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket);
    }

    @Override
    public void onImageClick(Picture item) {

    }

    @Override
    public boolean onImageLongClick(Picture item) {
        return false;
    }

    @Override
    public void onSearch(Uri uri) {

    }
}
