package course.labs.placebadges;

import java.util.List;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PlaceViewAdapter extends RecyclerView.Adapter<PlaceViewAdapter.ViewHolder> {

    private static LayoutInflater inflater = null;

    private List<PlaceRecord> mPlaces;
    private int mRowLayout;
    private Context mContext;

    public PlaceViewAdapter(List<PlaceRecord> places, int rowLayout, Context context) {
        mPlaces = places;
        mContext = context;
        mRowLayout = rowLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(mRowLayout, viewGroup, false);
        return new PlaceViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.mPlaceRecord = mPlaces.get(i);
        viewHolder.mFlagImage.setImageBitmap(viewHolder.mPlaceRecord.getFlagBitmap());
        viewHolder.mPlaceName.setText(viewHolder.mPlaceRecord.getPlace() + ",");
        viewHolder.mCountryName.setText(viewHolder.mPlaceRecord.getCountryName());
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mCountryName;
        public ImageView mFlagImage;
        public TextView mPlaceName;
        private PlaceRecord mPlaceRecord;

        public ViewHolder(View itemView) {
            super(itemView);
            mCountryName = (TextView) itemView.findViewById(course.labs.placebadges.R.id.country_name);
            mFlagImage = (ImageView) itemView.findViewById(course.labs.placebadges.R.id.flag_image);
            mPlaceName = (TextView) itemView.findViewById(course.labs.placebadges.R.id.place_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(view.getContext(), PlaceViewDetailActivity.class);
            intent.putExtras(mPlaceRecord.packageIntent());
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(), mFlagImage, "flag_image");
            view.getContext().startActivity(intent, activityOptions.toBundle());
        }
    }

    @Override
    public int getItemCount() {
        return (null == mPlaces) ? 0 : mPlaces.size();
    }

    public boolean intersects(Location location) {
        for (PlaceRecord item : mPlaces) {
            if (item.intersects(location)) {
                return true;
            }
        }
        return false;
    }

    public void add(PlaceRecord listItem) {
        mPlaces.add(listItem);
        notifyDataSetChanged();
    }

    public List<PlaceRecord> getList() {
        return mPlaces;
    }

    public void removeAllViews() {
        mPlaces.clear();
        this.notifyDataSetChanged();
    }
}
