package course.labs.multipane;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity implements
		TitlesFragment.SelectionListener {

	private static final String TAG = "Lab-Fragments";
    private static final int QUOTE_FRAG_CONTAINER_ID = R.id.fragment_container;
	private TitlesFragment mTitlesFragment;
	private QuoteFragment mQuoteFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);



		// If the layout is single-pane, create the FriendsFragment 
		// and add it to the Activity

		if (!isInTwoPaneMode()) {

			mTitlesFragment = new TitlesFragment();


			// Add (replace) the FriendsFragment
			
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_container, mTitlesFragment);
			transaction.commit();

		} else {

			// Otherwise, save a reference to the FeedFragment for later use

			mQuoteFragment = (QuoteFragment) getFragmentManager()
					.findFragmentById(R.id.feed_frag);
		}

	}

	// If there is no fragment_container ID, then the application is in
	// two-pane mode

	boolean isInTwoPaneMode() {

		return findViewById(R.id.fragment_container) == null;
	
	}

	// Display selected Twitter quote

	public void onItemSelected(int position) {

		Log.i(TAG, "Entered onItemSelected(" + position + ")");

		// If there is no FeedFragment instance, then create one

		if (mQuoteFragment == null)
			mQuoteFragment = new QuoteFragment();

		// If in single-pane mode, replace single visible Fragment

		if (!isInTwoPaneMode()) {

			//Replace the fragment_container with the FeedFragment
			
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();

			transaction.replace(R.id.fragment_container, mQuoteFragment);
			transaction.addToBackStack(null);

			transaction.commit();

			getFragmentManager().executePendingTransactions();

		}

		// Update Twitter quote display on FriendFragment
		mQuoteFragment.updateFeedDisplay(position);

	}

}
