package framgia.vn.readrss.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import framgia.vn.readrss.R;
import framgia.vn.readrss.models.Information;

public class FragmentMain extends Fragment {
    private Information mInformation = new Information();
    private TextView mTextViewMainTitle, mTextViewMainLink, mTextViewMainDescription,
            mTextViewMainLanguage, mTextViewMainCopyright, mTextViewMainTtl,
            mTextViewMainLastBuildDate, mTextViewMainGenerator, mTextViewMainAtom;

    public FragmentMain(Information information) {
        this.mInformation = information;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        getControl(view);
        setDataControl(mInformation);
        return view;
    }

    private void getControl(View view) {
        mTextViewMainTitle = (TextView) view.findViewById(R.id.textView_main_title);
        mTextViewMainLink = (TextView) view.findViewById(R.id.textView_main_link);
        mTextViewMainDescription = (TextView) view.findViewById(R.id.textView_main_description);
        mTextViewMainLanguage = (TextView) view.findViewById(R.id.textView_main_language);
        mTextViewMainCopyright = (TextView) view.findViewById(R.id.textView_main_copyright);
        mTextViewMainTtl = (TextView) view.findViewById(R.id.textView_main_ttl);
        mTextViewMainLastBuildDate = (TextView) view.findViewById(R.id.textView_main_lastBuildDate);
        mTextViewMainGenerator = (TextView) view.findViewById(R.id.textView_main_generator);
        mTextViewMainAtom = (TextView) view.findViewById(R.id.textView_main_atom);
    }

    private void setDataControl(Information mInformation) {
        mTextViewMainTitle.setText(mInformation.getTitle());
        mTextViewMainLink.setText(mInformation.getLink());
        mTextViewMainDescription.setText(mInformation.getDescription());
        mTextViewMainLanguage.setText(mInformation.getLanguage());
        mTextViewMainCopyright.setText(mInformation.getCopyright());
        mTextViewMainTtl.setText(mInformation.getTtl());
        mTextViewMainLastBuildDate.setText(mInformation.getLastBuildDate());
        mTextViewMainGenerator.setText(mInformation.getGenerator());
        mTextViewMainAtom.setText(mInformation.getAtom());
    }

    private void update() {
        setDataControl(mInformation);
    }
}
