package framgia.vn.readrss.activity.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    Button mButtonShareFacebook;
    ShareButton btn_share;

    public FragmentDetailPost(Data item) {
        this.mItem = item;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.M)
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
        Glide.with(this).load(mItem.getEnclosure()).into(mImageViewEnclosure);
        mTextViewLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextViewLinkClick();
            }
        });
        mButtonShareFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ShareLinkContent content = new ShareLinkContent.Builder()
//                        .setContentUrl(Uri.parse(mItem.getLink()))
//                        .build();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//                Uri screenshotUri = Uri.parse(mItem.getLink());
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, mItem.getLink());
                startActivity(Intent.createChooser(sharingIntent, "Share image using"));
                Toast.makeText(getActivity(), "Share", Toast.LENGTH_LONG).show();
            }
        });
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(mItem.getLink()))
                        .build();*/
                Toast.makeText(getContext(), "Share", Toast.LENGTH_LONG).show();
            }
        });
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
        mButtonShareFacebook = (Button) view.findViewById(R.id.button_share_facebook);
        btn_share = (ShareButton) view.findViewById(R.id.share_btn);
    }

    private void TextViewLinkClick() {
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
