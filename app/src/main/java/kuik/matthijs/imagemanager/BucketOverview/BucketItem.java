package kuik.matthijs.imagemanager.BucketOverview;

import java.io.Serializable;

/**
 * Created by Matthijs on 28/10/2016.
 */

public class BucketItem implements Serializable {

    private String path;
    private String title;

    public BucketItem(String path, String title) {
        this.path = path;
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
