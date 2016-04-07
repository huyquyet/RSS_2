package framgia.vn.readrss.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.FacebookSdk;

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
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        FacebookSdk.sdkInitialize(getApplicationContext());
        getData();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment);
        if (fragmentManager.getBackStackEntryCount() > 1) fragmentManager.popBackStack();
        else {
            exitApplication();
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
//        if (mId == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        selectFrag(id);
        DrawerLayout mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert mDrawer != null;
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setToolbar() {
        DrawerLayout mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert mDrawer != null;
        mDrawer.setDrawerListener(mToggle);
        mToggle.syncState();
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        assert mNavigationView != null;
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void setUI() {
        getInformationFromDataBase();
        getPostsFromDataBase();
        setToolbar();
        setFragmentMain();
    }

    private void getData() {
        mDatabase = new Database(this);
        //  Open connect mDatabase
        mSqLiteDatabase = Connection.connectDataBase(MainActivity.this);
        if (mSqLiteDatabase != null) {
            boolean checkInsert = mDatabase.insertDataBase(mSqLiteDatabase);
            if (checkInsert) {
                if (Connection.checkInternetConnection(MainActivity.this)) getDataFromLinkRss();
            } else {
                if (checkUpdate()) {
                    if (Connection.checkInternetConnection(MainActivity.this)) {
                        getDataFromLinkRss();
                        mDatabase.updateTimeUpdate();
                    } else {
                        setUI();
                        Toast.makeText(this, R.string.toast_main_error_connect_internet, Toast.LENGTH_LONG).show();
                    }
                } else setUI();
            }
        } else
            Toast.makeText(this, R.string.toast_main_error_connect_database, Toast.LENGTH_LONG).show();
    }

    private boolean checkUpdate() {
        return mDatabase.checkTimeUpdate();
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
                setUI();
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
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (check == REPLACE) {
            fragmentTransaction.replace(R.id.fragment, fr);
        } else if (check == ADD) {
            fragmentTransaction.add(R.id.fragment, fr);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void clearBackStack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1) {
            FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(1);
            fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private void setFragmentMain() {
        // get fragment manager
        setFragment(new FragmentMain(mInformation), ADD);
    }

    private void selectFrag(int idItem) {
        Fragment fr;
        clearBackStack();
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
            case R.id.nav_exit:
                exitApplication();
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

    private void exitApplication() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_main_title);
        builder.setMessage(R.string.dialog_main_message);
        builder.setNegativeButton(R.string.dialog_main_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setPositiveButton(R.string.dialog_main_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
}
