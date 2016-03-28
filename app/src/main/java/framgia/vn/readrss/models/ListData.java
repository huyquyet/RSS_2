package framgia.vn.readrss.models;

import java.util.ArrayList;

/**
 * Created by FRAMGIA\nguyen.huy.quyet on 22/03/2016.
 */
public class ListData {
    private ArrayList<Data> mDataArrayList = new ArrayList<>();
    private String mCategory;

    public ListData() {
    }

    public ListData(ArrayList<Data> arr, String category) {
        this.mDataArrayList = arr;
        this.mCategory = category;
    }

    public ArrayList<Data> getDataArrayList() {
        return mDataArrayList;
    }

    public void setDataArrayList(Data mDataArrayList) {
        this.mDataArrayList.add(mDataArrayList);
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        this.mCategory = category;
    }
}
