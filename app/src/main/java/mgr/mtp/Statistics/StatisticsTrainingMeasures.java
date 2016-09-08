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

        String[] exercises = new String[]{"Odwrotne rozpiętki na maszynie",
                "Podciąganie na drążku w szerokim uchwycie",
                "Prostowanie ramion w dół na wyciągu górnym",
                "Prostowanie łydek",
                "Przysiady ze sztangą",
                "Rozpiętki z hantlami w pozycji leżącej",
                "Rzymski martwy ciąg",
                "Skręty tułowia",
                "Skłony w pozycji leżącej",
                "Uginanie przedramion z hantlami",
                "Unoszenie hantli bokiem w pozycji siedzącej/stojącej",
                "Unoszenie nóg w zwisie na drążku",
                "Wiosłowanie sztangi w pozycji półprostej",
                "Wyciskanie sztangi na ławce w pozycji skośnej",
                "Wyciskanie sztangi nad głową",
                "Wyciskanie sztangi w pozycji leżącej (płasko)",
                "Wznoszenie ramion z hantlami",
                "Zginanie nadgarstków z wykorzystaniem nachwytu"};


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
