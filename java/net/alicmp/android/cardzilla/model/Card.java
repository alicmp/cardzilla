package net.alicmp.android.cardzilla.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import net.alicmp.android.cardzilla.helper.FileUtil;

import java.io.Serializable;

/**
 * Created by ali on 8/15/15.
 */
public class Card implements Serializable {

    private int id;
    private String frontText;
    private String backText;
    private int level;
    private String dateAndTime;
    private String frontImagePath;
    private String backImagePath;

    public Card() {}

    public Card(int id,
                String frontText,
                String backText,
                int level,
                String frontImagePath,
                String backImagePath) {
        this.id = id;
        this.frontText = frontText;
        this.backText = backText;
        this.level = level;
        this.frontImagePath = frontImagePath;
        this.backImagePath = backImagePath;

    }

    public int getId() {
        return id;
    }

    public String getFrontText() {
        return frontText;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public String getBackText() {
        return backText;
    }

    public int getLevel() {
        return level;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFrontText(String frontText) {
        this.frontText = frontText;
    }

    public void setBackText(String backText) {
        this.backText = backText;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getFrontImagePath() {
        return frontImagePath;
    }

    public void setFrontImagePath(String frontImagePath) {
        this.frontImagePath = frontImagePath;
    }

    public String getBackImagePath() {
        return backImagePath;
    }

    public void setBackImagePath(String backImagePath) {
        this.backImagePath = backImagePath;
    }

    public boolean hasFrontImage() {
        return getFrontImagePath() != null && !getFrontImagePath().isEmpty();
    }

    public boolean hasBackImage() {
        return getBackImagePath() != null && !getBackImagePath().isEmpty();
    }

    /**
     * Get a thumbnail of this front's picture, or null if the front doesn't have a
     * Image.
     *
     * @return Thumbnail of the front.
     */
    public Drawable getThumbnailOfFront(Context context) {

        return getScaledImageOfFront(context, 128, 128);
    }

    /**
     * Get a thumbnail of this back's picture, or null if the back doesn't have a
     * Image.
     *
     * @return Thumbnail of the back.
     */
    public Drawable getThumbnailOfBack(Context context) {

        return getScaledImageOfBack(context, 128, 128);
    }

    /**
     * Get this front's picture, or null if the front doesn't have a Image.
     *
     * @return Image of the front.
     */
    public Drawable getImageOfFront(Context context) {

        return getScaledImageOfFront(context, 512, 512);
    }

    /**
     * Get this back's picture, or null if the back doesn't have a Image.
     *
     * @return Image of the back.
     */
    public Drawable getImageOfBack(Context context) {

        return getScaledImageOfBack(context, 512, 512);
    }

    /**
     * Get a scaled version of this front's Image, or null if the front doesn't have
     * a Image.
     *
     * @return Image of the front.
     */
    private Drawable getScaledImageOfFront(Context context, int reqWidth, int reqHeight) {

        // If profile has a Image.
        if (hasFrontImage()) {

            // Decode the input stream into a bitmap.
            Bitmap bitmap = FileUtil.getResizedBitmap(getFrontImagePath(), reqWidth, reqHeight);

            // If was successfully created.
            if (bitmap != null) {

                // Return a drawable representation of the bitmap.
                return new BitmapDrawable(context.getResources(), bitmap);
            }
        }

        // Return the default image drawable.
        return null;
    }

    /**
     * Get a scaled version of this back's Image, or null if the back doesn't have
     * a Image.
     *
     * @return Image of the back.
     */
    private Drawable getScaledImageOfBack(Context context, int reqWidth, int reqHeight) {

        // If profile has a Image.
        if (hasBackImage()) {

            // Decode the input stream into a bitmap.
            Bitmap bitmap = FileUtil.getResizedBitmap(getBackImagePath(), reqWidth, reqHeight);

            // If was successfully created.
            if (bitmap != null) {

                // Return a drawable representation of the bitmap.
                return new BitmapDrawable(context.getResources(), bitmap);
            }
        }

        // Return the default image drawable.
        return null;
    }
}
