package mgr.mtp.Statistics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

import mgr.mtp.R;

/**
 * Created by lmedrzycki on 14.04.2016.
 */
public class StatisticsDisplayChart extends AppCompatActivity {

    LineChart mChart;
    Toolbar toolbar;
    String statisticName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            statisticName = extras.getString("statisticName");
        }

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

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(80, 0));
        entries.add(new Entry(85, 1));
        entries.add(new Entry(90, 2));
        entries.add(new Entry(95, 3));
        entries.add(new Entry(90, 4));
        entries.add(new Entry(85, 5));
        entries.add(new Entry(95, 6));
        entries.add(new Entry(100, 7));
        entries.add(new Entry(105, 8));
        entries.add(new Entry(107, 9));

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("16.05");
        labels.add("16.05");
        labels.add("16.05");
        labels.add("16.05");
        labels.add("16.05");
        labels.add("16.05");
        labels.add("16.05");
        labels.add("16.05");
        labels.add("16.05");
        labels.add("16.05");


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

        LineDataSet dataset = new LineDataSet(entries,"kg");
        LineData data = new LineData(labels, dataset);

        dataset.setLineWidth(3);

        data.setValueTextSize(13);


        dataset.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return ((int) value) + " kg";
            }
        });

        mChart.setExtraOffsets(20,10,30,10);
        mChart.setDescription("");
        mChart.getLegend().setEnabled(false);
        mChart.setData(data);

    }
}

