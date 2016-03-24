package framgia.vn.readrss.models;

import java.util.ArrayList;

import framgia.vn.readrss.stringInterface.Url;

/**
 * Created by FRAMGIA\nguyen.huy.quyet on 21/03/2016.
 */
public class LinkUrl implements Url {
    private String name;
    private String url;

    private ArrayList<LinkUrl> urlArrayList = new ArrayList<>();

    public LinkUrl() {
        LinkUrl item;
        item = new LinkUrl(NAME_URL_ALL, LINK_URL_ALL);
        urlArrayList.add(item);
        item = new LinkUrl(NAME_URL_USA, LINK_URL_USA);
        urlArrayList.add(item);
        item = new LinkUrl(NAME_URL_AFRICA, LINK_URL_AFRICA);
        urlArrayList.add(item);
        item = new LinkUrl(NAME_URL_ASIA, LINK_URL_ASIA);
        urlArrayList.add(item);
        item = new LinkUrl(NAME_URL_MIDDLE_EAST, LINK_URL_MIDDLE_EAST);
        urlArrayList.add(item);
        item = new LinkUrl(NAME_URL_EUROPE, LINK_URL_EUROPE);
        urlArrayList.add(item);
        item = new LinkUrl(NAME_URL_AMERICAS, LINK_URL_AMERICAS);
        urlArrayList.add(item);
        item = new LinkUrl(NAME_URL_SCIENCE_TECHNOLOGY, LINK_URL_SCIENCE_TECHNOLOGY);
        urlArrayList.add(item);
        item = new LinkUrl(NAME_URL_ECONOMY, LINK_URL_ECONOMY);
        urlArrayList.add(item);
        item = new LinkUrl(NAME_URL_HEALTH, LINK_URL_HEALTH);
        urlArrayList.add(item);
        item = new LinkUrl(NAME_URL_ARTS_ENTERTAINMENT, LINK_URL_ARTS_ENTERTAINMENT);
        urlArrayList.add(item);
        item = new LinkUrl(NAME_URL_2016_USA_VOTES, LINK_URL_2016_USA_VOTES);
        urlArrayList.add(item);
        item = new LinkUrl(NAME_URL_ONE_MINUTE_FEATURES, LINK_URL_ONE_MINUTE_FEATURES);
        urlArrayList.add(item);
        item = new LinkUrl(NAME_URL_VOA_EDITORS_PICKS, LINK_URL_VOA_EDITORS_PICKS);
        urlArrayList.add(item);
        item = new LinkUrl(NAME_URL_DAY_IN_PHOTOS, LINK_URL_DAY_IN_PHOTOS);
        urlArrayList.add(item);
        item = new LinkUrl(NAME_URL_SHAKA_EXTRA_TIME, LINK_URL_SHAKA_EXTRA_TIME);
        urlArrayList.add(item);
        item = new LinkUrl(NAME_URL_VISITING_THE_USA, LINK_URL_VISITING_THE_USA);
        urlArrayList.add(item);
    }

    public LinkUrl(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<LinkUrl> getUrlArrayList() {
        return urlArrayList;
    }
}
