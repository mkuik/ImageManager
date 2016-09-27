package kuik.matthijs.imagemanager;

import android.net.Uri;

/**
 * Created by Matthijs Kuik on 9/26/2016.
 */

public class GalleryItem {

    private String title;
    private Uri uri;
    private String image;

    public GalleryItem() {
    }

    public GalleryItem(String title, Uri uri) {
        this.title = title;
        this.uri = uri;
    }

    public GalleryItem(String title, String image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public Uri getUri() {
        return uri;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
