package framgia.vn.readrss.models;

import java.util.ArrayList;

import framgia.vn.readrss.stringInterface.Url;

/**
 * Created by FRAMGIA\nguyen.huy.quyet on 21/03/2016.
 */
public class LinkUrl implements Url {
    private String mName;
    private String mUrl;
    private ArrayList<LinkUrl> mUrlArrayList = new ArrayList<>();

    public LinkUrl() {
        LinkUrl item;
        item = new LinkUrl(NAME_URL_ALL, LINK_URL_ALL);
        mUrlArrayList.add(item);
        item = new LinkUrl(NAME_URL_USA, LINK_URL_USA);
        mUrlArrayList.add(item);
        item = new LinkUrl(NAME_URL_AFRICA, LINK_URL_AFRICA);
        mUrlArrayList.add(item);
        item = new LinkUrl(NAME_URL_ASIA, LINK_URL_ASIA);
        mUrlArrayList.add(item);
        item = new LinkUrl(NAME_URL_MIDDLE_EAST, LINK_URL_MIDDLE_EAST);
        mUrlArrayList.add(item);
        item = new LinkUrl(NAME_URL_EUROPE, LINK_URL_EUROPE);
        mUrlArrayList.add(item);
        item = new LinkUrl(NAME_URL_AMERICAS, LINK_URL_AMERICAS);
        mUrlArrayList.add(item);
        item = new LinkUrl(NAME_URL_SCIENCE_TECHNOLOGY, LINK_URL_SCIENCE_TECHNOLOGY);
        mUrlArrayList.add(item);
        item = new LinkUrl(NAME_URL_ECONOMY, LINK_URL_ECONOMY);
        mUrlArrayList.add(item);
        item = new LinkUrl(NAME_URL_HEALTH, LINK_URL_HEALTH);
        mUrlArrayList.add(item);
        item = new LinkUrl(NAME_URL_ARTS_ENTERTAINMENT, LINK_URL_ARTS_ENTERTAINMENT);
        mUrlArrayList.add(item);
        item = new LinkUrl(NAME_URL_2016_USA_VOTES, LINK_URL_2016_USA_VOTES);
        mUrlArrayList.add(item);
        item = new LinkUrl(NAME_URL_ONE_MINUTE_FEATURES, LINK_URL_ONE_MINUTE_FEATURES);
        mUrlArrayList.add(item);
        item = new LinkUrl(NAME_URL_VOA_EDITORS_PICKS, LINK_URL_VOA_EDITORS_PICKS);
        mUrlArrayList.add(item);
        item = new LinkUrl(NAME_URL_DAY_IN_PHOTOS, LINK_URL_DAY_IN_PHOTOS);
        mUrlArrayList.add(item);
        item = new LinkUrl(NAME_URL_SHAKA_EXTRA_TIME, LINK_URL_SHAKA_EXTRA_TIME);
        mUrlArrayList.add(item);
        item = new LinkUrl(NAME_URL_VISITING_THE_USA, LINK_URL_VISITING_THE_USA);
        mUrlArrayList.add(item);
    }

    public LinkUrl(String name, String url) {
        this.mName = name;
        this.mUrl = url;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public ArrayList<LinkUrl> getUrlArrayList() {
        return mUrlArrayList;
    }
}
