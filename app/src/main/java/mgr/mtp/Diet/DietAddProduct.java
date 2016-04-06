package mgr.mtp.Diet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.nio.charset.StandardCharsets;

import cz.msebera.android.httpclient.Header;
import mgr.mtp.Home;
import mgr.mtp.R;
import mgr.mtp.Utils.Constants;

/**
 * Created by lukas on 27.03.2016.
 */
public class DietAddProduct extends AppCompatActivity {

    ProgressDialog prgDialog;
    int mealId, productId, userId;
    float proteins, carbs, fat, calories, factor;
    float calculatedFat, calculatedProteins, calculatedCalories, calculatedCarbs, calculatedAmount;
    Toolbar toolbar;
    String date, productName, unit;
    TextView caloriesValue, fatValue, carbsValue, proteinValue, txtProductName, factorValue;
    EditText amount;
    Button btnAdd;
    Spinner sp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diet_addproduct);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage(getString(R.string.pleaseWait));
        prgDialog.setCancelable(false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        userId = prefs.getInt("userId", 0);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            date = extras.getString("date");
            mealId = extras.getInt("mealId");
            productName = extras.getString("name");
            productId = extras.getInt("productId");
            proteins = extras.getFloat("productProtein");
            carbs = extras.getFloat("productCarbs");
            fat = extras.getFloat("productFat");
            calories = extras.getFloat("productCalories");
            factor = extras.getFloat("factor");
        }

        caloriesValue = (TextView) findViewById(R.id.caloriesValue);
        proteinValue = (TextView) findViewById(R.id.proteinValue);
        factorValue = (TextView) findViewById(R.id.factorValue);
        carbsValue = (TextView) findViewById(R.id.carbsValue);
        fatValue = (TextView) findViewById(R.id.fatValue);
        amount = (EditText) findViewById(R.id.amount);
        btnAdd = (Button) findViewById(R.id.btnAdd);

        amount.setFocusable(true);
        amount.requestFocus();
        amount.requestFocusFromTouch();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        caloriesValue.setText("" + calories);
        proteinValue.setText("" + proteins);
        fatValue.setText("" + fat);
        carbsValue.setText("" + carbs);
        factorValue.setText("" + factor + " g");

        unit = "g";

        getSupportActionBar().setTitle(productName);


        amount.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.equals("")) { //do your work here }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {

                if (s.toString() != null && !s.toString().isEmpty()) {
                    Float localAmount = Float.parseFloat(s.toString());

                    if (sp.getSelectedItem().equals("g")) {
                        // g
                        unit = "g";
                        calculatedAmount = localAmount;
                        setLabels(calculatedAmount);
                    }

                    if (sp.getSelectedItem().equals("szt")) {
                        // szt
                        unit = "szt";
                        calculatedAmount = factor * localAmount;
                        setLabels(calculatedAmount);

                    }
                } else {
                    setLabels((float) 0);
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Float finalAmount = !amount.getText().toString().isEmpty() ?
                        Float.parseFloat(amount.getText().toString()) : (float) 0;

                prgDialog.show();
                RequestParams params = new RequestParams();
                params.put("mealId", mealId + 1);
                params.put("date", date);
                params.put("productId", productId);
                params.put("unit", unit);
                params.put("amount", finalAmount);
                params.put("calories", calculatedCalories);
                params.put("fat", calculatedFat);
                params.put("carbs", calculatedCarbs);
                params.put("proteins", calculatedProteins);
                params.put("productName", productName);
                params.put("userId", userId);

                AsyncHttpClient client = new AsyncHttpClient();
                client.get(Constants.host + "/meals/additem", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        prgDialog.hide();

                        Intent homeIntent = new Intent(getApplicationContext(), Home.class);
                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        homeIntent.putExtra("date", date);
                        startActivity(homeIntent);

                        Toast.makeText(DietAddProduct.this,
                                getString(R.string.successAdd), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        String response = new String(responseBody, StandardCharsets.UTF_8);

                        prgDialog.hide();

                        if (statusCode == 404) {
                            Toast.makeText(DietAddProduct.this,
                                    getString(R.string.noConnectionToServer), Toast.LENGTH_LONG).show();
                        } else if (statusCode == 500) {
                            Toast.makeText(DietAddProduct.this,
                                    getString(R.string.serverError), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(DietAddProduct.this,
                                    getString(R.string.unexpectedError), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        sp = (Spinner) findViewById(R.id.unitSpinner);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String input = amount.getText().toString();
                Float localAmount = Float.parseFloat(amount.getText().toString());

                if (input != null && !input.isEmpty()) {
                    if (sp.getSelectedItemPosition() == 0) {
                        // g
                        calculatedAmount = localAmount;
                        setLabels(calculatedAmount);
                    }

                    if (sp.getSelectedItemPosition() == 1) {
                        // szt
                        calculatedAmount = factor * localAmount;
                        setLabels(calculatedAmount);
                    }
                } else {
                    setLabels((float) 0);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

    }

    private void setLabels(Float s) {

        calculatedFat = s * (fat / 100);
        calculatedCalories = s * (calories / 100);
        calculatedCarbs = s * (carbs / 100);
        calculatedProteins = s * (proteins / 100);

        calculatedFat = (float) Math.round(calculatedFat * 100) / 100;
        calculatedCalories = (float) Math.round(calculatedCalories * 100) / 100;
        calculatedCarbs = (float) Math.round(calculatedCarbs * 100) / 100;
        calculatedProteins = (float) Math.round(calculatedProteins * 100) / 100;

        caloriesValue.setText(String.valueOf(calculatedCalories) + " kcal");
        carbsValue.setText(String.valueOf(calculatedCarbs) + " g");
        fatValue.setText(String.valueOf(calculatedFat) + " g");
        proteinValue.setText(String.valueOf(calculatedProteins) + " g");
    }
}
