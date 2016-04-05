package framgia.vn.readrss.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.ArrayList;
import java.util.List;

import framgia.vn.readrss.R;
import framgia.vn.readrss.activity.fragment.FragmentListPost;
import framgia.vn.readrss.activity.fragment.FragmentMain;
import framgia.vn.readrss.controller.Connection;
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
    final static private int REPLACE = 1;
    final static private int ADD = 0;
    private List<ListData> mListPosts;
    private Information mInformation;
    private List<Data> mCategoryArrList;
    private List<LinkUrl> mUrlArrayList;
    private ReadRssAsyncTask mReadRssAsyncTask;
    private Database mDatabase = null;
    private SQLiteDatabase mSqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        getData();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
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
        int mId = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (mId == R.id.action_settings) {
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
        DrawerLayout mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(mToggle);
        mToggle.syncState();
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void getData() {
        mDatabase = new Database(this);
        //  Open connect mDatabase
        mSqLiteDatabase = Connection.connectDataBase(MainActivity.this);
        if (mSqLiteDatabase != null) {
            mDatabase.insertDataBase(mSqLiteDatabase);
            if (Connection.checkInternetConnection(MainActivity.this)) {
                getDataFromLinkRss();

            } else {
                getInformationFromDataBase();
                getPostsFromDataBase();
                setFragmentMain();
            }
        }
    }

    private void getDataFromLinkRss() {
        LinkUrl linkUrl = new LinkUrl();
        mReadRssAsyncTask = new ReadRssAsyncTask(this);
        mUrlArrayList = linkUrl.getUrlArrayList();
        mReadRssAsyncTask.execute(mUrlArrayList);
        mReadRssAsyncTask.setUpdate(new ReadRssAsyncTask.UpdateData() {
            @Override
            public boolean updateData(boolean update) {
                if (!update) return false;
                List<ListData> listPosts;
                Information information;
                information = mReadRssAsyncTask.getInformation();
                listPosts = mReadRssAsyncTask.getListPosts();
                updateInformation(information);
                updatePost(listPosts);
                getInformationFromDataBase();
                getPostsFromDataBase();
                setFragmentMain();
                return false;
            }
        });
    }

    private void getInformationFromDataBase() {
        mInformation = mDatabase.returnDataInformation();
    }

    private void getPostsFromDataBase() {
        mListPosts = mDatabase.returnDataPost();
    }

    private void setFragment(Fragment fr, int check) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (check == REPLACE) {
            fragmentTransaction.replace(R.id.fragment, fr);
        } else if (check == ADD) {
            fragmentTransaction.add(R.id.fragment, fr);
        }
        fragmentTransaction.commit();
    }

    private void setFragmentMain() {
        // get fragment manager
        setFragment(new FragmentMain(mInformation), ADD);
    }

    private void selectFrag(int idItem) {
        Fragment fr;
        switch (idItem) {
            case R.id.nav_usa:
                getArrListCategory(NAME_URL_USA);
                fr = new FragmentListPost(MainActivity.this, mCategoryArrList, NAME_URL_USA);
                break;
            case R.id.nav_africa:
                getArrListCategory(NAME_URL_AFRICA);
                fr = new FragmentListPost(MainActivity.this, mCategoryArrList, NAME_URL_AFRICA);
                break;
            case R.id.nav_asia:
                getArrListCategory(NAME_URL_ASIA);
                fr = new FragmentListPost(MainActivity.this, mCategoryArrList, NAME_URL_ASIA);
                break;
            case R.id.nav_middle_east:
                getArrListCategory(NAME_URL_MIDDLE_EAST);
                fr = new FragmentListPost(MainActivity.this, mCategoryArrList, NAME_URL_MIDDLE_EAST);
                break;
            case R.id.nav_europe:
                getArrListCategory(NAME_URL_EUROPE);
                fr = new FragmentListPost(MainActivity.this, mCategoryArrList, NAME_URL_EUROPE);
                break;
            case R.id.nav_americas:
                getArrListCategory(NAME_URL_AMERICAS);
                fr = new FragmentListPost(MainActivity.this, mCategoryArrList, NAME_URL_AMERICAS);
                break;
            case R.id.nav_science_technology:
                getArrListCategory(NAME_URL_SCIENCE_TECHNOLOGY);
                fr = new FragmentListPost(MainActivity.this, mCategoryArrList, NAME_URL_SCIENCE_TECHNOLOGY);
                break;
            case R.id.nav_economy:
                getArrListCategory(NAME_URL_ECONOMY);
                fr = new FragmentListPost(MainActivity.this, mCategoryArrList, NAME_URL_ECONOMY);
                break;
            case R.id.nav_health:
                getArrListCategory(NAME_URL_HEALTH);
                fr = new FragmentListPost(MainActivity.this, mCategoryArrList, NAME_URL_HEALTH);
                break;
            case R.id.nav_arts_entertainment:
                getArrListCategory(NAME_URL_ARTS_ENTERTAINMENT);
                fr = new FragmentListPost(MainActivity.this, mCategoryArrList, NAME_URL_ARTS_ENTERTAINMENT);
                break;
            case R.id.nav_usa_vote:
                getArrListCategory(NAME_URL_2016_USA_VOTES);
                fr = new FragmentListPost(MainActivity.this, mCategoryArrList, NAME_URL_2016_USA_VOTES);
                break;
            case R.id.nav_features:
                getArrListCategory(NAME_URL_ONE_MINUTE_FEATURES);
                fr = new FragmentListPost(MainActivity.this, mCategoryArrList, NAME_URL_ONE_MINUTE_FEATURES);
                break;
            case R.id.nav_voa_editors_picks:
                getArrListCategory(NAME_URL_VOA_EDITORS_PICKS);
                fr = new FragmentListPost(MainActivity.this, mCategoryArrList, NAME_URL_VOA_EDITORS_PICKS);
                break;
            case R.id.nav_day_in_photo:
                getArrListCategory(NAME_URL_DAY_IN_PHOTOS);
                fr = new FragmentListPost(MainActivity.this, mCategoryArrList, NAME_URL_DAY_IN_PHOTOS);
                break;
            case R.id.nav_extra_time:
                getArrListCategory(NAME_URL_SHAKA_EXTRA_TIME);
                fr = new FragmentListPost(MainActivity.this, mCategoryArrList, NAME_URL_SHAKA_EXTRA_TIME);
                break;
            case R.id.nav_visiting:
                getArrListCategory(NAME_URL_VISITING_THE_USA);
                fr = new FragmentListPost(MainActivity.this, mCategoryArrList, NAME_URL_VISITING_THE_USA);
                break;
            case R.id.nav_exit:
                fr = new FragmentMain(mInformation);
                break;
            case R.id.nav_deleteDatabase:
                mDatabase.delete_Database(mSqLiteDatabase);
                fr = new FragmentMain(mInformation);
                break;
            default:
                fr = new FragmentMain(mInformation);
                break;
        }
        setFragment(fr, REPLACE);
    }

    private void getArrListCategory(String category) {
        mCategoryArrList = new ArrayList<>();
        for (ListData posts : mListPosts) {
            if (!posts.getCategory().equalsIgnoreCase(category.trim())) continue;
            mCategoryArrList = posts.getDataArrayList();
            break;
        }
    }

    private void updateInformation(Information data) {
        mDatabase.insertOrUpdateDataInformation(data);
    }

    private void updatePost(List<ListData> data) {
        mDatabase.insertOrUpdateDataPost(data);
    }
}
