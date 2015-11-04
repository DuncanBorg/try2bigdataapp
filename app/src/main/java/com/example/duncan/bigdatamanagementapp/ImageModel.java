package com.example.duncan.bigdatamanagementapp;

import android.graphics.Bitmap;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "Images")
public class ImageModel extends Model
{
    @Column(name = "Img")
    public Bitmap img;

    public ImageModel() {
        super();
    }

    public static List<ImageModel> getAll() {
        return new Select()
                .from(ImageModel.class)
                .execute();
    }
//
//    public static int getLastId()
//    {
//        List<ImageModel> images = getAll();
//        int lastId = 0;
//
//        for(ImageModel img : images)
//        {
//            if(img.id > lastId)
//                lastId = img.id;
//        }
//
//        return lastId;
//    }
}
