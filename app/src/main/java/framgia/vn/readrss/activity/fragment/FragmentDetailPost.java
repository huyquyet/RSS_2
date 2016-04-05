package framgia.vn.readrss.activity.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;

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
    private ShareButton btn_share;

    public FragmentDetailPost(Data item) {
        this.mItem = item;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_detail_post, container, false);
        getControl(mView);
        StringBuilder category = new StringBuilder();
        for (String mCate : mItem.getArrayListCategory()) {
            category.append(mCate).append(" ");
        }
        mTextViewTitle.setText(mItem.getTitle());
        mTextViewDescription.setText(mItem.getDescription());
        mTextViewLink.setText(mItem.getLink());
        mTextViewPubDate.setText(mItem.getPubDate());
        mTextViewAuthor.setText(mItem.getAuthor());
        mTextViewCategory.setText(category);
        Glide.with(this).load(mItem.getEnclosure()).into(mImageViewEnclosure);
        mTextViewLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewLinkClick();
            }
        });
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle(mItem.getTitle())
                .setContentDescription(mItem.getAuthor())
                .setContentUrl(Uri.parse(mItem.getLink()))
                .setImageUrl(Uri.parse(mItem.getEnclosure()))
                .build();
        btn_share.setShareContent(linkContent);
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
        btn_share = (ShareButton) view.findViewById(R.id.share_btn);
    }

    private void textViewLinkClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_click_link);
        builder.setMessage(R.string.dialog_click_message);
        builder.setNegativeButton(R.string.dialog_click_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(R.string.dialog_click_open, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mItem.getLink()));
                startActivity(intent);
            }
        });
        builder.create().show();
    }
}
