package net.alicmp.android.cardzilla;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.alicmp.android.cardzilla.adapter.PacketInfoAdapter;
import net.alicmp.android.cardzilla.helper.SimpleDividerItemDecoration;
import net.alicmp.android.cardzilla.helper.Utility;
import net.alicmp.android.cardzilla.model.Packet;
import net.alicmp.android.cardzilla.model.PacketStatistics;

/**
 * Created by ali on 8/15/15.
 */
public class PacketInfoActivity extends AppCompatActivity {

    public Packet mPacket;
    private TextView packetInfoView;
    private PacketInfoAdapter mAdapter;
    private PacketStatistics packetStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(net.alicmp.android.cardzilla.R.layout.activity_packet_info);

        mPacket = getIntent().getParcelableExtra("packet");

        Toolbar toolbar = (Toolbar) findViewById(net.alicmp.android.cardzilla.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView packetNameView = (TextView) findViewById(net.alicmp.android.cardzilla.R.id.tv_packet_name);
        packetInfoView = (TextView) findViewById(net.alicmp.android.cardzilla.R.id.tv_packet_info);
        packetInfoView.setText(generateStatisticText());
        if (mPacket != null)
            packetNameView.setText(mPacket.getPacketName());

        Button leitnerView = (Button) findViewById(net.alicmp.android.cardzilla.R.id.btn_leitner);
        Button reviewView = (Button) findViewById(net.alicmp.android.cardzilla.R.id.btn_review);
        assert leitnerView != null;
        leitnerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = Utility.getCards(PacketInfoActivity.this, mPacket.getId(), 2).size();
                if (!packetStatistics.isPacketEmpty() && n != 0) {
                    Intent intent = new Intent(PacketInfoActivity.this, StudyActivity.class);
                    intent.putExtra("packet", mPacket);
                    startActivity(intent);
                } else if (!packetStatistics.isPacketEmpty())
                    Toast.makeText(PacketInfoActivity.this,
                            net.alicmp.android.cardzilla.R.string.toast_txt_not_study_time,
                            Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(PacketInfoActivity.this,
                            net.alicmp.android.cardzilla.R.string.toast_txt_packet_is_empty,
                            Toast.LENGTH_LONG).show();

            }
        });
        assert reviewView != null;
        reviewView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!packetStatistics.isPacketEmpty()) {
                    Intent intent = new Intent(PacketInfoActivity.this, ReviewActivity.class);
                    intent.putExtra("packet", mPacket);
                    startActivity(intent);
                } else
                    Toast.makeText(
                            PacketInfoActivity.this,
                            net.alicmp.android.cardzilla.R.string.toast_txt_packet_is_empty,
                            Toast.LENGTH_LONG).show();
            }
        });

        RecyclerView mRecyclerView = (RecyclerView) findViewById(net.alicmp.android.cardzilla.R.id.recyclerview_package_info);
        mAdapter = new PacketInfoAdapter(this, mPacket);
        assert mRecyclerView != null;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this, 0));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(net.alicmp.android.cardzilla.R.menu.menu_packet_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (id == net.alicmp.android.cardzilla.R.id.action_reset_flashcard) {
            builder.setTitle(net.alicmp.android.cardzilla.R.string.dialog_txt_reset_packet_title);
            builder.setMessage(net.alicmp.android.cardzilla.R.string.dialog_txt_reset_packet_msg);
            builder.setPositiveButton(net.alicmp.android.cardzilla.R.string.dialog_txt_reset_packet_pos_btn,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utility.resetPacket(PacketInfoActivity.this, mPacket.getId());
                            changeStaticAndReminderTime();
                        }
                    });
            builder.setNegativeButton(net.alicmp.android.cardzilla.R.string.dialog_txt_neg_btn,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.show();
        } else if (id == net.alicmp.android.cardzilla.R.id.action_remove_packet) {
            builder.setTitle(net.alicmp.android.cardzilla.R.string.dialog_txt_rmv_packet_title);
            builder.setMessage(net.alicmp.android.cardzilla.R.string.dialog_txt_rmv_packet_msg);
            builder.setPositiveButton(net.alicmp.android.cardzilla.R.string.dialog_txt_rmv_packet_pos_btn,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utility.removePacket(PacketInfoActivity.this, mPacket.getId());
                            finish();
                        }
                    });
            builder.setNegativeButton(net.alicmp.android.cardzilla.R.string.dialog_txt_neg_btn,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.show();
        } else if (id == net.alicmp.android.cardzilla.R.id.action_rename_packet) {
            Intent intent = new Intent(PacketInfoActivity.this, AddPacketDialog.class);
            intent.putExtra("packet", mPacket);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        changeStaticAndReminderTime();
    }

    private String generateStatisticText() {
        packetStatistics = Utility.getStatics(this, mPacket.getId());

        return "New : " + packetStatistics.getNewCardsCount() + "\n"
                + "Learning : " + packetStatistics.getLearningCardsCount() + "\n"
                + "To Review : " + packetStatistics.getReviewingCardsCount();
    }

    private void changeStaticAndReminderTime() {
        packetInfoView.setText(generateStatisticText());

        mPacket.setTimeToRead(Utility.getPacketsTimeToRead(PacketInfoActivity.this, mPacket.getId()));
        mAdapter.notifyItemChanged(1);
    }
}
