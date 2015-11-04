package com.example.duncan.bigdatamanagementapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.activeandroid.ActiveAndroid;

import java.io.File;

/**
 * Created by Duncan on 29/10/2015.
 */
public class Upload extends ActionBarActivity
{
    public ImageButton img; //gets the image button from xml
    private static final int SELECT_PICTURE = 1; //set SELECT_PICTURE to true.
    public Button submit; //get submit button from xml
    public Button cancel; //get cancel button from xml
    private File Path = null;
    public String path2;
    private File selectedImagePath = null;
    private int Id = 0;

    protected void onCreate(Bundle savedInstanceState) {
        ActiveAndroid.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploadimage);

        img = (ImageButton) findViewById(R.id.imageButton); //loads the button from xml
        img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent photoPickerInt = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //creates new intent to access the gallery application to load external images.
                startActivityForResult(photoPickerInt, SELECT_PICTURE); //allows user to select photo and assigns the image to the image button through the intent.
            }
        });

        submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {

            //once the user clicks submit, it grabs the data inputted by the user and saves it in the database.
            @Override
            public void onClick(View v) {
                ImageModel image = new ImageModel();
                image.img = BitmapFactory.decodeFile(path2);

                image.save();

                Log.d("dbtest", ImageModel.getAll()+"");

                finish(); //exits the current screen
            }
        });

        cancel = (Button) findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }); //exits the current window



    }
    //this method gets the image path and sets the corresponding image on the image button
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == SELECT_PICTURE) {
                Uri image = data.getData();
                Log.d("image",image.toString());
                selectedImagePath = new File(getRealPathFromURI(image));
                String path = selectedImagePath.toString();
                path2 = path;
                img.setImageBitmap(BitmapFactory.decodeFile(path));


            }
        }
    }

    //this mathod gets the actual URI using a cursor from the database.
    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = null;
        try {
            String[] media = { MediaStore.Images.Media.DATA };
            cursor = getContentResolver().query(contentURI,
                    media, null, null, null);
            int c = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(c);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        //return result;
    }




}
