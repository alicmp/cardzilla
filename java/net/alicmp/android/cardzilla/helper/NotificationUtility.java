package net.alicmp.android.cardzilla.helper;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import net.alicmp.android.cardzilla.MainActivity;
import net.alicmp.android.cardzilla.NotificationPublisher;
import net.alicmp.android.cardzilla.PacketInfoActivity;
import net.alicmp.android.cardzilla.R;
import net.alicmp.android.cardzilla.SnoozeNotificationReceiver;
import net.alicmp.android.cardzilla.StudyActivity;
import net.alicmp.android.cardzilla.model.Packet;

/**
 * Created by ali on 2/19/16.
 */
public class NotificationUtility {

    public static Notification createNotification(Packet mPacket, Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Intent startAppIntent = new Intent(context, StudyActivity.class);
        startAppIntent.putExtra("packet", mPacket);
        Intent packetInfoIntent = new Intent(context, PacketInfoActivity.class);
        packetInfoIntent.putExtra("packet", mPacket);
        Intent mainActivityIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(mainActivityIntent);
        stackBuilder.addNextIntent(packetInfoIntent);
        stackBuilder.addNextIntent(startAppIntent);
        PendingIntent startAppPendIntent = stackBuilder.getPendingIntent(
                mPacket.getId(), PendingIntent.FLAG_ONE_SHOT);

//        Intent snoozeNotificationIntent = new Intent(context, SnoozeNotificationReceiver.class);
//        snoozeNotificationIntent.putExtra("packet", mPacket);
//        PendingIntent snoozeNotificationPendIntent = PendingIntent.getBroadcast(
//                context, mPacket.getId(), snoozeNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String msg = "It's time for your "
                + mPacket.getPacketName()
                + " study. Take 5 minutes now to complete it.";
        builder.setContentTitle("Study Time");
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND
                | NotificationCompat.DEFAULT_VIBRATE
                | NotificationCompat.DEFAULT_LIGHTS);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
        builder.setContentText(msg);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setAutoCancel(true);
        builder.setContentIntent(startAppPendIntent);
//        builder.addAction(new NotificationCompat.Action(
//                R.drawable.ic_access_alarms_white_24dp, "Remind me later", snoozeNotificationPendIntent));
        builder.setAutoCancel(true);
        return builder.build();
    }

    public static void scheduleNotification(Packet mPacket, long delay, Context context) {
        Notification notification = createNotification(mPacket, context);

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, mPacket.getId());
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, mPacket.getId(), notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public static void makeNotification(Context mContext, Packet mPacket, long readTime) {

        long now = Utility.getDateAndTime(0);
        long delay = 1000 * 60 * 60;

        if (readTime > now) {
            if (Utility.hasNewCard(mContext, mPacket.getId())) {
                delay *= 24;
                Log.v("MY TAG", "It has new cards.");
            } else {
                delay = readTime - now;
                Log.v("MY TAG", "It hasn't new card.");
            }
        } else
            readTime = now + delay;

//        delay = 1000 * 10;

        scheduleNotification(mPacket, delay, mContext);

        Utility.addNotifTimeIntoDB(mContext, mPacket.getId(), readTime);
    }

    public static void cancelAlarmManager(Packet mPacket, Context mContext) {
        Intent notificationIntent = new Intent(mContext, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                mContext, mPacket.getId(), notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        Utility.removeNotifTimeFromDB(mContext, mPacket.getId());
    }

    public static boolean isAlarmManagerActive(Packet mPacket, Context mContext) {
        Intent notificationIntent = new Intent(mContext, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                mContext, mPacket.getId(), notificationIntent, PendingIntent.FLAG_NO_CREATE);

        if (pendingIntent == null) {
            Log.v("Notification", "Not set");
            return false;
        }

        Log.v("Notification", "set");
        return true;
    }

//    private int getNotificationIcon() {
//        boolean useSilhouette = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
//        return useSilhouette ? R.drawable.ic_notification : R.drawable.ic_notifications_black;
//    }

}
