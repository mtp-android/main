package mgr.mtp.Statistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import mgr.mtp.R;
import mgr.mtp.Training.TrainingWorkout;
import mgr.mtp.Utils.Constants;

/**
 * Created by lmedrzycki on 13.04.2016.
 */
public class StatisticsLauncher extends Fragment {

    public StatisticsLauncher() {
        // Required empty public constructor
    }

    Button bodyMeasuresBtn, weightStatisticsBtn;
    TextView statDate;
    Date date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.statistics_launcher, container, false);


        bodyMeasuresBtn = (Button) view.findViewById(R.id.bodyMeasuresBtn);
        weightStatisticsBtn = (Button) view.findViewById(R.id.weightStatisticsBtn);
        statDate = (TextView) view.findViewById(R.id.statDate);

        Calendar cal = Calendar.getInstance();
        statDate.setText(Constants.queryDateFormat.format(cal.getTime()));

        bodyMeasuresBtn.setOnClickListener(runBodyMeasures);
        weightStatisticsBtn.setOnClickListener(runWeightStatistics);

        return view;

    }

    private View.OnClickListener runBodyMeasures = new View.OnClickListener() {

        @Override

        public void onClick(View v) {

            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            StatisticsBodyMeasures fragment = new StatisticsBodyMeasures();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }

    };

    private View.OnClickListener runWeightStatistics = new View.OnClickListener() {

        @Override

        public void onClick(View v) {

            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            StatisticsLauncher fragment = new StatisticsLauncher();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    };

}
