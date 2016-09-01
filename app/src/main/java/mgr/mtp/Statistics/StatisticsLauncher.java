package mgr.mtp.Statistics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import mgr.mtp.DataModel.Measure;
import mgr.mtp.Diet.DietSettings;
import mgr.mtp.R;
import mgr.mtp.Utils.Constants;

/**
 * Created by lmedrzycki on 13.04.2016.
 */
public class StatisticsLauncher extends Fragment {

    public StatisticsLauncher() {
        // Required empty public constructor
    }

    ArrayList<Measure> body;
    ArrayList<Measure> power;
    ArrayList<Measure > all;

    TextView statDate;
    int userId;
    ProgressDialog prgDialog;
    Button updateMeasuresBtn, updateDietBtn;
    ListView measuresGroupsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.statistics_launcher, container, false);


        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage(getString(R.string.pleaseWait));
        prgDialog.setCancelable(false);

        updateMeasuresBtn = (Button) view.findViewById(R.id.updateMeasuresBtn);
        updateMeasuresBtn.setOnClickListener(updateMeasures);

        updateDietBtn = (Button) view.findViewById(R.id.updateDietBtn);
        updateDietBtn.setOnClickListener(updateDiet);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        userId = prefs.getInt("userId", 0);

        getCurrentStats();

        statDate = (TextView) view.findViewById(R.id.statDate);
        Calendar cal = Calendar.getInstance();
        statDate.setText(Constants.queryDateFormat.format(cal.getTime()));

        final String[] groups = {"Pomiary ciała", "Wyniki siłowe"};

        measuresGroupsList = (ListView) view.findViewById(R.id.measuresGroupsList);
        measuresGroupsList.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.statistics_header, groups));

        measuresGroupsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos,   long id) {

                ArrayList<Integer> measures = new ArrayList<Integer>();

                if(pos == 0){

                    for (Measure el: body) {
                        measures.add(el.getValue());
                    }

                    Intent i = new Intent(getContext(), StatisticsBodyMeasures.class);
                    i.putExtra("stats", measures);
                    getContext().startActivity(i);
                }else if(pos == 1){

                    for (Measure el: power) {
                        measures.add(el.getValue());
                    }

                    Intent i = new Intent(getContext(), StatisticsTrainingMeasures.class);
                    i.putExtra("stats", measures);
                    getContext().startActivity(i);
                }
            }
        });

        return view;

    }

    @Override
    public void onResume(){
        super.onResume();
        getCurrentStats();
    }

    View.OnClickListener updateMeasures = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getContext(), StatisticsUpdateBodyMeasures.class);
            startActivity(i);
        }
    };

    View.OnClickListener updateDiet = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getContext(), DietSettings.class);
            startActivity(i);
        }
    };

    private void getCurrentStats() {

        prgDialog.show();
        RequestParams params = new RequestParams();
        params.put("userId", userId);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.host + "/stats/getcurrent", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);

                setCurrentStats(response);
                prgDialog.hide();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

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

    private void setCurrentStats(String response) {

        body = new ArrayList<>();
        power = new ArrayList<>();
        all = new ArrayList<>();

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Measure>>() {
        }.getType();
        all = gson.fromJson(response, listType);

        for (Measure measure : all) {

            switch (measure.getGroupId()) {
                case 1:
                    body.add(measure);
                    break;
                case 2:
                    power.add(measure);
                    break;
                default:
                    break;
            }
        }

        }

    }



