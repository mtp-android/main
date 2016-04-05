package mgr.mtp.Exercises;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.codepond.wizardroid.WizardStep;

import mgr.mtp.R;

/**
 * Created by lmedrzycki on 05.04.2016.
 */
public class Exercise4 extends WizardStep {

    Toolbar toolbar;

    //Wire the layout to the step
    public Exercise4() {
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.training_wizard, container, false);

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.barbellCurls);

        return v;
    }
}