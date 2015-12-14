package com.example.amreshranjan.sevencapplication;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity  extends AppCompatActivity {
    GridView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        listView = (GridView) findViewById(R.id.list);
        String URL = "content://com.example.amreshranjan.sevencapplication.provider/weathers";
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<Integer> listImage = new ArrayList<Integer>();
        ArrayList<String> listgrade = new ArrayList<String>();
        Uri students = Uri.parse(URL);
        Cursor c = managedQuery(students, null, null, null, "date");
        if (c.moveToFirst()) {
            do {
                String temp = c.getString(c.getColumnIndex(ContentProviderExample.TEMP));
                list.add(c.getString(c.getColumnIndex(ContentProviderExample.DATE)));
                listgrade.add(c.getString(c.getColumnIndex(ContentProviderExample.TEMP)));
                System.out.println("Current temp ::"+ temp);
                if(temp.equalsIgnoreCase("sunny")) {
                    listImage.add(R.drawable.sunny);
                }else{
                    listImage.add(R.drawable.sunny_intervals);
                }
            } while (c.moveToNext());
        }
        listView.setAdapter(new CustomAdapter(MainActivity.this, list,listgrade,listImage));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_assignment, menu);
        menu.findItem(R.id.action_month);
        return true;
    }}
