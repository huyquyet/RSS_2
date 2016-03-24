package framgia.vn.readrss.models;

import java.util.ArrayList;

/**
 * Created by FRAMGIA\nguyen.huy.quyet on 22/03/2016.
 */
public class ListData {
    private ArrayList<Data> dataArrayList = new ArrayList<>();
    private String category;

    public ListData() {
    }

    public ListData(ArrayList<Data> arr, String category) {
        this.dataArrayList = arr;
        this.category = category;
    }

    public ArrayList<Data> getDataArrayList() {
        return dataArrayList;
    }

    public void setDataArrayList(Data dataArrayList) {
        this.dataArrayList.add(dataArrayList);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
