package mgr.mtp.Diet;

/**
 * Created by lmedrzycki on 26.02.2016.
 */

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
import mgr.mtp.Utils.Constants;

public class DietHomeListAdapter extends BaseAdapter {
    String [] result;
    Date date;
    private Activity context;
    int userId;

    int [] calories = {0,0,0,0,0};
    private static LayoutInflater inflater=null;
    public DietHomeListAdapter(Activity context, String[] mealList, int userId) {
        // TODO Auto-generated constructor stub
        result= mealList;
        this.context = context;
        this.userId = userId;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
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

    public class Holder
    {
        TextView mealName;
        TextView caloriesValue;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.diet_header_list, null);
        holder.mealName =(TextView) rowView.findViewById(R.id.productName);
        holder.mealName.setText(result[position]);
        holder.caloriesValue = (TextView) rowView.findViewById(R.id.kcalValue);
        holder.caloriesValue.setText(""+calories[position]);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(context, DietMealHome.class);
                intent.putExtra("date", Constants.queryDateFormat.format(date));
                intent.putExtra("mealId", position);
                intent.putExtra("mealName", result[position]);
                intent.putExtra("userId", userId);
                context.startActivity(intent);
            }
        });
        return rowView;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void dataChanged(int[] calories) {
        this.calories = calories;
    }
}