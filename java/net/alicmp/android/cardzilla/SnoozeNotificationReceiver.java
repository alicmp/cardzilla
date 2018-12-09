package net.alicmp.android.cardzilla;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.alicmp.android.cardzilla.helper.NotificationUtility;
import net.alicmp.android.cardzilla.model.Packet;

/**
 * Created by ali on 11/13/15.
 */
public class SnoozeNotificationReceiver extends BroadcastReceiver {

    public static String INTENT_EXTRA_PACKET = "packet";

    @Override
    public void onReceive(Context context, Intent intent) {
        Packet mPacket = (Packet) intent.getSerializableExtra(INTENT_EXTRA_PACKET);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(mPacket.getId());
        NotificationUtility.scheduleNotification(mPacket, 1000 * 60 * 60, context);
    }
}
