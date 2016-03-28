package framgia.vn.readrss.activity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import framgia.vn.readrss.R;
import framgia.vn.readrss.controller.AdapterListPost;
import framgia.vn.readrss.models.Data;

public class FragmentListPost extends Fragment {
    private List<Data> mDataArrayList = new ArrayList<>();
    private Data mItemSelect = null;
    private ListView mListViewPost;
    private AdapterListPost mAdapter = null;
    private Activity mContext;
    private TextView mTextViewName;
    private String mNamelist;

    public FragmentListPost(Activity context, List<Data> dataArrayList, String nameList) {
        this.mDataArrayList = dataArrayList;
        this.mContext = context;
        this.mNamelist = nameList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_list_post, container, false);
        mTextViewName = (TextView) mView.findViewById(R.id.textView_name);
        mTextViewName.setText(mNamelist);
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
        return mView;
    }
}
