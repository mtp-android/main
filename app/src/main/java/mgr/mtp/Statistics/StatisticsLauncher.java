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
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import mgr.mtp.DataModel.Measure;
import mgr.mtp.DataModel.Product;
import mgr.mtp.Diet.DietListAdapter;
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

    TextView statDate;
    List<String> groupList;
    List<Measure> childList;
    ExpandableListView expListView;
    LinkedHashMap<String, List<Measure>> measuresCollection;
    int userId;
    StatisticsListAdapter expListAdapter = null;
    ProgressDialog prgDialog;
    Button updateMeasuresBtn;

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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        userId = prefs.getInt("userId", 0);

        measuresCollection = new LinkedHashMap<>();
        createGroupList();
        getCurrentStats();

        statDate = (TextView) view.findViewById(R.id.statDate);
        Calendar cal = Calendar.getInstance();
        statDate.setText(Constants.queryDateFormat.format(cal.getTime()));
        expListView = (ExpandableListView) view.findViewById(R.id.measureExpList);
        expListAdapter = new StatisticsListAdapter(
                getActivity(), groupList, measuresCollection, this);

        expListView.setAdapter(expListAdapter);
        expListView.expandGroup(0);

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < groupList.size(); i++) {
                    if (i != groupPosition) {
                        expListView.collapseGroup(i);
                    }
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
        ArrayList<Measure> body = new ArrayList<>(), power = new ArrayList<>(), all;

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

        for (String measure : groupList) {
            if (measure.equals(getString(R.string.bodyMeasures))) {
                loadChild(body);
            } else if (measure.equals(getString(R.string.powerMeasures)))
                loadChild(power);

            measuresCollection.put(measure, childList);
        }
        expListAdapter.notifyDataSetChanged();
    }

    private void createGroupList() {
        groupList = new ArrayList<>();
        groupList.add(getString(R.string.bodyMeasures));
        groupList.add(getString(R.string.powerMeasures));
    }


    private void loadChild(List<Measure> measures) {
        childList = new ArrayList<>();
        childList.addAll(measures);
    }

}
