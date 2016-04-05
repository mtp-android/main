package mgr.mtp.Training;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import mgr.mtp.DataModel.ExerciseSet;
import mgr.mtp.Diet.DietAddProduct;
import mgr.mtp.R;
import mgr.mtp.Utils.Constants;
import mgr.mtp.Utils.DatePickerFragment;

/**
 * Created by lukas on 25.02.2016.
 */
public class TrainingHome extends Fragment {

    ExpandableListView expListView;
    TrainingListAdapter expListAdapter = null;
    LinkedHashMap<String, List<ExerciseSet>> excersisesCollection;

    List<String> groupList;
    List<ExerciseSet> childList;
    TextView trainingDate;
    Button startTraining,setDateBtn;
    Date selectedDate;

    public TrainingHome() {
        // Required empty public constructor
    }

    private void createGroupList() {
        groupList = new ArrayList<>();
        groupList.add(getString(R.string.squats));
        groupList.add(getString(R.string.benchPress));
        groupList.add(getString(R.string.barbellRow));
        groupList.add(getString(R.string.barbellCurls));
        groupList.add(getString(R.string.dips));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.hometraining, container, false);

        Calendar cal = Calendar.getInstance();
        selectedDate = cal.getTime();
        String today = getArguments() != null ? getArguments().getString("date") : Constants.displayDateFormat.format(selectedDate);

        trainingDate = (TextView) view.findViewById(R.id.trainingDate);
        startTraining = (Button) view.findViewById(R.id.startTraining);
        setDateBtn = (Button) view.findViewById(R.id.setDateBtn);

        trainingDate.setText(today);
        setDateBtn.setOnClickListener(setDateBtnListener);
        startTraining.setOnClickListener(setStartTrainingListener);

        // prepare static exercises
        createGroupList();

        expListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        expListAdapter = new TrainingListAdapter(
                getActivity(), groupList, excersisesCollection, this);

        expListView.setAdapter(expListAdapter);
        expListAdapter.setDate(selectedDate);


        return view;
    }


    private View.OnClickListener setStartTrainingListener = new View.OnClickListener() {

        @Override

        public void onClick(View v) {
            startActivity(new Intent(getContext(), TrainingWorkout.class));
        }

    };

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
            //getMealsForDay(date);
            //getSummaryForDay(date);

            // update label
            trainingDate.setText(Constants.displayDateFormat.format(date));
        }


    };

}