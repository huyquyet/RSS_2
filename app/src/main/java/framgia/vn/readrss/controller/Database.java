package framgia.vn.readrss.controller;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import framgia.vn.readrss.models.Category;
import framgia.vn.readrss.models.Data;
import framgia.vn.readrss.models.Informations;
import framgia.vn.readrss.models.ListData;

/**
 * Created by FRAMGIA\nguyen.huy.quyet on 18/03/2016.
 */
public class Database {
    Activity context;
    Cursor cursor = null;

    public Database(Activity context) {
        this.context = context;
    }
//    private SQLiteDatabase database = null;

    private boolean isDatabaseExists(SQLiteDatabase database, String tableName) {
        Cursor cursor = database.rawQuery("Select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
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
                if (!isDatabaseExists(database, "tblPost")) {
                    /**
                     * ko ton tai bang => tao moi CSDL trong
                     * bat dau tao moi cac bang
                     */
                    database.setLocale(Locale.getDefault());
                    database.setVersion(1);
                    /**
                     * Create table category
                     */
                    String sqlCategory = "create table tblCategory (" +
                            "id integer primary key autoincrement," +
                            "name)";
                    database.execSQL(sqlCategory);
                    Toast.makeText(context, "Insert tblCategory success !", Toast.LENGTH_SHORT).show();

                    /**
                     * Create table tblInformation
                     */
                    String sqlInformations = "create table tblInformation (" +
                            "id integer primary key autoincrement, " +
                            "title text, " +
                            "link text, " +
                            "description text, " +
                            "image text, " +
                            "language text, " +
                            "copyright text, " +
                            "ttl text, " +
                            "lastBuildDate text," +
                            "generator text, " +
                            "atom text)";
                    database.execSQL(sqlInformations);
                    Toast.makeText(context, "Insert tblInformation success !", Toast.LENGTH_SHORT).show();

                    /**
                     * Create table tblPost
                     */
                    String sqlPost = "create table tblPost (" +
                            "id integer primary key autoincrement, " +
                            "title text, " +
                            "description text, " +
                            "link text, " +
                            "guid text, " +
                            "pubDate text, " +
                            "category text, " +
                            "author text, " +
                            "enclosure text)";
                    database.execSQL(sqlPost);
                    Toast.makeText(context, "Insert tblPost success !", Toast.LENGTH_SHORT).show();

                    /**
                     * Create table tblTimeUpdate
                     */
                    String sqlTimeUpdate = " create table tblTimeUpdate (" +
                            "time datetime)";
                    database.execSQL(sqlTimeUpdate);
                    Toast.makeText(context, "Insert tblTimeUpdate success !", Toast.LENGTH_SHORT).show();

                    /**
                     * Create table tblCategoryPost
                     */
                    String sqlCategory_post = "create table tblCategoryPost(" +
                            "id_post integer not null constraint id_post references tblPost(id) on delete cascade," +
                            "id_category integer not null constraint id_category references tblCategory(id) on delete cascade," +
                            "category_primary integer)";

                    database.execSQL(sqlCategory_post);
                    Toast.makeText(context, "Insert tblCategoryPost success !", Toast.LENGTH_SHORT).show();
                    Category category = new Category();
                    ContentValues values = new ContentValues();
                    for (Category category1 : category.getCategoryArrayList()) {
                        values.put("name", category1.getName());
                        long check = database.insert("tblCategory", null, values);
                        if (check == -1) {
                            Toast.makeText(context, "Error insert data Category", Toast.LENGTH_SHORT).show();
                            delete_Database(database);
                            break;
                        } else
                            values.clear();

                    }
                    Toast.makeText(context, "Insert database CategoryPost success !", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Insert database success !", Toast.LENGTH_LONG).show();
                }   // End if (!isDatabaseExists(database, "tblPost"))
                database.close();
            }   // End if(database != null)
        } catch (Exception ex) {
            Log.d("error", ex.toString());
            delete_Database(database);
        }
    }

    public void delete_Database(SQLiteDatabase database) {
        String msg = "";
        if (context.deleteDatabase("readRss.db")) {
            msg = " Delete data success !";
        } else
            msg = " Delete data error !";
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

//    public void insertOrUpdateDataPost(SQLiteDatabase sqLiteDatabase, ArrayList<Data> data) {
//        if (checkDataPost(sqLiteDatabase)) {
//            updateDataPost(sqLiteDatabase, data);
//            sqLiteDatabase.close();
//        } else {
//            insertDataPost(sqLiteDatabase, data);
//            sqLiteDatabase.close();
//        }
//    }

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

    public void insertOrUpdateDataInformation(SQLiteDatabase sqLiteDatabase, Informations data) {
        int id_information = checkDataInformation(sqLiteDatabase);
        if (id_information != -1) { //  Exists data in table
            updateDataInformation(sqLiteDatabase, data, id_information);
            sqLiteDatabase.close();
        } else {
            insertDataInformation(sqLiteDatabase, data);
            sqLiteDatabase.close();
        }
    }

    public ListData returnPostsOfCategory(SQLiteDatabase sqLiteDatabase, String category, int start, int number) {
        ListData result = new ListData();
//        cursor
        return result;
    }

    private int checkDataInformation(SQLiteDatabase sqLiteDatabase) {
        int id_information = -1;
        cursor = sqLiteDatabase.query("tblInformation", null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id_information = Integer.parseInt(cursor.getString(0));
                cursor.close();
                return id_information;
            }
            cursor.close();
        }
        return id_information;
    }

    private void updateDataInformation(SQLiteDatabase sqLiteDatabase, Informations data, int id_information) {
        ContentValues values = new ContentValues();
        values.put("title", data.getTitle());
        values.put("link", data.getLink());
        values.put("description", data.getDescription());
        values.put("image", data.getImage());
        values.put("language", data.getLanguage());
        values.put("copyright", data.getCopyright());
        values.put("ttl", data.getTtl());
        values.put("lastBuildDate", data.getLastBuildDate());
        values.put("generator", data.getGenerator());
        values.put("atom", data.getAtom());
        int check = sqLiteDatabase.update("tblInformation", values, "id=?", new String[]{String.valueOf(id_information)});
        if (check > 0) {
            values.clear();
//            Toast.makeText(context, "Update data Information success !", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Error update data Information !", Toast.LENGTH_SHORT).show();
            values.clear();
        }
    }

    private void insertDataInformation(SQLiteDatabase sqLiteDatabase, Informations data) {
        ContentValues values = new ContentValues();
        values.put("title", data.getTitle());
        values.put("link", data.getLink());
        values.put("description", data.getDescription());
        values.put("image", data.getImage());
        values.put("language", data.getLanguage());
        values.put("copyright", data.getCopyright());
        values.put("ttl", data.getTtl());
        values.put("lastBuildDate", data.getLastBuildDate());
        values.put("generator", data.getGenerator());
        values.put("atom", data.getAtom());
        long check = sqLiteDatabase.insert("tblInformation", null, values);
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
        cursor = sqLiteDatabase.query("tblPost", null, null, null, null, null, null);
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
        ListData arrayList = null;
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
        cursor = sqLiteDatabase.query("tblPost", null, "title = ? and category = ?", new String[]{title, category}, null, null, null, null);
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

    private void insertDataPost(SQLiteDatabase sqLiteDatabase, List<ListData> data) {
        ContentValues values = new ContentValues();
        int check = 0;
        for (ListData posts : data) {
            for (Data post : posts.getDataArrayList()) {
                values.put("title", post.getTitle());
                values.put("description", post.getDescription());
                values.put("link", post.getLink());
                values.put("guid", post.getGuid());
                values.put("pubDate", post.getPubDate());
                values.put("category", post.getCategory());
                values.put("author", post.getAuthor());
                values.put("enclosure", post.getEnclosure());
                long id_post = sqLiteDatabase.insert("tblPost", null, values);
                if (id_post == -1) {
                    Toast.makeText(context, "Error insert data Post !", Toast.LENGTH_SHORT).show();
                    values.clear();
                    check = 1;
                } else {
                    insertDataPostCategory(sqLiteDatabase, post.getCategory(), post.getarrayListcategory(), id_post);
                    values.clear();
                }
            }
        }
        if (check == 0)
            Toast.makeText(context, "Insert data Post  success !", Toast.LENGTH_LONG).show();

    }

    private void insertDataPostCategory(SQLiteDatabase sqLiteDatabase, String category, ArrayList<String> arrCategory, long id_post) {
        String id_category = null;
        ContentValues values = new ContentValues();
        for (String item_category : arrCategory) {
            id_category = selectIdCategoryByName(sqLiteDatabase, item_category);
            values.put("id_post", id_post);
            values.put("id_category", id_category);
            if (item_category.equalsIgnoreCase(category))
                values.put("category_primary", 1);
            else
                values.put("category_primary", 0);
            long id = sqLiteDatabase.insert("tblCategoryPost", null, values);
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
        String id_category = null;
        try {
            cursor = sqLiteDatabase.query("tblCategoryPost", null, "name = ?", new String[]{name}, null, null, null, null);
            cursor.moveToFirst();
            id_category = cursor.getString(0);
        } catch (Exception ex) {
            id_category = "0";
        } finally {
            cursor.close();
        }
        return id_category;
    }

    /**
     * Delete all Posts if pubDate >10 day
     *
     * @param sqLiteDatabase
     */
    private void deletePostTimeLarger10Day(SQLiteDatabase sqLiteDatabase) {
//        cursor = sqLiteDatabase.delete("tblPost","")
    }
}