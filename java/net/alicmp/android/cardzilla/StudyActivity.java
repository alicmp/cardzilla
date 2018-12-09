package net.alicmp.android.cardzilla;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import net.alicmp.android.cardzilla.fragment.CardFragment;
import net.alicmp.android.cardzilla.fragment.ResultFragment;
import net.alicmp.android.cardzilla.helper.Utility;
import net.alicmp.android.cardzilla.model.Card;
import net.alicmp.android.cardzilla.model.Packet;

import java.util.ArrayList;
import java.util.List;

public class StudyActivity extends AppCompatActivity {

    public Packet mPacket;
    public static List<Card> mCards = new ArrayList<>();
    public TextView counter;
    public int numberOfCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(net.alicmp.android.cardzilla.R.layout.activity_study);

        mPacket = getIntent().getParcelableExtra("packet");

        if (savedInstanceState == null) {
            Toolbar toolbarView = (Toolbar) findViewById(net.alicmp.android.cardzilla.R.id.toolbar);
            setSupportActionBar(toolbarView);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getCards();
            fragmentTransaction(0);
            numberOfCards = mCards.size();

            counter = (TextView) findViewById(net.alicmp.android.cardzilla.R.id.tv_cards_counter);
            counter.setText(mCards.size() + " cards to learn");
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
                        CardFragment.newInstance(mCards.get(position), isFront, position, false))
                .commit();
    }

    public void onCardAnswered(int position) {
        position++;
        int packetSize = mCards.size();
        if (position < packetSize)
            counter.setText((packetSize - position) + " cards to learn");
        else if (position == packetSize)
            counter.setText("");
        fragmentTransaction(position);
    }

    public void getCards() {
        mCards = Utility.getCards(this, mPacket.getId(), getDailyGoal());
    }

    public void fragmentTransaction(int position) {
        if (mCards.size() == 0) {
            // TODO: change this toast to a dialog, and put string in the file
            counter.setText("");
            Toast.makeText(this, "There is no card to read.", Toast.LENGTH_LONG)
                    .show();
        } else if (position >= mCards.size()) {
            // TODO: handle the null pointer exception.
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(net.alicmp.android.cardzilla.R.animator.slide_in_right, net.alicmp.android.cardzilla.R.animator.slide_out_left)
                    .replace(net.alicmp.android.cardzilla.R.id.container_card, new ResultFragment())
                    .commit();
        } else {
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(net.alicmp.android.cardzilla.R.animator.slide_in_right, net.alicmp.android.cardzilla.R.animator.slide_out_left)
                    .replace(
                            net.alicmp.android.cardzilla.R.id.container_card,
                            CardFragment.newInstance(mCards.get(position), true, position, false))
                    .commit();
        }
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

    public void setReadInfoText() {
        counter.setText(mCards.size() + " cards to learn");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mCards.size() == 0)
            getCards();
    }

    private int getDailyGoal() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String s = sharedPref.getString("daily_goal_preference", "");
        switch (s) {
            case "Regular":
                return 5;
            case "Casual":
                return 2;
            case "Serious":
                return 10;
        }

        return 5;
    }
}
