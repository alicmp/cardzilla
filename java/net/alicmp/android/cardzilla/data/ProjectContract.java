package net.alicmp.android.cardzilla.data;

import android.provider.BaseColumns;

/**
 * Created by ali on 9/1/15.
 */
public final class ProjectContract {

    public ProjectContract() {}

    public static abstract class PacketEntry implements BaseColumns {
        public static final String TABLE_NAME = "packet";
        public static final String COLUMN_PACKET_TITLE = "packet_title";
        public static final String COLUMN_LAST_READ_TIME = "last_read_time";
        public static final String COLUMN_NEXT_READ_TIME = "next_read_time";
    }

    public static abstract class CardEntry implements BaseColumns {
        public static final String TABLE_NAME = "card";
        public static final String COLUMN_PACKET_ID = "packet_id"; // foreign key
        public static final String COLUMN_FRONT_TEXT = "front_text";
        public static final String COLUMN_BACK_TEXT = "back_text";
        public static final String COLUMN_BACK_IMAGE_PATH = "back_image_path";
        public static final String COLUMN_FRONT_IMAGE_PATH = "front_image_path";
        public static final String COLUMN_CARD_LEVEL = "card_level";
        public static final String COLUMN_READ_TIME = "read_time";
    }

}
