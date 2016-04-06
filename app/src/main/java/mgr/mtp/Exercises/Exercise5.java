package mgr.mtp.Exercises;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.codepond.wizardroid.WizardStep;

import mgr.mtp.R;
import mgr.mtp.Utils.ProgressWheel;

/**
 * Created by lmedrzycki on 05.04.2016.
 */
public class Exercise5 extends WizardStep {

    ProgressWheel pw;
    Toolbar toolbar;
    Button btnStart;
    long timeRemaining;
    private boolean isPaused = false;
    private boolean isCanceled = false;

    //Wire the layout to the step
    public Exercise5() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.training_wizard, container, false);

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.dips);

        pw = (ProgressWheel) v.findViewById(R.id.pw_spinner);
        pw.setProgress(360);

        btnStart = (Button) v.findViewById(R.id.restButton);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        pw.setProgress(360);
                        btnStart.setEnabled(true);
                    }
                }.start();
            }
        });

        return v;
    }
}