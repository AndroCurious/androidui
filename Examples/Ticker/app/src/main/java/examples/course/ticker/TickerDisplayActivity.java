package examples.course.ticker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.os.Handler;

public class TickerDisplayActivity extends Activity {

    private TextView mCounterView;
    private int mCounter = 0;
    private Handler mHandler = new Handler();
    Runnable update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticker_display);

        mCounterView = (TextView) findViewById(R.id.counter);

        update = new Runnable() {
            @Override
            public void run() {
                mCounterView.setText(String.valueOf(++mCounter));
                mHandler.postDelayed(this, 1000);
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        setTimerEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTimerEnabled(true);
        }

    private void setTimerEnabled(boolean isInForeground) {
        if (isInForeground) {
            mCounterView.setText(String.valueOf(mCounter));
            mHandler.post(update);
        } else {
            mHandler.removeCallbacks(update);
            }
    }
}
