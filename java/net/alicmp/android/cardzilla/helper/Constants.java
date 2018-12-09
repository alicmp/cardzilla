package net.alicmp.android.cardzilla.helper;

import android.os.Environment;

import java.io.File;

/**
 * Created by ali on 3/31/16.
 */
public class Constants {

    public static final String ARG_CARD = "card";
    public static final String ARG_CARD_POSITION = "cardPosition";
    public static final String KEY_FRONT_IMAGE_URI = "frontImageUri";
    public static final String KEY_BACK_IMAGE_URI = "backImageUri";
    public static final String KEY_FRONT_IMAGE_PATH = "frontImagePath";
    public static final String KEY_BACK_IMAGE_PATH = "backImagePath";
    public static final String KEY_FRONT_TEXT = "frontText";
    public static final String KEY_BACK_TEXT = "backText";

    public static final int REQUEST_ADD = 1002;
    public static final int REQUEST_EDIT = 1003;
    public static final int REQUEST_REMOVE_FRONT_IMAGE = 1004;
    public static final int REQUEST_REMOVE_BACK_IMAGE = 1005;
    public static int FRONT_IMAGE = 1000;
    public static int BACK_IMAGE = 1001;
    public static final String PICTURE_DIRECTORY = Environment.getExternalStorageDirectory()
            + File.separator + "DCIM" + File.separator + "Cardzila" + File.separator;

}
