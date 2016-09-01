package mgr.mtp.Statistics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import mgr.mtp.R;

public class StatisticsBodyMeasures extends AppCompatActivity {

    TextView neckMeasure, bodyFatMeasure, waistMeasure, chestMeasure, bicepMeasure, weightMeasure,
    thighMeasure, weightLabel, bodyFatLabel, bicepLabel, chestLabel, thighLabel, neckLabel, waistLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_body_measures);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        int weight = Integer.parseInt(prefs.getString("user_weight", "1"));
        int bicep = Integer.parseInt(prefs.getString("measure_bicep", "1"));
        int chest = Integer.parseInt(prefs.getString("measure_chest", "1"));
        int waist = Integer.parseInt(prefs.getString("measure_waist", "1"));
        int thigh = Integer.parseInt(prefs.getString("measure_thigh", "1"));
        int bodyFat = prefs.getInt("bodyFat", 1);
        int neck = Integer.parseInt(prefs.getString("measure_neck", "1"));

        weightMeasure = (TextView) findViewById(R.id.weightMeasure);
        weightMeasure.setText(""+weight);

        bicepMeasure = (TextView) findViewById(R.id.bicepMeasure);
        bicepMeasure.setText(""+bicep);

        chestMeasure = (TextView) findViewById(R.id.chestMeasure);
        chestMeasure.setText(""+chest);

        waistMeasure = (TextView) findViewById(R.id.waistMeasure);
        waistMeasure.setText(""+waist);

        thighMeasure = (TextView) findViewById(R.id.thighMeasure);
        thighMeasure.setText(""+thigh);

        bodyFatMeasure = (TextView) findViewById(R.id.bodyFatMeasure);
        bodyFatMeasure.setText(""+bodyFat);

        neckMeasure = (TextView) findViewById(R.id.neckMeasure);
        neckMeasure.setText(""+neck);

        weightLabel = (TextView) findViewById(R.id.weightLabel);
        bodyFatLabel = (TextView) findViewById(R.id.bodyFatLabel);
        bicepLabel = (TextView) findViewById(R.id.bicepLabel);
        chestLabel = (TextView) findViewById(R.id.chestLabel);
        neckLabel = (TextView) findViewById(R.id.neckLabel);
        waistLabel = (TextView) findViewById(R.id.waistLabel);
        thighLabel = (TextView) findViewById(R.id.thighLabel);

        setOnClick(weightLabel, weightLabel.getText().toString(),1);
        setOnClick(bodyFatLabel, bodyFatLabel.getText().toString(),13);
        setOnClick(bicepLabel, bicepLabel.getText().toString(),3);
        setOnClick(chestLabel, chestLabel.getText().toString(),4);
        setOnClick(neckLabel, neckLabel.getText().toString(),12);
        setOnClick(waistLabel, waistLabel.getText().toString(),5);
        setOnClick(thighLabel, thighLabel.getText().toString(),6);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setOnClick(final TextView btn, final String name, final int id){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(StatisticsBodyMeasures.this, StatisticsDisplayChart.class);
                i.putExtra("statisticName", name);
                i.putExtra("typeId", id);
                StatisticsBodyMeasures.this.startActivity(i);
            }
        });
    }
}
