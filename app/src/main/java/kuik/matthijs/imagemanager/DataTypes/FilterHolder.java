package kuik.matthijs.imagemanager.DataTypes;

/**
 * Created by Matthijs on 17/10/2016.
 */

public class FilterHolder {
    public FilterType type;
    public float A;
    public float B;

    public FilterHolder(FilterType type) {
        this.type = type;
        this.A = 0;
        this.B = 0;
    }

    @Override
    public String toString() {
        return "FilterHolder{" +
                "A=" + A +
                ", type=" + type +
                ", B=" + B +
                '}';
    }
}
