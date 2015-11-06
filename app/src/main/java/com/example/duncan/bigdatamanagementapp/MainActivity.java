package com.example.duncan.bigdatamanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.activeandroid.ActiveAndroid;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity
{
    static List<ImageModel> images = new ArrayList<>();
    static ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        ActiveAndroid.initialize(this);
        super.onCreate(savedInstanceState);
        //setContentView();
        setContentView(R.layout.activity_main);
        onCreateView();
    }

    public View onCreateView()
    {
        images.clear();
        images.addAll(ImageModel.getAll());
        GridView grid = (GridView) findViewById(R.id.gridview);

        adapter = new ImageAdapter(this, images);
        grid.setAdapter(adapter);

        return grid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.upload) {
            startActivity(new Intent(MainActivity.this,Upload.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
