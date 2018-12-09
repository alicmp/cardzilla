package net.alicmp.android.cardzilla;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import net.alicmp.android.cardzilla.fragment.CardFragment;
import net.alicmp.android.cardzilla.helper.Utility;
import net.alicmp.android.cardzilla.model.Card;
import net.alicmp.android.cardzilla.model.Packet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali on 4/18/16.
 */
public class ReviewActivity extends AppCompatActivity {

    public Packet mPacket;
    public static List<Card> mCards = new ArrayList<>();
    public TextView counter;
    public int numberOfCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(net.alicmp.android.cardzilla.R.layout.activity_review);

        mPacket = getIntent().getParcelableExtra("packet");

        if (savedInstanceState == null) {
            Toolbar toolbarView = (Toolbar) findViewById(net.alicmp.android.cardzilla.R.id.toolbar);
            setSupportActionBar(toolbarView);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getCards();
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(net.alicmp.android.cardzilla.R.animator.slide_in_right, net.alicmp.android.cardzilla.R.animator.slide_out_left)
                    .replace(
                            net.alicmp.android.cardzilla.R.id.container_card,
                            CardFragment.newInstance(mCards.get(0), true, 0, true))
                    .commit();
            numberOfCards = mCards.size();

            counter = (TextView) findViewById(net.alicmp.android.cardzilla.R.id.tv_cards_counter);
            counter.setText("1/" + mCards.size());
        }
    }

    public void onFlipButtonClickListener(boolean isFront, int position) {
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        net.alicmp.android.cardzilla.R.animator.card_flip_right_in, net.alicmp.android.cardzilla.R.animator.card_flip_right_out,
                        net.alicmp.android.cardzilla.R.animator.card_flip_left_in, net.alicmp.android.cardzilla.R.animator.card_flip_left_out)
                .replace(
                        net.alicmp.android.cardzilla.R.id.container_card,
                        CardFragment.newInstance(mCards.get(position), isFront, position, true))
                .commit();
    }

    public void onForwardButtonClickListener(int position) {
        position++;

        if (position >= 0 && position < mCards.size()) {
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(net.alicmp.android.cardzilla.R.animator.slide_in_right, net.alicmp.android.cardzilla.R.animator.slide_out_left)
                    .replace(
                            net.alicmp.android.cardzilla.R.id.container_card,
                            CardFragment.newInstance(mCards.get(position), true, position, true))
                    .commit();

            counter.setText((position + 1) + "/" + mCards.size());
        }
    }

    public void onBackwardButtonClickListener(int position) {
        position--;

        if (position >= 0 && position < mCards.size()) {
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(net.alicmp.android.cardzilla.R.animator.slide_in_left, net.alicmp.android.cardzilla.R.animator.slide_out_right)
                    .replace(
                            net.alicmp.android.cardzilla.R.id.container_card,
                            CardFragment.newInstance(mCards.get(position), true, position, true))
                    .commit();

            counter.setText((position + 1) + "/" + mCards.size());
        }
    }

    public void getCards() {
        mCards = Utility.getAllCards(this, mPacket.getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_study, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == net.alicmp.android.cardzilla.R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCards.size() == 0)
            getCards();
    }
}
