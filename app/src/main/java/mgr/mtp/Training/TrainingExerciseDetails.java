package mgr.mtp.Training;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import mgr.mtp.DataModel.Measure;
import mgr.mtp.R;
import mgr.mtp.Utils.Constants;

public class TrainingExerciseDetails extends AppCompatActivity {

    String date, exerciseName;
    int userId,exerciseId;
    ArrayList<Integer> weights;
    ArrayList<Integer> reps;
    TextView trainigDate;
    LineChart mChart;
    TextView set1reps, set2reps,set3reps, set4reps,set5reps,set1weight,
    set2weight,set3weight,set4weight,set5weight;
    ProgressDialog prgDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_exercise_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage(getString(R.string.pleaseWait));
        prgDialog.setCancelable(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            date = extras.getString("date");
            exerciseName = extras.getString("exerciseName");
            exerciseId = extras.getInt("exerciseId");
            weights = extras.getIntegerArrayList("weights");
            reps = extras.getIntegerArrayList("reps");
            userId = extras.getInt("userId");
        }

        trainigDate = (TextView) findViewById(R.id.trainingDate);
        set1reps = (TextView) findViewById(R.id.set1reps);
        set1weight = (TextView) findViewById(R.id.set1weight);
        set2reps = (TextView) findViewById(R.id.set2reps);
        set2weight = (TextView) findViewById(R.id.set2weight);
        set3reps = (TextView) findViewById(R.id.set3reps);
        set3weight = (TextView) findViewById(R.id.set3weight);
        set4reps = (TextView) findViewById(R.id.set4reps);
        set4weight = (TextView) findViewById(R.id.set4weight);
        set5reps = (TextView) findViewById(R.id.set5reps);
        set5weight = (TextView) findViewById(R.id.set5weight);

        toolbar.setTitle(exerciseName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        set1reps.setText(""+reps.get(0));
        set1weight.setText(""+weights.get(0));
        set2reps.setText(""+reps.get(1));
        set2weight.setText(""+weights.get(1));
        set3reps.setText(""+reps.get(2));
        set3weight.setText(""+weights.get(2));
        set4reps.setText(""+reps.get(3));
        set4weight.setText(""+weights.get(3));
        set5reps.setText(""+reps.get(4));
        set5weight.setText(""+weights.get(4));

        trainigDate.setText(date);

        mChart = (LineChart) findViewById(R.id.compareChart);

        getChartData();
    }

    private void getChartData() {

        prgDialog.show();
        RequestParams params = new RequestParams();
        params.put("exerciseId", exerciseId);
        params.put("date", date);
        params.put("userId", userId);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.host + "/stats/getprevioustrainingchart", params, new AsyncHttpResponseHandler() {
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

        ArrayList<Entry> previousEntries = new ArrayList<>();
        ArrayList<String> previousLabels = new ArrayList<>();

        for(int i = 0; i < measures.size(); i++){
            previousEntries.add(new Entry(i,measures.get(i).getValue()));
            previousLabels.add(""+measures.get(i).getTypeId());
        }

        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for(int i = 0; i < weights.size(); i++){
            entries.add(new Entry(i,weights.get(i)));
            labels.add(""+(i+1));
        }

        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setTextSize(13);
        xAxis.setYOffset(20);

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setDrawGridLines(true);
        yAxis.setDrawLabels(false);
        yAxis.setTextSize(13);

        YAxis yAxis1 = mChart.getAxisRight();
        yAxis1.setDrawGridLines(true);
        yAxis1.setDrawLabels(false);

        List<ILineDataSet> lines = new ArrayList<ILineDataSet> ();
        final String[] xAxisLabels = new String[] {"1", "2", "3", "4", "5"};
        LineDataSet lDataSet1 = new LineDataSet(previousEntries, "previousTrainig");
        lDataSet1.setDrawFilled(true);
        lDataSet1.setColor(Color.RED);
        lDataSet1.setCircleColor(Color.RED);
        lines.add(lDataSet1);

        LineDataSet lDataSet2 = new LineDataSet(entries, "currentTraining");
        lDataSet2.setDrawFilled(true);
        lines.add(lDataSet2);

        LineData data = new LineData(lines);

        mChart.setData(new LineData(lines));



        lDataSet1.setLineWidth(3);

        data.setValueTextSize(13);

        AxisValueFormatter formatter = new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xAxisLabels[(int) value];
            }

            // we don't draw numbers, so no decimal digits needed
            @Override
            public int getDecimalDigits() {  return 0; }
        };

        lDataSet1.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return ((int) value) + " kg";
            }
        });

        lDataSet2.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return ((int) value) + " kg";
            }
        });

        XAxis x = mChart.getXAxis();
        x.setGranularity(1f); // minimum axis-step (interval) is 1
        x.setValueFormatter(formatter);

        mChart.setExtraOffsets(25,10,30,10);
        mChart.setDescription("");
        mChart.getLegend().setEnabled(false);
        mChart.setData(data);
        mChart.invalidate();

    }

}
