package mgr.mtp.Statistics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import mgr.mtp.DataModel.ExerciseSet;
import mgr.mtp.R;

/**
 * Created by lmedrzycki on 01.09.2016.
 */
public class StatisticsTrainingMeasuresListAdapter extends BaseAdapter {

    String[] exercises;
    private Activity context;
    int userId;
    Date date;

    private static LayoutInflater inflater=null;
    public StatisticsTrainingMeasuresListAdapter(Activity context, String[] exercises, int userId) {
        // TODO Auto-generated constructor stub
        this.exercises = exercises;
        this.context = context;
        this.userId = userId;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return exercises.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.stats_training_list, null);
        holder.exerciseName =(TextView) rowView.findViewById(R.id.exerciseName);
        holder.exerciseName.setText(exercises[position]);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, StatisticsDisplayChart.class);
                i.putExtra("statisticName", exercises[position]);
                i.putExtra("typeId", position+7);
                context.startActivity(i);

            }
        });
        return rowView;
    }

    public class Holder
    {
        TextView exerciseName;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
