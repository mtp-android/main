package mgr.mtp.Training;

import org.codepond.wizardroid.WizardFlow;
import org.codepond.wizardroid.layouts.BasicWizardLayout;

import mgr.mtp.Exercises.Exercise1;
import mgr.mtp.Exercises.Exercise2;
import mgr.mtp.Exercises.Exercise3;
import mgr.mtp.Exercises.Exercise4;
import mgr.mtp.Exercises.Exercise5;
import mgr.mtp.Exercises.Exercise6;
import mgr.mtp.R;

/**
 * Created by lmedrzycki on 05.04.2016.
 */
public class TrainingWizard extends BasicWizardLayout {

    /**
     * Note that initially BasicWizardLayout inherits from {@link android.support.v4.app.Fragment} and therefore you must have an empty constructor
     */
    public TrainingWizard() {
        super();
    }

    //You must override this method and create a wizard flow by
    //using WizardFlow.Builder as shown in this example
    @Override
    public WizardFlow onSetup() {

        setNextButtonText(getString(R.string.next));
        setBackButtonText(getString(R.string.previous));
        setFinishButtonText(getString(R.string.finishTraining));

        return new WizardFlow.Builder()
                .addStep(Exercise1.class)
                .addStep(Exercise2.class)
                .addStep(Exercise3.class)
                .addStep(Exercise4.class)
                .addStep(Exercise5.class)
                .addStep(Exercise6.class)
                .create();
    }
}