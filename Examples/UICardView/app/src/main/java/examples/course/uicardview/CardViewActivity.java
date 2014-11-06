package examples.course.uicardview;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


public class CardViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardview);

        CardView cardView = (CardView) findViewById(R.id.card);

        ImageView imageView = (ImageView) findViewById(R.id.flag_image);
        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),(R.drawable.us)));

        TextView place = (TextView) findViewById(R.id.place_name);
        place.setText("Washington, D.C., ");

        TextView country = (TextView) findViewById(R.id.country_name);
        country.setText("United States");

        TextView date = (TextView) findViewById(R.id.date_string);
        date.setText("Visited On: July 4, 1776");

        TextView location = (TextView) findViewById(R.id.location);
        location.setText("Location: (38.8977,-77.0366)");

    }
}
