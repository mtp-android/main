package mgr.mtp.Exercises;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.persistence.ContextVariable;

import java.util.ArrayList;

import mgr.mtp.DataModel.ExerciseSet;
import mgr.mtp.R;
import mgr.mtp.Training.TrainingWorkout;
import mgr.mtp.Utils.ProgressWheel;

/**
 * Created by lmedrzycki on 05.04.2016.
 */
public class Exercise4 extends WizardStep {

    ProgressWheel pw;
    Toolbar toolbar;
    Button btnStart;
    TextView ex4_firstSetWeightET, ex4_firstSetRepsET, ex4_secondSetWeightET, ex4_secondSetRepsET,
            ex4_thirdSetWeightET, ex4_thirdSetRepsET, ex4_fourthSetWeightET, ex4_fourthSetRepsET,
            ex4_fifthSetWeightET, ex4_fifthSetRepsET;
    TextView restLabel;
    long timeRemaining;
    private boolean isPaused = false;
    private boolean isCanceled = false;
    int timerCounter = 1;
    ImageView editOne, editTwo, editThree, editFour, editFive;
    int set1max,set2max,set3max,set4max,set5max;
    int trainingSetId;

    @ContextVariable
    private ArrayList<ExerciseSet> exerciseOne;

    @ContextVariable
    private ArrayList<ExerciseSet> exerciseTwo;

    @ContextVariable
    private ArrayList<ExerciseSet> exerciseThree;

    @ContextVariable
    private ArrayList<ExerciseSet> exerciseFour;

    //Wire the layout to the step
    public Exercise4() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.training_wizard, container, false);

        exerciseFour = new ArrayList<>();

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle("Ćwiczenie 4 z 6");
        TextView exName = (TextView) v.findViewById(R.id.exName);

        Activity trainingWorkout = getActivity();
        trainingSetId = 0;


        if (trainingWorkout instanceof TrainingWorkout) {
            trainingSetId = ((TrainingWorkout) trainingWorkout).getTrainingSetId();
        }

        String name = "";

        switch (trainingSetId) {
            case 1:
                name = "Wyciskanie sztangi nad głową";
                break;

            case 2:
                name = "Unoszenie hantli bokiem w pozycji siedzącej/stojącej\n";
                break;

            case 3:
                name = "Odwrotne rozpiętki na maszynie";
                break;

            default:
                break;
        }

        exName.setText(name);

        ex4_firstSetRepsET = (TextView) v.findViewById(R.id.firstSet_reps);
        ex4_firstSetWeightET = (TextView) v.findViewById(R.id.firstSet_weight);

        ex4_secondSetRepsET = (TextView) v.findViewById(R.id.secondSet_reps);
        ex4_secondSetWeightET = (TextView) v.findViewById(R.id.secondSet_weight);

        ex4_thirdSetRepsET = (TextView) v.findViewById(R.id.thirdSet_reps);
        ex4_thirdSetWeightET = (TextView) v.findViewById(R.id.thirdSet_weight);

        ex4_fourthSetRepsET = (TextView) v.findViewById(R.id.fourthSet_reps);
        ex4_fourthSetWeightET = (TextView) v.findViewById(R.id.fourthSet_weight);

        ex4_fifthSetRepsET = (TextView) v.findViewById(R.id.fifthSet_reps);
        ex4_fifthSetWeightET = (TextView) v.findViewById(R.id.fifthSet_weight);

        getWeightFromPreferences();

        ex4_firstSetWeightET.setText(""+set1max);
        ex4_secondSetWeightET.setText(""+set2max);
        ex4_thirdSetWeightET.setText(""+set3max);
        ex4_fourthSetWeightET.setText(""+set4max);
        ex4_fifthSetWeightET.setText(""+set5max);

        editOne = (ImageView) v.findViewById(R.id.editone);
        editTwo = (ImageView) v.findViewById(R.id.edittwo);
        editThree = (ImageView) v.findViewById(R.id.editthree);
        editFour = (ImageView) v.findViewById(R.id.editfour);
        editFive = (ImageView) v.findViewById(R.id.editfive);

        editOne.setOnClickListener(showNumberPickers);
        editTwo.setOnClickListener(showNumberPickers);
        editThree.setOnClickListener(showNumberPickers);
        editFour.setOnClickListener(showNumberPickers);
        editFive.setOnClickListener(showNumberPickers);

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
                    btnStart.setText("Odliczam...");

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
                            btnStart.setText("Odpoczynek");
                        }
                    }.start();
                }
            }
        });

        return v;
    }

    View.OnClickListener showNumberPickers = new View.OnClickListener() {
        public void onClick(View v) {

            final int id = v.getId();
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View theView = inflater.inflate(R.layout.training_weight_numberpicker_dialog, null);

            final NumberPicker reps = (NumberPicker) theView.findViewById(R.id.reps_picker);
            reps.setWrapSelectorWheel(true);
            reps.setMinValue(5);
            reps.setMaxValue(15);

            final NumberPicker weight = (NumberPicker) theView.findViewById(R.id.weight_picker);
            weight.setWrapSelectorWheel(true);
            weight.setMinValue(5);
            weight.setMaxValue(150);

            builder.setView(theView)
                    .setPositiveButton(R.string.confirmed, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            int repsValue = reps.getValue();
                            int weightValue = weight.getValue();

                            switch (id) {
                                case R.id.editone:
                                    ex4_firstSetRepsET.setText(""+repsValue);
                                    ex4_firstSetWeightET.setText(""+weightValue);
                                    break;
                                case R.id.edittwo:
                                    ex4_secondSetRepsET.setText(""+repsValue);
                                    ex4_secondSetWeightET.setText(""+weightValue);
                                    break;
                                case R.id.editthree:
                                    ex4_thirdSetRepsET.setText(""+repsValue);
                                    ex4_thirdSetWeightET.setText(""+weightValue);
                                    break;
                                case R.id.editfour:
                                    ex4_fourthSetRepsET.setText(""+repsValue);
                                    ex4_fourthSetWeightET.setText(""+weightValue);
                                    break;
                                case R.id.editfive:
                                    ex4_fifthSetRepsET.setText(""+repsValue);
                                    ex4_fifthSetWeightET.setText(""+weightValue);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.show();

        }
    };

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
        restLabel.setText("Przerwa pomiędzy seriami " + timerCounter + " z 5");
    }

    private void bindDataFields() {
        //Do some work
        //...
        //The values of these fields will be automatically stored in the wizard context
        //and will be populated in the next steps only if the same field names are used.

        exerciseFour.add(new ExerciseSet(1, Integer.parseInt(ex4_firstSetWeightET.getText().toString())
                , Integer.parseInt(ex4_firstSetRepsET.getText().toString()), 1, trainingSetId));
        exerciseFour.add(new ExerciseSet(1, Integer.parseInt(ex4_secondSetWeightET.getText().toString())
                , Integer.parseInt(ex4_secondSetRepsET.getText().toString()), 2, trainingSetId));
        exerciseFour.add(new ExerciseSet(1, Integer.parseInt(ex4_thirdSetWeightET.getText().toString())
                , Integer.parseInt(ex4_thirdSetRepsET.getText().toString()), 3, trainingSetId));
        exerciseFour.add(new ExerciseSet(1, Integer.parseInt(ex4_fourthSetWeightET.getText().toString())
                , Integer.parseInt(ex4_fourthSetRepsET.getText().toString()), 4, trainingSetId));
        exerciseFour.add(new ExerciseSet(1, Integer.parseInt(ex4_fifthSetWeightET.getText().toString())
                , Integer.parseInt(ex4_fifthSetRepsET.getText().toString()), 5, trainingSetId));

        int finalSet1 = Integer.parseInt(ex4_firstSetWeightET.getText().toString());
        int finalSet2 = Integer.parseInt(ex4_secondSetWeightET.getText().toString());
        int finalSet3 = Integer.parseInt(ex4_thirdSetWeightET.getText().toString());
        int finalSet4 = Integer.parseInt(ex4_fourthSetWeightET.getText().toString());
        int finalSet5 = Integer.parseInt(ex4_fifthSetWeightET.getText().toString());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("ex4set1max", set1max >= finalSet1 ? set1max : finalSet1);
        editor.putInt("ex4set2max", set2max >= finalSet1 ? set2max : finalSet2);
        editor.putInt("ex4set3max", set3max >= finalSet1 ? set3max : finalSet3);
        editor.putInt("ex4set4max", set4max >= finalSet1 ? set4max : finalSet4);
        editor.putInt("ex4set5max", set5max >= finalSet1 ? set5max : finalSet5);

        editor.commit();
    }

    private void getWeightFromPreferences(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        set1max = prefs.getInt("ex4set1max", 5);
        set2max = prefs.getInt("ex4set2max", 5);
        set3max = prefs.getInt("ex4set3max", 5);
        set4max = prefs.getInt("ex4set4max", 5);
        set5max = prefs.getInt("ex4set5max", 5);


    }

}