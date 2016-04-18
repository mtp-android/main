package mgr.mtp.Exercises;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
public class Exercise1 extends WizardStep {

    ProgressWheel pw;
    Toolbar toolbar;
    Button btnStart;
    TextView ex1_firstSetWeightET, ex1_firstSetRepsET, ex1_secondSetWeightET, ex1_secondSetRepsET,
            ex1_thirdSetWeightET, ex1_thirdSetRepsET, ex1_fourthSetWeightET, ex1_fourthSetRepsET,
            ex1_fifthSetWeightET, ex1_fifthSetRepsET;
    TextView restLabel;
    long timeRemaining;
    private boolean isPaused = false;
    private boolean isCanceled = false;
    int timerCounter = 1;
    ImageView editOne, editTwo, editThree, editFour, editFive;

    @ContextVariable
    private ArrayList<ExerciseSet> exerciseOne;

    //Wire the layout to the step
    public Exercise1() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.training_wizard, container, false);

        exerciseOne = new ArrayList<>();

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.squats);

        ex1_firstSetRepsET = (TextView) v.findViewById(R.id.firstSet_reps);
        ex1_firstSetWeightET = (TextView) v.findViewById(R.id.firstSet_weight);

        ex1_secondSetRepsET = (TextView) v.findViewById(R.id.secondSet_reps);
        ex1_secondSetWeightET = (TextView) v.findViewById(R.id.secondSet_weight);

        ex1_thirdSetRepsET = (TextView) v.findViewById(R.id.thirdSet_reps);
        ex1_thirdSetWeightET = (TextView) v.findViewById(R.id.thirdSet_weight);

        ex1_fourthSetRepsET = (TextView) v.findViewById(R.id.fourthSet_reps);
        ex1_fourthSetWeightET = (TextView) v.findViewById(R.id.fourthSet_weight);

        ex1_fifthSetRepsET = (TextView) v.findViewById(R.id.fifthSet_reps);
        ex1_fifthSetWeightET = (TextView) v.findViewById(R.id.fifthSet_weight);

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
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
                                    ex1_firstSetRepsET.setText(""+repsValue);
                                    ex1_firstSetWeightET.setText(""+weightValue);
                                    break;
                                case R.id.edittwo:
                                    ex1_secondSetRepsET.setText(""+repsValue);
                                    ex1_secondSetWeightET.setText(""+weightValue);
                                    break;
                                case R.id.editthree:
                                    ex1_thirdSetRepsET.setText(""+repsValue);
                                    ex1_thirdSetWeightET.setText(""+weightValue);
                                    break;
                                case R.id.editfour:
                                    ex1_fourthSetRepsET.setText(""+repsValue);
                                    ex1_fourthSetWeightET.setText(""+weightValue);
                                    break;
                                case R.id.editfive:
                                    ex1_fifthSetRepsET.setText(""+repsValue);
                                    ex1_fifthSetWeightET.setText(""+weightValue);
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
        restLabel.setText("Przerwa pomiędzy seriami " + timerCounter + "/5");
    }

    private void bindDataFields() {
        //Do some work
        //...
        //The values of these fields will be automatically stored in the wizard context
        //and will be populated in the next steps only if the same field names are used.

        exerciseOne.add(new ExerciseSet(1, Integer.parseInt(ex1_firstSetWeightET.getText().toString())
                , Integer.parseInt(ex1_firstSetRepsET.getText().toString()), 1));
        exerciseOne.add(new ExerciseSet(1, Integer.parseInt(ex1_secondSetWeightET.getText().toString())
                , Integer.parseInt(ex1_secondSetRepsET.getText().toString()), 2));
        exerciseOne.add(new ExerciseSet(1, Integer.parseInt(ex1_thirdSetWeightET.getText().toString())
                , Integer.parseInt(ex1_thirdSetRepsET.getText().toString()), 3));
        exerciseOne.add(new ExerciseSet(1, Integer.parseInt(ex1_fourthSetWeightET.getText().toString())
                , Integer.parseInt(ex1_fourthSetRepsET.getText().toString()), 4));
        exerciseOne.add(new ExerciseSet(1, Integer.parseInt(ex1_fifthSetWeightET.getText().toString())
                , Integer.parseInt(ex1_fifthSetRepsET.getText().toString()), 5));
    }
}