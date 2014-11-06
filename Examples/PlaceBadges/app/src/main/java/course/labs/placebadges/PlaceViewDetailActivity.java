package course.labs.placebadges;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


public class PlaceViewDetailActivity extends Activity {

    private TextView mCountryName;
    private ImageView mFlagImage;
    private TextView mPlaceName;
    private TextView mDate;
    private TextView mLocationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setAllowEnterTransitionOverlap(true);

        setContentView(course.labs.placebadges.R.layout.place_detail_view);
        PlaceRecord placeRecord = new PlaceRecord(getIntent());

        mFlagImage = (ImageView) findViewById(course.labs.placebadges.R.id.flag_image);
        mFlagImage.setImageBitmap(placeRecord.getFlagBitmap());

        mPlaceName = (TextView) findViewById(course.labs.placebadges.R.id.place_name);
        mPlaceName.setText(placeRecord.getPlace() + ", ");

        mCountryName = (TextView) findViewById(course.labs.placebadges.R.id.country_name);
        mCountryName.setText(placeRecord.getCountryName());

        mDate = (TextView) findViewById(course.labs.placebadges.R.id.date_string);
        mDate.setText("Visited on: " + placeRecord.getDate());

        mLocationView = (TextView) findViewById(course.labs.placebadges.R.id.location);
        mLocationView.setText("Location: (" + placeRecord.getLocation().getLatitude()
                + ","
                + placeRecord.getLocation().getLongitude()
                + ")");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAfterTransition();
    }
}
