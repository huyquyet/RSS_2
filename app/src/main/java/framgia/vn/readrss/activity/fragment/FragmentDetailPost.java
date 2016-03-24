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
    Data item = null;
    TextView textViewName, textViewTitle, textViewDescription, textViewLink, textViewGuid,
            textViewPubDate, textViewAuthor, textViewCategory;
    ImageView imageViewEnclosure;
//    ScrollView

    public FragmentDetailPost(Data item) {
        this.item = item;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_post, container, false);
        getControl(view);
//        textViewName.setText();
        String category = "";
        for (String cate : item.getarrayListcategory()) {
            category += cate + " ";
        }
        textViewTitle.setText(item.getTitle());
        textViewDescription.setText(item.getDescription());
        textViewLink.setText(item.getLink());
        textViewPubDate.setText(item.getPubDate());
        textViewAuthor.setText(item.getAuthor());
        textViewCategory.setText(category);
        Glide.with(this).load(item.getEnclosure()).into(imageViewEnclosure);
        return view;
    }

    private void getControl(View view) {
        textViewName = (TextView) view.findViewById(R.id.textView_name);
        textViewTitle = (TextView) view.findViewById(R.id.textView_fragment_detail_post_title);
        textViewDescription = (TextView) view.findViewById(R.id.textView_fragment_detail_post_description);
        textViewLink = (TextView) view.findViewById(R.id.textView_fragment_detail_post_link);
        textViewGuid = (TextView) view.findViewById(R.id.textView_fragment_detail_post_guid);
        textViewPubDate = (TextView) view.findViewById(R.id.textView_fragment_detail_post_pubDate);
        textViewAuthor = (TextView) view.findViewById(R.id.textView_fragment_detail_post_author);
        textViewCategory = (TextView) view.findViewById(R.id.textView_fragment_detail_post_category);
        imageViewEnclosure = (ImageView) view.findViewById(R.id.imageView_fragment_detail_post_enclosure);

    }
}
