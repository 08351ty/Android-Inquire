package com.pascalso.inquire;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pso on 12/27/15.
 */
public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {
    private final Activity context;
    private final ArrayList<String> subjects;
    private final ArrayList<String> timecreated;
    private final ArrayList<String> answered;
    private final Integer[] imgid;
    private TextView txtTitle;
    private ImageView imageView;
    private TextView extratxt;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public StudentListAdapter(Activity context,
                              ArrayList<String> subjects,
                              ArrayList<String> timecreated,
                              Integer[] imgid,
                              ArrayList<String> answered) {
        super();
        this.context = context;
        this.subjects = subjects;
        this.timecreated = timecreated;
        this.answered = answered;
        this.imgid = imgid;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public StudentListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mylist, parent, false);
        // set the view's size, margins, paddings and layout parameters
        txtTitle = (TextView) v.findViewById(R.id.item);
        imageView = (ImageView) v.findViewById(R.id.icon);
        extratxt = (TextView) v.findViewById(R.id.textView1);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(subjects.get(position));
        txtTitle.setText(subjects.get(position));
        if(answered.get(position).equals("yes")) {
            imageView.setImageResource(imgid[1]);
            extratxt.setText("Replied: " + timecreated.get(position));
        }
        else{
            imageView.setImageResource(imgid[0]);
            extratxt.setText("Sent: " + timecreated.get(position));
        }

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return subjects.size();
    }

}
