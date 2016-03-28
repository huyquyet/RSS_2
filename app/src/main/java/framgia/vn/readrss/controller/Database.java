package framgia.vn.readrss.controller;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.Locale;

import framgia.vn.readrss.models.Category;
import framgia.vn.readrss.stringInterface.ConstDB;

public class Database implements ConstDB {
    private static final int INSERT_ERROR = -1;
    Activity mContext;
    Cursor mCursor;
    ContentValues mValues;

    public Database(Activity context) {
        this.mContext = context;
    }

    public SQLiteDatabase connectDataBase() {
        SQLiteDatabase Database = null;
        try {
            /**
             * Mo CSDL neu ko co thi tao moi
             */
            Database = mContext.openOrCreateDatabase(NAME_DATABASE, SQLiteDatabase.CREATE_IF_NECESSARY, null);
        } catch (Exception ex) {
        }
        return Database;
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
                database.close();
            }   // End if(database != null)
        } catch (Exception ex) {
            delete_Database(database);
        }
    }

    private void insertDataCategoryPost(SQLiteDatabase database) {
        Category category = new Category();
        mValues = new ContentValues();
        for (Category category1 : category.getCategoryArrayList()) {
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

    public void delete_Database(SQLiteDatabase database) {
        if (mContext.deleteDatabase(NAME_DATABASE)) {
            displayToast("Delete data success !");
        } else
            displayToast(" Delete data error !");
    }

    private void displayToast(String display) {
        Toast.makeText(mContext, display, Toast.LENGTH_SHORT).show();
    }
}