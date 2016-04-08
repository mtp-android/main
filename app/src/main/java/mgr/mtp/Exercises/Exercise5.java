package mgr.mtp.Exercises;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.persistence.ContextVariable;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import mgr.mtp.DataModel.ExerciseSet;
import mgr.mtp.R;
import mgr.mtp.Training.TrainingHome;
import mgr.mtp.Training.TrainingWorkout;
import mgr.mtp.Utils.Constants;
import mgr.mtp.Utils.ProgressWheel;

/**
 * Created by lmedrzycki on 05.04.2016.
 */
public class Exercise5 extends WizardStep {

    ProgressWheel pw;
    ProgressDialog prgDialog;
    Toolbar toolbar;
    Button btnStart;
    EditText ex5_firstSetWeightET, ex5_firstSetRepsET, ex5_secondSetWeightET, ex5_secondSetRepsET,
            ex5_thirdSetWeightET, ex5_thirdSetRepsET, ex5_fourthSetWeightET, ex5_fourthSetRepsET,
            ex5_fifthSetWeightET, ex5_fifthSetRepsET;
    TextView restLabel;
    long timeRemaining;
    private boolean isPaused = false;
    private boolean isCanceled = false;
    int timerCounter = 1;

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

        ex5_firstSetRepsET = (EditText) v.findViewById(R.id.firstSet_reps);
        ex5_firstSetWeightET = (EditText) v.findViewById(R.id.firstSet_weight);

        ex5_secondSetRepsET = (EditText) v.findViewById(R.id.secondSet_reps);
        ex5_secondSetWeightET = (EditText) v.findViewById(R.id.secondSet_weight);

        ex5_thirdSetRepsET = (EditText) v.findViewById(R.id.thirdSet_reps);
        ex5_thirdSetWeightET = (EditText) v.findViewById(R.id.thirdSet_weight);

        ex5_fourthSetRepsET = (EditText) v.findViewById(R.id.fourthSet_reps);
        ex5_fourthSetWeightET = (EditText) v.findViewById(R.id.fourthSet_weight);

        ex5_fifthSetRepsET = (EditText) v.findViewById(R.id.fifthSet_reps);
        ex5_fifthSetWeightET = (EditText) v.findViewById(R.id.fifthSet_weight);

        restLabel = (TextView) v.findViewById(R.id.restLabel);
        updateRestLabel(timerCounter);

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.dips);

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
        params.put("ex2", gson.toJson(exerciseOne));
        params.put("ex3", gson.toJson(exerciseOne));
        params.put("ex4", gson.toJson(exerciseOne));
        params.put("ex5", gson.toJson(exerciseOne));

        Activity trainingWorkout = getActivity();

        if (trainingWorkout instanceof TrainingWorkout) {
            date = ((TrainingWorkout) trainingWorkout).getDate();
        }

        params.put("date", date);

        AsyncHttpClient client = new AsyncHttpClient();
        final String finalDate = date;
        client.post(Constants.host + "/trainig/addtrainig", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                prgDialog.hide();

                Intent homeIntent = new Intent(getActivity(), TrainingHome.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                homeIntent.putExtra("date", finalDate);
                startActivity(homeIntent);

                Toast.makeText(getActivity(),
                        getString(R.string.training_completed), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = new String(responseBody, StandardCharsets.UTF_8);

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
        restLabel.setText("Przerwa pomiędzy seriami " + timerCounter + "/5");
    }

    private void bindDataFields() {
        //Do some work
        //...
        //The values of these fields will be automatically stored in the wizard context
        //and will be populated in the next steps only if the same field names are used.

        exerciseFive.add(new ExerciseSet(1, Integer.parseInt(ex5_firstSetWeightET.getText().toString())
                , Integer.parseInt(ex5_firstSetRepsET.getText().toString()), 1));
        exerciseFive.add(new ExerciseSet(1, Integer.parseInt(ex5_secondSetWeightET.getText().toString())
                , Integer.parseInt(ex5_secondSetRepsET.getText().toString()), 1));
        exerciseFive.add(new ExerciseSet(1, Integer.parseInt(ex5_thirdSetWeightET.getText().toString())
                , Integer.parseInt(ex5_thirdSetRepsET.getText().toString()), 1));
        exerciseFive.add(new ExerciseSet(1, Integer.parseInt(ex5_fourthSetWeightET.getText().toString())
                , Integer.parseInt(ex5_fourthSetRepsET.getText().toString()), 1));
        exerciseFive.add(new ExerciseSet(1, Integer.parseInt(ex5_fifthSetWeightET.getText().toString())
                , Integer.parseInt(ex5_fifthSetRepsET.getText().toString()), 1));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.confirmendtraining))
                .setNegativeButton(getString(R.string.back), dialogClickListener).setPositiveButton(getString(R.string.confirmed), dialogClickListener).show();
    }

}