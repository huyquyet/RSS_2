package framgia.vn.readrss.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import framgia.vn.readrss.R;
import framgia.vn.readrss.activity.fragment.FragmentListPost;
import framgia.vn.readrss.activity.fragment.FragmentMain;
import framgia.vn.readrss.controller.Database;
import framgia.vn.readrss.controller.ReadRssAsyncTask;
import framgia.vn.readrss.models.Data;
import framgia.vn.readrss.models.Information;
import framgia.vn.readrss.models.LinkUrl;
import framgia.vn.readrss.models.ListData;
import framgia.vn.readrss.stringInterface.ConstDB;
import framgia.vn.readrss.stringInterface.Url;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConstDB, Url {
    List<ListData> listPosts = null;
    Information information = null;
    List<Data> categoryArrList = null;
    List<LinkUrl> urlArrayList = null;
    ReadRssAsyncTask readRssAsyncTask = null;
    Database database = null;
    SQLiteDatabase sqLiteDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        database = new Database(this);
        //  Open connect database
        sqLiteDatabase = connectDataBase();
        if (sqLiteDatabase != null) //  if database !+ null
            //  Insert new database
            database.insertDataBase(sqLiteDatabase);
        readRssAsyncTask = new ReadRssAsyncTask(this);
        if (checkInternetConnection()) { // Check connection Internet
            LinkUrl linkUrl = new LinkUrl();
            urlArrayList = linkUrl.getUrlArrayList();
            readRssAsyncTask.execute(urlArrayList);
            readRssAsyncTask.setUpdate(new ReadRssAsyncTask.UpdateData() {
                @Override
                public boolean updateData(boolean update) {
                    if (update) {
                        getData();
                        updateInformation(information);
                        updatePost(listPosts);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        selectFrag(id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private boolean checkInternetConnection() {
        int TypeWifi = ConnectivityManager.TYPE_WIFI;
        int TypeMobile = ConnectivityManager.TYPE_MOBILE;
        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        // Check for network connections
        if (connec.getNetworkInfo(TypeMobile).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(TypeMobile).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(TypeWifi).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(TypeWifi).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (
                connec.getNetworkInfo(TypeMobile).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(TypeWifi).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    public SQLiteDatabase connectDataBase() {
        SQLiteDatabase Database = null;
        try {
            /**
             * Mo CSDL neu ko co thi tao moi
             */
            Database = openOrCreateDatabase(NAME_DATABASE, SQLiteDatabase.CREATE_IF_NECESSARY, null);
        } catch (Exception ex) {
        }
        return Database;
    }

    private void getData() {
        this.information = readRssAsyncTask.getInformations();
        setFragment();
        this.listPosts = readRssAsyncTask.getListPosts();
    }

    private void updateInformation(Information data) {
        sqLiteDatabase = connectDataBase();
        if (sqLiteDatabase != null) {
            database.insertOrUpdateDataInformation(sqLiteDatabase, data);
        }
    }

    private void updatePost(List<ListData> data) {
        sqLiteDatabase = connectDataBase();
        if (sqLiteDatabase != null) {
            database.insertOrUpdateDataPost(sqLiteDatabase, data);
        }
    }

    private void setFragment() {
        // get fragment manager
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment, new FragmentMain(information));
        ft.commit();
    }

    private void selectFrag(int idItem) {
        Fragment fr;
        switch (idItem) {
            case R.id.nav_usa:
                getArrListCatrgory(NAME_URL_USA);
                fr = new FragmentListPost(categoryArrList, MainActivity.this, NAME_URL_USA);
                break;
            case R.id.nav_africa:
                getArrListCatrgory(NAME_URL_AFRICA);
                fr = new FragmentListPost(categoryArrList, MainActivity.this, NAME_URL_AFRICA);
                break;
            case R.id.nav_asia:
                getArrListCatrgory(NAME_URL_ASIA);
                fr = new FragmentListPost(categoryArrList, MainActivity.this, NAME_URL_ASIA);
                break;
            case R.id.nav_middle_east:
                getArrListCatrgory(NAME_URL_MIDDLE_EAST);
                fr = new FragmentListPost(categoryArrList, MainActivity.this, NAME_URL_MIDDLE_EAST);
                break;
            case R.id.nav_europe:
                getArrListCatrgory(NAME_URL_EUROPE);
                fr = new FragmentListPost(categoryArrList, MainActivity.this, NAME_URL_EUROPE);
                break;
            case R.id.nav_americas:
                getArrListCatrgory(NAME_URL_AMERICAS);
                fr = new FragmentListPost(categoryArrList, MainActivity.this, NAME_URL_AMERICAS);
                break;
            case R.id.nav_science_technology:
                getArrListCatrgory(NAME_URL_SCIENCE_TECHNOLOGY);
                fr = new FragmentListPost(categoryArrList, MainActivity.this, NAME_URL_SCIENCE_TECHNOLOGY);
                break;
            case R.id.nav_economy:
                getArrListCatrgory(NAME_URL_ECONOMY);
                fr = new FragmentListPost(categoryArrList, MainActivity.this, NAME_URL_ECONOMY);
                break;
            case R.id.nav_health:
                getArrListCatrgory(NAME_URL_HEALTH);
                fr = new FragmentListPost(categoryArrList, MainActivity.this, NAME_URL_HEALTH);
                break;
            case R.id.nav_arts_entertainment:
                getArrListCatrgory(NAME_URL_ARTS_ENTERTAINMENT);
                fr = new FragmentListPost(categoryArrList, MainActivity.this, NAME_URL_ARTS_ENTERTAINMENT);
                break;
            case R.id.nav_usa_vote:
                getArrListCatrgory(NAME_URL_2016_USA_VOTES);
                fr = new FragmentListPost(categoryArrList, MainActivity.this, NAME_URL_2016_USA_VOTES);
                break;
            case R.id.nav_features:
                getArrListCatrgory(NAME_URL_ONE_MINUTE_FEATURES);
                fr = new FragmentListPost(categoryArrList, MainActivity.this, NAME_URL_ONE_MINUTE_FEATURES);
                break;
            case R.id.nav_voa_editors_picks:
                getArrListCatrgory(NAME_URL_VOA_EDITORS_PICKS);
                fr = new FragmentListPost(categoryArrList, MainActivity.this, NAME_URL_VOA_EDITORS_PICKS);
                break;
            case R.id.nav_day_in_photo:
                getArrListCatrgory(NAME_URL_DAY_IN_PHOTOS);
                fr = new FragmentListPost(categoryArrList, MainActivity.this, NAME_URL_DAY_IN_PHOTOS);
                break;
            case R.id.nav_extra_time:
                getArrListCatrgory(NAME_URL_SHAKA_EXTRA_TIME);
                fr = new FragmentListPost(categoryArrList, MainActivity.this, NAME_URL_SHAKA_EXTRA_TIME);
                break;
            case R.id.nav_visiting:
                getArrListCatrgory(NAME_URL_VISITING_THE_USA);
                fr = new FragmentListPost(categoryArrList, MainActivity.this, NAME_URL_VISITING_THE_USA);
                break;
            case R.id.nav_exit:
                fr = new FragmentMain(information);
                break;
            case R.id.nav_deleteDatabase:
                database.delete_Database(sqLiteDatabase);
                fr = new FragmentMain(information);
                break;
            default:
                fr = new FragmentMain(information);
                break;
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fr);
        fragmentTransaction.commit();
    }

    private void getArrListCatrgory(String category) {
        categoryArrList = new ArrayList<>();
        for (ListData posts : listPosts) {
            if (posts.getCategory().equalsIgnoreCase(category.trim())) {
                categoryArrList = posts.getDataArrayList();
                break;
            }
        }
    }
}
