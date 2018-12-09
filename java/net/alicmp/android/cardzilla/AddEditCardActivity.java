package net.alicmp.android.cardzilla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import net.alicmp.android.cardzilla.adapter.AddEditCardAdapter;
import net.alicmp.android.cardzilla.helper.Constants;
import net.alicmp.android.cardzilla.helper.SimpleDividerItemDecoration;
import net.alicmp.android.cardzilla.helper.Utility;
import net.alicmp.android.cardzilla.model.Card;
import net.alicmp.android.cardzilla.model.Packet;

import java.util.ArrayList;

public class AddEditCardActivity extends AppCompatActivity {

    private Packet mPacket;
    private RecyclerView mRecycleView;
    private AddEditCardAdapter mAdapter;
    private ArrayList<Card> cardsList;
    private LinearLayout emptyStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(net.alicmp.android.cardzilla.R.layout.activity_add_edit_card);

        mPacket = getIntent().getParcelableExtra("packet");

        Toolbar toolbar = (Toolbar) findViewById(net.alicmp.android.cardzilla.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emptyStateView = (LinearLayout) findViewById(net.alicmp.android.cardzilla.R.id.lv_add_edit_empty_state);

        mRecycleView = (RecyclerView) findViewById(net.alicmp.android.cardzilla.R.id.recyclerview_cards);
        cardsList = Utility.getAllCards(this, mPacket.getId());
        mAdapter = new AddEditCardAdapter(AddEditCardActivity.this, cardsList, mPacket);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.addItemDecoration(new SimpleDividerItemDecoration(this, 0));
        mRecycleView.setAdapter(mAdapter);

        checkEmptyState();

    }

    private void checkEmptyState() {
        if(cardsList.size() == 0) {
            mRecycleView.setVisibility(View.GONE);
            emptyStateView.setVisibility(View.VISIBLE);
        } else {
            mRecycleView.setVisibility(View.VISIBLE);
            emptyStateView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkEmptyState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(net.alicmp.android.cardzilla.R.menu.menu_add_edit_card, menu);
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
        } else if (id == net.alicmp.android.cardzilla.R.id.action_add_card) {
            Intent intent = new Intent(AddEditCardActivity.this, AddEditCardDialog.class);
            intent.putExtra("packet", mPacket);
            intent.putExtra("isEditMode", false);
            startActivityForResult(intent, Constants.REQUEST_ADD);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQUEST_ADD) {
                // get data and add it to the list
                Card mCard = (Card) data.getSerializableExtra("card");
                mAdapter.add(cardsList.size(), mCard);
            } else if (requestCode == Constants.REQUEST_EDIT) {
                // get data and change the corresponding row
                Card mCard = (Card) data.getSerializableExtra("card");
                int position = data.getExtras().getInt("position");
                if (mCard == null) {
                    mAdapter.remove(position);
                } else {
                    mAdapter.edit(position, mCard);
                }
            }
        }
    }
}
