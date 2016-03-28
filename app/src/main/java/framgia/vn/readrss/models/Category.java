package framgia.vn.readrss.models;

import java.util.ArrayList;

import framgia.vn.readrss.stringInterface.Url;

/**
 * Created by FRAMGIA\nguyen.huy.quyet on 17/03/2016.
 */
public class Category implements Url {
    private String mName;
    private ArrayList<Category> mCategoryArrayList = new ArrayList<>();

    public ArrayList<Category> getCategoryArrayList() {
        return mCategoryArrayList;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Category() {
        Category category = null;
        category = new Category(NAME_URL_USA);
        this.mCategoryArrayList.add(category);
        category = new Category(NAME_URL_AFRICA);
        this.mCategoryArrayList.add(category);
        category = new Category(NAME_URL_ASIA);
        this.mCategoryArrayList.add(category);
        category = new Category(NAME_URL_MIDDLE_EAST);
        this.mCategoryArrayList.add(category);
        category = new Category(NAME_URL_EUROPE);
        this.mCategoryArrayList.add(category);
        category = new Category(NAME_URL_AMERICAS);
        this.mCategoryArrayList.add(category);
        category = new Category(NAME_URL_SCIENCE_TECHNOLOGY);
        this.mCategoryArrayList.add(category);
        category = new Category(NAME_URL_ECONOMY);
        this.mCategoryArrayList.add(category);
        category = new Category(NAME_URL_HEALTH);
        this.mCategoryArrayList.add(category);
        category = new Category(NAME_URL_ARTS_ENTERTAINMENT);
        this.mCategoryArrayList.add(category);
        category = new Category(NAME_URL_2016_USA_VOTES);
        this.mCategoryArrayList.add(category);
        category = new Category(NAME_URL_ONE_MINUTE_FEATURES);
        this.mCategoryArrayList.add(category);
        category = new Category(NAME_URL_VOA_EDITORS_PICKS);
        this.mCategoryArrayList.add(category);
        category = new Category(NAME_URL_DAY_IN_PHOTOS);
        this.mCategoryArrayList.add(category);
        category = new Category(NAME_URL_SHAKA_EXTRA_TIME);
        this.mCategoryArrayList.add(category);
        category = new Category(NAME_URL_VISITING_THE_USA);
        this.mCategoryArrayList.add(category);
    }

    public Category(String name) {
        this.mName = name;
    }
}