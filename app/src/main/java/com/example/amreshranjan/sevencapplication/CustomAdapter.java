package com.example.amreshranjan.sevencapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by amresh.ranjan on 14-12-2015.
 */
public class CustomAdapter extends BaseAdapter {

    ArrayList<String> date, temp;
    Context context;
    ArrayList<Integer> imageId;
    private static LayoutInflater inflater=null;

    public CustomAdapter(Activity mainActivity, ArrayList<String> dateTime,ArrayList<String> temperature, ArrayList<Integer> climateImages) {
        date =dateTime;
        temp = temperature;
        context=mainActivity;
        imageId=climateImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return date.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView mTime,mTemp;
        ImageView mImage;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.programlist, null);
        holder.mTime=(TextView) rowView.findViewById(R.id.textView1);
        holder.mTemp=(TextView) rowView.findViewById(R.id.textView);
        holder.mImage=(ImageView) rowView.findViewById(R.id.imageView1);

        holder.mTime.setText(date.get(position));
        holder.mTemp.setText(temp.get(position));
        holder.mImage.setImageResource(imageId.get(position));

        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + date.get(position), Toast.LENGTH_LONG).show();
            }
        });

        return rowView;
    }


}
