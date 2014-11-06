package course.labs.placebadges_prel;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlaceViewAdapter extends BaseAdapter {

	private ArrayList<PlaceRecord> list = new ArrayList<PlaceRecord>();
	private static LayoutInflater inflater = null;
	private Context mContext;

	public PlaceViewAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View newView = convertView;
		ViewHolder holder;

		final PlaceRecord curr = list.get(position);

		if (null == convertView) {
			holder = new ViewHolder();
			newView = inflater.inflate(R.layout.place_badge_view, null);
			holder.flag = (ImageView) newView.findViewById(R.id.flag);
			holder.country = (TextView) newView.findViewById(R.id.country_name);
			holder.place = (TextView) newView.findViewById(R.id.place_name);
			newView.setTag(holder);

		} else {
			holder = (ViewHolder) newView.getTag();
		}

		holder.flag.setImageBitmap(curr.getFlagBitmap());
		holder.place.setText(curr.getPlace() + ",");
		holder.country.setText(curr.getCountryName());
		
		newView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,
						PlaceViewDetailActivity.class);
				intent.putExtras(curr.packageIntent());
				mContext.startActivity(intent);
			}
		});

		return newView;
	}

	static class ViewHolder {

		ImageView flag;
		TextView country;
		TextView place;

	}

	public boolean intersects(Location location) {
		for (PlaceRecord item : list) {
			if (item.intersects(location)) {
				return true;
			}
		}
		return false;
	}

	public void add(PlaceRecord listItem) {
		list.add(listItem);
		notifyDataSetChanged();
	}

	public ArrayList<PlaceRecord> getList() {
		return list;
	}

	public void removeAllViews() {
		list.clear();
		this.notifyDataSetChanged();
	}
}
