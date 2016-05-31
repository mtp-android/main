package mgr.mtp.Statistics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.List;

import mgr.mtp.DataModel.Measure;
import mgr.mtp.R;

/**
 * Created by lmedrzycki on 14.04.2016.
 */
public class StatisticsListAdapter  extends BaseExpandableListAdapter {

    ImageView viewHistory;

    private Activity context;
    private LinkedHashMap<String, List<Measure>> measuresCollection;
    private List<String> groupList;
    private StatisticsLauncher fragment;

    public StatisticsListAdapter(Activity context, List<String> groupList,
                           LinkedHashMap<String, List<Measure>> measuresCollection, StatisticsLauncher fragment) {
        this.context = context;
        this.measuresCollection = measuresCollection;
        this.groupList = groupList;
        this.fragment = fragment;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return measuresCollection.get(groupList.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, final ViewGroup parent) {
        final Measure measure = (Measure) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item_stat, null);
        }

        viewHistory = (ImageView) convertView.findViewById(R.id.viewHistory);
        viewHistory.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(context, StatisticsDisplayChart.class);
                i.putExtra("statisticName", measure.getName());
                context.startActivity(i);
            }
        });

        TextView statName = (TextView) convertView.findViewById(R.id.statName);
        TextView statValue = (TextView) convertView.findViewById(R.id.statValue);

        statName.setText(measure.getName());
        statValue.setText(measure.getValue());

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return measuresCollection.get(groupList.get(groupPosition)) != null ? measuresCollection.get(groupList.get(groupPosition)).size() : 0;
    }

    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    public int getGroupCount() {
        return groupList.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String mealIngredient = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item_statistics,
                    null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.measureName);

        item.setTypeface(null, Typeface.BOLD);
        item.setText(mealIngredient);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}