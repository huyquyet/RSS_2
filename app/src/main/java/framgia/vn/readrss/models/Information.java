package framgia.vn.readrss.models;

/**
 * Created by FRAMGIA\nguyen.huy.quyet on 15/03/2016.
 */
public class Information {
    private String mTitle;
    private String mLink;
    private String mDescription;
    private String mImage;
    private String mLanguage;
    private String mCopyright;
    private String mTtl;
    private String mLastBuildDate;
    private String mGenerator;
    private String mAtom;

    public String getAtom() {
        return mAtom;
    }

    public void setAtom(String atom) {
        this.mAtom = atom;
    }

    public String getCopyright() {
        return mCopyright;
    }

    public void setCopyright(String copyright) {
        this.mCopyright = copyright;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getGenerator() {
        return mGenerator;
    }

    public void setGenerator(String generator) {
        this.mGenerator = generator;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        this.mLanguage = language;
    }

    public String getLastBuildDate() {
        return mLastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.mLastBuildDate = lastBuildDate;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        this.mLink = link;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getTtl() {
        return mTtl;
    }

    public void setTtl(String ttl) {
        this.mTtl = ttl;
    }
}
