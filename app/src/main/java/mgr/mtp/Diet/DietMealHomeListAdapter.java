package mgr.mtp.Diet;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mgr.mtp.DataModel.Product;
import mgr.mtp.R;

/**
 * Created by lukas on 29.08.2016.
 */
public class DietMealHomeListAdapter extends BaseAdapter {

    private Activity context;
    private static LayoutInflater inflater=null;
    private ArrayList<Product> products = new ArrayList<>();

    int count,mealId;

    public DietMealHomeListAdapter(Activity context) {

        this.context = context;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView productName;
        TextView productAmount;
        TextView productUnit;
        TextView productCalories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        final Product tempProduct = products.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.diet_meal_products_list, null);
            holder = new ViewHolder();

            holder.productName = (TextView) convertView.findViewById(R.id.productName);
            holder.productAmount = (TextView) convertView.findViewById(R.id.productAmount);
            holder.productUnit = (TextView) convertView.findViewById(R.id.productUnit);
            holder.productCalories = (TextView) convertView.findViewById(R.id.productCalories);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.productName.setText(tempProduct.getName());
        holder.productAmount.setText(String.valueOf(tempProduct.getAmount()));
        holder.productUnit.setText(String.valueOf(tempProduct.getUnit()));
        holder.productCalories.setText(String.valueOf(tempProduct.getCalories()));


        return convertView;
    }

    public void dataChanged(ArrayList<Product> products) {
        this.products = products;
    }
}
