package com.example.amreshranjan.sevencapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Amresh.ranjan on 14-12-2015.
 */
public class ContentProviderHandler extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
    }
    public void onClickAddName(View view) {
        // Add a new student record
        ContentValues values = new ContentValues();

        values.put(ContentProviderExample.DATE,
                ((EditText)findViewById(R.id.editText2)).getText().toString());

        values.put(ContentProviderExample.TEMP,
                ((EditText) findViewById(R.id.editText3)).getText().toString());

        Uri uri = getContentResolver().insert(
                ContentProviderExample.CONTENT_URI, values);

        Toast.makeText(getBaseContext(),
                uri.toString(), Toast.LENGTH_LONG).show();
    }

    public void onClickRetrieveStudents(View view) {

        // Retrieve student records
        String URL = "content://com.example.amreshranjan.sevencapplication.provider/weathers";

        Uri students = Uri.parse(URL);
        Cursor c = managedQuery(students, null, null, null, "name");

        if (c.moveToFirst()) {
            do{
                Toast.makeText(this,
                        c.getString(c.getColumnIndex(ContentProviderExample._ID)) +
                                ", " +  c.getString(c.getColumnIndex( ContentProviderExample.DATE)) +
                                ", " + c.getString(c.getColumnIndex( ContentProviderExample.TEMP)),
                        Toast.LENGTH_SHORT).show();
            } while (c.moveToNext());
        }
    }

    public void onClickDeleteStudents(View view) {

        int result = getContentResolver().delete(
                ContentProviderExample.CONTENT_URI, null, null);
    }
}
