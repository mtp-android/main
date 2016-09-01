package mgr.mtp.Statistics;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import mgr.mtp.R;
import mgr.mtp.Training.TrainingHomeListAdapter;

public class StatisticsTrainingMeasures extends AppCompatActivity {

    ListView listView;
    StatisticsTrainingMeasuresListAdapter listViewAdapter;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_training_measures);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.statsTrainingListView);

        // get user id from settings
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = prefs.getInt("userId", 0);

        String[] exercises = {getString(R.string.squats),getString(R.string.benchPress), getString(R.string.barbellRow),
                getString(R.string.barbellCurls), getString(R.string.dips)};

        listViewAdapter = new StatisticsTrainingMeasuresListAdapter(StatisticsTrainingMeasures.this, exercises, userId);
        listView.setAdapter(listViewAdapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
