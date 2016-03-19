package mgr.mtp;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import mgr.mtp.DataModel.Food;

/**
 * Created by lukas on 25.02.2016.
 */
public class HomeDiet extends Fragment {

    ExpandableListAdapter expListAdapter = null;

    private Toolbar toolbar;
    ProgressDialog prgDialog;
    private TextView dietDate;
    private Button setDateBtn;
    DateFormat queryDateFormat;

    Date selectedDate;
    List<String> groupList;
    List<Food> childList;
    LinkedHashMap<String, List<Food>> mealsCollection;
    ExpandableListView expListView;

    public HomeDiet() {
        // Required empty public constructor
    }

    private void createGroupList() {
        groupList = new ArrayList<String>();
        groupList.add("Śniadanie");
        groupList.add("II Śniadanie");
        groupList.add("Obiad");
        groupList.add("Podwieczorek");
        groupList.add("Kolacja");
    }


    private void loadChild(List<Food> mealIngredients) {
            childList = new ArrayList<Food>();
            childList.addAll(mealIngredients);
    }

    private void setGroupIndicatorToRight() {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
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

        Date selectedDate = new Date();
        String today = new SimpleDateFormat("dd-MM-yyyy").format(selectedDate);
        queryDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        dietDate = (TextView) view.findViewById(R.id.dietDate);
        dietDate.setText(today);

        setDateBtn = (Button) view.findViewById(R.id.setDateBtn);
        setDateBtn.setOnClickListener(setDateBtnListener);

        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Proszę czekać...");
        prgDialog.setCancelable(false);

        // prepare static meals
        createGroupList();

        // meals details for day
        getMealsForDay(selectedDate.getYear(),selectedDate.getMonth(), selectedDate.getDay());


        expListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        expListAdapter = new ExpandableListAdapter(
                getActivity(), groupList, mealsCollection);

        expListView.setAdapter(expListAdapter);
        expListAdapter.setDate(selectedDate);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });

        return view;
    }

    private void getMealsForDay(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        Date preDate = cal.getTime();

        String date = new SimpleDateFormat("yyyy-MM-dd").format(preDate);

        prgDialog.show();
        RequestParams params = new RequestParams();
        params.put("date",date);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://10.0.2.2:8080/meals/getmeals",params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody, "UTF-8");

                        setMealsOnDay(response);
                        prgDialog.hide();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    String response = new String(responseBody, "UTF-8");

                    //prgDialog.hide();

                    if (statusCode == 404) {
                        Toast.makeText(getActivity(), "Brak połączenia z serwerem", Toast.LENGTH_LONG).show();
                    } else if (statusCode == 500) {
                        Toast.makeText(getActivity(), "Błąd serwera", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Nietypowy wyjątek", Toast.LENGTH_LONG).show();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            });

    }


    public void setMealsOnDay(String response)
    {
        ArrayList<Food> breakfast = new ArrayList<>(),secondBreakfast = new ArrayList<>(),
                dinner = new ArrayList<>(),afternoonSnacks = new ArrayList<>(),supper = new ArrayList<>()
                ,all;

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Food>>(){}.getType();
        all = gson.fromJson(response, listType);

        for(Food food: all){

            switch (food.getMeal()) {
                case 0:
                    breakfast.add(food);
                    break;

                case 1:
                    secondBreakfast.add(food);
                    break;

                case 2:
                    dinner.add(food);
                    break;

                case 3:
                    afternoonSnacks.add(food);
                    break;

                case 4:
                    supper.add(food);
                    break;

                default:
                    break;
            }

        }

        mealsCollection = new LinkedHashMap<String, List<Food>>();

        for (String meal : groupList) {
            if (meal.equals("Śniadanie")) {
                loadChild(breakfast);
            } else if (meal.equals("II Śniadanie"))
                loadChild(secondBreakfast);
            else if (meal.equals("Obiad"))
                loadChild(dinner);
            else if (meal.equals("Podwieczorek"))
                loadChild(afternoonSnacks);
            else if (meal.equals("Kolacja"))
                loadChild(supper);

            mealsCollection.put(meal, childList);


        }

        expListAdapter.dataChanged(mealsCollection);
        expListAdapter.notifyDataSetChanged();

    }

            private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
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

            Date date = new Date(year,monthOfYear,dayOfMonth);

            expListAdapter.setDate(date);
            getMealsForDay(year,monthOfYear,dayOfMonth);

            // update label
            dietDate.setText(
                    String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(year));
        }


    };


}
