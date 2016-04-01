package framgia.vn.readrss.controller;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;

import framgia.vn.readrss.stringInterface.ConstDB;

/**
 * Created by FRAMGIA\nguyen.huy.quyet on 28/03/2016.
 */
public final class Connection implements ConstDB {
    public static boolean checkInternetConnection(Activity context) {
        int TypeWifi = ConnectivityManager.TYPE_WIFI;
        int TypeMobile = ConnectivityManager.TYPE_MOBILE;
        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(context.getBaseContext().CONNECTIVITY_SERVICE);
        // Check for network connections
        if (connec.getNetworkInfo(TypeMobile).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(TypeWifi).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (
                connec.getNetworkInfo(TypeMobile).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(TypeWifi).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    public static SQLiteDatabase connectDataBase(Activity context) {
        SQLiteDatabase database = null;
        try {
            /**
             * Mo CSDL neu ko co thi tao moi
             */
            database = context.openOrCreateDatabase(NAME_DATABASE, SQLiteDatabase.CREATE_IF_NECESSARY, null);
        } catch (Exception ex) {
        }
        return database;
    }
}
