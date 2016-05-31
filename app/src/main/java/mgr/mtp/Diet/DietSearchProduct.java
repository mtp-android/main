package mgr.mtp.Diet;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.loopj.android.http.HttpGet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import mgr.mtp.DataModel.Product;
import mgr.mtp.R;
import mgr.mtp.Utils.Constants;
import mgr.mtp.Utils.JSONParser;

/**
 * Created by lukas on 27.03.2016.
 */
public class DietSearchProduct extends AppCompatActivity {

    SearchView search;
    ListView searchResults;
    Toolbar toolbar;
    int mealId;
    String date;

    ArrayList<Product> productResults = new ArrayList<>();
    ArrayList<Product> filteredProductResults = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diet_search);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            date = extras.getString("date");
            mealId = extras.getInt("mealId");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        search = (SearchView) findViewById(R.id.searchView1);
        search.setQueryHint("Wpisz nazwę produktu...");
        search.setFocusable(true);
        search.setIconified(false);
        search.requestFocusFromTouch();

        searchResults = (ListView) findViewById(R.id.listview_search);

        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                //Toast.makeText(v.getContext(), String.valueOf(hasFocus), Toast.LENGTH_SHORT).show();
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 2) {
                    searchResults.setVisibility(View.VISIBLE);
                    myAsyncTask m = (myAsyncTask) new myAsyncTask().execute(newText);
                } else {
                    searchResults.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
    }

    public void filterProductArray(String newText)
    {
        String pName;

        filteredProductResults.clear();
        for (int i = 0; i < productResults.size(); i++)
        {
            pName = productResults.get(i).getName().toLowerCase();
            if ( pName.contains(newText.toLowerCase()))
            {
                filteredProductResults.add(productResults.get(i));
            }
        }
    }

    //in this myAsyncTask, we are fetching data from server for the search string entered by user.
    class myAsyncTask extends AsyncTask<String, Void, String> {
        JSONParser jParser;
        JSONArray productList;
        String url = new String();
        String textSearch;
        ProgressDialog pd;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            productList = new JSONArray();
            jParser = new JSONParser();
            pd = new ProgressDialog(DietSearchProduct.this);
            pd.setCancelable(false);
            pd.setMessage("Szukam...");
            pd.getWindow().setGravity(Gravity.CENTER);
            pd.show();
        }

        @Override
        protected String doInBackground(String... sText) {

            url = Constants.host+"/products/getproducts/?search=" + sText[0];
            String returnResult = getProductList(url);
            this.textSearch = sText[0];
            return returnResult;

        }

        public String getProductList(String url) {

            Product tempProduct;
            String matchFound;

            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(url));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String json = in.readLine();

                JSONArray responseArray = new JSONArray(json);

                for (int i = 0; i < responseArray.length(); i++) {
                    JSONObject obj = responseArray.getJSONObject(i);

                    if (obj.has("name")) {

                        tempProduct = new Product();
                        tempProduct.setId(obj.getInt("id"));
                        tempProduct.setName(obj.getString("name"));
                        tempProduct.setProteins(obj.getInt("proteins"));
                        tempProduct.setCarbohydrates(obj.getInt("carbs"));
                        tempProduct.setFat(obj.getInt("fat"));
                        tempProduct.setCalories(obj.getInt("calories"));
                        tempProduct.setFactor(obj.getInt("factor"));

                        Log.e("name", tempProduct.getName());

                        //check if this product is already there in productResults, if yes, then don't add it again.
                        matchFound = "N";

                        for (int j=0; j < productResults.size();j++)
                        {
                            if (productResults.get(j).getId() == (tempProduct.getId()))
                            {
                                matchFound = "Y";
                            }
                        }

                        if (matchFound == "N")
                        {
                            productResults.add(tempProduct);
                        }
                    }
                }
                return "OK";

            } catch (Exception e) {
                e.printStackTrace();
                return ("Exception Caught");
            }
        }

    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);

        if (result.equalsIgnoreCase("Exception Caught")) {
            Toast.makeText(DietSearchProduct.this, "Unable to connect to server,please try later", Toast.LENGTH_LONG).show();

            pd.dismiss();
        } else {
            //calling this method to filter the search results from productResults and move them to
            //filteredProductResults
            filterProductArray(textSearch);
            searchResults.setAdapter(new DietResultsAdapter(DietSearchProduct.this, filteredProductResults, date, mealId));
            pd.dismiss();
        }}
    }
}



