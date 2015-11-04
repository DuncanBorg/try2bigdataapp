package com.example.duncan.bigdatamanagementapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.activeandroid.ActiveAndroid;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

    public static AsyncTask<String, Void, String> executeTest(String targetURL)
    {
        Log.d("dbconntest", "hello");
        return new AsyncTask<String, Void, String>()
        {
            @Override
            protected String doInBackground(String... params)
            {
                Log.d("dbconntest", "hello2");
                HttpURLConnection connection = null;
                try
                {
                    //Create connection
                    URL url = new URL(params[0]);
                    connection = (HttpURLConnection)url.openConnection();
                    //connection.setRequestMethod("POST");
                    connection.setRequestMethod("GET");
                    //connection.setRequestProperty("Content-Type",
                    //        "application/x-www-form-urlencoded");

    //            connection.setRequestProperty("Content-Length",
    //                    Integer.toString(urlParameters.getBytes().length));
    //            connection.setRequestProperty("Content-Language", "en-US");
    //
    //            connection.setUseCaches(false);
    //            connection.setDoOutput(true);

                    //Send request
    //            DataOutputStream wr = new DataOutputStream (
    //                    connection.getOutputStream());
    //            wr.writeBytes(urlParameters);
    //            wr.close();

                    //Get Response
                    InputStream is = connection.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+
                    String line;
                    Log.d("dbconntest", "hello5");
                    while((line = rd.readLine()) != null)
                    {
                        response.append(line);
                        response.append('\r');
                    }
                    Log.d("dbconntest", "hello6");
                    rd.close();
                    Log.d("dbconntest", "hello4 "+response.toString());
                    return response.toString();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.d("dbconntest", "hello3 "+e.getMessage());
                    return null;
                }
                finally
                {
                    if(connection != null)
                    {
                        connection.disconnect();
                    }
                }
            }
        }.execute(targetURL);
    }

    public static AsyncTask<String, Void, Bitmap> downloadImage(String target)
    {
        Log.d("dbconntest", "hello");
        return new AsyncTask<String, Void, Bitmap>()
        {
            @Override
            protected Bitmap doInBackground(String... params)
            {
                Log.d("dbconntest", "hello2");
                HttpURLConnection connection = null;
                try
                {
                    //Create connection
                    URL url = new URL(params[0]);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type",
                            "application/octet-stream");

                   // connection.setFixedLengthStreamingMode(150000);
//
//            connection.setRequestProperty("Content-Length",
//                    Integer.toString(urlParameters.getBytes().length));
//            connection.setRequestProperty("Content-Language", "en-US");
//
//            connection.setUseCaches(false);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setConnectTimeout(1000);
                    connection.setReadTimeout(1000);
//
//                   // Send request
//            DataOutputStream wr = new DataOutputStream (
//                    connection.getOutputStream());
//            wr.writeBytes(urlParameters);
//            wr.close();

                    int responseCode = connection.getResponseCode();
                    Log.d("dbconntest", "RespCode "+responseCode);

                    //Get Response
                    InputStream is = connection.getInputStream();
                    byte data[] = new byte[connection.getContentLength()];
                    Log.d("dbconntest", "helloLen " + data.length);
                    int total = 0;

                    while(total < data.length)
                    {
                        int read = is.read(data, total, data.length-total);
                        total += read;
                    }

                    is.close();
                    Log.d("dbconntest", "helloRead " + new String(data, 0, 1044) );
                    Log.d("dbconntest", "helloRead "+ total);

                    return BitmapFactory.decodeByteArray(data, 0, data.length);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.d("dbconntest", "hello3 "+e.getMessage());
                    return null;
                }
                finally
                {
                    if(connection != null)
                    {
                        connection.disconnect();
                    }
                }
            }
        }.execute(target);
    }

    protected void onCreate(Bundle savedInstanceState)
    {
        ActiveAndroid.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploadimage);

        //executeGet("http://largedata.azurewebsites.net");


        img = (ImageButton) findViewById(R.id.imageButton); //loads the button from xml
        img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent photoPickerInt = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //creates new intent to access the gallery application to load external images.
                startActivityForResult(photoPickerInt, SELECT_PICTURE); //allows user to select photo and assigns the image to the image button through the intent.
            }
        });

        //Download image to the app
        AsyncTask<String, Void, Bitmap> var =  downloadImage("http://largedata.azurewebsites.net/Home/DownloadImage/1");

        try
        {
            Bitmap bit = var.get();
            img.setBackgroundDrawable(new BitmapDrawable(getResources(), bit));
            Log.d("dbconntest", "helloExc " + bit);
//          MediaStore.Images.Media.insertImage(getContentResolver(), bit, "Testimage", "TestDesc");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("dbconntest", "helloExc " + e.getMessage());
        }

        submit = (Button) findViewById(R.id.submitButton);

        submit.setOnClickListener(new View.OnClickListener()
        {
            //once the user clicks submit, it grabs the data inputted by the user and saves it in the database.
            @Override
            public void onClick(View v)
            {
                ImageModel image = new ImageModel();
                image.img = BitmapFactory.decodeFile(path2);

                image.save();

                Log.d("dbtest", ImageModel.getAll()+"");

                finish(); //exits the current screen
            }
        });

        cancel = (Button) findViewById(R.id.cancelButton);

        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        }); //exits the current window
    }
    //this method gets the image path and sets the corresponding image on the image button
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == SELECT_PICTURE)
            {
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
    private String getRealPathFromURI(Uri contentURI)
    {
        Cursor cursor = null;
        try
        {
            String[] media = { MediaStore.Images.Media.DATA };
            cursor = getContentResolver().query(contentURI,
                    media, null, null, null);
            int c = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(c);
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        //return result;
    }
}
