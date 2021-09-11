package net.entity;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class FolderEntity {
    String name;
    int num;
    Bitmap[] url;

    public Bitmap[] getUrl() {
        return url;
    }

    public void setUrl(Bitmap[] url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
