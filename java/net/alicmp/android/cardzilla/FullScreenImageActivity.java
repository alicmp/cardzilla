package net.alicmp.android.cardzilla;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import net.alicmp.android.cardzilla.helper.Utility;
import net.alicmp.android.cardzilla.model.Card;

/**
 * Created by ali on 4/7/16.
 */
public class FullScreenImageActivity extends AppCompatActivity {

    private Card mCard;
    private boolean isFront;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(net.alicmp.android.cardzilla.R.layout.activity_full_screen_image);

        Toolbar toolbar = (Toolbar) findViewById(net.alicmp.android.cardzilla.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCard = (Card) getIntent().getSerializableExtra("card");
        isFront = getIntent().getBooleanExtra("isFront", true);

        ImageView image = (ImageView) findViewById(net.alicmp.android.cardzilla.R.id.iv_image);

        if (isFront) {
            assert image != null;
            image.setImageDrawable(mCard.getImageOfFront(this));
        } else {
            assert image != null;
            image.setImageDrawable(mCard.getImageOfBack(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(net.alicmp.android.cardzilla.R.menu.menu_full_screen_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == net.alicmp.android.cardzilla.R.id.action_remove_image) {
            if (isFront)
                mCard.setFrontImagePath("");
            else
                mCard.setBackImagePath("");

            Utility.editCard(FullScreenImageActivity.this, mCard);
            setResult(RESULT_OK);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        setResult(RESULT_CANCELED);
    }
}
