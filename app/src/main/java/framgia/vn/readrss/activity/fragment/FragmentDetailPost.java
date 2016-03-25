package framgia.vn.readrss.activity.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import framgia.vn.readrss.R;
import framgia.vn.readrss.models.Data;

/**
 * Created by FRAMGIA\nguyen.huy.quyet on 17/03/2016.
 */
public class FragmentDetailPost extends Fragment {
    private Data mItem;
    private TextView mTextViewName, mTextViewTitle, mTextViewDescription, mTextViewLink, mTextViewGuid,
            mTextViewPubDate, mTextViewAuthor, mTextViewCategory;
    private ImageView mImageViewEnclosure;

    public FragmentDetailPost(Data item) {
        this.mItem = item;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_detail_post, container, false);
        getControl(mView);
        String mCategory = "";
        for (String mCate : mItem.getArrayListCategory()) {
            mCategory += mCate + " ";
        }
        mTextViewTitle.setText(mItem.getTitle());
        mTextViewDescription.setText(mItem.getDescription());
        mTextViewLink.setText(mItem.getLink());
        mTextViewPubDate.setText(mItem.getPubDate());
        mTextViewAuthor.setText(mItem.getAuthor());
        mTextViewCategory.setText(mCategory);
//        Glide.with(this).load(mItem.getEnclosure()).into(mImageViewEnclosure);
        return mView;
    }

    private void getControl(View view) {
        mTextViewName = (TextView) view.findViewById(R.id.textView_name);
        mTextViewTitle = (TextView) view.findViewById(R.id.textView_fragment_detail_post_title);
        mTextViewDescription = (TextView) view.findViewById(R.id.textView_fragment_detail_post_description);
        mTextViewLink = (TextView) view.findViewById(R.id.textView_fragment_detail_post_link);
        mTextViewGuid = (TextView) view.findViewById(R.id.textView_fragment_detail_post_guid);
        mTextViewPubDate = (TextView) view.findViewById(R.id.textView_fragment_detail_post_pubDate);
        mTextViewAuthor = (TextView) view.findViewById(R.id.textView_fragment_detail_post_author);
        mTextViewCategory = (TextView) view.findViewById(R.id.textView_fragment_detail_post_category);
        mImageViewEnclosure = (ImageView) view.findViewById(R.id.imageView_fragment_detail_post_enclosure);

    }
}
