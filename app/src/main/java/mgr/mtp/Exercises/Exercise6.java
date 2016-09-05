package mgr.mtp.Exercises;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.persistence.ContextVariable;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import mgr.mtp.DataModel.ExerciseSet;
import mgr.mtp.Home;
import mgr.mtp.R;
import mgr.mtp.Training.TrainingWorkout;
import mgr.mtp.Utils.Constants;
import mgr.mtp.Utils.ProgressWheel;

/**
 * Created by lmedrzycki on 05.04.2016.
 */
public class Exercise6 extends WizardStep {

    ProgressWheel pw;
    ProgressDialog prgDialog;
    Toolbar toolbar;
    Button btnStart;
    TextView ex6_firstSetWeightET, ex6_firstSetRepsET, ex6_secondSetWeightET, ex6_secondSetRepsET,
            ex6_thirdSetWeightET, ex6_thirdSetRepsET, ex6_fourthSetWeightET, ex6_fourthSetRepsET,
            ex6_fifthSetWeightET, ex6_fifthSetRepsET;
    TextView restLabel;
    long timeRemaining;
    private boolean isPaused = false;
    private boolean isCanceled = false;
    int timerCounter = 1,userId;
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

    @ContextVariable
    private ArrayList<ExerciseSet> exerciseSix;

    //Wire the layout to the step
    public Exercise6() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.training_wizard, container, false);


        // get user id from settings
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(v.getContext());
        userId = prefs.getInt("userId", 0);

        exerciseSix = new ArrayList<>();

        ex6_firstSetRepsET = (TextView) v.findViewById(R.id.firstSet_reps);
        ex6_firstSetWeightET = (TextView) v.findViewById(R.id.firstSet_weight);

        ex6_secondSetRepsET = (TextView) v.findViewById(R.id.secondSet_reps);
        ex6_secondSetWeightET = (TextView) v.findViewById(R.id.secondSet_weight);

        ex6_thirdSetRepsET = (TextView) v.findViewById(R.id.thirdSet_reps);
        ex6_thirdSetWeightET = (TextView) v.findViewById(R.id.thirdSet_weight);

        ex6_fourthSetRepsET = (TextView) v.findViewById(R.id.fourthSet_reps);
        ex6_fourthSetWeightET = (TextView) v.findViewById(R.id.fourthSet_weight);

        ex6_fifthSetRepsET = (TextView) v.findViewById(R.id.fifthSet_reps);
        ex6_fifthSetWeightET = (TextView) v.findViewById(R.id.fifthSet_weight);

        getWeightFromPreferences();

        ex6_firstSetWeightET.setText(""+set1max);
        ex6_secondSetWeightET.setText(""+set2max);
        ex6_thirdSetWeightET.setText(""+set3max);
        ex6_fourthSetWeightET.setText(""+set4max);
        ex6_fifthSetWeightET.setText(""+set5max);

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

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle("Ćwiczenie 6 z 6");
        TextView exName = (TextView) v.findViewById(R.id.exName);

        Activity trainingWorkout = getActivity();
        trainingSetId = 0;


        if (trainingWorkout instanceof TrainingWorkout) {
            trainingSetId = ((TrainingWorkout) trainingWorkout).getTrainingSetId();
        }

        String name = "";

        switch (trainingSetId) {
            case 1:
                name = "Skłony w pozycji leżącej";
                break;

            case 2:
                name = "Unoszenie nóg w zwisie na drążku";
                break;

            case 3:
                name = "Skręty tułowia";
                break;

            default:
                break;
        }

        exName.setText(name);

        pw = (ProgressWheel) v.findViewById(R.id.pw_spinner);
        pw.setProgress(360);

        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage(getString(R.string.pleaseWait));
        prgDialog.setCancelable(false);

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
                                    ex6_firstSetRepsET.setText(""+repsValue);
                                    ex6_firstSetWeightET.setText(""+weightValue);
                                    break;
                                case R.id.edittwo:
                                    ex6_secondSetRepsET.setText(""+repsValue);
                                    ex6_secondSetWeightET.setText(""+weightValue);
                                    break;
                                case R.id.editthree:
                                    ex6_thirdSetRepsET.setText(""+repsValue);
                                    ex6_thirdSetWeightET.setText(""+weightValue);
                                    break;
                                case R.id.editfour:
                                    ex6_fourthSetRepsET.setText(""+repsValue);
                                    ex6_fourthSetWeightET.setText(""+weightValue);
                                    break;
                                case R.id.editfive:
                                    ex6_fifthSetRepsET.setText(""+repsValue);
                                    ex6_fifthSetWeightET.setText(""+weightValue);
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

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    saveTrainingToDatabase();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    private void saveTrainingToDatabase() {

        String date = null;

        prgDialog.show();
        RequestParams params = new RequestParams();
        Gson gson = new Gson();

        params.put("ex1", gson.toJson(exerciseOne));
        params.put("ex2", gson.toJson(exerciseTwo));
        params.put("ex3", gson.toJson(exerciseThree));
        params.put("ex4", gson.toJson(exerciseFour));
        params.put("ex5", gson.toJson(exerciseFive));
        params.put("ex6", gson.toJson(exerciseSix));

        Activity trainingWorkout = getActivity();

        if (trainingWorkout instanceof TrainingWorkout) {
            date = ((TrainingWorkout) trainingWorkout).getDate();
        }

        params.put("date", date);
        params.put("userId", userId);

        AsyncHttpClient client = new AsyncHttpClient();
        final String finalDate = date;
        client.get(Constants.host + "/training/addtraining", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                prgDialog.hide();

                Intent homeIntent = new Intent(getActivity(), Home.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                homeIntent.putExtra("date", finalDate);
                homeIntent.putExtra("tab", 1);

                startActivity(homeIntent);

                Toast.makeText(getActivity(),
                        getString(R.string.training_completed), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //String response = new String(responseBody, StandardCharsets.UTF_8);

                prgDialog.hide();

                if (statusCode == 404) {
                    Toast.makeText(getActivity(),
                            getString(R.string.noConnectionToServer), Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getActivity(),
                            getString(R.string.serverError), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(),
                            getString(R.string.unexpectedError), Toast.LENGTH_LONG).show();
                }
            }
        });

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
        restLabel.setText("Przerwa pomiędzy seriami " + timerCounter + " z 5");
    }

    private void bindDataFields() {
        //Do some work
        //...
        //The values of these fields will be automatically stored in the wizard context
        //and will be populated in the next steps only if the same field names are used.

        Activity trainingWorkout = getActivity();
        int trainingSetId = 0;


        if (trainingWorkout instanceof TrainingWorkout) {
            trainingSetId = ((TrainingWorkout) trainingWorkout).getTrainingSetId();
        }

        exerciseSix.add(new ExerciseSet(1, Integer.parseInt(ex6_firstSetWeightET.getText().toString())
                , Integer.parseInt(ex6_firstSetRepsET.getText().toString()), 1, trainingSetId));
        exerciseSix.add(new ExerciseSet(1, Integer.parseInt(ex6_secondSetWeightET.getText().toString())
                , Integer.parseInt(ex6_secondSetRepsET.getText().toString()), 2, trainingSetId));
        exerciseSix.add(new ExerciseSet(1, Integer.parseInt(ex6_thirdSetWeightET.getText().toString())
                , Integer.parseInt(ex6_thirdSetRepsET.getText().toString()), 3, trainingSetId));
        exerciseSix.add(new ExerciseSet(1, Integer.parseInt(ex6_fourthSetWeightET.getText().toString())
                , Integer.parseInt(ex6_fourthSetRepsET.getText().toString()), 4, trainingSetId));
        exerciseSix.add(new ExerciseSet(1, Integer.parseInt(ex6_fifthSetWeightET.getText().toString())
                , Integer.parseInt(ex6_fifthSetRepsET.getText().toString()), 5, trainingSetId));


        int finalSet1 = Integer.parseInt(ex6_firstSetWeightET.getText().toString());
        int finalSet2 = Integer.parseInt(ex6_secondSetWeightET.getText().toString());
        int finalSet3 = Integer.parseInt(ex6_thirdSetWeightET.getText().toString());
        int finalSet4 = Integer.parseInt(ex6_fourthSetWeightET.getText().toString());
        int finalSet5 = Integer.parseInt(ex6_fifthSetWeightET.getText().toString());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("ex6set1max", set1max >= finalSet1 ? set1max : finalSet1);
        editor.putInt("ex6set2max", set2max >= finalSet1 ? set2max : finalSet2);
        editor.putInt("ex6set3max", set3max >= finalSet1 ? set3max : finalSet3);
        editor.putInt("ex6set4max", set4max >= finalSet1 ? set4max : finalSet4);
        editor.putInt("ex6set5max", set5max >= finalSet1 ? set5max : finalSet5);

        editor.commit();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.confirmendtraining))
                .setNegativeButton(getString(R.string.back), dialogClickListener).setPositiveButton(getString(R.string.confirmed), dialogClickListener).show();
    }

    private void getWeightFromPreferences(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        set1max = prefs.getInt("ex6set1max", 5);
        set2max = prefs.getInt("ex6set2max", 5);
        set3max = prefs.getInt("ex6set3max", 5);
        set4max = prefs.getInt("ex6set4max", 5);
        set5max = prefs.getInt("ex6set5max", 5);


    }

}