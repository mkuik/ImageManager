package kuik.matthijs.imagemanager;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class GalleryFilterActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    boolean isShow = false;
    int scrollRange = -1;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_filter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        setSupportActionBar(toolbar);
//        appBarLayout.addOnOffsetChangedListener(this);
//        collapsingToolbarLayout.setTitle(" ");
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (scrollRange == -1) {
            scrollRange = appBarLayout.getTotalScrollRange();
        }
        if (scrollRange + verticalOffset == 0) {
            collapsingToolbarLayout.setTitle("Title");
            isShow = true;
        } else if(isShow) {
            collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
            isShow = false;
        }
    }
}
