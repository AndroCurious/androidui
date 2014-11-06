package course.examples.Fragments.StaticConfigLayout;

import android.app.Activity;
import android.os.Bundle;

import course.examples.Fragments.StaticConfigLayout.TitlesFragment.ListSelectionListener;

public class QuoteViewerActivity extends Activity implements
		ListSelectionListener {

	public static String[] mTitleArray;
	public static String[] mQuoteArray;
	private QuotesFragment mQuotesFragment;

	private static final String TAG = "QuoteViewerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get the string arrays with the titles and qutoes
		mTitleArray = getResources().getStringArray(R.array.Titles);
		mQuoteArray = getResources().getStringArray(R.array.Quotes);
		
		setContentView(R.layout.main);

		// Get a reference to the QuotesFragment
		mQuotesFragment = (QuotesFragment) getFragmentManager()
				.findFragmentById(R.id.quotes);
	}

	// Called when the user selects an item in the TitlesFragment
	@Override
	public void onListSelection(int index) {
		if (mQuotesFragment.getShownIndex() != index) {

			// Show the quote string at position index
			mQuotesFragment.showQuoteAtIndex(index);
		}
	}
}