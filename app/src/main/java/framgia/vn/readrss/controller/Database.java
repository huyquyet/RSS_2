package framgia.vn.readrss.controller;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import framgia.vn.readrss.models.Category;
import framgia.vn.readrss.models.Data;
import framgia.vn.readrss.models.Information;
import framgia.vn.readrss.models.ListData;
import framgia.vn.readrss.stringInterface.ConstDB;
import framgia.vn.readrss.stringInterface.ConstQuery;

public final class Database implements ConstDB, ConstQuery{
    private static final int INSERT_ERROR = -1;
    private static final int ID_INFORMATION_ERROR = -1;
    private Activity mContext;
    private Cursor mCursor;
    private ContentValues mValues;
    private Category mCategory = new Category();
    private SQLiteDatabase mSQLiteDatabase;

    public Database(Activity context) {
        this.mContext = context;
    }

    public boolean insertDataBase(SQLiteDatabase database) {
        try {
            if (database != null) {
                if (!isDatabaseExists(database, TBL_POST)) {
                    /**
                     * ko ton tai bang => tao moi CSDL trong
                     * bat dau tao moi cac bang
                     */
                    database.setLocale(Locale.getDefault());
                    database.setVersion(1);
                    /**
                     * Create table category
                     */
                    database.execSQL(SQL_CATEGORY);
                    /**
                     * Create table tblInformation
                     */
                    database.execSQL(SQL_INFORMATION);
                    /**
                     * Create table tblPost
                     */
                    database.execSQL(SQL_POST);
                    /**
                     * Create table tblTimeUpdate
                     */
                    database.execSQL(SQL_TIMEUPDATE);
                    /**
                     * Create table tblCategoryPost
                     */
                    database.execSQL(SQL_CATEGORY_POST);
                    insertDataCategoryPost(database);
                    insertTimeUpdate(database);
                    database.close();
                    return true;
                } else return false;  // End if (!isDatabaseExists(database, TBL_POST))
            } else return false;  // End if(database != null)
        } catch (Exception ex) {
            delete_Database(database);
            return false;
        }
    }

    public void delete_Database(SQLiteDatabase database) {
        if (mContext.deleteDatabase(NAME_DATABASE)) {
            Toast.makeText(mContext, "Delete data success", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(mContext, "Delete data error", Toast.LENGTH_SHORT).show();
    }

    public boolean checkTimeUpdate() {
        String dateNow = returnDateNowStringFormatDDMMYYY();
        if (!checkConnectDataBase()) return false;
        mCursor = mSQLiteDatabase.query(TBL_TIMEUPDATE, null, null, null, null, null, null);
        if (mCursor == null || mCursor.getCount() <= 0) {
            closeConnectDataBase();
            return false;
        }
        mCursor.moveToFirst();
        String date = mCursor.getString(0);
        try {
            Date date1 = FormatDate.formatStringToDate_DDMMYYY(date);
            Date date2 = FormatDate.formatStringToDate_DDMMYYY(dateNow);
            if (date1.compareTo(date2) < 0) {
                closeConnectDataBase();
                return true;
            }
        } catch (ParseException e) {
            closeConnectDataBase();
            return false;
        }
        closeConnectDataBase();
        return false;
    }

    public void updateTimeUpdate() {
        mValues = new ContentValues();
        mValues.put(COL_TIMEUPDATE_TIME, returnDateNowStringFormatDDMMYYY());
        if (!checkConnectDataBase()) return;
        int check = mSQLiteDatabase.update(TBL_TIMEUPDATE, mValues, COL_TIMEUPDATE_ID + "=?", new String[]{String.valueOf(1)});
        if (check > 0) mValues.clear();
        else {
            Toast.makeText(mContext, "Error update time !", Toast.LENGTH_SHORT).show();
            mValues.clear();
        }
        closeConnectDataBase();
    }

    public void insertOrUpdateDataPost(List<ListData> data) {
        if (!checkConnectDataBase()) return;
        else {
            if (checkDataTable(mSQLiteDatabase, TBL_POST)) {
//                deletePostTimeLarger10Day(mSQLiteDatabase);
                updateDataPost(mSQLiteDatabase, data);
            } else {
                insertDataPost(mSQLiteDatabase, data);
            }
            closeConnectDataBase();
        }
    }

    public void insertOrUpdateDataInformation(Information data) {
        if (!checkConnectDataBase()) return;
        int idInformation = checkDataInformation(mSQLiteDatabase);
        if (idInformation != ID_INFORMATION_ERROR)
            updateDataInformation(mSQLiteDatabase, data, idInformation);
        else insertDataInformation(mSQLiteDatabase, data);
        closeConnectDataBase();
    }

    /**
     * @return Object Information
     */
    public Information returnDataInformation() {
        Information result = new Information();
        if (!checkConnectDataBase()) return result;
        mCursor = mSQLiteDatabase.query(TBL_INFORMATION, null, null, null, null, null, null);
        if (mCursor == null || mCursor.getCount() <= 0) return result;
        mCursor.moveToFirst();
        result.setTitle(mCursor.getString(1));
        result.setLink(mCursor.getString(2));
        result.setDescription(mCursor.getString(3));
        result.setImage(mCursor.getString(4));
        result.setLanguage(mCursor.getString(5));
        result.setCopyright(mCursor.getString(6));
        result.setTtl(mCursor.getString(7));
        result.setLastBuildDate(mCursor.getString(8));
        result.setGenerator(mCursor.getString(9));
        result.setAtom(mCursor.getString(10));
        mCursor.close();
        closeConnectDataBase();
        return result;
    }

    /**
     * @return List<ListData> with 20 first record in ListData
     */
    public List<ListData> returnDataPost() {
        List<ListData> result = new ArrayList<>();
        if (!checkConnectDataBase()) return result;
        int limit = LIMIT_QUERY_LIST_POST; // So ban ghi lay ve trong 1 lan truy van
        for (Category cate : mCategory.getCategoryArrayList()) {
            result.add(returnPostsOfCategory(mSQLiteDatabase, cate.getName(), 0, limit));
        }
        closeConnectDataBase();
        return result;
    }

    /**
     * Return record Posts by Category
     *
     * @param sqLiteDatabase
     * @param category       Name Category
     * @param start          Number record start, default 0
     * @param number         Number record return
     * @return Return Posts
     */
    public ListData returnPostsOfCategory(SQLiteDatabase sqLiteDatabase, String category, int start, int number) {
        ListData result = new ListData();
        result.setCategory(category);
        Data post;
        String limit = start + "," + number;
        mCursor = sqLiteDatabase.query(TBL_POST, null, COL_POST_CATEGORY + " = ?", new String[]{category}, null, null, COL_POST_ID, limit);
        mCursor.moveToFirst();
        while (!mCursor.isAfterLast()) {
            post = new Data();
            post.setId(mCursor.getString(0));
            post.setTitle(mCursor.getString(1));
            post.setDescription(mCursor.getString(2));
            post.setLink(mCursor.getString(3));
            post.setGuid(mCursor.getString(4));
            post.setPubDate(mCursor.getString(5));
            post.setCategory(mCursor.getString(6));
            post.setAuthor(mCursor.getString(7));
            post.setEnclosure(mCursor.getString(8));
            result.setDataArrayList(post);
            mCursor.moveToNext();
        }
        mCursor.close();
        return result;
    }

    /**
     * Function Private
     */

    private boolean checkConnectDataBase() {
        mSQLiteDatabase = Connection.connectDataBase(mContext);
        if (mSQLiteDatabase != null) return true;
        else return false;
    }

    private void closeConnectDataBase() {
        if (mSQLiteDatabase != null) mSQLiteDatabase.close();
    }

    private boolean isDatabaseExists(SQLiteDatabase database, String tableName) {
        String query = "Select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'";
        mCursor = database.rawQuery(query, null);
        if (mCursor == null || mCursor.getCount() <= 0) return false;
        mCursor.close();
        return true;
    }

    private void insertDataCategoryPost(SQLiteDatabase database) {
        mValues = new ContentValues();
        for (Category category1 : mCategory.getCategoryArrayList()) {
            mValues.put(COL_CATEGORY_NAME, category1.getName());
            long check = database.insert(TBL_CATEGORY, null, mValues);
            if (check == INSERT_ERROR) {
                Toast.makeText(mContext, "Error insert data Category", Toast.LENGTH_SHORT).show();
                delete_Database(database);
                break;
            } else
                mValues.clear();
        }
        Toast.makeText(mContext, "Insert database CategoryPost success !", Toast.LENGTH_SHORT).show();
    }

    private void insertTimeUpdate(SQLiteDatabase database) {
        mValues = new ContentValues();
        mValues.put(COL_TIMEUPDATE_TIME, returnDateNowStringFormatDDMMYYY());
        long check = database.insert(TBL_TIMEUPDATE, null, mValues);
        if (check == INSERT_ERROR) {
            Toast.makeText(mContext, "Error insert TimeUpdate", Toast.LENGTH_SHORT).show();
            mValues.clear();
        } else mValues.clear();
    }


    private int checkDataInformation(SQLiteDatabase sqLiteDatabase) {
        int idInformation = ID_INFORMATION_ERROR;
        mCursor = sqLiteDatabase.query(TBL_INFORMATION, null, null, null, null, null, null);
        if (mCursor == null || mCursor.getCount() <= 0) return idInformation;
        mCursor.moveToFirst();
        idInformation = Integer.parseInt(mCursor.getString(0));
        mCursor.close();
        return idInformation;
    }

    /**
     * Kiem tra bang tableName co du lieu hay ko, voi lan dau mo app
     *
     * @param sqLiteDatabase
     * @param tableName
     * @return true if data exists
     */
    private boolean checkDataTable(SQLiteDatabase sqLiteDatabase, String tableName) {
        mCursor = sqLiteDatabase.query(tableName, null, null, null, null, null, null);
        if (mCursor == null || mCursor.getCount() <= 0) return false;
        mCursor.close();
        return true;
    }

    private ContentValues setContentValuesInformation(Information data) {
        ContentValues values = new ContentValues();
        values.put(COL_INFORMATION_TITLE, data.getTitle());
        values.put(COL_INFORMATION_LINK, data.getLink());
        values.put(COL_INFORMATION_DESCRIPTION, data.getDescription());
        values.put(COL_INFORMATION_IMAGE, data.getImage());
        values.put(COL_INFORMATION_LANGUAGE, data.getLanguage());
        values.put(COL_INFORMATION_COPYRIGHT, data.getCopyright());
        values.put(COL_INFORMATION_TTL, data.getTtl());
        values.put(COL_INFORMATION_LAST_BUILD_DATE, data.getLastBuildDate());
        values.put(COL_INFORMATION_GENERATOR, data.getGenerator());
        values.put(COL_INFORMATION_ATOM, data.getAtom());
        return values;
    }

    private ContentValues setContentValuesPost(Data data) {
        ContentValues values = new ContentValues();
        values.put(COL_POST_TITLE, data.getTitle());
        values.put(COL_POST_DESCRIPTION, data.getDescription());
        values.put(COL_POST_LINK, data.getLink());
        values.put(COL_POST_GUID, data.getGuid());
        values.put(COL_POST_PUBDATE, data.getPubDate());
        values.put(COL_POST_CATEGORY, data.getCategory());
        values.put(COL_POST_AUTHOR, data.getAuthor());
        values.put(COL_POST_ENCLOSURE, data.getEnclosure());
        return values;
    }

    private void updateDataInformation(SQLiteDatabase sqLiteDatabase, Information data, int idInformation) {
        ContentValues values = setContentValuesInformation(data);
        int check = sqLiteDatabase.update(TBL_INFORMATION, values, COL_INFORMATION_ID + "=?", new String[]{String.valueOf(idInformation)});
        if (check > 0) {
            Toast.makeText(mContext, "insert data Information !", Toast.LENGTH_SHORT).show();
            values.clear();
        } else {
            Toast.makeText(mContext, "Error update data Information !", Toast.LENGTH_SHORT).show();
            values.clear();
        }
    }

    private void insertDataInformation(SQLiteDatabase sqLiteDatabase, Information data) {
        ContentValues values = setContentValuesInformation(data);
        long check = sqLiteDatabase.insert(TBL_INFORMATION, null, values);
        if (check == INSERT_ERROR) {
            Toast.makeText(mContext, "Error insert data Information !", Toast.LENGTH_SHORT).show();
            values.clear();
        } else {
            Toast.makeText(mContext, "Insert data Information success !", Toast.LENGTH_SHORT).show();
            values.clear();
        }
    }

    private void updateDataPost(SQLiteDatabase sqLiteDatabase, List<ListData> data) {
        List<ListData> listData = new ArrayList<>();
        ListData arrayList;
        for (ListData posts : data) {
            arrayList = new ListData();
            for (Data post : posts.getDataArrayList()) {
                boolean check = checkPostInDatabase(sqLiteDatabase, post.getTitle(), post.getCategory());
                if (!check) {
                    arrayList.setDataArrayList(post);
                } else break;
            }
            if (arrayList.getDataArrayList().size() > 0) listData.add(arrayList);
        }
        if (listData.size() > 0) insertDataPost(sqLiteDatabase, listData);
        else Toast.makeText(mContext, "Update Data Success !", Toast.LENGTH_SHORT).show();
    }

    /**
     * @param sqLiteDatabase
     * @param title
     * @param category
     * @return
     */
    private boolean checkPostInDatabase(SQLiteDatabase sqLiteDatabase, String title, String category) {
        boolean check = true;
        mCursor = sqLiteDatabase.query(TBL_POST, null, COL_POST_TITLE + " = ? and " + COL_POST_CATEGORY + " = ?", new String[]{title, category}, null, null, null, null);
        if (mCursor != null) {
            if (mCursor.getCount() > 0) {
                mCursor.close();
                check = true;
            } else {
                mCursor.close();
                check = false;
            }
        }
        return check;
    }

    private void insertDataPost(SQLiteDatabase sqLiteDatabase, List<ListData> data) {
        ContentValues values;
        int check = 0;
        for (ListData posts : data) {
            for (Data post : posts.getDataArrayList()) {
                values = setContentValuesPost(post);
                long idPost = sqLiteDatabase.insert(TBL_POST, null, values);
                if (idPost == -1) {
                    Toast.makeText(mContext, "Error insert data Post !", Toast.LENGTH_SHORT).show();
                    values.clear();
                    check = 1;
                } else {
                    insertDataPostCategory(sqLiteDatabase, post.getCategory(), post.getArrayListCategory(), idPost);
                    values.clear();
                }
            }
        }
        if (check == 0)
            Toast.makeText(mContext, "Insert data Post  success !", Toast.LENGTH_SHORT).show();
    }

    private void insertDataPostCategory(SQLiteDatabase sqLiteDatabase, String category, ArrayList<String> arrCategory, long id_post) {
        String idCategory = "";
        ContentValues values = new ContentValues();
        for (String itemCategory : arrCategory) {
            idCategory = selectIdCategoryByName(sqLiteDatabase, itemCategory);
            values.put(COL_CATEGORYPOST_ID_POST, id_post);
            values.put(COL_CATEGORYPOST_ID_CATEGORY, idCategory);
            if (itemCategory.equalsIgnoreCase(category))
                values.put(COL_CATEGORYPOST_CATEGORY_PRIMARY, 1);
            else
                values.put(COL_CATEGORYPOST_CATEGORY_PRIMARY, 0);
            long id = sqLiteDatabase.insert(TBL_CATEGORYPOST, null, values);
            if (id == -1) {
                Toast.makeText(mContext, "Error insert data Category !", Toast.LENGTH_SHORT).show();
                values.clear();
            } else
                values.clear();
        }
    }

    /**
     * @param sqLiteDatabase
     * @param name           category
     * @return ID Category
     */
    private String selectIdCategoryByName(SQLiteDatabase sqLiteDatabase, String name) {
        String idCategory = null;
        try {
            mCursor = sqLiteDatabase.query(TBL_CATEGORYPOST, null, COL_CATEGORY_NAME + " = ?", new String[]{name}, null, null, null, null);
            mCursor.moveToFirst();
            idCategory = mCursor.getString(0);
        } catch (Exception ex) {
            idCategory = "0";
        } finally {
            mCursor.close();
        }
        return idCategory;
    }

    /**
     * @return
     */
    private String returnDateNowStringFormatDDMMYYY() {
        Calendar now = Calendar.getInstance();
        return FormatDate.formatDateToString_DDMMYYY(now.getTime());
    }
}