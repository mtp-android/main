package mgr.mtp.Training;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import mgr.mtp.DataModel.ExerciseSet;
import mgr.mtp.R;
import mgr.mtp.Utils.Constants;
import mgr.mtp.Utils.DatePickerFragment;

/**
 * Created by lukas on 25.02.2016.
 */
public class TrainingHome extends Fragment {

    ProgressDialog prgDialog;
    ListView listView;
    TrainingHomeListAdapter listViewAdapter;

    TextView trainingDate, trainingTooltip;
    FloatingActionButton startTraining;
    Button setDateBtn;
    Date selectedDate;
    int userId;

    ArrayList<ExerciseSet> squats;
    ArrayList<ExerciseSet> benchPress;
    ArrayList<ExerciseSet> barbellRow = new ArrayList<>();
    ArrayList<ExerciseSet> barbellCurls = new ArrayList<>();
    ArrayList<ExerciseSet> dips = new ArrayList<>();
    ArrayList<ExerciseSet> all = new ArrayList<>();

    public TrainingHome() {
        // Required empty public constructor
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
        String today = getArguments() != null ? getArguments().getString("date") : Constants.queryDateFormat.format(selectedDate);

        trainingTooltip = (TextView) view.findViewById(R.id.trainingToolTip);

        trainingDate = (TextView) view.findViewById(R.id.trainingDate);
        startTraining = (FloatingActionButton) view.findViewById(R.id.startTraining);
        setDateBtn = (Button) view.findViewById(R.id.setDateBtn);

        trainingDate.setText(today);
        setDateBtn.setOnClickListener(setDateBtnListener);
        startTraining.setOnClickListener(setStartTrainingListener);

        // create progress dialog
        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage(getString(R.string.pleaseWait));
        prgDialog.setCancelable(false);

        // get user id from settings
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        userId = prefs.getInt("userId", 0);

        listView = (ListView) view.findViewById(R.id.trainingListView);

        getTrainingForDay(selectedDate);

        // prepare static exercises
        String[] exercises = {getString(R.string.squats),getString(R.string.benchPress), getString(R.string.barbellRow),
                getString(R.string.barbellCurls), getString(R.string.dips)};

        listViewAdapter = new TrainingHomeListAdapter(getActivity(), exercises, userId);
        listView.setAdapter(listViewAdapter);

        listViewAdapter.setDate(selectedDate);

        return view;
    }


    private View.OnClickListener setStartTrainingListener = new View.OnClickListener() {

        @Override

        public void onClick(View v) {
            Intent intent = new Intent(getContext(), TrainingWorkout.class);
            intent.putExtra("date", Constants.queryDateFormat.format(selectedDate));

            getContext().startActivity(intent);
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

            selectedDate = date;

            listViewAdapter.setDate(date);
            getTrainingForDay(date);

            // update label
            trainingDate.setText(Constants.queryDateFormat.format(date));
        }


    };

    private void getTrainingForDay(Date selectedDate) {

        String date = Constants.queryDateFormat.format(selectedDate);

        prgDialog.show();
        RequestParams params = new RequestParams();
        params.put("date", date);
        params.put("userId", userId);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.host + "/training/gettraining", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody, StandardCharsets.UTF_8);

                if (response.equals("[]")) {
                    startTraining.setVisibility(View.VISIBLE);
                    trainingTooltip.setText("Brak zalogowanego treningu. Do dzieła!");
                    listView.setVisibility(View.INVISIBLE);
                } else {
                    startTraining.setVisibility(View.INVISIBLE);
                    trainingTooltip.setText("Wybierz nazwę ćwiczenia aby poznać szczegóły");
                    listView.setVisibility(View.VISIBLE);
                }
                setTrainingOnDay(response);
                prgDialog.hide();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

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

    private void setTrainingOnDay(String response) {

        squats = new ArrayList<>();
        benchPress = new ArrayList<>();
        barbellRow = new ArrayList<>();
        barbellCurls = new ArrayList<>();
        dips = new ArrayList<>();
        all = new ArrayList<>();

        Gson gson = new Gson();
        Type listType = new TypeToken<List<ExerciseSet>>() {
        }.getType();
        all = gson.fromJson(response, listType);

        for (ExerciseSet set : all) {

            switch (set.getExerciseId()) {
                case 1:
                    squats.add(set);
                    break;
                case 2:
                    benchPress.add(set);
                    break;
                case 3:
                    barbellRow.add(set);
                    break;
                case 4:
                    barbellCurls.add(set);
                    break;
                case 5:
                    dips.add(set);
                    break;
                default:
                    break;
            }
        }

        listViewAdapter.dataChanged(all);
    }

}