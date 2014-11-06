package course.labs.placebadges;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Outline;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class PlaceViewActivity extends Activity implements LocationListener {

    private static final long FIVE_MINS = 5 * 60 * 1000;
    private static final String TAG = "PlaceViewActivity";
    private static final int DELETE_ID = 0;
    private static final int CREDITS_ID = 1;

    private PlaceViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    // default minimum time between new readings
    private long mMinTime = 5000;
    // default minimum distance between old and new readings.
    private float mMinDistance = 1000.0f;
    private LocationManager mLocationManager;
    // A fake location provider used for testing
    private MockLocationProvider mMockLocationProvider;
    private Location mLastLocationReading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setAllowReturnTransitionOverlap(true);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setContentView(course.labs.placebadges.R.layout.place_activity);

        // Style the Floating Action Button
        styleFAB();

        //Set up RecyclerView
        mRecyclerView = (RecyclerView) findViewById(course.labs.placebadges.R.id.card);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new PlaceViewAdapter(new ArrayList<PlaceRecord>(),
                course.labs.placebadges.R.layout.place_badge_view,
                this);

        mRecyclerView.setAdapter(mAdapter);

    }

    private void styleFAB() {

        // Create circular outline
        Outline outline = new Outline();
        ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int diameter = getResources().getDimensionPixelSize(R.dimen.add_button_diameter);
                outline.setOval(0, 0, diameter, diameter);
            }
        };

        // Set outline on Floating Action Button
        ImageButton floatingButton = (ImageButton) findViewById(R.id.add_button);
        floatingButton.setOutlineProvider(viewOutlineProvider);

        // Add OnClickListener to Floating Action Button
        floatingButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Determine whether or not location data is available
                if (null == mLastLocationReading) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.no_location_data_string), Toast.LENGTH_LONG)
                            .show();
                } else {
                    // Check whether user already has a PlaceBadge for this location
                    if (mAdapter.intersects(mLastLocationReading)) {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.duplicate_place_string),
                                Toast.LENGTH_LONG).show();
                    } else {
                        // Start an AsyncTask to download PlaceBadge and place information
                        new PlaceDownloaderTask(PlaceViewActivity.this)
                                .execute(mLastLocationReading);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Create MockLocationProvider used for testing
        mMockLocationProvider = new MockLocationProvider(
                LocationManager.NETWORK_PROVIDER, this);

        // Check all Providers for a recent location reading
        for (String provider : mLocationManager.getAllProviders()) {
            Location tmp = mLocationManager.getLastKnownLocation(provider);
            if ((null != tmp && (System.currentTimeMillis() - tmp.getTime() < FIVE_MINS))
                    || (null != tmp && null != mLastLocationReading && tmp
                    .getAccuracy() < mLastLocationReading.getAccuracy())) {
                mLastLocationReading = tmp;
            }
        }

        // Register to receive location updates from all providers
        for (String provider : mLocationManager.getAllProviders()) {
            mLocationManager.requestLocationUpdates(provider, mMinTime,
                    mMinDistance, this);
        }
    }

    @Override
    protected void onPause() {
        mMockLocationProvider.shutdown();
        mLocationManager.removeUpdates(this);
        super.onPause();
    }

    // Callback method used to display newly acquired PlaceBadge
    public void addNewPlace(PlaceRecord place) {
        mAdapter.add(place);
    }

    // Return the age of this Location
    private long age(Location location) {
        return System.currentTimeMillis() - location.getTime();
    }

    /* Implement LocationListener interface */

    @Override
    public void onLocationChanged(Location currentLocation) {
        if ((null == mLastLocationReading) || (age(currentLocation) < age(mLastLocationReading))) {
            mLastLocationReading = currentLocation;
            Log.i(TAG, currentLocation.toString());
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // not implemented
    }

    @Override
    public void onProviderEnabled(String provider) {
        // not implemented
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // not implemented
    }

    /* Handle Menu interaction */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(course.labs.placebadges.R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_badges:
                show(DELETE_ID);
                return true;
            case R.id.credits:
                show(CREDITS_ID);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void show(int dialogID) {
        DialogFragment newFragment = MyAlertDialogFragment.newInstance(dialogID, mAdapter);
        newFragment.show(getFragmentManager(), null);
    }

    /* Implement custom AlertDialog */

    public static class MyAlertDialogFragment extends DialogFragment {
        private static PlaceViewAdapter mAdapter;
        private final int mDialogID;

        public static MyAlertDialogFragment newInstance(int dialogID, PlaceViewAdapter adapter) {
            return new MyAlertDialogFragment(dialogID, adapter);
        }

        private MyAlertDialogFragment(int dialogID, PlaceViewAdapter adapter) {
            super();
            mAdapter = adapter;
            mDialogID = dialogID;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Inflate the dialog
            LayoutInflater layoutInflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View dialogView = null;

            switch (mDialogID) {
                case DELETE_ID: {

                    dialogView = layoutInflater.inflate(R.layout.delete_confirm_layout, null);

                    // Attach listener to "No" Button
                    Button noButton = (Button) dialogView
                            .findViewById(R.id.no_button);
                    noButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                        }
                    });

                    // Attach listener to "Yes" Button
                    Button yesButton = (Button) dialogView
                            .findViewById(R.id.yes_button);
                    yesButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAdapter.removeAllViews();
                            dismiss();
                        }
                    });

                    break;
                }

                case CREDITS_ID: {
                    dialogView = layoutInflater.inflate(R.layout.credits_layout, null);

                    // Attach listener to "Done" Button
                    Button doneButton = (Button) dialogView
                            .findViewById(R.id.done_button);
                    doneButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                        }
                    });

                }
            }

            if (null != dialogView) {
                // Create the AlertDialog

                return new AlertDialog.Builder(getActivity()).setView(dialogView)
                        .create();
            } else {
                return super.onCreateDialog(savedInstanceState);
            }
        }
    }
}
