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
                Toast.makeText(DietSettings.this, "Zapisano cele", Toast.LENGTH_LONG).show();
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

    public void recalculateDiet() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        int weight = Integer.parseInt(prefs.getString("user_weight", "75"));
        int activityLevel = Integer.parseInt(prefs.getString("user_activityLevel", "1"));
        int age = Integer.parseInt(prefs.getString("user_age", "25"));
        int gender = Integer.parseInt(prefs.getString("user_gender", "1"));
        int target = Integer.parseInt(prefs.getString("user_target", "1"));
        int height = Integer.parseInt(prefs.getString("user_height", "170"));


        // Na podstawie Anita Bean [2008]
        // Algorytm obliczania zapotrzebowania kalorycznego

        double calories, proteins, carbs, fat, rmr, factorA = 0, factorB = 0;
        boolean isMan;

        // Obliczanie tempa metabolizmu spoczynkowego (RMR)
        isMan = gender == 1;

        if(age <= 18){
            factorA = isMan ? 17.5 : 12.2;
            factorB = isMan ? 651 : 746;

        } else if(age > 18 && age <= 30){
            factorA = isMan ? 15.3 : 14.7;
            factorB = isMan ? 679 : 496;

        } else if(age > 30 && age <= 60){
            factorA = isMan ? 11.6 : 8.7;
            factorB = isMan ? 879 : 829;
        }

        rmr = (weight * factorA) + factorB;

        // Obliczanie dziennego wydatku kalorycznego
        // 1 - brak aktywnosci
        // 2 - umiarkowana aktywnosc
        // 3 - bardzo aktywny

        switch (activityLevel) {
            case 1:
                rmr = rmr * 1.4;
                break;
            case 2:
                rmr = rmr * 1.7;
                break;
            case 3:
                rmr = rmr * 2.0;
                break;
        }

        // Wydatek energetyczny podczas ćwiczeń
        // przy zalozeniu 1h treningu x 3 dni w tygodniu
        // 1h treningu = srednio 360 kcal
        calories = rmr + (3 * 360);

        // Uwzglednienie celu dietetycznego
        // 1 - redukcja
        // 2 - wzrost masy miesniowej
        // 3 - utrzymanie wagi

        switch (target) {
            case 1:
                calories = calories * 1.2;
                break;
            case 2:
                calories = calories * 0.85;
                break;
            case 3:
                break;
        }

        // Udzial poszczegolnych makroskladnikow
        // Bialko 1.6 g na kazdy kilogram masy ciala
        // Tluszcze 25% zapotrzebowania kalorycznego
        // Weglowodany - pozostala ilosc

        proteins = weight * 1.6;
        fat = (calories * 0.25);
        carbs = (calories - ((proteins * 4) + fat));

        // Przeliczenie z kcal na g
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
