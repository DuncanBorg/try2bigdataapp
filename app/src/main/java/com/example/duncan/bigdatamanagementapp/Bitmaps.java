package com.example.duncan.bigdatamanagementapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;

public class Bitmaps {
    public static HashMap<Integer, Bitmap> images = new HashMap<>();

    public static Bitmap get(int img) {
        Bitmap bmp = images.get(img);
        if(bmp == null) {
            ImageModel m = MainActivity.images.get(img);
            byte[] bytes = m.img;
            bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            images.put(img, bmp);
            m.img = null;
        }
        return bmp;
    }
}
