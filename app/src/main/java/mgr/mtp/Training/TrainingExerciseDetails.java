package mgr.mtp.Training;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import mgr.mtp.DataModel.ExerciseSet;
import mgr.mtp.R;

public class TrainingExerciseDetails extends AppCompatActivity {

    String date, exerciseName;
    int userId;
    ArrayList<Integer> weights;
    TextView trainigDate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_exercise_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            date = extras.getString("date");
            exerciseName = extras.getString("exerciseName");
            weights = extras.getIntegerArrayList("weights");
            userId = extras.getInt("userId");
        }

        trainigDate = (TextView) findViewById(R.id.trainingDate);

        toolbar.setTitle(exerciseName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        trainigDate.setText(date);
    }

}
