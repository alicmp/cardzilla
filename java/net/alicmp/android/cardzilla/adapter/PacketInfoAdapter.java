package net.alicmp.android.cardzilla.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.alicmp.android.cardzilla.AddEditCardActivity;
import net.alicmp.android.cardzilla.helper.NotificationUtility;
import net.alicmp.android.cardzilla.R;
import net.alicmp.android.cardzilla.helper.Utility;
import net.alicmp.android.cardzilla.model.Packet;
import net.alicmp.android.cardzilla.model.PacketStatistics;

/**
 * Created by ali on 8/15/15.
 */
public class PacketInfoAdapter extends RecyclerView.Adapter<PacketInfoAdapter.ViewHolder> {
    private Context mContext;
    private String[] mTitles = {
            "Add/Edit Cards",
            "Reminder",
            "Tell Your Friends"};
    private int[] mIcons = {
            R.drawable.ic_add_black,
            R.drawable.ic_notifications_black,
            R.drawable.ic_people_black};
    private Packet mPacket;

    public PacketInfoAdapter(Context context, Packet packet) {
        this.mContext = context;
        this.mPacket = packet;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView titleView;
        public final ImageView iconView;
        // this two variables are for reminder list row
        public final SwitchCompat reminderSwitch;
        public final TextView infoView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.titleView = (TextView) itemView.findViewById(R.id.list_item_title);
            this.iconView = (ImageView) itemView.findViewById(R.id.list_item_icon);
            this.reminderSwitch = (SwitchCompat) itemView.findViewById(R.id.revie_switch);
            this.infoView = (TextView) itemView.findViewById(R.id.list_item_info);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Intent intent;
            final SwitchCompat reminderSwitch = (SwitchCompat) v.findViewById(R.id.revie_switch);
            final TextView remindersTimeInfo = (TextView) v.findViewById(R.id.list_item_info);

            switch (adapterPosition) {
                case 0:
                    // open add edit activity
                    intent = new Intent(mContext, AddEditCardActivity.class);
                    intent.putExtra("packet", mPacket);
                    mContext.startActivity(intent);
                    break;
                case 1:
                    // set reminder
                    boolean isChecked = reminderSwitch.isChecked();
                    if (isChecked) {
                        NotificationUtility.cancelAlarmManager(mPacket, mContext);
                        remindersTimeInfo.setText("Reminder hasn't set");
                        Log.v("Reminder", "Canceled");
                    } else {
                        NotificationUtility.makeNotification(
                                mContext, mPacket, Utility.nextReadTime(mContext, mPacket.getId()));
                        mPacket.setTimeToRead(Utility.getPacketsTimeToRead(mContext, mPacket.getId()));
                        boolean b = mPacket.isTimeToReadValid();
                        if (b)
                            remindersTimeInfo.setText("Next read time: " + mPacket.timeToReadString());
                        else
                            remindersTimeInfo.setText("Reminder hasn't set");
                        Log.v("Reminder", "Set");
                    }
                    reminderSwitch.setChecked(!isChecked);
                    break;
                case 2:
                    // tell your friends
                    PacketStatistics statistics = Utility.getStatics(mContext, mPacket.getId());
                    String shareBody = "I studied "
                            + (statistics.getReviewingCardsCount()
                            + statistics.getLearningCardsCount())
                            + " cards with #CardZilla application.";
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    mContext.startActivity(
                            Intent.createChooser(
                                    sharingIntent, mContext.getResources().getText(R.string.tell_friends)));
                    break;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_packet_info, parent, false);
                break;
            case 1:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_packet_info_reminder, parent, false);
                break;
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.iconView.setImageResource(mIcons[position]);
        holder.titleView.setText(mTitles[position]);

        if (position == 1) {
            boolean b = mPacket.isTimeToReadValid();
            holder.reminderSwitch.setChecked(b);
            if (b)
                holder.infoView.setText("Next read time: " + mPacket.timeToReadString());
            else
                holder.infoView.setText("Reminder hasn't set");
        }
    }

    @Override
    public int getItemCount() {
        return mTitles.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 1 ? 1 : 0;
    }
}
