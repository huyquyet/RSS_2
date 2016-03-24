package framgia.vn.readrss.controller;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import framgia.vn.readrss.models.Category;
import framgia.vn.readrss.models.Data;
import framgia.vn.readrss.models.Information;
import framgia.vn.readrss.models.ListData;
import framgia.vn.readrss.stringInterface.ConstDB;

public class Database implements ConstDB {
    Activity context;
    Cursor cursor = null;

    public Database(Activity context) {
        this.context = context;
    }

    private boolean isDatabaseExists(SQLiteDatabase database, String tableName) {
        String query = "Select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
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
                    Toast.makeText(context, "Insert tblCategory success !", Toast.LENGTH_SHORT).show();
                    /**
                     * Create table tblInformation
                     */
                    database.execSQL(SQL_INFORMATION);
                    Toast.makeText(context, "Insert tblInformation success !", Toast.LENGTH_SHORT).show();
                    /**
                     * Create table tblPost
                     */
                    database.execSQL(SQL_POST);
                    Toast.makeText(context, "Insert tblPost success !", Toast.LENGTH_SHORT).show();
                    /**
                     * Create table tblTimeUpdate
                     */
                    database.execSQL(SQL_TIMEUPDATE);
                    Toast.makeText(context, "Insert tblTimeUpdate success !", Toast.LENGTH_SHORT).show();
                    /**
                     * Create table tblCategoryPost
                     */
                    database.execSQL(SQL_CATEGORY_POST);
                    Toast.makeText(context, "Insert tblCategoryPost success !", Toast.LENGTH_SHORT).show();
                    insertDataCategoryPost(database);
                    Toast.makeText(context, "Insert database success !", Toast.LENGTH_LONG).show();
                }   // End if (!isDatabaseExists(database, TBL_POST))
                database.close();
            }   // End if(database != null)
        } catch (Exception ex) {
            delete_Database(database);
        }
    }

    private void insertDataCategoryPost(SQLiteDatabase database) {
        Category category = new Category();
        ContentValues values = new ContentValues();
        for (Category category1 : category.getCategoryArrayList()) {
            values.put(COL_CATEGORY_NAME, category1.getName());
            long check = database.insert(TBL_CATEGORY, null, values);
            if (check == -1) {
                Toast.makeText(context, "Error insert data Category", Toast.LENGTH_SHORT).show();
                delete_Database(database);
                break;
            } else
                values.clear();
        }
        Toast.makeText(context, "Insert database CategoryPost success !", Toast.LENGTH_SHORT).show();
    }

    public void delete_Database(SQLiteDatabase database) {
        String msg = "";
        if (context.deleteDatabase(NAME_DATABASE)) {
            msg = " Delete data success !";
        } else
            msg = " Delete data error !";
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public void insertOrUpdateDataPost(SQLiteDatabase sqLiteDatabase, List<ListData> data) {
        if (checkDataPost(sqLiteDatabase)) {
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
        if (idInformation != -1) { //  Exists data in table
            updateDataInformation(sqLiteDatabase, data, idInformation);
            sqLiteDatabase.close();
        } else {
            insertDataInformation(sqLiteDatabase, data);
            sqLiteDatabase.close();
        }
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
        String limit = "20";
        cursor = sqLiteDatabase.query(TBL_POST, null, COL_POST_CATEGORY + " = ?", new String[]{category}, null, null, COL_POST_ID, limit);
        return result;
    }

    private int checkDataInformation(SQLiteDatabase sqLiteDatabase) {
        int idInformation = -1;
        cursor = sqLiteDatabase.query(TBL_INFORMATION, null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                idInformation = Integer.parseInt(cursor.getString(0));
                cursor.close();
                return idInformation;
            }
            cursor.close();
        }
        return idInformation;
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
        values.put(COL_INFORMATION_LASTBUILDATE, data.getLastBuildDate());
        values.put(COL_INFORMATION_GENERATOR, data.getGenerator());
        values.put(COL_INFORMATION_ATOM, data.getAtom());
        return values;
    }

    private void updateDataInformation(SQLiteDatabase sqLiteDatabase, Information data, int idInformation) {
        ContentValues values = setContentValuesInformation(data);
        int check = sqLiteDatabase.update(TBL_INFORMATION, values, COL_INFORMATION_ID + "=?", new String[]{String.valueOf(idInformation)});
        if (check > 0) {
            values.clear();
//            Toast.makeText(context, "Update data Information success !", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Error update data Information !", Toast.LENGTH_SHORT).show();
            values.clear();
        }
    }

    private void insertDataInformation(SQLiteDatabase sqLiteDatabase, Information data) {
        ContentValues values = setContentValuesInformation(data);
        long check = sqLiteDatabase.insert(TBL_INFORMATION, null, values);
        if (check == -1) {
            Toast.makeText(context, "Error insert data Information !", Toast.LENGTH_SHORT).show();
            values.clear();
        } else {
            Toast.makeText(context, "Insert data Information success !", Toast.LENGTH_SHORT).show();
            values.clear();
        }
    }

    /**
     * Kiem tra bang tblPost co du lieu hay ko, voi lan dau mo app
     *
     * @param sqLiteDatabase
     * @return true if data exists
     */
    private boolean checkDataPost(SQLiteDatabase sqLiteDatabase) {
        cursor = sqLiteDatabase.query(TBL_POST, null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
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
        else Toast.makeText(context, "Update Data Success !", Toast.LENGTH_SHORT).show();
    }

    private boolean checkPostInDatabase(SQLiteDatabase sqLiteDatabase, String title, String category) {
        boolean check = true;
        cursor = sqLiteDatabase.query(TBL_POST, null, COL_POST_TITLE + " = ? and " + COL_POST_CATEGORY + " = ?", new String[]{title, category}, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                check = true;
            } else {
                cursor.close();
                check = false;
            }
        }
        return check;
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

    private void insertDataPost(SQLiteDatabase sqLiteDatabase, List<ListData> data) {
        ContentValues values;
        int check = 0;
        for (ListData posts : data) {
            for (Data post : posts.getDataArrayList()) {
                values = setContentValuesPost(post);
                long idPost = sqLiteDatabase.insert(TBL_POST, null, values);
                if (idPost == -1) {
                    Toast.makeText(context, "Error insert data Post !", Toast.LENGTH_SHORT).show();
                    values.clear();
                    check = 1;
                } else {
                    insertDataPostCategory(sqLiteDatabase, post.getCategory(), post.getarrayListcategory(), idPost);
                    values.clear();
                }
            }
        }
        if (check == 0)
            Toast.makeText(context, "Insert data Post  success !", Toast.LENGTH_LONG).show();
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
                Toast.makeText(context, "Error insert data Category !", Toast.LENGTH_SHORT).show();
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
            cursor = sqLiteDatabase.query(TBL_CATEGORYPOST, null, COL_CATEGORY_NAME + " = ?", new String[]{name}, null, null, null, null);
            cursor.moveToFirst();
            idCategory = cursor.getString(0);
        } catch (Exception ex) {
            idCategory = "0";
        } finally {
            cursor.close();
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
}