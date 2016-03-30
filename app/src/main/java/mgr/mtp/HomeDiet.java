package mgr.mtp;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import mgr.mtp.DataModel.Product;
import mgr.mtp.Diet.DatePickerFragment;
import mgr.mtp.Diet.ExpandableListAdapter;
import mgr.mtp.Utils.Constants;

/**
 * Created by lukas on 25.02.2016.
 */
public class HomeDiet extends Fragment {

    ExpandableListAdapter expListAdapter = null;

    private Toolbar toolbar;
    ProgressDialog prgDialog;
    private TextView dietDate;
    private Button setDateBtn;


    Date selectedDate;
    List<String> groupList;
    List<Product> childList;
    LinkedHashMap<String, List<Product>> mealsCollection;
    TextView caloriesTxt,proteinsTxt,fatTxt,carbsTxt;
    ExpandableListView expListView;
    ProgressBar caloriesBar,proteinsBar,fatBar,carbsBar;

    public HomeDiet() {

    }

    private void createGroupList() {
        groupList = new ArrayList<>();
        groupList.add(getString(R.string.breakfast));
        groupList.add(getString(R.string.secondBreakfast));
        groupList.add(getString(R.string.dinner));
        groupList.add(getString(R.string.afternoonSnacks));
        groupList.add(getString(R.string.supper));
    }


    private void loadChild(List<Product> mealIngredients) {
            childList = new ArrayList<>();
            childList.addAll(mealIngredients);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.homediet, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        caloriesBar = (ProgressBar) view.findViewById(R.id.caloriesBar);
        carbsBar = (ProgressBar) view.findViewById(R.id.carbsBar);
        fatBar = (ProgressBar) view.findViewById(R.id.fatBar);
        proteinsBar = (ProgressBar) view.findViewById(R.id.proteinBar);
        caloriesTxt = (TextView) view.findViewById(R.id.caloriesTxt);
        carbsTxt = (TextView) view.findViewById(R.id.carbsTxt);
        fatTxt = (TextView) view.findViewById(R.id.fatTxt);
        proteinsTxt = (TextView) view.findViewById(R.id.proteinTxt);

        refreshBars();

        Calendar cal = Calendar.getInstance();
        Date selectedDate = cal.getTime();

        String today = getArguments() != null ? getArguments().getString("date") : Constants.queryDateFormat.format(selectedDate);
        try {
            selectedDate = Constants.queryDateFormat.parse(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dietDate = (TextView) view.findViewById(R.id.dietDate);
        dietDate.setText(today);

        setDateBtn = (Button) view.findViewById(R.id.setDateBtn);
        setDateBtn.setOnClickListener(setDateBtnListener);

        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage(getString(R.string.pleaseWait));
        prgDialog.setCancelable(false);


        caloriesBar.setProgress(2000);
        fatBar.setProgress(2000);
        carbsBar.setProgress(2000);
        proteinsBar.setProgress(2000);

        // prepare static meals
        createGroupList();

        // meals details for day
        getMealsForDay(selectedDate);


        expListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        expListAdapter = new ExpandableListAdapter(
                getActivity(), groupList, mealsCollection, this);

        expListView.setAdapter(expListAdapter);
        expListAdapter.setDate(selectedDate);

        return view;
    }

    private void getMealsForDay(Date selectedDate) {

        String date = Constants.queryDateFormat.format(selectedDate);

        prgDialog.show();
        RequestParams params = new RequestParams();
        params.put("date", date);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.host + "/meals/getmeals", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);

                setMealsOnDay(response);
                prgDialog.hide();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = new String(responseBody, StandardCharsets.UTF_8);

                prgDialog.hide();

                if (statusCode == 404) {
                    Toast.makeText(getActivity(),
                            getString(R.string.noConnectionToServer), Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getActivity(),
                            getString(R.string.serverError), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(),
                            getString(R.string.unexpectedError), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void setMealsOnDay(String response)
    {
        ArrayList<Product> breakfast = new ArrayList<>(),secondBreakfast = new ArrayList<>(),
                        dinner = new ArrayList<>(),afternoonSnacks = new ArrayList<>(),
                        supper = new ArrayList<>(),all;

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Product>>(){}.getType();
        all = gson.fromJson(response, listType);

        for(Product product : all){

            switch (product.getMeal()) {
                case 0:
                    breakfast.add(product);
                    break;
                case 1:
                    secondBreakfast.add(product);
                    break;
                case 2:
                    dinner.add(product);
                    break;
                case 3:
                    afternoonSnacks.add(product);
                    break;
                case 4:
                    supper.add(product);
                    break;
                default:
                    break;
            }
        }

        mealsCollection = new LinkedHashMap<>();

        for (String meal : groupList) {
            if (meal.equals(getString(R.string.breakfast))) {
                loadChild(breakfast);
            } else if (meal.equals(getString(R.string.secondBreakfast)))
                loadChild(secondBreakfast);
            else if (meal.equals(getString(R.string.dinner)))
                loadChild(dinner);
            else if (meal.equals(getString(R.string.afternoonSnacks)))
                loadChild(afternoonSnacks);
            else if (meal.equals(getString(R.string.supper)))
                loadChild(supper);

            mealsCollection.put(meal, childList);

        }
        expListAdapter.dataChanged(mealsCollection);
        expListAdapter.notifyDataSetChanged();

    }

    private View.OnClickListener setDateBtnListener = new View.OnClickListener() {

        @Override

        public void onClick(View v) {
            showDatePicker();
        }

    };


    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(onDate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener onDate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Date date = cal.getTime();

            expListAdapter.setDate(date);
            getMealsForDay(date);

            // update label
            dietDate.setText(
                    String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(year));
        }


    };


    public void removeChild(int id, final int groupPosition, final int childPosition) {

        prgDialog.show();
        RequestParams params = new RequestParams();
        params.put("id", id);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.host + "/meals/removeitem", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody,StandardCharsets.UTF_8);
                prgDialog.hide();
                Toast.makeText(getActivity(),
                        getString(R.string.successRemove), Toast.LENGTH_LONG).show();

                List<Product> child = mealsCollection.get(groupList.get(groupPosition));
                child.remove(childPosition);
                expListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String response = new String(responseBody, StandardCharsets.UTF_8);

                prgDialog.hide();

                if (statusCode == 404) {
                    Toast.makeText(getActivity(),
                            getString(R.string.noConnectionToServer), Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getActivity(),
                            getString(R.string.serverError), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(),
                            getString(R.string.unexpectedError), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void refreshBars() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        int calories = prefs.getInt("calculatedCalories", 2000);
        int proteins = prefs.getInt("calculatedProteins", 100);
        int fat = prefs.getInt("calculatedFat", 100);
        int carbs = prefs.getInt("calculatedCarbs", 100);

        carbsBar.setMax(carbs);
        proteinsBar.setMax(proteins);
        fatBar.setMax(fat);
        caloriesBar.setMax(calories);

        caloriesTxt.setText("Kalorie: "+calories);
        proteinsTxt.setText("Białko: "+proteins);
        fatTxt.setText("Tłuszcze: "+fat);
        carbsTxt.setText("Węglowodany: "+carbs);






    }
}
