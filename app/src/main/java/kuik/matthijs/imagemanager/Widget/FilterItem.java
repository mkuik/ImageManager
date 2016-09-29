package kuik.matthijs.imagemanager.Widget;

import kuik.matthijs.imagemanager.FilterType;

/**
 * Created by Matthijs on 29/09/2016.
 */

public class FilterItem {

    private FilterType type;
    private int valueA;
    private int valueB;

    public FilterItem(FilterType type, int valueA, int valueB) {
        this.type = type;
        this.valueA = valueA;
        this.valueB = valueB;
    }

    public FilterType getType() {
        return type;
    }

    public void setType(FilterType type) {
        this.type = type;
    }

    public int getValueA() {
        return valueA;
    }

    public void setValueA(int valueA) {
        this.valueA = valueA;
    }

    public int getValueB() {
        return valueB;
    }

    public void setValueB(int valueB) {
        this.valueB = valueB;
    }
}
