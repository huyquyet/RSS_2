package framgia.vn.readrss.controller;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
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
import framgia.vn.readrss.stringInterface.noteXml;

public class ReadRssAsyncTask extends AsyncTask<List<LinkUrl>, ArrayList<Data>, ArrayList<Data>> implements noteXml {
    private Activity context;
    private URL url;
    private HttpURLConnection conn;
    private InputStream stream;
    private XmlPullParserFactory xmlFactoryObject;
    private XmlPullParser myparser;
    private Data item;
    private ArrayList<Data> items = new ArrayList<Data>();
    private List<ListData> listPosts = new ArrayList<>();
    private UpdateData update;
    private Information informations = new Information();
    private FormatDate formatDate = new FormatDate();

    public ReadRssAsyncTask(Activity ctx) {
        context = ctx;
    }

    public Information getInformations() {
        return informations;
    }

    public ArrayList<Data> getItems() {
        return items;
    }

    public List<ListData> getListPosts() {
        return listPosts;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Data> doInBackground(List<LinkUrl>... params) {
        List<LinkUrl> arr = params[0];
        try {
            for (LinkUrl linkUrl : arr) {
                myparser = parserXML(linkUrl.getUrl());
                if (linkUrl.getName().equals("all"))
                    parseXMLInformation(myparser);
                else {
                    parseXMLPost(myparser, linkUrl.getName());
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NetworkOnMainThreadException e) {
            e.printStackTrace();
            Toast.makeText(context, " Errors ", Toast.LENGTH_LONG).show();
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
    protected void onProgressUpdate(ArrayList<Data>... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ArrayList<Data> data) {
        if (informations != null) {
            update.updateData(true);
        } else {
            update.updateData(false);
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
                                informations.setTitle(text);
                                break;
                            case XML_INFORMATION_LINK:
                                informations.setLink(text);
                                break;
                            case XML_INFORMATION_DESCRIPTION:
                                informations.setDescription(text);
                                break;
                            case XML_INFORMATION_IMAGE_URL:
                                if (check_image) {
                                    informations.setImage(text);
                                    check_image = false;
                                }
                                break;
                            case XML_INFORMATION_LANGUAGE:
                                informations.setLanguage(text);
                                break;
                            case XML_INFORMATION_COPYRIGHT:
                                informations.setCopyright(text);
                                break;
                            case XML_INFORMATION_LASTBUILDATE:
                                informations.setLastBuildDate(formatDate.formatDateToString(formatDate.formatDate(text)));
                                break;
                            case XML_INFORMATION_GENERATOR:
                                informations.setGenerator(text);
                                break;
                            case XML_INFORMATION_ATOM:
                                informations.setAtom(myParser.getAttributeValue(0));
                                break;
                            case XML_INFORMATION_TTL:
                                informations.setTtl(text);
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
            Log.d("Loi", e.toString());
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
                            item = new Data();
                            item.setCategory(category);
                        }
                        break;
                    case XmlPullParser.TEXT: // event = 4
                        text = myParser.getText().trim();
                        break;
                    case XmlPullParser.END_TAG: // event = 3
                        if (insideItem) {
                            if (name.equalsIgnoreCase(XML_ITEM_TITLE)) {
                                item.setTitle(text); //extract the headline
                            } else if (name.equals(XML_ITEM_DESCRIPTION)) {
                                item.setDescription(text);
                            } else if (name.equalsIgnoreCase(XML_ITEM_LINK)) {
                                item.setLink(text); //extract the link of article
                            } else if (name.equals(XML_ITEM_GUID)) {
                                item.setGuid(text);
                            } else if (name.equals(XML_ITEM_PUBDATE)) {
                                item.setPubDate(formatDate.formatDateToString(formatDate.formatDate(text)));
                            } else if (name.equals(XML_ITEM_CATEGORY)) {
                                item.setarrayListcategory(text);
                            } else if (name.equals(XML_ITEM_AUTHOR)) {
                                item.setAuthor(text);
                            } else if (name.equals(XML_ITEM_ENCLOSURE)) {
                                item.setEnclosure(myParser.getAttributeValue(0));
                            } else if (name.equals(XML_ITEM_ITEM)) {
                                insideItem = false;
                                result.add(item);
                            }
                        }
                        break;
                }
                event = myParser.next();
            }
            ListData list = new ListData(result, category);
            listPosts.add(list);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            Log.d("Loi", e.toString());
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

    public void setUpdate(UpdateData update) {
        this.update = update;
    }

    private XmlPullParser parserXML(String source) throws IOException, XmlPullParserException {
        XmlPullParser parser = null;
        url = new URL(source);
        conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        stream = conn.getInputStream();
        xmlFactoryObject = XmlPullParserFactory.newInstance();
        parser = xmlFactoryObject.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(stream, null);
        return parser;
    }
}