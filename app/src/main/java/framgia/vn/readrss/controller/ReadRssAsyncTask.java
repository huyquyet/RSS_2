package framgia.vn.readrss.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import framgia.vn.readrss.models.Data;
import framgia.vn.readrss.models.Information;
import framgia.vn.readrss.models.LinkUrl;
import framgia.vn.readrss.models.ListData;
import framgia.vn.readrss.stringInterface.NoteXml;

public class ReadRssAsyncTask extends AsyncTask<List<LinkUrl>, Void, Void> implements NoteXml {
    private Activity mContext;
    private URL mUrl;
    private HttpURLConnection mConn;
    private InputStream mStream;
    private XmlPullParserFactory mXmlFactoryObject;
    private XmlPullParser mParser;
    private Data mItem;
    private ArrayList<Data> mItems = new ArrayList<Data>();
    private List<ListData> mListPosts = new ArrayList<>();
    private UpdateData mUpdate;
    private Information mInformation = new Information();
    private FormatDate mFormatDate = new FormatDate();
    private ProgressDialog mDialog;

    public ReadRssAsyncTask(Activity ctx) {
        mContext = ctx;
    }

    public Information getInformation() {
        return mInformation;
    }

    public ArrayList<Data> getItems() {
        return mItems;
    }

    public List<ListData> getListPosts() {
        return mListPosts;
    }

    @Override
    protected void onPreExecute() {
        // Showing progress dialog before sending http request
        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage("Please wait load data");
        mDialog.setIndeterminate(true);
        mDialog.setCancelable(false);
        mDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(List<LinkUrl>... params) {
        List<LinkUrl> arr = params[0];
        try {
            for (LinkUrl linkUrl : arr) {
                mParser = parserXML(linkUrl.getUrl());
                if (linkUrl.getName().equals("all"))
                    parseXMLInformation(mParser);
                else {
                    parseXMLPost(mParser, linkUrl.getName());
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NetworkOnMainThreadException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void data) {
        if (mInformation != null) {
            mDialog.dismiss();
            mUpdate.updateData(true);
        } else {
            mUpdate.updateData(false);
        }
        super.onPostExecute(data);
    }

    public void parseXMLInformation(XmlPullParser myParser) {
        int event;
        String text = null;
        try {
            event = myParser.getEventType();
            boolean check = false;
            boolean check_image = false;
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG: // event = 2
                        if (name.equals(XML_ITEM_ITEM)) {
                            check = true;
                            break;
                        }
                        if (name.equals(XML_INFORMATION_IMAGE)) {
                            check_image = true;
                            break;
                        }
                        break;
                    case XmlPullParser.TEXT: // event = 4
                        text = myParser.getText().trim();
                        break;
                    case XmlPullParser.END_TAG: // event = 3
                        switch (name) {
                            case XML_INFORMATION_TITLE:
                                mInformation.setTitle(text);
                                break;
                            case XML_INFORMATION_LINK:
                                mInformation.setLink(text);
                                break;
                            case XML_INFORMATION_DESCRIPTION:
                                mInformation.setDescription(text);
                                break;
                            case XML_INFORMATION_IMAGE_URL:
                                if (check_image) {
                                    mInformation.setImage(text);
                                    check_image = false;
                                }
                                break;
                            case XML_INFORMATION_LANGUAGE:
                                mInformation.setLanguage(text);
                                break;
                            case XML_INFORMATION_COPYRIGHT:
                                mInformation.setCopyright(text);
                                break;
                            case XML_INFORMATION_LASTBUILDATE:
                                mInformation.setLastBuildDate(mFormatDate.formatDateToString(mFormatDate.formatDate(text)));
                                break;
                            case XML_INFORMATION_GENERATOR:
                                mInformation.setGenerator(text);
                                break;
                            case XML_INFORMATION_ATOM:
                                mInformation.setAtom(myParser.getAttributeValue(0));
                                break;
                            case XML_INFORMATION_TTL:
                                mInformation.setTtl(text);
                                break;
                        }
                        break;
                }
                event = myParser.next();
                if (check) break;
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void parseXMLPost(XmlPullParser myParser, String category) {
        ArrayList<Data> result = new ArrayList<Data>();
        int event;
        String text = null;
        try {
            event = myParser.getEventType();
            boolean insideItem = false;
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG: // event = 2
                        if (name.equals(XML_ITEM_ITEM)) {
                            insideItem = true;
                            mItem = new Data();
                            mItem.setCategory(category);
                        }
                        break;
                    case XmlPullParser.TEXT: // event = 4
                        text = myParser.getText().trim();
                        break;
                    case XmlPullParser.END_TAG: // event = 3
                        if (insideItem) {
                            if (name.equalsIgnoreCase(XML_ITEM_TITLE)) {
                                mItem.setTitle(text); //extract the headline
                            } else if (name.equals(XML_ITEM_DESCRIPTION)) {
                                mItem.setDescription(text);
                            } else if (name.equalsIgnoreCase(XML_ITEM_LINK)) {
                                mItem.setLink(text); //extract the link of article
                            } else if (name.equals(XML_ITEM_GUID)) {
                                mItem.setGuid(text);
                            } else if (name.equals(XML_ITEM_PUBDATE)) {
                                mItem.setPubDate(mFormatDate.formatDateToString(mFormatDate.formatDate(text)));
                            } else if (name.equals(XML_ITEM_CATEGORY)) {
                                mItem.setArrayListCategory(text);
                            } else if (name.equals(XML_ITEM_AUTHOR)) {
                                mItem.setAuthor(text);
                            } else if (name.equals(XML_ITEM_ENCLOSURE)) {
                                mItem.setEnclosure(myParser.getAttributeValue(0));
                            } else if (name.equals(XML_ITEM_ITEM)) {
                                insideItem = false;
                                result.add(mItem);
                            }
                        }
                        break;
                }
                event = myParser.next();
            }
            ListData list = new ListData(result, category);
            mListPosts.add(list);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    public interface UpdateData {
        public boolean updateData(boolean update);
    }

    public void setUpdate(UpdateData mUpdate) {
        this.mUpdate = mUpdate;
    }

    private XmlPullParser parserXML(String source) throws IOException, XmlPullParserException {
        XmlPullParser parser = null;
        mUrl = new URL(source);
        mConn = (HttpURLConnection) mUrl.openConnection();
        mConn.setRequestMethod("GET");
        mConn.setDoInput(true);
        mConn.connect();
        mStream = mConn.getInputStream();
        mXmlFactoryObject = XmlPullParserFactory.newInstance();
        parser = mXmlFactoryObject.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(mStream, null);
        return parser;
    }
}