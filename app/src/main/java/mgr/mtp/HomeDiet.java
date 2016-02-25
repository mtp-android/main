package mgr.mtp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lukas on 25.02.2016.
 */
public class HomeDiet extends Fragment {

    private Toolbar toolbar;

    public HomeDiet() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.homediet, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        return view;


    }

}
