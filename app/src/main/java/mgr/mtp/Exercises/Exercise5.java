package mgr.mtp.Exercises;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
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

/**
 * Created by lmedrzycki on 05.04.2016.
 */
public class Exercise5 extends WizardStep {

    Button btnStart;
    TextView ex5_firstSetWeightET, ex5_firstSetRepsET, ex5_secondSetWeightET, ex5_secondSetRepsET,
            ex5_thirdSetWeightET, ex5_thirdSetRepsET, ex5_fourthSetWeightET, ex5_fourthSetRepsET,
            ex5_fifthSetWeightET, ex5_fifthSetRepsET;
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

    @ContextVariable
    private ArrayList<ExerciseSet> exerciseFive;

    //Wire the layout to the step
    public Exercise5() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.training_wizard, container, false);

        exerciseFive = new ArrayList<>();

        //toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        //toolbar.setTitle("Ćwiczenie 5 z 6");
        TextView exName = (TextView) v.findViewById(R.id.exName);

        Activity trainingWorkout = getActivity();
        trainingSetId = 0;


        if (trainingWorkout instanceof TrainingWorkout) {
            trainingSetId = ((TrainingWorkout) trainingWorkout).getTrainingSetId();
        }

        String name = "";

        switch (trainingSetId) {
            case 1:
                name = "Uginanie przedramion z hantlami";
                break;

            case 2:
                name = "Prostowanie ramion w dół na wyciągu górnym";
                break;

            case 3:
                name = "Zginanie nadgarstków z wykorzystaniem nachwytu";
                break;

            default:
                break;
        }

        exName.setText(name);

        ex5_firstSetRepsET = (TextView) v.findViewById(R.id.firstSet_reps);
        ex5_firstSetWeightET = (TextView) v.findViewById(R.id.firstSet_weight);

        ex5_secondSetRepsET = (TextView) v.findViewById(R.id.secondSet_reps);
        ex5_secondSetWeightET = (TextView) v.findViewById(R.id.secondSet_weight);

        ex5_thirdSetRepsET = (TextView) v.findViewById(R.id.thirdSet_reps);
        ex5_thirdSetWeightET = (TextView) v.findViewById(R.id.thirdSet_weight);

        ex5_fourthSetRepsET = (TextView) v.findViewById(R.id.fourthSet_reps);
        ex5_fourthSetWeightET = (TextView) v.findViewById(R.id.fourthSet_weight);

        ex5_fifthSetRepsET = (TextView) v.findViewById(R.id.fifthSet_reps);
        ex5_fifthSetWeightET = (TextView) v.findViewById(R.id.fifthSet_weight);

        getWeightFromPreferences();

        ex5_firstSetWeightET.setText(""+set1max);
        ex5_secondSetWeightET.setText(""+set2max);
        ex5_thirdSetWeightET.setText(""+set3max);
        ex5_fourthSetWeightET.setText(""+set4max);
        ex5_fifthSetWeightET.setText(""+set5max);

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
                                restLabel.setText("Odpoczywaj przez: "+value+" sekund");
                                timeRemaining = millisUntilFinished;
                            }
                        }

                        public void onFinish() {
                            final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.alert);
                            mp.start();
                            timerCounter++;
                            updateRestLabel(timerCounter);
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

            switch (id) {
                case R.id.editone:
                    reps.setValue(Integer.parseInt(ex5_firstSetRepsET.getText().toString()));
                    weight.setValue(Integer.parseInt(ex5_firstSetWeightET.getText().toString()));
                    break;
                case R.id.edittwo:
                    reps.setValue(Integer.parseInt(ex5_secondSetRepsET.getText().toString()));
                    weight.setValue(Integer.parseInt(ex5_secondSetWeightET.getText().toString()));
                    break;
                case R.id.editthree:
                    reps.setValue(Integer.parseInt(ex5_thirdSetRepsET.getText().toString()));
                    weight.setValue(Integer.parseInt(ex5_thirdSetWeightET.getText().toString()));
                    break;
                case R.id.editfour:
                    reps.setValue(Integer.parseInt(ex5_fourthSetRepsET.getText().toString()));
                    weight.setValue(Integer.parseInt(ex5_fourthSetWeightET.getText().toString()));
                    break;
                case R.id.editfive:
                    reps.setValue(Integer.parseInt(ex5_fifthSetRepsET.getText().toString()));
                    weight.setValue(Integer.parseInt(ex5_fifthSetWeightET.getText().toString()));
                    break;
                default:
                    break;
            }

            builder.setView(theView)
                    .setPositiveButton(R.string.confirmed, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            int repsValue = reps.getValue();
                            int weightValue = weight.getValue();

                            switch (id) {
                                case R.id.editone:
                                    ex5_firstSetRepsET.setText(""+repsValue);
                                    ex5_firstSetWeightET.setText(""+weightValue);
                                    break;
                                case R.id.edittwo:
                                    ex5_secondSetRepsET.setText(""+repsValue);
                                    ex5_secondSetWeightET.setText(""+weightValue);
                                    break;
                                case R.id.editthree:
                                    ex5_thirdSetRepsET.setText(""+repsValue);
                                    ex5_thirdSetWeightET.setText(""+weightValue);
                                    break;
                                case R.id.editfour:
                                    ex5_fourthSetRepsET.setText(""+repsValue);
                                    ex5_fourthSetWeightET.setText(""+weightValue);
                                    break;
                                case R.id.editfive:
                                    ex5_fifthSetRepsET.setText(""+repsValue);
                                    ex5_fifthSetWeightET.setText(""+weightValue);
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
        restLabel.setText("Aktualna seria: " + timerCounter + " z 5");
    }

    private void bindDataFields() {
        //Do some work
        //...
        //The values of these fields will be automatically stored in the wizard context
        //and will be populated in the next steps only if the same field names are used.

        exerciseFive.add(new ExerciseSet(1, Integer.parseInt(ex5_firstSetWeightET.getText().toString())
                , Integer.parseInt(ex5_firstSetRepsET.getText().toString()), 1, trainingSetId));
        exerciseFive.add(new ExerciseSet(1, Integer.parseInt(ex5_secondSetWeightET.getText().toString())
                , Integer.parseInt(ex5_secondSetRepsET.getText().toString()), 2, trainingSetId));
        exerciseFive.add(new ExerciseSet(1, Integer.parseInt(ex5_thirdSetWeightET.getText().toString())
                , Integer.parseInt(ex5_thirdSetRepsET.getText().toString()), 3, trainingSetId));
        exerciseFive.add(new ExerciseSet(1, Integer.parseInt(ex5_fourthSetWeightET.getText().toString())
                , Integer.parseInt(ex5_fourthSetRepsET.getText().toString()), 4, trainingSetId));
        exerciseFive.add(new ExerciseSet(1, Integer.parseInt(ex5_fifthSetWeightET.getText().toString())
                , Integer.parseInt(ex5_fifthSetRepsET.getText().toString()), 5, trainingSetId));

        int finalSet1 = Integer.parseInt(ex5_firstSetWeightET.getText().toString());
        int finalSet2 = Integer.parseInt(ex5_secondSetWeightET.getText().toString());
        int finalSet3 = Integer.parseInt(ex5_thirdSetWeightET.getText().toString());
        int finalSet4 = Integer.parseInt(ex5_fourthSetWeightET.getText().toString());
        int finalSet5 = Integer.parseInt(ex5_fifthSetWeightET.getText().toString());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("set"+trainingSetId+"ex5set1max", set1max >= finalSet1 ? set1max : finalSet1 + 2);
        editor.putInt("set"+trainingSetId+"ex5set2max", set2max >= finalSet1 ? set2max : finalSet2 + 2);
        editor.putInt("set"+trainingSetId+"ex5set3max", set3max >= finalSet1 ? set3max : finalSet3 + 2);
        editor.putInt("set"+trainingSetId+"ex5set4max", set4max >= finalSet1 ? set4max : finalSet4 + 2);
        editor.putInt("set"+trainingSetId+"ex5set5max", set5max >= finalSet1 ? set5max : finalSet5 + 2);

        editor.commit();
    }

    private void getWeightFromPreferences(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        set1max = prefs.getInt("set"+trainingSetId+"ex5set1max", 5);
        set2max = prefs.getInt("set"+trainingSetId+"ex5set2max", 5);
        set3max = prefs.getInt("set"+trainingSetId+"ex5set3max", 5);
        set4max = prefs.getInt("set"+trainingSetId+"ex5set4max", 5);
        set5max = prefs.getInt("set"+trainingSetId+"ex5set5max", 5);


    }

}