package mgr.mtp;

/**
 * Created by lmedrzycki on 26.02.2016.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> mealCollections;
    private List<String> mealIngredients;

    public ExpandableListAdapter(Activity context, List<String> mealIngredients,
                                 Map<String, List<String>> mealCollections) {
        this.context = context;
        this.mealCollections = mealCollections;
        this.mealIngredients = mealIngredients;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return mealCollections.get(mealIngredients.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String mealIngredient = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.mealIngredient);

/*        ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to remove?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                List<String> child =
                                        mealCollections.get(mealIngredients.get(groupPosition));
                                child.remove(childPosition);
                                notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });*/

        item.setText(mealIngredient);
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return mealCollections.get(mealIngredients.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return mealIngredients.get(groupPosition);
    }

    public int getGroupCount() {
        return mealIngredients.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String mealIngredient = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item,
                    null);
        }

        ImageView add = (ImageView) convertView.findViewById(R.id.addMeaIngredient);
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
}