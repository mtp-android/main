package mgr.mtp.Statistics;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import mgr.mtp.DataModel.Measure;
import mgr.mtp.R;
import mgr.mtp.Utils.Constants;

/**
 * Created by lmedrzycki on 14.04.2016.
 */
public class StatisticsDisplayChart extends AppCompatActivity {

    LineChart mChart;
    Toolbar toolbar;
    String statisticName = "";
    ProgressDialog prgDialog;
    Integer userId, typeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            statisticName = extras.getString("statisticName");
            typeId = extras.getInt("typeId");
        }

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage(getString(R.string.pleaseWait));
        prgDialog.setCancelable(false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = prefs.getInt("userId", 0);


        setContentView(R.layout.statistics_chart);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(statisticName);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mChart = (LineChart) findViewById(R.id.chart);

        getChartData();

    }

    private void getChartData() {

        prgDialog.show();
        RequestParams params = new RequestParams();
        params.put("typeId", typeId);
        params.put("userId", userId);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.host + "/stats/getchartdata", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);

                prgDialog.hide();
                setChartData(response);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                prgDialog.hide();

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

    private void setChartData(String response) {

        final ArrayList<Measure> measures;

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Measure>>() {
        }.getType();
        measures = gson.fromJson(response, listType);

        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for(int i = 0; i < measures.size(); i++){
            entries.add(new Entry(measures.get(i).getValue(), i));
            labels.add(measures.get(i).getDate());
        }

        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setTextSize(13);
        xAxis.setSpaceBetweenLabels(5);
        xAxis.setYOffset(20);


        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setDrawGridLines(true);
        yAxis.setDrawLabels(false);
        yAxis.setTextSize(13);



        YAxis yAxis1 = mChart.getAxisRight();
        yAxis1.setDrawGridLines(true);
        yAxis1.setDrawLabels(false);

        LineDataSet dataSet = new LineDataSet(entries,"");
        LineData data = new LineData(labels, dataSet);

        dataSet.setLineWidth(3);

        data.setValueTextSize(13);


        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return ((int) value) + " "+ measures.get(dataSetIndex).getUnit();
            }
        });

        mChart.setExtraOffsets(25,10,30,10);
        mChart.setDescription("");
        mChart.getLegend().setEnabled(false);
        mChart.setData(data);
        mChart.invalidate();
    }
}
