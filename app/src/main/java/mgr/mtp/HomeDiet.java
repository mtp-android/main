package mgr.mtp;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lukas on 25.02.2016.
 */
public class HomeDiet extends Fragment {

    private Toolbar toolbar;
    private TextView dietDate;
    private Button setDateBtn;


    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> mealsCollection;
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

    private void createCollection() {

        String[] breakfast = { "Jajko","Chleb" };
        String[] secondBreakfast = { "WPC KFD" };
        String[] dinner = { "Kurczak","Ryż" };
        String[] afternoonSnacks = { "Banan" };
        String[] supper = { "Wędzona makrela" };

        mealsCollection = new LinkedHashMap<String, List<String>>();

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
    }

    private void loadChild(String[] mealIngredients) {
        childList = new ArrayList<String>();
        for (String model : mealIngredients)
            childList.add(model);
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


        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");

        String today = sdf.format(c.getTime());

        dietDate = (TextView) view.findViewById(R.id.dietDate);
        dietDate.setText(today);

        setDateBtn = (Button) view.findViewById(R.id.setDateBtn);
        setDateBtn.setOnClickListener(setDateBtnListener);

        createGroupList();

        createCollection();

        expListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
                getActivity(), groupList, mealsCollection);

        expListView.setAdapter(expListAdapter);

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

            dietDate.setText(
                    String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1) + "-" + String.valueOf(year));
        }
    };


}
