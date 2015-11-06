package com.example.duncan.bigdatamanagementapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    public List<ImageModel> list;

    public ImageAdapter(Context c, List<ImageModel> l) {
        mContext = c;
        list = l;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {  // if it's not recycled, initialize some attributes
            Log.d("imagead","createview");
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //imageView = new ImageView(mContext);

            //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
            view = inflater.inflate(R.layout.row, parent, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
            //convertView.setLayoutParams(new GridView.LayoutParams(params));
//            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //imageView.setPadding(8, 8, 8, 8);

            Bitmap b = Bitmaps.get(position);
            Log.d("setImage ", "" + b);
            imageView.setImageBitmap(b);
        } else {
            view =  convertView;
        }
        return view;
    }

}

