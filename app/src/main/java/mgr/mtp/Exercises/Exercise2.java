package mgr.mtp.Exercises;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.persistence.ContextVariable;

import java.util.ArrayList;

import mgr.mtp.DataModel.ExerciseSet;
import mgr.mtp.R;
import mgr.mtp.Utils.ProgressWheel;

/**
 * Created by lmedrzycki on 05.04.2016.
 */
public class Exercise2 extends WizardStep {

    ProgressWheel pw;
    Toolbar toolbar;
    Button btnStart;
    EditText ex2_firstSetWeightET, ex2_firstSetRepsET, ex2_secondSetWeightET, ex2_secondSetRepsET,
            ex2_thirdSetWeightET, ex2_thirdSetRepsET, ex2_fourthSetWeightET, ex2_fourthSetRepsET,
            ex2_fifthSetWeightET, ex2_fifthSetRepsET;
    TextView restLabel;
    long timeRemaining;
    private boolean isPaused = false;
    private boolean isCanceled = false;
    int timerCounter = 1;

    @ContextVariable
    private ArrayList<ExerciseSet> exerciseOne;

    @ContextVariable
    private ArrayList<ExerciseSet> exerciseTwo;

    //Wire the layout to the step
    public Exercise2() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.training_wizard, container, false);

        exerciseTwo = new ArrayList<>();

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.benchPress);

        ex2_firstSetRepsET = (EditText) v.findViewById(R.id.firstSet_reps);
        ex2_firstSetWeightET = (EditText) v.findViewById(R.id.firstSet_weight);

        ex2_secondSetRepsET = (EditText) v.findViewById(R.id.secondSet_reps);
        ex2_secondSetWeightET = (EditText) v.findViewById(R.id.secondSet_weight);

        ex2_thirdSetRepsET = (EditText) v.findViewById(R.id.thirdSet_reps);
        ex2_thirdSetWeightET = (EditText) v.findViewById(R.id.thirdSet_weight);

        ex2_fourthSetRepsET = (EditText) v.findViewById(R.id.fourthSet_reps);
        ex2_fourthSetWeightET = (EditText) v.findViewById(R.id.fourthSet_weight);

        ex2_fifthSetRepsET = (EditText) v.findViewById(R.id.fifthSet_reps);
        ex2_fifthSetWeightET = (EditText) v.findViewById(R.id.fifthSet_weight);

        restLabel = (TextView) v.findViewById(R.id.restLabel);
        updateRestLabel(timerCounter);

        pw = (ProgressWheel) v.findViewById(R.id.pw_spinner);
        pw.setProgress(360);

        btnStart = (Button) v.findViewById(R.id.restButton);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (timerCounter == 5) {
                    Toast.makeText(getActivity(),
                            getString(R.string.trainig_gotonextexercise), Toast.LENGTH_LONG).show();
                } else {
                    isPaused = false;
                    isCanceled = false;

                    btnStart.setEnabled(false);

                    CountDownTimer timer;
                    long millisInFuture = 90000; //90 seconds
                    long countDownInterval = 1000; //1 second

                    timer = new CountDownTimer(millisInFuture, countDownInterval) {
                        public void onTick(long millisUntilFinished) {
                            if (isPaused || isCanceled) {
                                cancel();
                            } else {

                                long value = millisUntilFinished / 1000;
                                pw.setProgress((int) (value) * 4);
                                pw.setText("" + value);
                                timeRemaining = millisUntilFinished;
                            }
                        }

                        public void onFinish() {
                            final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.alert);
                            mp.start();
                            timerCounter++;
                            updateRestLabel(timerCounter);
                            pw.setProgress(360);
                            pw.setText("90");
                            btnStart.setEnabled(true);
                        }
                    }.start();
                }
            }
        });

        return v;
    }

    @Override
    public void onExit(int exitCode) {
        switch (exitCode) {
            case WizardStep.EXIT_NEXT:
                bindDataFields();
                break;
            case WizardStep.EXIT_PREVIOUS:
                break;
        }
    }

    private void updateRestLabel(int timerCounter) {
        restLabel.setText("Przerwa pomiędzy seriami " + timerCounter + "/5");
    }

    private void bindDataFields() {
        //Do some work
        //...
        //The values of these fields will be automatically stored in the wizard context
        //and will be populated in the next steps only if the same field names are used.

        exerciseTwo.add(new ExerciseSet(1, Integer.parseInt(ex2_firstSetWeightET.getText().toString())
                , Integer.parseInt(ex2_firstSetRepsET.getText().toString()), 1));
        exerciseTwo.add(new ExerciseSet(1, Integer.parseInt(ex2_secondSetWeightET.getText().toString())
                , Integer.parseInt(ex2_secondSetRepsET.getText().toString()), 2));
        exerciseTwo.add(new ExerciseSet(1, Integer.parseInt(ex2_thirdSetWeightET.getText().toString())
                , Integer.parseInt(ex2_thirdSetRepsET.getText().toString()), 3));
        exerciseTwo.add(new ExerciseSet(1, Integer.parseInt(ex2_fourthSetWeightET.getText().toString())
                , Integer.parseInt(ex2_fourthSetRepsET.getText().toString()), 4));
        exerciseTwo.add(new ExerciseSet(1, Integer.parseInt(ex2_fifthSetWeightET.getText().toString())
                , Integer.parseInt(ex2_fifthSetRepsET.getText().toString()), 5));
    }
}