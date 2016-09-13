package mgr.mtp.Diet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import mgr.mtp.DataModel.Product;
import mgr.mtp.R;
import mgr.mtp.Utils.Constants;

/**
 * Created by lukas on 29.08.2016.
 */
public class DietMealHomeListAdapter extends BaseAdapter {

    private Activity context;
    private static LayoutInflater inflater=null;
    private ArrayList<Product> products = new ArrayList<>();
    ProgressDialog prgDialog;

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
        ImageView removeProduct;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final int id = position;
        ViewHolder holder;
        final Context context = parent.getContext();

        final Product tempProduct = products.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.diet_meal_products_list, null);
            holder = new ViewHolder();

            prgDialog = new ProgressDialog(convertView.getContext());
            prgDialog.setMessage(convertView.getContext().getString(R.string.pleaseWait));
            prgDialog.setCancelable(false);


            holder.productName = (TextView) convertView.findViewById(R.id.productName);
            holder.productAmount = (TextView) convertView.findViewById(R.id.kcalValue);
            holder.productUnit = (TextView) convertView.findViewById(R.id.productUnit);
            holder.productCalories = (TextView) convertView.findViewById(R.id.productCalories);
            holder.removeProduct = (ImageView) convertView.findViewById(R.id.deleteProduct);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.productName.setText(tempProduct.getName());
        holder.productAmount.setText(String.valueOf(tempProduct.getAmount()));
        holder.productUnit.setText(String.valueOf(tempProduct.getUnit()));
        holder.productCalories.setText(String.valueOf(tempProduct.getCalories()));

        holder.removeProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Uwaga");
                alert.setMessage("Czy chcesz usunąć produkt?");
                alert.setPositiveButton("Tak", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        prgDialog.show();
                        RequestParams params = new RequestParams();
                        params.put("id", tempProduct.getId());

                        AsyncHttpClient client = new AsyncHttpClient();
                        client.get(Constants.host + "/meals/removeitem", params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                prgDialog.hide();
                                Toast.makeText(context, context.getString(R.string.successRemove), Toast.LENGTH_LONG).show();

                                products.remove(position);
                                dataChanged(products);
                                notifyDataSetInvalidated();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                prgDialog.hide();

                                if (statusCode == 404) {
                                    Toast.makeText(context, context.getString(R.string.noConnectionToServer), Toast.LENGTH_LONG).show();
                                } else if (statusCode == 500) {
                                    Toast.makeText(context, context.getString(R.string.serverError), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(context, context.getString(R.string.unexpectedError), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("Nie", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();
            }
        });

        return convertView;
    }

    public void dataChanged(ArrayList<Product> products) {
        this.products = products;
        TextView totalCalories = (TextView) context.findViewById(R.id.totalCalories);

        float total = 0;
        for (Product el: products) {
            total = total + el.getCalories();
        }
        totalCalories.setText(total+" kcal");
    }
}
