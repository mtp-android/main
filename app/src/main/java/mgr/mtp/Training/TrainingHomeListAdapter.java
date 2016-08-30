package mgr.mtp.Training;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import mgr.mtp.DataModel.ExerciseSet;
import mgr.mtp.R;
import mgr.mtp.Utils.Constants;

/**
 * Created by lmedrzycki on 30.08.2016.
 */
public class TrainingHomeListAdapter extends BaseAdapter{

    Date date;
    String[] exercises;
    private Activity context;
    ArrayList<ExerciseSet> all;
    int userId;
    PopupWindow mPopupWindow;

    private static LayoutInflater inflater=null;
    public TrainingHomeListAdapter(Activity context, String[] exercises, int userId) {
        // TODO Auto-generated constructor stub
        this.exercises = exercises;
        this.context = context;
        this.userId = userId;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
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
        rowView = inflater.inflate(R.layout.training_header_list, null);
        holder.exerciseName =(TextView) rowView.findViewById(R.id.exerciseName);
        holder.exerciseName.setText(exercises[position]);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Integer> weights = new ArrayList<Integer>();
                ArrayList<Integer> reps = new ArrayList<Integer>();

                for (ExerciseSet set: all) {
                    if(set.getExerciseId() == position+1){
                        weights.add(set.getWeight());
                        reps.add(set.getReps());
                    }
                }

                Intent intent = new Intent(context, TrainingExerciseDetails.class);
                intent.putExtra("date", Constants.queryDateFormat.format(date));
                intent.putExtra("exerciseName", exercises[position]);
                intent.putExtra("reps", reps);
                intent.putExtra("weights", weights);
                intent.putExtra("userId", userId);
                context.startActivity(intent);

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

    public void dataChanged(ArrayList<ExerciseSet> exercises) {
        this.all = exercises;
    }
}
