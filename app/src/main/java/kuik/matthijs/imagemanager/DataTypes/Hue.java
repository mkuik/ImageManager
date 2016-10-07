package kuik.matthijs.imagemanager.DataTypes;

import java.util.Comparator;

/**
 * Created by Matthijs on 06/10/2016.
 */

public class Hue {

    private short hue;
    private short count;

    public Hue(short hue, short count) {
        this.count = count;
        this.hue = hue;
    }

    public short getHue() {
        return hue;
    }

    public short getCount() {
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
        return "Hue{" +
                "count=" + count +
                ", hue=" + hue +
                '}';
    }
}
