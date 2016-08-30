package mgr.mtp.Diet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import mgr.mtp.DataModel.Product;
import mgr.mtp.R;
import mgr.mtp.Utils.Constants;

public class DietMealHome extends AppCompatActivity {

    Toolbar toolbar;
    String mealName, date;
    int mealId = 0, userId = 0;
    TextView mealDate;
    ProgressDialog prgDialog;
    ListView listView;
    DietMealHomeListAdapter listViewAdapter = null;
    ArrayList<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_meal_home);

        listView = (ListView) findViewById(R.id.mealDetails);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            date = extras.getString("date");
            mealId = extras.getInt("mealId");
            mealName = extras.getString("mealName");
            userId = extras.getInt("userId");
        }

        // Assign adapter to ListView
        listViewAdapter = new DietMealHomeListAdapter(DietMealHome.this);
        listView.setAdapter(listViewAdapter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mealDate = (TextView) findViewById(R.id.mealDate);
        setSupportActionBar(toolbar);

        toolbar.setTitle(mealName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        mealDate.setText(date);

        prgDialog = new ProgressDialog(DietMealHome.this);
        prgDialog.setMessage(getString(R.string.pleaseWait));
        prgDialog.setCancelable(false);

        getProductsForMeal(date, mealId);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DietMealHome.this, DietSearchProduct.class);
                                intent.putExtra("date", date);
                                intent.putExtra("mealId", mealId);
                                intent.putExtra("mealName", mealName);
                                DietMealHome.this.startActivity(intent);

                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }

    private void getProductsForMeal(String date, int mealId) {

        prgDialog.show();
        RequestParams params = new RequestParams();
        params.put("date", date);
        params.put("mealId", mealId);
        params.put("userId", userId);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.host + "/meals/getproductsformeal", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);

                setProductsForMeal(response);
                prgDialog.hide();

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

        public void setProductsForMeal(String response) {
        ArrayList<Product> products = new ArrayList<>();

        products.clear();

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Product>>() {
        }.getType();

        products = gson.fromJson(response, listType);

        listViewAdapter.dataChanged(products);
        listViewAdapter.notifyDataSetChanged();
    }

}
