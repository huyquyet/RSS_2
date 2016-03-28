package framgia.vn.readrss.controller;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import framgia.vn.readrss.R;
import framgia.vn.readrss.models.Data;

public class AdapterListPost extends ArrayAdapter<Data> {
    Activity mContext;
    int mResource;
    List<Data> mArrayList;

    public AdapterListPost(Context context, int resource, List<Data> arrayList) {
        super(context, resource, arrayList);
        this.mContext = (Activity) context;
        this.mResource = resource;
        this.mArrayList = arrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        convertView = inflater.inflate(mResource, null);
        Data item = mArrayList.get(position);
        TextView textViewTitle = (TextView) convertView.findViewById(R.id.textView_title);
        TextView textViewDate = (TextView) convertView.findViewById(R.id.textView_date);
        TextView textViewCategory = (TextView) convertView.findViewById(R.id.textView_category);
        String category = "";
        for (String data : item.getArrayListCategory()) {
            category += (data + " - ");
        }
        textViewTitle.setText(item.getTitle());
        textViewDate.setText(item.getPubDate());
        textViewCategory.setText(category);
        return convertView;
    }
}