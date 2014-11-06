package course.labs.placebadges_prel;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class PlaceViewDetailActivity extends Activity {

	private ImageView mFlagImage;
	private TextView mLocationView;
	private TextView mDate;
	private TextView mCountryName;
	private TextView mPlaceName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_view_detail);

		PlaceRecord placeRecord = new PlaceRecord(getIntent());

		mFlagImage = (ImageView) findViewById(R.id.flag_image);
		mFlagImage.setImageBitmap(placeRecord.getFlagBitmap());

		mPlaceName = (TextView) findViewById(R.id.place_name);
		mPlaceName.setText(placeRecord.getPlace() + ", ");

		mCountryName = (TextView) findViewById(R.id.country_name);
		mCountryName.setText(placeRecord.getCountryName());

		mDate = (TextView) findViewById(R.id.date_string);
		mDate.setText("Visited on: " + placeRecord.getDate());

		mLocationView = (TextView) findViewById(R.id.location);
		mLocationView.setText("Location: ("
				+ placeRecord.getLocation().getLatitude() + ","
				+ placeRecord.getLocation().getLongitude() + ")");

	}
}
