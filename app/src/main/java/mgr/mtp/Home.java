package mgr.mtp;

/**
 * Created by lukas on 21.02.2016.
 */
import android.app.Activity;
import android.os.Bundle;
/**
 *
 * Home Screen Activity
 */
public class Home extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Displays Home Screen
        setContentView(R.layout.home);
    }

}
