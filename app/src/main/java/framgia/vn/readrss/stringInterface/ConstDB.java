package framgia.vn.readrss.stringInterface;

/**
 * Created by FRAMGIA\nguyen.huy.quyet on 24/03/2016.
 */
public interface ConstDB {
    /**
     * String Name Table
     */
    public static final String NAME_DATABASE = "readRss.db";
    public static final String TBL_CATEGORY = "tblCategory";
    public static final String TBL_POST = "tblPost";
    public static final String TBL_TIMEUPDATE = "tblTimeUpdate";
    public static final String TBL_CATEGORYPOST = "tblCategoryPost";
    public static final String TBL_INFORMATION = "tblInformation";

    /**
     * String column in table
     */
    //  Table TBL_CATEGORY
    public static final String COL_CATEGORY_ID = "id";
    public static final String COL_CATEGORY_NAME = "name";

    //  Table TBL_INFORMATION
    public static final String COL_INFORMATION_ID = "id";
    public static final String COL_INFORMATION_TITLE = "title";
    public static final String COL_INFORMATION_LINK = "link";
    public static final String COL_INFORMATION_DESCRIPTION = "description";
    public static final String COL_INFORMATION_IMAGE = "image";
    public static final String COL_INFORMATION_LANGUAGE = "language";
    public static final String COL_INFORMATION_COPYRIGHT = "copyright";
    public static final String COL_INFORMATION_TTL = "ttl";
    public static final String COL_INFORMATION_LASTBUILDATE = "lastBuildDate";
    public static final String COL_INFORMATION_GENERATOR = "generator";
    public static final String COL_INFORMATION_ATOM = "atom";

    //  Table TBL_POST
    public static final String COL_POST_ID = "id";
    public static final String COL_POST_TITLE = "title";
    public static final String COL_POST_DESCRIPTION = "description";
    public static final String COL_POST_LINK = "link";
    public static final String COL_POST_GUID = "guid";
    public static final String COL_POST_PUBDATE = "pubDate";
    public static final String COL_POST_CATEGORY = "category";
    public static final String COL_POST_AUTHOR = "author";
    public static final String COL_POST_ENCLOSURE = "enclosure";

    //  Table TBL_TIMEUPDATE
    public static final String COL_TIMEUPDATE_TIME = "time";

    //  Table TBL_CATEGORYPOST
    public static final String COL_CATEGORYPOST_ID_POST = "id_post";
    public static final String COL_CATEGORYPOST_ID_CATEGORY = "id_category";
    public static final String COL_CATEGORYPOST_CATEGORY_PRIMARY = "category_primary";

    /**
     * String create table
     */
    public static final String SQL_CATEGORY = "create table " + TBL_CATEGORY + " (" +
            COL_CATEGORY_ID + " integer primary key autoincrement," +
            COL_CATEGORY_NAME + " text)";
    public static final String SQL_INFORMATION = "create table " + TBL_INFORMATION + " (" +
            COL_INFORMATION_ID + " integer primary key autoincrement, " +
            COL_INFORMATION_TITLE + " text, " +
            COL_INFORMATION_LINK + " text, " +
            COL_INFORMATION_DESCRIPTION + " text, " +
            COL_INFORMATION_IMAGE + " text, " +
            COL_INFORMATION_LANGUAGE + " text, " +
            COL_INFORMATION_COPYRIGHT + " text, " +
            COL_INFORMATION_TTL + " text, " +
            COL_INFORMATION_LASTBUILDATE + " text," +
            COL_INFORMATION_GENERATOR + " text, " +
            COL_INFORMATION_ATOM + " text)";
    public static final String SQL_POST = "create table " + TBL_POST + " (" +
            COL_POST_ID + " integer primary key autoincrement, " +
            COL_POST_TITLE + " text, " +
            COL_POST_DESCRIPTION + " text, " +
            COL_POST_LINK + " text, " +
            COL_POST_GUID + " text, " +
            COL_POST_PUBDATE + " text, " +
            COL_POST_CATEGORY + " text, " +
            COL_POST_AUTHOR + " text, " +
            COL_POST_ENCLOSURE + " text)";
    public static final String SQL_TIMEUPDATE = " create table " + TBL_TIMEUPDATE + " (" +
            COL_TIMEUPDATE_TIME + " datetime)";
    public static final String SQL_CATEGORY_POST = "create table " + TBL_CATEGORYPOST + "(" +
            COL_CATEGORYPOST_ID_POST + " integer not null constraint " + COL_CATEGORYPOST_ID_POST +
            " references " + TBL_POST + "(" + COL_POST_ID + ") on delete cascade," +
            COL_CATEGORYPOST_ID_CATEGORY + " integer not null constraint " + COL_CATEGORYPOST_ID_CATEGORY +
            " references " + TBL_CATEGORY + "(" + COL_CATEGORY_ID + ") on delete cascade," +
            COL_CATEGORYPOST_CATEGORY_PRIMARY + " integer)";
}
