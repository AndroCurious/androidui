package course.examples.UI.ListLayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ListViewAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mLayoutInflater;

    public ListViewAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View dataView = convertView;

        // Check for recycled View
        if (null == dataView) {

            // Create the View
            dataView = mLayoutInflater.inflate(R.layout.list_item, parent, false);

            // Cache View information in ViewHolder Object
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) dataView.findViewById(R.id.text);
            dataView.setTag(viewHolder);

        }

        // Retrieve the viewHolder Object
        ViewHolder storedViewHolder = (ViewHolder) dataView.getTag();

        //Set the data in the data View
        storedViewHolder.text.setText(getItem(position));

        return dataView;
    }

    static class ViewHolder {
        public TextView text;
    }
}
