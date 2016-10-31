package kuik.matthijs.imagemanager.DataTypes;

import java.io.Serializable;

/**
 * Created by Matthijs on 05/10/2016.
 */

public class Size implements Serializable {

    private int width = 0;
    private int height = 0;

    public Size(int width, int height) {
        this.height = height;
        this.width = width;
    }

    public Size() {
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Size fitInside(int value) {
        int max = Math.max(width, height);
        float scale = (float)max / value;
        return new Size((int)(width * scale), (int)(height * scale));
    }

    @Override
    public String toString() {
        return width + "x" + height;
    }
}
