package framgia.vn.readrss.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by FRAMGIA\nguyen.huy.quyet on 15/03/2016.
 */
public class Data implements Serializable {
    private String mId;
    private String mTitle;
    private String mDescription;
    private String mLink;
    private String mGuid;
    private String mPubDate;
    private String mCategory;
    private ArrayList<String> mArrayListCategory = new ArrayList<>();
    private String mAuthor;
    private String mEnclosure;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        this.mAuthor = author;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        this.mCategory = category;
    }

    public ArrayList<String> getArrayListCategory() {
        return mArrayListCategory;
    }

    public void setArrayListCategory(String category) {
        this.mArrayListCategory.add(category);
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getEnclosure() {
        return mEnclosure;
    }

    public void setEnclosure(String enclosure) {
        this.mEnclosure = enclosure;
    }

    public String getGuid() {
        return mGuid;
    }

    public void setGuid(String guid) {
        this.mGuid = guid;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        this.mLink = link;
    }

    public String getPubDate() {
        return mPubDate;
    }

    public void setPubDate(String pubDate) {
        this.mPubDate = pubDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public Data() {
        super();
    }

    public Data(String title, String description, String link, String guid, String pubDate, ArrayList<String> arrayListCategory, String author, String enclosure) {
        this.mTitle = title;
        this.mDescription = description;
        this.mLink = link;
        this.mGuid = guid;
        this.mPubDate = pubDate;
        this.mArrayListCategory = arrayListCategory;
        this.mAuthor = author;
        this.mEnclosure = enclosure;
    }
}