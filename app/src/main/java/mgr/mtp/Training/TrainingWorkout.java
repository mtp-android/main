package mgr.mtp.Training;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import mgr.mtp.R;

/**
 * Created by lmedrzycki on 05.04.2016.
 */
public class TrainingWorkout extends FragmentActivity {

    String date;
    int trainingSetId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_workout);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            date = extras.getString("date");
            trainingSetId = extras.getInt("trainingSetId");
        }

    }

    public String getDate() {
        return date;
    }

    public int getTrainingSetId() {
        return trainingSetId;
    }
}