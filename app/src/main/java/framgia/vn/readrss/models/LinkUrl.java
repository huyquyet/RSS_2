package framgia.vn.readrss.models;

import java.util.ArrayList;

/**
 * Created by FRAMGIA\nguyen.huy.quyet on 21/03/2016.
 */
public class LinkUrl {
    private String name;
    private String url;

    private ArrayList<LinkUrl> urlArrayList = new ArrayList<>();

    public LinkUrl() {
        String text = "http://www.voanews.com/api/";
        LinkUrl item = null;
        item = new LinkUrl("all", text + "epiqq");
        urlArrayList.add(item);
        item = new LinkUrl("USA", text + "/zq$omekvi_");
        urlArrayList.add(item);
        item = new LinkUrl("Africa", text + "z-$otevtiq");
        urlArrayList.add(item);
        item = new LinkUrl("Asia", text + "zo$o_egviy");
        urlArrayList.add(item);
        item = new LinkUrl("Middle East", text + "zr$opeuvim");
        urlArrayList.add(item);
        item = new LinkUrl("Europe", text + "zj$oveytit");
        urlArrayList.add(item);
        item = new LinkUrl("Americas", text + "zoripegtim");
        urlArrayList.add(item);
        item = new LinkUrl("Science & Technology", text + "zyritequir");
        urlArrayList.add(item);
        item = new LinkUrl("Economy", text + "zy$oqeqtii");
        urlArrayList.add(item);
        item = new LinkUrl("Health", text + "zt$opeitim");
        urlArrayList.add(item);
        item = new LinkUrl("Arts & Entertainment", text + "zp$ove-vir");
        urlArrayList.add(item);
        item = new LinkUrl("2016 USA Votes", text + "zuriqiepuiqm");
        urlArrayList.add(item);
        item = new LinkUrl("One-Minute Features", text + "z$roqmetuoqm");
        urlArrayList.add(item);
        item = new LinkUrl("VOA Editors Picks", text + "zgkoq_e_miqv");
        urlArrayList.add(item);
        item = new LinkUrl("Day in Photos", text + "z$-jqetv-i");
        urlArrayList.add(item);
        item = new LinkUrl("Shaka: Extra Time", text + "zpuoqre-iiqq");
        urlArrayList.add(item);
        item = new LinkUrl("Visiting the USA", text + "zj$qqmeytoqm");
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
