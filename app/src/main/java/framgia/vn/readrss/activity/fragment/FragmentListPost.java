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

import framgia.vn.readrss.R;
import framgia.vn.readrss.controller.AdapterListPost;
import framgia.vn.readrss.models.Data;

/**
 * Created by FRAMGIA\nguyen.huy.quyet on 17/03/2016.
 */
public class FragmentListPost extends Fragment {
    private ArrayList<Data> dataArrayList = new ArrayList<>();
    private Data itemSelect = null;
    private ListView lv_post;
    private AdapterListPost adapter = null;
    private Activity context;
    private TextView textViewName;
    private String namelist;

    public FragmentListPost(ArrayList<Data> dataArrayList, Activity context, String name_List) {
        this.dataArrayList = dataArrayList;
        this.context = context;
        this.namelist = name_List;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_post, container, false);
        textViewName = (TextView) view.findViewById(R.id.textView_name);
        textViewName.setText(namelist);
        lv_post = (ListView) view.findViewById(R.id.lv_post);
        adapter = new AdapterListPost(context, R.layout.custom_list_post, dataArrayList);
        lv_post.setAdapter(adapter);

        lv_post.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemSelect = dataArrayList.get(position);
                Fragment fr = new FragmentDetailPost(itemSelect);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, fr);
                fragmentTransaction.commit();
            }
        });
        return view;
    }


}
