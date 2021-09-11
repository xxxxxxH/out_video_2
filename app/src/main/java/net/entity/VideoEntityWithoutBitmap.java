package net.entity;

import android.os.Parcel;
import android.os.Parcelable;

import net.utils.Utils;

import java.io.Serializable;

public class VideoEntityWithoutBitmap implements Serializable, Parcelable {
    String name;
    String size;
    String url;
    String duration;
    String time;
    byte[] bitmap;

    public VideoEntityWithoutBitmap(){

    }

    public VideoEntityWithoutBitmap(String name,String size,String url,String duration,String time,byte[] bitmap){
        this.name = name;
        this.size = size;
        this.url = url;
        this.duration = duration;
        this.time = time;
        this.bitmap = bitmap;
    }

    protected VideoEntityWithoutBitmap(Parcel in) {
        name = in.readString();
        size = in.readString();
        url = in.readString();
        duration = in.readString();
        time = in.readString();
        byte[] ba = in.createByteArray();
        in.unmarshall(ba, 0, ba.length);
        bitmap = ba;
    }

    public static final Creator<VideoEntityWithoutBitmap> CREATOR = new Creator<VideoEntityWithoutBitmap>() {
        @Override
        public VideoEntityWithoutBitmap createFromParcel(Parcel in) {
            return new VideoEntityWithoutBitmap(in);
        }

        @Override
        public VideoEntityWithoutBitmap[] newArray(int size) {
            return new VideoEntityWithoutBitmap[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public byte[] getBitmap() {
        return bitmap;
    }

    public void setBitmap(byte[] bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(size);
        parcel.writeString(url);
        parcel.writeString(duration);
        parcel.writeString(time);
        parcel.writeByteArray(bitmap);
    }
}
