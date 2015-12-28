package com.pascalso.inquire;

/**
 * Created by pso on 12/28/15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class TutorGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private final LayoutInflater mInflater;

    public TutorGridViewAdapter(Context c) {
        mInflater = LayoutInflater.from(c);
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(500, 500));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            /**
             imageView.setPadding(0,0,0,0);
             imageView.setMaxHeight(100);
             imageView.setMaxWidth(100);
             */
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.ic_gridview_biology, R.drawable.ic_gridview_chemistry,
            R.drawable.ic_gridview_compsci, R.drawable.ic_gridview_economics,
            R.drawable.ic_gridview_math, R.drawable.ic_gridview_physics,

    };
}
