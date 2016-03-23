package framgia.vn.readrss.activity.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import framgia.vn.readrss.R;
import framgia.vn.readrss.models.Information;

public class FragmentMain extends Fragment {
    private Information informations = new Information();
    TextView textViewMainTitle, textViewMainLink,
            textViewMainDescription, textViewMainLanguage,
            textViewMainCopyright, textViewMainTtl,
            textViewMainLastBuildDate, textViewMainGenerator,
            textViewMainAtom;

    public FragmentMain(Information informations) {
        this.informations = informations;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        getControl(view);
        setDataControl(informations);
        return view;
    }

    private void getControl(View view) {
        textViewMainTitle = (TextView) view.findViewById(R.id.textView_main_title);
        textViewMainLink = (TextView) view.findViewById(R.id.textView_main_link);
        textViewMainDescription = (TextView) view.findViewById(R.id.textView_main_description);
        textViewMainLanguage = (TextView) view.findViewById(R.id.textView_main_language);
        textViewMainCopyright = (TextView) view.findViewById(R.id.textView_main_copyright);
        textViewMainTtl = (TextView) view.findViewById(R.id.textView_main_ttl);
        textViewMainLastBuildDate = (TextView) view.findViewById(R.id.textView_main_lastBuildDate);
        textViewMainGenerator = (TextView) view.findViewById(R.id.textView_main_generator);
        textViewMainAtom = (TextView) view.findViewById(R.id.textView_main_atom);
    }

    private void setDataControl(Information informations) {
        textViewMainTitle.setText(informations.getTitle());
        textViewMainLink.setText(informations.getLink());
        textViewMainDescription.setText(informations.getDescription());
        textViewMainLanguage.setText(informations.getLanguage());
        textViewMainCopyright.setText(informations.getCopyright());
        textViewMainTtl.setText(informations.getTtl());
        textViewMainLastBuildDate.setText(informations.getLastBuildDate());
        textViewMainGenerator.setText(informations.getGenerator());
        textViewMainAtom.setText(informations.getAtom());
    }

    private void update() {
        setDataControl(informations);
    }
}
