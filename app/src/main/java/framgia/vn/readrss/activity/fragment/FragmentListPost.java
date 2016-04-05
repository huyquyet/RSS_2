package framgia.vn.readrss.activity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import framgia.vn.readrss.R;
import framgia.vn.readrss.controller.AdapterListPost;
import framgia.vn.readrss.controller.Connection;
import framgia.vn.readrss.controller.Database;
import framgia.vn.readrss.models.Data;
import framgia.vn.readrss.models.ListData;

public class FragmentListPost extends Fragment {
    private List<Data> mDataArrayList = new ArrayList<>();
    private Data mItemSelect = null;
    private ListView mListViewPost;
    private AdapterListPost mAdapter = null;
    private Activity mContext;
    private TextView mTextViewName;
    private String mNameList;
    private Database mDatabase = new Database(mContext);
    private boolean mCheckEndScroll = false;

    public FragmentListPost(Activity context, List<Data> dataArrayList, String nameList) {
        this.mDataArrayList = dataArrayList;
        this.mContext = context;
        this.mNameList = nameList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_list_post, container, false);
        mTextViewName = (TextView) mView.findViewById(R.id.textView_name);
        mTextViewName.setText(mNameList);
        mListViewPost = (ListView) mView.findViewById(R.id.lv_post);
        mAdapter = new AdapterListPost(mContext, R.layout.custom_list_post, mDataArrayList);
        mListViewPost.setAdapter(mAdapter);
        mListViewPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mItemSelect = mDataArrayList.get(position);
                Fragment mFragment = new FragmentDetailPost(mItemSelect);
                FragmentManager mFragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, mFragment);
                fragmentTransaction.commit();
            }
        });
        mListViewPost.setOnScrollListener(new EndlessScrollListener());
        return mView;
    }

    public class EndlessScrollListener implements AbsListView.OnScrollListener {
        // The minimum amount of items to have below your current scroll position
        // before loading more.
        private int visibleThreshold = 0;
        // The total number of items in the dataset after the last load
        private int previousTotalItemCount = 0;
        // True if we are still waiting for the last set of data to load.
        private boolean loading = true;

        public EndlessScrollListener() {
        }

        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (loading && (totalItemCount > previousTotalItemCount)) {
                loading = false;
                previousTotalItemCount = totalItemCount;
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                if (mDataArrayList.size() % 10 == 0 && !mCheckEndScroll) {
                    new loadMoreData().execute(mDataArrayList.size());
                    loading = true;
                } else {
                    Toast.makeText(mContext, R.string.fragment_list_post_toast_het_du_lieu, Toast.LENGTH_SHORT).show();
                    loading = false;
                }
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }

    public class loadMoreData extends AsyncTask<Integer, Void, ListData> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ListData doInBackground(Integer... params) {
            ListData result;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SQLiteDatabase sqLiteDatabase = Connection.connectDataBase(mContext);
            int start = params[0];
            int number = 10;
            result = mDatabase.returnPostsOfCategory(sqLiteDatabase, mNameList, start, number);
            sqLiteDatabase.close();
            return result;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ListData listData) {
            super.onPostExecute(listData);
            if (listData.getDataArrayList().size() == 0) mCheckEndScroll = true;
            else {
                for (Data data : listData.getDataArrayList()) {
                    mDataArrayList.add(data);
                }
                Toast.makeText(mContext, String.valueOf(mDataArrayList.size()), Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
