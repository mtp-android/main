package mgr.mtp;

/**
 * Created by lmedrzycki on 26.02.2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import mgr.mtp.DataModel.Food;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    ImageView addMeal;
    ImageView removeChildItem;
    Date date;

    private Activity context;
    private LinkedHashMap<String, List<Food>> mealsCollection;
    private List<String> groupList;
    private HomeDiet fragment;

    public ExpandableListAdapter(Activity context, List<String> groupList,
                                 LinkedHashMap<String, List<Food>> mealsCollection, HomeDiet fragment) {
        this.context = context;
        this.mealsCollection = mealsCollection;
        this.groupList = groupList;
        this.fragment = fragment;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return mealsCollection.get(groupList.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, final ViewGroup parent) {
        final Food mealIngredient = (Food) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item, null);
        }

        removeChildItem = (ImageView) convertView.findViewById(R.id.removeChildItem);
        removeChildItem.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                new AlertDialog.Builder(v.getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(fragment.getString(R.string.removeTitle))
                        .setMessage(fragment.getString(R.string.confirmRemoveText))
                        .setPositiveButton(fragment.getString(R.string.yes), new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fragment.removeChild(mealIngredient.getId(), groupPosition, childPosition);
                            }

                        })
                        .setNegativeButton(fragment.getString(R.string.no), null)
                        .show();
            }
        });

        TextView item = (TextView) convertView.findViewById(R.id.mealIngredient);

        item.setText(mealIngredient.getName()+" - "+mealIngredient.getAmount()+" "+mealIngredient.getUnit());
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return mealsCollection.get(groupList.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    public int getGroupCount() {
        return groupList.size();
    }

    public void setDate(Date date)
    {
        this.date = date;
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
            convertView = infalInflater.inflate(R.layout.group_item,
                    null);
        }

        addMeal = (ImageView) convertView.findViewById(R.id.addMeaIngredient);
        addMeal.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(v.getContext(),"toDo posilek nr: "+getGroupId(groupPosition)+
                        " z dnia: "+Constants.displayDateFormat.format(date),Toast.LENGTH_SHORT).show();
            }
        });

        TextView item = (TextView) convertView.findViewById(R.id.mealName);

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

    public void dataChanged(LinkedHashMap<String, List<Food>> mealsCollection) {
        this.mealsCollection = mealsCollection;
    }


}