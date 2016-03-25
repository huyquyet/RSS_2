package framgia.vn.readrss.models;

import java.util.ArrayList;

/**
 * Created by FRAMGIA\nguyen.huy.quyet on 17/03/2016.
 */
public class Category {

    private String name;

    private ArrayList<Category> categoryArrayList = new ArrayList<>();

    public ArrayList<Category> getCategoryArrayList() {
        return categoryArrayList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category() {
        Category category = null;
        category = new Category("USA");
        this.categoryArrayList.add(category);
        category = new Category("Africa");
        this.categoryArrayList.add(category);
        category = new Category("Asia");
        this.categoryArrayList.add(category);
        category = new Category("Middle East");
        this.categoryArrayList.add(category);
        category = new Category("Europe");
        this.categoryArrayList.add(category);
        category = new Category("Americas");
        this.categoryArrayList.add(category);
        category = new Category("Science & Technology");
        this.categoryArrayList.add(category);
        category = new Category("Economy");
        this.categoryArrayList.add(category);
        category = new Category("Health");
        this.categoryArrayList.add(category);
        category = new Category("Arts & Entertainment");
        this.categoryArrayList.add(category);
        category = new Category("2016 USA Votes");
        this.categoryArrayList.add(category);
        category = new Category("One-Minute Features");
        this.categoryArrayList.add(category);
        category = new Category("VOA Editors Picks");
        this.categoryArrayList.add(category);
        category = new Category("Day in Photos");
        this.categoryArrayList.add(category);
        category = new Category("Shaka: Extra Time");
        this.categoryArrayList.add(category);
        category = new Category("Visiting the USA");
        this.categoryArrayList.add(category);
    }

    public Category(String name) {
        this.name = name;
    }
}