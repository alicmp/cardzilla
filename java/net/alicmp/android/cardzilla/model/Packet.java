package net.alicmp.android.cardzilla.model;

import android.os.Parcel;
import android.os.Parcelable;

import net.alicmp.android.cardzilla.helper.Utility;

/**
 * Created by ali on 8/29/15.
 */
public class Packet implements Parcelable {

    private int id;
    private String packetName;
    private int numberOfCards;
    private long timeToRead;

    public Packet() {
    }

    public Packet(int id, String packetName, int numberOfCards, long timeToRead) {
        this.id = id;
        this.packetName = packetName;
        this.numberOfCards = numberOfCards;
        this.timeToRead = timeToRead;
    }

    protected Packet(Parcel in) {
        id = in.readInt();
        packetName = in.readString();
        numberOfCards = in.readInt();
        timeToRead = in.readLong();
    }

    public static final Creator<Packet> CREATOR = new Creator<Packet>() {
        @Override
        public Packet createFromParcel(Parcel in) {
            return new Packet(in);
        }

        @Override
        public Packet[] newArray(int size) {
            return new Packet[size];
        }
    };

    public long getTimeToRead() {
        return timeToRead;
    }

    public void setTimeToRead(long timeToRead) {
        this.timeToRead = timeToRead;
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards(int numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPacketName() {
        return packetName;
    }

    public void setPacketName(String packetName) {
        this.packetName = packetName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(packetName);
        dest.writeInt(numberOfCards);
        dest.writeLong(timeToRead);
    }

    public boolean isTimeToReadValid() {
        long now = Utility.getDateAndTime(0);
        return timeToRead > now;
    }

    public String timeToReadString() {
        long now = Utility.getDateAndTime(0);
        int aDay = 1000 * 60 * 60 * 24;
        long diff = timeToRead - now;
        String ttr;

        if (diff <= aDay) {
            ttr = (diff / (1000 * 60)) + " min";
        } else {
            ttr = (diff / aDay) + " day";
        }

        return ttr;
    }

    public String timeToReadString(long timeToRead) {
        long now = Utility.getDateAndTime(0);
        int aDay = 1000 * 60 * 60 * 24;
        long diff = timeToRead - now;
        String ttr;

        if (diff <= aDay) {
            ttr = (diff / (1000 * 60)) + " min";
        } else {
            ttr = (diff / aDay) + " day";
        }

        return ttr;
    }
}
