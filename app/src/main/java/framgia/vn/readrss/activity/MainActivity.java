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
import android.util.Log;
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
import framgia.vn.readrss.models.Informations;
import framgia.vn.readrss.models.LinkUrl;
import framgia.vn.readrss.models.ListData;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    List<ListData> listPosts = null;
    Informations informations = null;
    ArrayList<Data> categoryArrList = null;
    ArrayList<LinkUrl> urlArrayList = null;
    ReadRssAsyncTask readRssAsyncTask = null;
    Database database = null;
    SQLiteDatabase sqLiteDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
//                        getFragmentManager().beginTransaction().
                        getData();
                        updateInformation(informations);
                        updatePost(listPosts);
                    }
                    return false;
                }
            });
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
//            Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
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
            Database = openOrCreateDatabase("readRss.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        } catch (Exception ex) {
            Log.d("error", ex.toString());
        }
        return Database;
    }

    private void getData() {
        this.informations = readRssAsyncTask.getInformations();
        setFragment();
        this.listPosts = readRssAsyncTask.getListPosts();
    }

    private void updateInformation(Informations data) {
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
        ft.add(R.id.fragment, new FragmentMain(informations));
        ft.commit();
    }

    private void selectFrag(int idItem) {
        Fragment fr;
        switch (idItem) {
            case R.id.nav_usa:
                getArrListCatrgory("USA");
                fr = new FragmentListPost(categoryArrList, MainActivity.this, "USA");
                break;
            case R.id.nav_africa:
                getArrListCatrgory("Africa");
                fr = new FragmentListPost(categoryArrList, MainActivity.this, "Africa");
                break;
            case R.id.nav_asia:
                getArrListCatrgory("Asia");
                fr = new FragmentListPost(categoryArrList, MainActivity.this, "Asia");
                break;
            case R.id.nav_middle_east:
                getArrListCatrgory("Middle East");
                fr = new FragmentListPost(categoryArrList, MainActivity.this, "Middle East");
                break;
            case R.id.nav_europe:
                getArrListCatrgory("Europe");
                fr = new FragmentListPost(categoryArrList, MainActivity.this, "Europe");
                break;
            case R.id.nav_americas:
                getArrListCatrgory("Americas");
                fr = new FragmentListPost(categoryArrList, MainActivity.this, "Americas");
                break;
            case R.id.nav_science_technology:
                getArrListCatrgory("Science & Technology");
                fr = new FragmentListPost(categoryArrList, MainActivity.this, "Science & Technology");
                break;
            case R.id.nav_economy:
                getArrListCatrgory("Economy");
                fr = new FragmentListPost(categoryArrList, MainActivity.this, "Economy");
                break;
            case R.id.nav_health:
                getArrListCatrgory("Health");
                fr = new FragmentListPost(categoryArrList, MainActivity.this, "Health");
                break;
            case R.id.nav_arts_entertainment:
                getArrListCatrgory("Arts & Entertainment");
                fr = new FragmentListPost(categoryArrList, MainActivity.this, "Arts & Entertainment");
                break;
            case R.id.nav_usa_vote:
                getArrListCatrgory("2016 USA Votes");
                fr = new FragmentListPost(categoryArrList, MainActivity.this, "2016 USA Votes");
                break;
            case R.id.nav_features:
                getArrListCatrgory("One-Minute Features");
                fr = new FragmentListPost(categoryArrList, MainActivity.this, "One-Minute Features");
                break;
            case R.id.nav_voa_editors_picks:
                getArrListCatrgory("VOA Editors Picks");
                fr = new FragmentListPost(categoryArrList, MainActivity.this, "VOA Editors Picks");
                break;
            case R.id.nav_day_in_photo:
                getArrListCatrgory("Day in Photos");
                fr = new FragmentListPost(categoryArrList, MainActivity.this, "Day in Photos");
                break;
            case R.id.nav_extra_time:
                getArrListCatrgory("Shaka: Extra Time");
                fr = new FragmentListPost(categoryArrList, MainActivity.this, "Shaka: Extra Time");
                break;
            case R.id.nav_visiting:
                getArrListCatrgory("Visiting the USA");
                fr = new FragmentListPost(categoryArrList, MainActivity.this, "Visiting the USA");
                break;
            case R.id.nav_exit:
                fr = new FragmentMain(informations);
                break;
            case R.id.nav_deleteDatabase:
                database.delete_Database(sqLiteDatabase);
                fr = new FragmentMain(informations);
                break;
            default:
                fr = new FragmentMain(informations);
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
