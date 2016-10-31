package kuik.matthijs.imagemanager.DataTypes;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by Matthijs on 06/10/2016.
 */

public class Hue implements Serializable {

    private short hue;
    private float count;

    public Hue(short hue, float count) {
        this.count = count;
        this.hue = hue;
    }

    public short getHue() {
        return hue;
    }

    public float getCount() {
        return count;
    }

    static class SortByCount implements Comparator<Hue> {

        @Override
        public int compare(Hue A, Hue B) {
            if (A.getCount() == B.getCount()) {
                return 0;
            } else if (A.getCount() > B.getCount()) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "Hue{%d, %.1f}", hue, count);
    }
}
