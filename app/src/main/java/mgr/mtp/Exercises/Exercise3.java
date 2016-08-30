package mgr.mtp.Exercises;

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
import mgr.mtp.Utils.ProgressWheel;

/**
 * Created by lmedrzycki on 05.04.2016.
 */
public class Exercise3 extends WizardStep {

    ProgressWheel pw;
    Toolbar toolbar;
    Button btnStart;
    TextView ex3_firstSetWeightET, ex3_firstSetRepsET, ex3_secondSetWeightET, ex3_secondSetRepsET,
            ex3_thirdSetWeightET, ex3_thirdSetRepsET, ex3_fourthSetWeightET, ex3_fourthSetRepsET,
            ex3_fifthSetWeightET, ex3_fifthSetRepsET;
    TextView restLabel;
    long timeRemaining;
    private boolean isPaused = false;
    private boolean isCanceled = false;
    int timerCounter = 1;
    ImageView editOne, editTwo, editThree, editFour, editFive;
    int set1max,set2max,set3max,set4max,set5max;


    @ContextVariable
    private ArrayList<ExerciseSet> exerciseOne;

    @ContextVariable
    private ArrayList<ExerciseSet> exerciseTwo;

    @ContextVariable
    private ArrayList<ExerciseSet> exerciseThree;

    //Wire the layout to the step
    public Exercise3() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.training_wizard, container, false);

        exerciseThree = new ArrayList<>();

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.trainingBarbellRow);

        ex3_firstSetRepsET = (TextView) v.findViewById(R.id.firstSet_reps);
        ex3_firstSetWeightET = (TextView) v.findViewById(R.id.firstSet_weight);

        ex3_secondSetRepsET = (TextView) v.findViewById(R.id.secondSet_reps);
        ex3_secondSetWeightET = (TextView) v.findViewById(R.id.secondSet_weight);

        ex3_thirdSetRepsET = (TextView) v.findViewById(R.id.thirdSet_reps);
        ex3_thirdSetWeightET = (TextView) v.findViewById(R.id.thirdSet_weight);

        ex3_fourthSetRepsET = (TextView) v.findViewById(R.id.fourthSet_reps);
        ex3_fourthSetWeightET = (TextView) v.findViewById(R.id.fourthSet_weight);

        ex3_fifthSetRepsET = (TextView) v.findViewById(R.id.fifthSet_reps);
        ex3_fifthSetWeightET = (TextView) v.findViewById(R.id.fifthSet_weight);

        getWeightFromPreferences();

        ex3_firstSetWeightET.setText(""+set1max);
        ex3_secondSetWeightET.setText(""+set2max);
        ex3_thirdSetWeightET.setText(""+set3max);
        ex3_fourthSetWeightET.setText(""+set4max);
        ex3_fifthSetWeightET.setText(""+set5max);

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
                                    ex3_firstSetRepsET.setText(""+repsValue);
                                    ex3_firstSetWeightET.setText(""+weightValue);
                                    break;
                                case R.id.edittwo:
                                    ex3_secondSetRepsET.setText(""+repsValue);
                                    ex3_secondSetWeightET.setText(""+weightValue);
                                    break;
                                case R.id.editthree:
                                    ex3_thirdSetRepsET.setText(""+repsValue);
                                    ex3_thirdSetWeightET.setText(""+weightValue);
                                    break;
                                case R.id.editfour:
                                    ex3_fourthSetRepsET.setText(""+repsValue);
                                    ex3_fourthSetWeightET.setText(""+weightValue);
                                    break;
                                case R.id.editfive:
                                    ex3_fifthSetRepsET.setText(""+repsValue);
                                    ex3_fifthSetWeightET.setText(""+weightValue);
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

        exerciseThree.add(new ExerciseSet(1, Integer.parseInt(ex3_firstSetWeightET.getText().toString())
                , Integer.parseInt(ex3_firstSetRepsET.getText().toString()), 1));
        exerciseThree.add(new ExerciseSet(1, Integer.parseInt(ex3_secondSetWeightET.getText().toString())
                , Integer.parseInt(ex3_secondSetRepsET.getText().toString()), 2));
        exerciseThree.add(new ExerciseSet(1, Integer.parseInt(ex3_thirdSetWeightET.getText().toString())
                , Integer.parseInt(ex3_thirdSetRepsET.getText().toString()), 3));
        exerciseThree.add(new ExerciseSet(1, Integer.parseInt(ex3_fourthSetWeightET.getText().toString())
                , Integer.parseInt(ex3_fourthSetRepsET.getText().toString()), 4));
        exerciseThree.add(new ExerciseSet(1, Integer.parseInt(ex3_fifthSetWeightET.getText().toString())
                , Integer.parseInt(ex3_fifthSetRepsET.getText().toString()), 5));

        int finalSet1 = Integer.parseInt(ex3_firstSetWeightET.getText().toString());
        int finalSet2 = Integer.parseInt(ex3_secondSetWeightET.getText().toString());
        int finalSet3 = Integer.parseInt(ex3_thirdSetWeightET.getText().toString());
        int finalSet4 = Integer.parseInt(ex3_fourthSetWeightET.getText().toString());
        int finalSet5 = Integer.parseInt(ex3_fifthSetWeightET.getText().toString());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("ex3set1max", set1max >= finalSet1 ? set1max : finalSet1);
        editor.putInt("ex3set2max", set2max >= finalSet1 ? set2max : finalSet2);
        editor.putInt("ex3set3max", set3max >= finalSet1 ? set3max : finalSet3);
        editor.putInt("ex3set4max", set4max >= finalSet1 ? set4max : finalSet4);
        editor.putInt("ex3set5max", set5max >= finalSet1 ? set5max : finalSet5);

        editor.commit();
    }

    private void getWeightFromPreferences(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        set1max = prefs.getInt("ex3set1max", 5);
        set2max = prefs.getInt("ex3set2max", 5);
        set3max = prefs.getInt("ex3set3max", 5);
        set4max = prefs.getInt("ex3set4max", 5);
        set5max = prefs.getInt("ex3set5max", 5);


    }
}