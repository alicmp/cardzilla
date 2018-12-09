package net.alicmp.android.cardzilla;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.alicmp.android.cardzilla.helper.Utility;
import net.alicmp.android.cardzilla.model.Packet;

public class AddPacketDialog extends AppCompatActivity {

    private Packet mPacket;
    private EditText packetTitle;
    private Button positive;
    private Button negative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(net.alicmp.android.cardzilla.R.layout.dialog_add_packet);

        mPacket = getIntent().getParcelableExtra("packet");

        packetTitle = (EditText) findViewById(net.alicmp.android.cardzilla.R.id.et_packet_title);
        negative = (Button) findViewById(net.alicmp.android.cardzilla.R.id.bt_neg);
        positive = (Button) findViewById(net.alicmp.android.cardzilla.R.id.bt_pos);

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (mPacket == null) {
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mTitle = packetTitle.getText().toString();
                    if (mTitle.equals(""))
                        Toast.makeText(AddPacketDialog.this, "Enter the packet name.", Toast.LENGTH_LONG)
                                .show();
                    else {
                        Packet mPacket = new Packet();
                        mPacket.setPacketName(mTitle);
                        int id = (int) Utility.addPacket(AddPacketDialog.this, mPacket);
                        if (id != -1) {
                            mPacket.setId(id);
                            Intent intent = new Intent(AddPacketDialog.this, PacketInfoActivity.class);
                            intent.putExtra("packet", mPacket);
                            startActivity(intent);
                            finish();
                        } else
                            Toast.makeText(AddPacketDialog.this, "Something is wrong!", Toast.LENGTH_LONG)
                                    .show();
                    }
                }
            });
        } else {
            setTitle("Rename packet");
            packetTitle.setText(mPacket.getPacketName());
            positive.setText(net.alicmp.android.cardzilla.R.string.dialog_txt_rename_packet_pos_btn);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mTitle = packetTitle.getText().toString();
                    if (mTitle.equals(""))
                        Toast.makeText(AddPacketDialog.this, "Enter the packet name.", Toast.LENGTH_LONG)
                                .show();
                    else {
                        mPacket.setPacketName(mTitle);
                        Utility.editPacket(AddPacketDialog.this, mPacket);
                        finish();
                    }
                }
            });
        }
    }

}
