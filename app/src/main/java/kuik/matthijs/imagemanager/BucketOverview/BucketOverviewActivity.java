package kuik.matthijs.imagemanager.BucketOverview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import kuik.matthijs.imagemanager.Bucket.BucketActivity;
import kuik.matthijs.imagemanager.Bucket.BucketFragment;
import kuik.matthijs.imagemanager.R;

public class BucketOverviewActivity extends AppCompatActivity implements BucketOverviewFragment.OnBucketInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_overview);
    }

    @Override
    public void onBucketClick(BucketItem item) {
        Intent intent = new Intent(this, BucketActivity.class);
        intent.putExtra(BucketFragment.ARG_BUCKET_NAME, item.getTitle());
        startActivity(intent);
    }

    @Override
    public boolean onBucketLongClick(BucketItem item) {
        return false;
    }
}
