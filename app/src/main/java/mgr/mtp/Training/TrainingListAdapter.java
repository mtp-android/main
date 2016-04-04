package mgr.mtp.Training;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import mgr.mtp.DataModel.Excersise;
import mgr.mtp.DataModel.Product;
import mgr.mtp.Diet.DietHome;

/**
 * Created by lukas on 04.04.2016.
 */
public class TrainingListAdapter extends BaseExpandableListAdapter {

    Date date;
    private Activity context;
    private LinkedHashMap<String, List<Excersise>> excersisesCollection;
    private List<String> groupList;
    private TrainingHome fragment;

    public TrainingListAdapter(Activity context, List<String> groupList,
                           LinkedHashMap<String, List<Excersise>> excersisesCollection, TrainingHome fragment) {
        this.context = context;
        this.excersisesCollection = excersisesCollection;
        this.groupList = groupList;
        this.fragment = fragment;
    }

    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
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
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
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
