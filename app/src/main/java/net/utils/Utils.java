package net.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mmkv.MMKV;

import net.entity.VideoEntity;
import net.entity.VideoEntityWithoutBitmap;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Utils {
    public static final String byteToString(long size) {

        long GB = 1024 * 1024 * 1024;//定义GB的计算常量
        long MB = 1024 * 1024;//定义MB的计算常量
        long KB = 1024;//定义KB的计算常量
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String resultSize = "";
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = df.format(size / (float) GB) + " GB   ";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = df.format(size / (float) MB) + " MB   ";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = df.format(size / (float) KB) + " KB   ";
        } else {
            resultSize = size + " B   ";
        }
        return resultSize;
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static byte[] bitmap2Bytes(Bitmap bitmap) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();

    }


    public static Bitmap byte2Bitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public static ArrayList<VideoEntityWithoutBitmap> handleEntityBitmap(ArrayList<VideoEntity> data) {
        ArrayList<VideoEntityWithoutBitmap> result = new ArrayList<>();
        for (VideoEntity item : data) {
            if (data == null)
                continue;
            VideoEntityWithoutBitmap entiy = new VideoEntityWithoutBitmap();
            entiy.setName(item.getName());
            entiy.setSize(item.getSize());
            entiy.setUrl(item.getUrl());
            entiy.setDuration(item.getDuration());
            entiy.setTime(item.getTime());
            try {
                entiy.setBitmap(bitmap2Bytes(item.getBitmap()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.add(entiy);
        }
        return result;
    }

    public static VideoEntityWithoutBitmap handleSingleEntityBitmap(VideoEntity item) {
        VideoEntityWithoutBitmap entiy = new VideoEntityWithoutBitmap();
        entiy.setName(item.getName());
        entiy.setSize(item.getSize());
        entiy.setUrl(item.getUrl());
        entiy.setDuration(item.getDuration());
        entiy.setTime(item.getTime());
        try {
            entiy.setBitmap(bitmap2Bytes(item.getBitmap()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entiy;
    }

    public static ArrayList<VideoEntity> returnData(ArrayList<VideoEntityWithoutBitmap> data) {
        ArrayList<VideoEntity> result = new ArrayList<>();
        for (VideoEntityWithoutBitmap item : data) {
            if (data == null)
                continue;
            VideoEntity entiy = new VideoEntity();
            entiy.setName(item.getName());
            entiy.setSize(item.getSize());
            entiy.setUrl(item.getUrl());
            entiy.setDuration(item.getDuration());
            entiy.setTime(item.getTime());
            entiy.setBitmap(byte2Bitmap(item.getBitmap()));
            result.add(entiy);
        }
        return result;
    }

    public static VideoEntity returnSingleData(VideoEntityWithoutBitmap item) {
        VideoEntity entiy = new VideoEntity();
        entiy.setName(item.getName());
        entiy.setSize(item.getSize());
        entiy.setUrl(item.getUrl());
        entiy.setDuration(item.getDuration());
        entiy.setTime(item.getTime());
        entiy.setBitmap(byte2Bitmap(item.getBitmap()));
        return entiy;
    }

    public static void saveKey(String newKey) {
        Set<String> key = MMKV.defaultMMKV().decodeStringSet("key");
        if (key == null) {
            key = new HashSet<>();
        }
        key.add(newKey);
        MMKV.defaultMMKV().encode("key", key);
    }
}
