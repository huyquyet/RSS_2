package framgia.vn.readrss.stringInterface;

/**
 * Created by FRAMGIA\nguyen.huy.quyet on 24/03/2016.
 */
public interface NoteXml {
    /**
     * Xml Information
     */
    public static final String XML_INFORMATION_TITLE = "title";
    public static final String XML_INFORMATION_LINK = "link";
    public static final String XML_INFORMATION_DESCRIPTION = "description";
    public static final String XML_INFORMATION_IMAGE_URL = "url";
    public static final String XML_INFORMATION_LANGUAGE = "language";
    public static final String XML_INFORMATION_COPYRIGHT = "copyright";
    public static final String XML_INFORMATION_LASTBUILDATE = "lastBuildDate";
    public static final String XML_INFORMATION_GENERATOR = "generator";
    public static final String XML_INFORMATION_ATOM = "atom:link";
    public static final String XML_INFORMATION_TTL = "ttl";
    public static final String XML_INFORMATION_IMAGE = "image";

    /**
     * Xml Item
     */
    public static final String XML_ITEM_ITEM = "item";
    public static final String XML_ITEM_TITLE = "title";
    public static final String XML_ITEM_DESCRIPTION = "description";
    public static final String XML_ITEM_LINK = "link";
    public static final String XML_ITEM_GUID = "guid";
    public static final String XML_ITEM_PUBDATE = "pubDate";
    public static final String XML_ITEM_CATEGORY = "category";
    public static final String XML_ITEM_AUTHOR = "author";
    public static final String XML_ITEM_ENCLOSURE = "enclosure";
}
