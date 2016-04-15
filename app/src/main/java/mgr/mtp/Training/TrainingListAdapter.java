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
    private LinkedHashMap<String, List<ExerciseSet>> exercisesCollection;
    private List<String> groupList;
    private TrainingHome fragment;

    public TrainingListAdapter(Activity context, List<String> groupList,
                               LinkedHashMap<String, List<ExerciseSet>> exercisesCollection, TrainingHome fragment) {
        this.context = context;
        this.exercisesCollection = exercisesCollection;
        this.groupList = groupList;
        this.fragment = fragment;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return exercisesCollection.get(groupList.get(groupPosition)) != null ? exercisesCollection.get(groupList.get(groupPosition)).size() : 0;
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
        return exercisesCollection.get(groupList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
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
            convertView = inflater.inflate(R.layout.child_item_training, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.exerciseSet);

        item.setText(set.getSetNo()+") "+set.getReps()+" x "+set.getWeight()+" kg");
        return convertView;
    }

    public void dataChanged(LinkedHashMap<String, List<ExerciseSet>> exercisesCollection) {
        this.exercisesCollection = exercisesCollection;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
