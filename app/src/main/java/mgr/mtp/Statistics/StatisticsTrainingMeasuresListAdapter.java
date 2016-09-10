package mgr.mtp.Statistics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Date;

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

                int typeId = 0;

                typeId = exercises[position].equals("Przysiady ze sztangą") ? 8 : typeId;
                typeId = exercises[position].equals("Podciąganie na drążku w szerokim uchwycie") ? 9 : typeId;
                typeId = exercises[position].equals("Wyciskanie sztangi w pozycji leżącej (płasko)") ? 10 : typeId;
                typeId = exercises[position].equals("Wyciskanie sztangi nad głową") ? 11 : typeId;
                typeId = exercises[position].equals("Uginanie przedramion z hantlami") ? 12 : typeId;
                typeId = exercises[position].equals("Skłony w pozycji leżącej") ? 13 : typeId;
                typeId = exercises[position].equals("Rzymski martwy ciąg") ? 14 : typeId;
                typeId = exercises[position].equals("Wiosłowanie sztangi w pozycji półprostej") ? 15 : typeId;
                typeId = exercises[position].equals("Wyciskanie sztangi na ławce w pozycji skośnej") ? 16 : typeId;
                typeId = exercises[position].equals("Unoszenie hantli bokiem w pozycji siedzącej/stojącej") ? 17 : typeId;
                typeId = exercises[position].equals("Prostowanie ramion w dół na wyciągu górnym") ? 18 : typeId;
                typeId = exercises[position].equals("Unoszenie nóg w zwisie na drążku") ? 19 : typeId;
                typeId = exercises[position].equals("Prostowanie łydek") ? 20 : typeId;
                typeId = exercises[position].equals("Wznoszenie ramion z hantlami") ? 21 : typeId;
                typeId = exercises[position].equals("Rozpiętki z hantlami w pozycji leżącej") ? 22 : typeId;
                typeId = exercises[position].equals("Odwrotne rozpiętki na maszynie") ? 23 : typeId;
                typeId = exercises[position].equals("Zginanie nadgarstków z wykorzystaniem nachwytu") ? 24 : typeId;
                typeId = exercises[position].equals("Skręty tułowia") ? 25 : typeId;

                Intent i = new Intent(context, StatisticsDisplayChart.class);
                i.putExtra("statisticName", exercises[position]);
                i.putExtra("typeId", typeId);
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
