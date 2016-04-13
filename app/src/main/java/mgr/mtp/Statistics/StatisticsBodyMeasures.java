package mgr.mtp.Statistics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mgr.mtp.R;

/**
 * Created by lmedrzycki on 13.04.2016.
 */
public class StatisticsBodyMeasures extends Fragment {

    public StatisticsBodyMeasures() {
        // Required empty public constructor
    }

    Button addMeasureBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.statistics_body_measures, container, false);
        addMeasureBtn = (Button) view.findViewById(R.id.addMeasureBtn);

        return view;

    }
}
