package mgr.mtp.Statistics;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.nio.charset.StandardCharsets;

import cz.msebera.android.httpclient.Header;
import mgr.mtp.Diet.DietSettings;
import mgr.mtp.R;
import mgr.mtp.Utils.Constants;

/**
 * Created by lmedrzycki on 14.04.2016.
 */
public class StatisticsUpdateBodyMeasures extends PreferenceActivity {

    Integer userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.bodymeasures);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        userId = prefs.getInt("userId", 0);

        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        bar.setTitle(getString(R.string.updateMeasures));
        root.addView(bar, 0); // insert at top

        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StatisticsUpdateBodyMeasures.this, "Zapisano pomiary", Toast.LENGTH_LONG).show();
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
        saveBodyMeasures();
    }

    private void saveBodyMeasures() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        int weight = Integer.parseInt(prefs.getString("user_weight", "1"));
        int height = Integer.parseInt(prefs.getString("user_height", "1"));
        int bicep = Integer.parseInt(prefs.getString("measure_bicep", "1"));
        int chest = Integer.parseInt(prefs.getString("measure_chest", "1"));
        int waist = Integer.parseInt(prefs.getString("measure_waist", "1"));
        int thigh = Integer.parseInt(prefs.getString("measure_thigh", "1"));
        int neck = Integer.parseInt(prefs.getString("measure_neck", "1"));
        int gender = Integer.parseInt(prefs.getString("user_gender", "1"));

        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("weight", weight);
        params.put("height", height);
        params.put("bicep", bicep);
        params.put("chest", chest);
        params.put("waist", waist);
        params.put("thigh", thigh);
        params.put("neck", neck);

        double a = (4.15 * waist);
        double b = a / 2.54;
        double c = 0.082 * weight * 2.2;
        double d = b - c - (gender == 1? 98.42 : 76.76);
        double e = weight * 2.2;
        Double bodyFat = d / e * 100;


        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("bodyFat", bodyFat.intValue());
        params.put("bodyFat", bodyFat.intValue());
        editor.commit();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.host + "/stats/savebodymeasures", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.noConnectionToServer), Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.serverError), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.unexpectedError), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
