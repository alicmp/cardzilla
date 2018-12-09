package net.alicmp.android.cardzilla.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import net.alicmp.android.cardzilla.model.Card;
import net.alicmp.android.cardzilla.model.Packet;
import net.alicmp.android.cardzilla.model.PacketStatistics;
import net.alicmp.android.cardzilla.data.ProjectContract;
import net.alicmp.android.cardzilla.data.ProjectDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ali on 8/27/15.
 */
public class Utility {

    public static ArrayList<Card> getAllCards(Context context, int packetId) {

        ArrayList<Card> mCardsList = new ArrayList<>();
        try {
            ProjectDbHelper dbHelper = new ProjectDbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] projection = {
                    ProjectContract.CardEntry._ID,
                    ProjectContract.CardEntry.COLUMN_FRONT_TEXT,
                    ProjectContract.CardEntry.COLUMN_BACK_TEXT,
                    ProjectContract.CardEntry.COLUMN_CARD_LEVEL,
                    ProjectContract.CardEntry.COLUMN_FRONT_IMAGE_PATH,
                    ProjectContract.CardEntry.COLUMN_BACK_IMAGE_PATH
            };
            String selection = ProjectContract.CardEntry.COLUMN_PACKET_ID + "=?";
            String[] selectionArgs = {Integer.toString(packetId)};

            Cursor c = db.query(
                    ProjectContract.CardEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null);

            if (c.moveToFirst())
                do {
                    mCardsList.add(new Card(
                            c.getInt(c.getColumnIndex(ProjectContract.CardEntry._ID)),
                            c.getString(c.getColumnIndex(ProjectContract.CardEntry.COLUMN_FRONT_TEXT)),
                            c.getString(c.getColumnIndex(ProjectContract.CardEntry.COLUMN_BACK_TEXT)),
                            c.getInt(c.getColumnIndex(ProjectContract.CardEntry.COLUMN_CARD_LEVEL)),
                            c.getString(c.getColumnIndex(ProjectContract.CardEntry.COLUMN_FRONT_IMAGE_PATH)),
                            c.getString(c.getColumnIndex(ProjectContract.CardEntry.COLUMN_BACK_IMAGE_PATH))
                    ));
                } while (c.moveToNext());

            db.close();
            dbHelper.close();
        } catch (SQLiteException e) {
            System.out.println("Problem in loading cards: " + e.toString());
        }
        return mCardsList;
    }

    public static ArrayList<Packet> getPackets(Context context) {

        ArrayList<Packet> mPacketsList = new ArrayList<>();

        try {
            ProjectDbHelper dbHelper = new ProjectDbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] projection = {
                    ProjectContract.PacketEntry._ID,
                    ProjectContract.PacketEntry.COLUMN_PACKET_TITLE,
                    ProjectContract.PacketEntry.COLUMN_NEXT_READ_TIME
            };

            Cursor c = db.query(
                    ProjectContract.PacketEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null);

            String selection = ProjectContract.CardEntry.COLUMN_PACKET_ID + "=?";

            if (c.moveToFirst())
                do {
                    int packetId = c.getInt(c.getColumnIndex(ProjectContract.PacketEntry._ID));
                    int numberOfCards = (int) DatabaseUtils.queryNumEntries(
                            db,
                            ProjectContract.CardEntry.TABLE_NAME,
                            selection,
                            new String[]{packetId + ""});

                    mPacketsList.add(new Packet(
                            packetId,
                            c.getString(c.getColumnIndex(ProjectContract.PacketEntry.COLUMN_PACKET_TITLE)),
                            numberOfCards,
                            c.getLong(c.getColumnIndex(ProjectContract.PacketEntry.COLUMN_NEXT_READ_TIME))
                    ));
                } while (c.moveToNext());

            db.close();
            dbHelper.close();
        } catch (SQLiteException e) {
            System.out.println("Problem in loading packages: " + e.toString());
        }

        return mPacketsList;
    }

    public static void onCardAnswered(Context context, Card card, boolean isTrue) {
        int level = card.getLevel();
        int offset = 1000 * 60 * 60;
        if (!isTrue) {
            level = 1;
            offset *= 2;
        } else {
            switch (level) {
                case 0:
                    offset *= 48;
                    level = 3;
                    break;
                case 1:
                    offset *= 2;
                    level = 2;
                    break;
                case 2:
                    offset *= 24;
                    level = 3;
                    break;
                case 3:
                    offset *= 72;
                    level = 4;
                    break;
                default:
                    offset *= (level * 24);
                    level *= 2;
                    break;
            }
        }

        try {
            ProjectDbHelper dbHelper = new ProjectDbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            ContentValues values = new ContentValues();
            values.put(ProjectContract.CardEntry.COLUMN_CARD_LEVEL, level);
            values.put(ProjectContract.CardEntry.COLUMN_READ_TIME, getDateAndTime(offset));

            String selection = ProjectContract.CardEntry._ID + " LIKE ?";
            String[] selectionArgs = {String.valueOf(card.getId())};

            db.update(
                    ProjectContract.CardEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

            db.close();
            dbHelper.close();
        } catch (SQLiteException e) {
            System.out.println("Problem in onCardAnswered: " + e.toString());
        }
    }

    public static ArrayList<Card> getCards(Context context, int packetId, int numberOfNewCards) {
        ArrayList<Card> cardsList = new ArrayList<>();

        try {
            ProjectDbHelper dbHelper = new ProjectDbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            // Getting cards which their time to read has arrived
            String[] projection = {
                    ProjectContract.CardEntry._ID,
                    ProjectContract.CardEntry.COLUMN_FRONT_TEXT,
                    ProjectContract.CardEntry.COLUMN_BACK_TEXT,
                    ProjectContract.CardEntry.COLUMN_CARD_LEVEL,
                    ProjectContract.CardEntry.COLUMN_FRONT_IMAGE_PATH,
                    ProjectContract.CardEntry.COLUMN_BACK_IMAGE_PATH
            };
            String selection = ProjectContract.CardEntry.COLUMN_PACKET_ID + "=? AND "
                    + ProjectContract.CardEntry.COLUMN_READ_TIME + "<=? ";
            String[] selectionArgs = {
                    Integer.toString(packetId),
                    getDateAndTime(0) + ""
            };

            Cursor c = db.query(
                    ProjectContract.CardEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null);

            if (c.moveToFirst())
                do {
                    cardsList.add(new Card(
                            c.getInt(c.getColumnIndex(ProjectContract.CardEntry._ID)),
                            c.getString(c.getColumnIndex(ProjectContract.CardEntry.COLUMN_FRONT_TEXT)),
                            c.getString(c.getColumnIndex(ProjectContract.CardEntry.COLUMN_BACK_TEXT)),
                            c.getInt(c.getColumnIndex(ProjectContract.CardEntry.COLUMN_CARD_LEVEL)),
                            c.getString(c.getColumnIndex(ProjectContract.CardEntry.COLUMN_FRONT_IMAGE_PATH)),
                            c.getString(c.getColumnIndex(ProjectContract.CardEntry.COLUMN_BACK_IMAGE_PATH))
                    ));
                } while (c.moveToNext());

            // Getting unread cards
            selection = ProjectContract.CardEntry.COLUMN_PACKET_ID + "=? AND "
                    + ProjectContract.CardEntry.COLUMN_CARD_LEVEL + "=? ";
            selectionArgs[1] = "0";

            c = db.query(
                    ProjectContract.CardEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null,
                    numberOfNewCards + "");

            if (c.moveToFirst())
                do {
                    cardsList.add(new Card(
                            c.getInt(c.getColumnIndex(ProjectContract.CardEntry._ID)),
                            c.getString(c.getColumnIndex(ProjectContract.CardEntry.COLUMN_FRONT_TEXT)),
                            c.getString(c.getColumnIndex(ProjectContract.CardEntry.COLUMN_BACK_TEXT)),
                            c.getInt(c.getColumnIndex(ProjectContract.CardEntry.COLUMN_CARD_LEVEL)),
                            c.getString(c.getColumnIndex(ProjectContract.CardEntry.COLUMN_FRONT_IMAGE_PATH)),
                            c.getString(c.getColumnIndex(ProjectContract.CardEntry.COLUMN_BACK_IMAGE_PATH))
                    ));
                } while (c.moveToNext());

            c.close();
            db.close();
            dbHelper.close();
        } catch (SQLiteException e) {
            System.out.println("Problem in getting cards: " + e.toString());
        }

        return cardsList;
    }

    public static long addCard(Context context, Card card, int packetId) {
        long id = 0;
        try {
            ProjectDbHelper dbHelper = new ProjectDbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ProjectContract.CardEntry.COLUMN_PACKET_ID, packetId); // Foreign Key
            values.put(ProjectContract.CardEntry.COLUMN_BACK_TEXT, card.getBackText());
            values.put(ProjectContract.CardEntry.COLUMN_FRONT_TEXT, card.getFrontText());
            values.put(ProjectContract.CardEntry.COLUMN_CARD_LEVEL, card.getLevel());
            values.put(ProjectContract.CardEntry.COLUMN_FRONT_IMAGE_PATH, card.getFrontImagePath());
            values.put(ProjectContract.CardEntry.COLUMN_BACK_IMAGE_PATH, card.getBackImagePath());

            id = db.insert(
                    ProjectContract.CardEntry.TABLE_NAME,
                    null,
                    values);

            db.close();
            dbHelper.close();
        } catch (SQLiteException e) {
            System.out.println("Problem in adding cards: " + e.toString());
        }
        return id;
    }

    public static long addPacket(Context context, Packet packet) {
        long id = 0;

        try {
            ProjectDbHelper dbHelper = new ProjectDbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ProjectContract.PacketEntry.COLUMN_PACKET_TITLE, packet.getPacketName());

            id = db.insert(
                    ProjectContract.PacketEntry.TABLE_NAME,
                    null,
                    values);

            db.close();
            dbHelper.close();
        } catch (SQLiteException e) {
            System.out.println("Problem in adding package: " + e.toString());
        }

        return id;
    }

    public static void editCard(Context context, Card card) {
        try {
            ProjectDbHelper dbHelper = new ProjectDbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            ContentValues values = new ContentValues();
            values.put(ProjectContract.CardEntry.COLUMN_FRONT_TEXT, card.getFrontText());
            values.put(ProjectContract.CardEntry.COLUMN_BACK_TEXT, card.getBackText());
            values.put(ProjectContract.CardEntry.COLUMN_FRONT_IMAGE_PATH, card.getFrontImagePath());
            values.put(ProjectContract.CardEntry.COLUMN_BACK_IMAGE_PATH, card.getBackImagePath());

            String selection = ProjectContract.CardEntry._ID + " LIKE ?";
            String[] selectionArgs = {String.valueOf(card.getId())};

            db.update(
                    ProjectContract.CardEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

            db.close();
            dbHelper.close();
        } catch (SQLiteException e) {
            System.out.println("Problem in editing card: " + e.toString());
        }
    }

    public static void editPacket(Context context, Packet mPacket) {
        try {
            ProjectDbHelper dbHelper = new ProjectDbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            ContentValues values = new ContentValues();
            values.put(ProjectContract.PacketEntry.COLUMN_PACKET_TITLE, mPacket.getPacketName());

            String selection = ProjectContract.PacketEntry._ID + " LIKE ?";
            String[] selectionArgs = {String.valueOf(mPacket.getId())};

            db.update(
                    ProjectContract.PacketEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

            db.close();
            dbHelper.close();
        } catch (SQLiteException e) {
            System.out.println("Problem in editing card: " + e.toString());
        }
    }

    public static void resetPacket(Context context, int packetId) {
        try {
            ProjectDbHelper dbHelper = new ProjectDbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            ContentValues values = new ContentValues();
            values.put(ProjectContract.CardEntry.COLUMN_CARD_LEVEL, 0);
            values.putNull(ProjectContract.CardEntry.COLUMN_READ_TIME);

            String selection = ProjectContract.CardEntry.COLUMN_PACKET_ID + " LIKE ?";
            String[] selectionArgs = {String.valueOf(packetId)};

            db.update(
                    ProjectContract.CardEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

            values.clear();
            values.put(ProjectContract.PacketEntry.COLUMN_NEXT_READ_TIME, 0);
            values.put(ProjectContract.PacketEntry.COLUMN_LAST_READ_TIME, 0);

            selection = ProjectContract.PacketEntry._ID + " LIKE ?";

            db.update(
                    ProjectContract.PacketEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

            db.close();
            dbHelper.close();
        } catch (SQLiteException e) {
            System.out.println("Problem in editing card: " + e.toString());
        }
    }

    public static void deleteCard(Context context, int cardId) {
        try {
            ProjectDbHelper dbHelper = new ProjectDbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String selection = ProjectContract.CardEntry._ID + " LIKE ?";
            String[] selectionArgs = {String.valueOf(cardId)};

            db.delete(
                    ProjectContract.CardEntry.TABLE_NAME,
                    selection,
                    selectionArgs);

            db.close();
            dbHelper.close();
        } catch (SQLiteException e) {
            System.out.println("Problem in editing card: " + e.toString());
        }
    }

    public static void removePacket(Context context, int packetId) {
        try {
            ProjectDbHelper dbHelper = new ProjectDbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String selection = ProjectContract.CardEntry.COLUMN_PACKET_ID + " LIKE ?";
            String[] selectionArgs = {String.valueOf(packetId)};

            db.delete(
                    ProjectContract.CardEntry.TABLE_NAME,
                    selection,
                    selectionArgs);

            selection = ProjectContract.PacketEntry._ID + " LIKE ?";

            db.delete(
                    ProjectContract.PacketEntry.TABLE_NAME,
                    selection,
                    selectionArgs);

            db.close();
            dbHelper.close();
        } catch (SQLiteException e) {
            System.out.println("Problem in editing card: " + e.toString());
        }
    }

    public static long getDateAndTime(int offset) {
        Calendar calendar = new GregorianCalendar();
        return calendar.getTimeInMillis() + offset;
    }

    public static boolean hasNewCard(Context context, int packetId) {
        try {
            ProjectDbHelper dbHelper = new ProjectDbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] projection = {
                    ProjectContract.CardEntry._ID
            };
            String selection = ProjectContract.CardEntry.COLUMN_PACKET_ID + "=? AND "
                    + ProjectContract.CardEntry.COLUMN_CARD_LEVEL + "=? ";
            String[] selectionArgs = {
                    Integer.toString(packetId),
                    "0"
            };

            Cursor c = db.query(
                    ProjectContract.CardEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null,
                    null);

            if (c.getCount() <= 0) {
                c.close();
                return false;
            }

            db.close();
            dbHelper.close();
        } catch (SQLiteException e) {
            System.out.println("Problem in getting cards: " + e.toString());
        }

        return true;
    }

    public static long nextReadTime(Context context, int packetId) {
        long nextReadTime = 0;
        try {
            ProjectDbHelper dbHelper = new ProjectDbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String selection = ProjectContract.CardEntry.COLUMN_PACKET_ID + "=? AND "
                    + ProjectContract.CardEntry.COLUMN_READ_TIME + " !=?";
            Cursor c = db.query(
                    ProjectContract.CardEntry.TABLE_NAME,
                    new String[]{"MIN(" + ProjectContract.CardEntry.COLUMN_READ_TIME + ")"},
                    selection,
                    new String[]{String.valueOf(packetId), 0 + ""},
                    null,
                    null,
                    null);

            if (c.moveToFirst())
                nextReadTime = c.getLong(0);

            db.close();
            dbHelper.close();
        } catch (SQLiteException e) {
            System.out.println("Problem in getting nextReadTime: " + e.toString());
        }

        return nextReadTime;
    }

    public static PacketStatistics getStatics(Context context, int packetId) {

        int reviewingCardsCount = 0;
        int newCardsCount = 0;
        int learningCardsCount = 0;

        PacketStatistics statistics;
        try {
            ProjectDbHelper dbHelper = new ProjectDbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String selection = ProjectContract.CardEntry.COLUMN_PACKET_ID + "=? AND "
                    + ProjectContract.CardEntry.COLUMN_CARD_LEVEL + "=? ";

            newCardsCount = (int) DatabaseUtils.queryNumEntries(
                    db,
                    ProjectContract.CardEntry.TABLE_NAME,
                    selection,
                    new String[]{packetId + "", "0"});

            selection = ProjectContract.CardEntry.COLUMN_PACKET_ID + "=? AND ("
                    + ProjectContract.CardEntry.COLUMN_CARD_LEVEL + "=? OR "
                    + ProjectContract.CardEntry.COLUMN_CARD_LEVEL + "=? OR "
                    + ProjectContract.CardEntry.COLUMN_CARD_LEVEL + "=?) ";

            learningCardsCount = (int) DatabaseUtils.queryNumEntries(
                    db,
                    ProjectContract.CardEntry.TABLE_NAME,
                    selection,
                    new String[]{packetId + "", "1", "2", "3"});

            selection = ProjectContract.CardEntry.COLUMN_PACKET_ID + "=? AND "
                    + ProjectContract.CardEntry.COLUMN_CARD_LEVEL + ">? ";

            reviewingCardsCount = (int) DatabaseUtils.queryNumEntries(
                    db,
                    ProjectContract.CardEntry.TABLE_NAME,
                    selection,
                    new String[]{packetId + "", "3"});
        } catch (SQLiteException e) {
            System.out.println("Problem in getting statics: " + e.toString());
        }

        statistics = new PacketStatistics(reviewingCardsCount, newCardsCount, learningCardsCount);

        return statistics;
    }

    public static void addNotifTimeIntoDB(Context context, int packetId, long notifTime) {
        try {
            ProjectDbHelper dbHelper = new ProjectDbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            ContentValues values = new ContentValues();
            values.put(ProjectContract.PacketEntry.COLUMN_NEXT_READ_TIME, notifTime);

            String selection = ProjectContract.PacketEntry._ID + " LIKE ?";
            String[] selectionArgs = {String.valueOf(packetId)};

            db.update(
                    ProjectContract.PacketEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

            db.close();
            dbHelper.close();
        } catch (SQLiteException e) {
            System.out.println("Problem in editing card: " + e.toString());
        }
    }

    public static void removeNotifTimeFromDB(Context context, int packetId) {
        try {
            ProjectDbHelper dbHelper = new ProjectDbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            ContentValues values = new ContentValues();
            values.put(ProjectContract.PacketEntry.COLUMN_NEXT_READ_TIME, "");

            String selection = ProjectContract.PacketEntry._ID + " LIKE ?";
            String[] selectionArgs = {String.valueOf(packetId)};

            db.update(
                    ProjectContract.PacketEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

            db.close();
            dbHelper.close();
        } catch (SQLiteException e) {
            System.out.println("Problem in editing card: " + e.toString());
        }
    }

    public static long getPacketsTimeToRead(Context context, int packetId) {
        long timeToRead = 0;
        try {
            ProjectDbHelper dbHelper = new ProjectDbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String selection = ProjectContract.PacketEntry._ID + "=?";

            String[] projection = {
                    ProjectContract.PacketEntry.COLUMN_NEXT_READ_TIME
            };

            String[] selectionArgs = {
                    Integer.toString(packetId)
            };

            Cursor c = db.query(
                    ProjectContract.PacketEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null,
                    null);

            if (c.moveToFirst())
                timeToRead = c.getLong(0);

        } catch (SQLiteException e) {
            System.out.println("Problem in getting statics: " + e.toString());
        }

        return timeToRead;
    }

}
