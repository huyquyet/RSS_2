package framgia.vn.readrss.controller;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import framgia.vn.readrss.R;
import framgia.vn.readrss.models.Category;
import framgia.vn.readrss.models.Data;
import framgia.vn.readrss.models.Information;
import framgia.vn.readrss.models.ListData;
import framgia.vn.readrss.stringInterface.ConstDB;

public final class Database implements ConstDB {
    private static final int INSERT_ERROR = -1;
    private static final int ID_INFORMATION_ERROR = -1;
    private Activity mContext;
    private Cursor mCursor;
    private ContentValues mValues;
    private Category mCategory = new Category();

    public Database(Activity context) {
        this.mContext = context;
    }

    public void insertDataBase(SQLiteDatabase database) {
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
                    displayToast("Insert tblCategory success !");
                    /**
                     * Create table tblInformation
                     */
                    database.execSQL(SQL_INFORMATION);
                    displayToast("Insert tblInformation success !");
                    /**
                     * Create table tblPost
                     */
                    database.execSQL(SQL_POST);
                    displayToast("Insert tblPost success !");
                    /**
                     * Create table tblTimeUpdate
                     */
                    database.execSQL(SQL_TIMEUPDATE);
                    displayToast("Insert tblTimeUpdate success !");
                    /**
                     * Create table tblCategoryPost
                     */
                    database.execSQL(SQL_CATEGORY_POST);
                    displayToast("Insert tblCategoryPost success !");
                    insertDataCategoryPost(database);
                    displayToast("Insert database success !");
                }   // End if (!isDatabaseExists(database, TBL_POST))
            }   // End if(database != null)
            database.close();
        } catch (Exception ex) {
            delete_Database(database);
        }
    }

    public void delete_Database(SQLiteDatabase database) {
        if (mContext.deleteDatabase(NAME_DATABASE)) {
            displayToast("Delete data success !");
        } else
            displayToast(" Delete data error !");
    }

    public void insertOrUpdateDataPost(SQLiteDatabase sqLiteDatabase, List<ListData> data) {
        if (checkDataTable(sqLiteDatabase, TBL_POST)) {
            deletePostTimeLarger10Day(sqLiteDatabase);
            updateDataPost(sqLiteDatabase, data);
            sqLiteDatabase.close();
        } else {
            insertDataPost(sqLiteDatabase, data);
            sqLiteDatabase.close();
        }
    }

    public void insertOrUpdateDataInformation(SQLiteDatabase sqLiteDatabase, Information data) {
        int idInformation = checkDataInformation(sqLiteDatabase);
        if (idInformation != ID_INFORMATION_ERROR) {
            //  Exists data in table
            updateDataInformation(sqLiteDatabase, data, idInformation);
            sqLiteDatabase.close();
        } else {
            insertDataInformation(sqLiteDatabase, data);
            sqLiteDatabase.close();
        }
    }

    /**
     * @param sqLiteDatabase
     * @return Object Information
     */
    public Information returnDataInformation(SQLiteDatabase sqLiteDatabase) {
        Information result = new Information();
        mCursor = sqLiteDatabase.query(TBL_INFORMATION, null, null, null, null, null, null);
        if (mCursor != null) {
            if (mCursor.getCount() > 0) {
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
                return result;
            }
            mCursor.close();
        }
        return result;
    }

    /**
     * @param sqLiteDatabase
     * @return List<ListData> with 20 first record in ListData
     */
    public List<ListData> returnDataPost(SQLiteDatabase sqLiteDatabase) {
        List<ListData> result = new ArrayList<>();
        int limit = returnLimitQueryTblPost();
        for (Category cate : mCategory.getCategoryArrayList()) {
            result.add(returnPostsOfCategory(sqLiteDatabase, cate.getName(), 0, limit));
        }
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
        mCursor = sqLiteDatabase.query(TBL_POST, null, COL_POST_CATEGORY + " = ?", new String[]{category}, null, null, COL_POST_ID, "20");
//        mCursor = sqLiteDatabase.query(TBL_POST, null, COL_POST_CATEGORY + " = ?", new String[]{category}, null, null, null, null);
        mCursor.moveToFirst();
        while (!mCursor.isAfterLast()) {
            post =new Data();
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

    private void displayToast(String display) {
        Toast.makeText(mContext, display, Toast.LENGTH_SHORT).show();
    }

    private boolean isDatabaseExists(SQLiteDatabase database, String tableName) {
        String query = "Select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'";
        mCursor = database.rawQuery(query, null);
        if (mCursor != null) {
            if (mCursor.getCount() > 0) {
                mCursor.close();
                return true;
            }
            mCursor.close();
        }
        return false;
    }

    private void insertDataCategoryPost(SQLiteDatabase database) {
        mValues = new ContentValues();
        for (Category category1 : mCategory.getCategoryArrayList()) {
            mValues.put(COL_CATEGORY_NAME, category1.getName());
            long check = database.insert(TBL_CATEGORY, null, mValues);
            if (check == INSERT_ERROR) {
                displayToast("Error insert data Category");
                delete_Database(database);
                break;
            } else
                mValues.clear();
        }
        displayToast("Insert database CategoryPost success !");
    }

    private int checkDataInformation(SQLiteDatabase sqLiteDatabase) {
        int idInformation = ID_INFORMATION_ERROR;
        mCursor = sqLiteDatabase.query(TBL_INFORMATION, null, null, null, null, null, null);
        if (mCursor != null) {
            if (mCursor.getCount() > 0) {
                mCursor.moveToFirst();
                idInformation = Integer.parseInt(mCursor.getString(0));
                mCursor.close();
                return idInformation;
            }
            mCursor.close();
        }
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
        if (mCursor != null) {
            if (mCursor.getCount() > 0) {
                mCursor.close();
                return true;
            }
            mCursor.close();
        }
        return false;
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
            values.clear();
//            Toast.makeText(context, "Update data Information success !", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Error update data Information !", Toast.LENGTH_SHORT).show();
            values.clear();
        }
    }

    private void insertDataInformation(SQLiteDatabase sqLiteDatabase, Information data) {
        ContentValues values = setContentValuesInformation(data);
        long check = sqLiteDatabase.insert(TBL_INFORMATION, null, values);
        if (check == -1) {
            displayToast("Error insert data Information !");
            values.clear();
        } else {
            displayToast("Insert data Information success !");
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
        else displayToast("Update Data Success !");
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
                    displayToast("Error insert data Post !");
                    values.clear();
                    check = 1;
                } else {
                    insertDataPostCategory(sqLiteDatabase, post.getCategory(), post.getArrayListCategory(), idPost);
                    values.clear();
                }
            }
        }
        if (check == 0)
            displayToast("Insert data Post  success !");
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
                displayToast("Error insert data Category !");
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
     * Delete all Posts if pubDate >10 day
     *
     * @param sqLiteDatabase
     */
    private void deletePostTimeLarger10Day(SQLiteDatabase sqLiteDatabase) {
//        cursor = sqLiteDatabase.delete(tblPost," ")
    }

    private int returnLimitQueryTblPost() {
        int limit;
        try {
            String s_limit = String.valueOf(R.string.limit_query_tblPost);
            limit = Integer.parseInt(s_limit);
        } catch (Exception e) {
            limit = 20;
        }
        return limit;
    }
}