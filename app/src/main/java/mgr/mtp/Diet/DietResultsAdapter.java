package mgr.mtp.Diet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import mgr.mtp.DataModel.Product;
import mgr.mtp.R;

/**
 * Created by lukas on 04.04.2016.
 */
public class DietResultsAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;

    private ArrayList<Product> productDetails = new ArrayList<>();
    int count,mealId;
    String date;
    Context context;

    //constructor method
    public DietResultsAdapter(Context context, ArrayList<Product> product_details, String date, int mealId) {

        layoutInflater = LayoutInflater.from(context);
        this.mealId = mealId;
        this.date = date;
        this.productDetails = product_details;
        this.count = product_details.size();
        this.context = context;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Product getItem(int arg0) {
        return productDetails.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        final Product tempProduct = productDetails.get(position);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.diet_searchresults, null);
            holder = new ViewHolder();

            holder.product_name = (TextView) convertView.findViewById(R.id.product_name);
            holder.product_proteins = (TextView) convertView.findViewById(R.id.product_protein);
            holder.product_carbs = (TextView) convertView.findViewById(R.id.product_carbs);
            holder.product_fat = (TextView) convertView.findViewById(R.id.product_fat);
            holder.product_calories = (TextView) convertView.findViewById(R.id.product_calories);

            holder.addToMeal = (Button) convertView.findViewById(R.id.add_cart);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.product_name.setText(tempProduct.getName());
        holder.product_proteins.setText(String.valueOf(tempProduct.getProteins()));
        holder.product_carbs.setText(String.valueOf(tempProduct.getCarbs()));
        holder.product_fat.setText(String.valueOf(tempProduct.getFat()));
        holder.product_calories.setText(String.valueOf(tempProduct.getCalories()));

        holder.addToMeal.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent intent= new Intent(context, DietAddProduct.class);
                intent.putExtra("date",date);
                intent.putExtra("name", tempProduct.getName());
                intent.putExtra("productId",tempProduct.getId());
                intent.putExtra("productProtein",tempProduct.getProteins());
                intent.putExtra("productCarbs",tempProduct.getCarbs());
                intent.putExtra("productFat",tempProduct.getFat());
                intent.putExtra("productCalories",tempProduct.getCalories());
                intent.putExtra("mealId",mealId);
                intent.putExtra("factor",tempProduct.getFactor());

                context.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView product_name;
        TextView product_proteins;
        TextView product_carbs;
        TextView product_fat;
        TextView product_calories;

        Button addToMeal;

    }
}
