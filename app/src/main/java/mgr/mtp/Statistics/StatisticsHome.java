package mgr.mtp.Statistics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mgr.mtp.R;

/**
 * Created by lukas on 25.02.2016.
 */
public class StatisticsHome extends Fragment {

    public StatisticsHome() {
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
        View view = inflater.inflate(R.layout.homestatistics, container, false);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        StatisticsLauncher fragment = new StatisticsLauncher();
        transaction.add(R.id.fragment_container, fragment);
        transaction.commit();


        return view;
    }

}