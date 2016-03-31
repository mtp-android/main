package mgr.mtp.Diet;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import mgr.mtp.R;

/**
 * Created by lukas on 30.03.2016.
 */
public class DietSettings extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.dietpreferences);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        root.addView(bar, 0); // insert at top

        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DietSettings.this, "Zapisano ustawienia", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        recalculateDiet();
    }

    public void recalculateDiet(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        int weight = Integer.parseInt(prefs.getString("user_weight", "75"));
        int activityLevel = Integer.parseInt(prefs.getString("user_activityLevel", "1"));
        int age = Integer.parseInt(prefs.getString("user_age", "25"));
        int gender = Integer.parseInt(prefs.getString("user_gender", "1"));
        int target = Integer.parseInt(prefs.getString("user_target", "1"));
        int height = Integer.parseInt(prefs.getString("user_height", "170"));

        double calories = 0,proteins,carbs,fat;

        switch (gender) {
            case 1:
                calories = 66.5 + (13.7 * weight) + (5 * height) - (6.8 * age);
                break;
            case 2:
                calories = 655 + (9.6 * weight) + (1.85 * height) - (4.7 * age);
                break;
        }

        switch (activityLevel) {
            case 1:
                calories = calories * 1.0;
                break;
            case 2:
                calories = calories * 1.4;
                break;
            case 3:
                calories = calories * 1.6;
                break;
        }


        switch (target) {
            case 1:
                calories = calories - 300;
                break;
            case 2:
                calories = calories + 300;
                break;
            case 3:
                break;
        }

        proteins = calories * 0.3;
        fat = calories * 0.2;
        carbs = (calories - (proteins+fat));

        proteins = proteins / 4;
        fat = fat / 9;
        carbs = carbs / 4;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("calculatedCalories", (int) calories);
        editor.putInt("calculatedProteins", (int) proteins);
        editor.putInt("calculatedFat", (int) fat);
        editor.putInt("calculatedCarbs", (int) carbs);

        editor.commit();


    }
}
