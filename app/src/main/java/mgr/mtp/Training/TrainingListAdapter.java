package mgr.mtp.Training;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import mgr.mtp.DataModel.ExerciseSet;
import mgr.mtp.R;

/**
 * Created by lukas on 04.04.2016.
 */
public class TrainingListAdapter extends BaseExpandableListAdapter {

    Date date;
    private Activity context;
    private LinkedHashMap<String, List<ExerciseSet>> excersisesCollection;
    private List<String> groupList;
    private TrainingHome fragment;

    public TrainingListAdapter(Activity context, List<String> groupList,
                           LinkedHashMap<String, List<ExerciseSet>> excersisesCollection, TrainingHome fragment) {
        this.context = context;
        this.excersisesCollection = excersisesCollection;
        this.groupList = groupList;
        this.fragment = fragment;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return excersisesCollection.get(groupList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String exerciseName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item_training,
                    null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.exerciseName);

        item.setTypeface(null, Typeface.BOLD);
        item.setText(exerciseName);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ExerciseSet set = (ExerciseSet) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item_diet, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.mealIngredient);

        item.setText(set.getExerciseName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }
}
