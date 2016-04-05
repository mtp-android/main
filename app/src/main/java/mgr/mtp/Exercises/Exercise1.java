package mgr.mtp.Exercises;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.codepond.wizardroid.WizardStep;

import mgr.mtp.R;

/**
 * Created by lmedrzycki on 05.04.2016.
 */
public class Exercise1 extends WizardStep {

    ProgressBar progressBar;
    Toolbar toolbar;
    TextView tView;
    Button btnStart;
    private long timeRemaining = 0;
    private boolean isPaused = false;
    private boolean isCanceled = false;

    //Wire the layout to the step
    public Exercise1() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.training_wizard, container, false);

        progressBar = (ProgressBar) v.findViewById(R.id.timeProgressBar);
        progressBar.setMax(90);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.squats);

        tView = (TextView) v.findViewById(R.id.timer);
        btnStart = (Button) v.findViewById(R.id.restButton);

        CountDownTimer timer;
        long millisInFuture = 30000; //30 seconds
        long countDownInterval = 1000; //1 second

        btnStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                isPaused = false;
                isCanceled = false;

                btnStart.setEnabled(false);

                CountDownTimer timer;
                long millisInFuture = 90000; //30 seconds
                long countDownInterval = 1000; //1 second

                timer = new CountDownTimer(millisInFuture,countDownInterval){
                    public void onTick(long millisUntilFinished){
                        if(isPaused || isCanceled)
                        {
                            cancel();
                        }
                        else {

                            long value = millisUntilFinished/1000;
                            progressBar.setProgress((int) (90-value));
                            tView.setText("" + value);

                            timeRemaining = millisUntilFinished;
                        }
                    }
                    public void onFinish(){
                        final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.alert);
                        mp.start();
                        progressBar.setProgress(0);
                        tView.setText("Następna seria!");
                        btnStart.setEnabled(true);
                    }
                }.start();
            }
        });


        return v;
    }
}