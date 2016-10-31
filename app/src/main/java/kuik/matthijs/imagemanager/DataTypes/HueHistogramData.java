package kuik.matthijs.imagemanager.DataTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthijs on 31/10/2016.
 */

public class HueHistogramData extends ArrayList<Hue> implements Serializable {

    private Hue max = null;

    @Override
    public boolean add(Hue hue) {
        if (max == null || (hue != null && hue.getCount() > max.getCount())) {
            max = hue;
        }
        return super.add(hue);
    }

    public Hue getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "HueHistogramData{max=" + max + ", " + super.toString() + '}';
    }
}
