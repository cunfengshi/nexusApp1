package com.example.cunfe.nexusapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


import Tools.DataBaseHelper;
import Tools.ImageAdapter;

public class GridViewTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_test);
        GridView gridView = (GridView)findViewById(R.id.MainGridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowPic(position);
            }
        });
        Display display = getWindowManager().getDefaultDisplay();
        final Point point = new Point();
        display.getSize(point);
        Thread loadGridView = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                InitGridView(point.x, point.y);
            }
        });
        loadGridView.start();
    }

    private void ShowPic(int position) {
        Intent intent=new Intent(this,PicShowActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);
    }

    private ImageAdapter imageAdapter;
    private Handler UIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg)
        {
            GridView gridView = (GridView)findViewById(R.id.MainGridView);
            imageAdapter=(ImageAdapter)msg.obj;
            if(imageAdapter!=null)
            gridView.setAdapter(imageAdapter);
        }

    };

    private void InitGridView(int screenwidth,int screenheigth) {
        DataBaseHelper dbhelper = new DataBaseHelper(this);
        SQLiteDatabase database = dbhelper.getWritableDatabase();
        String sql ="SELECT IMAGEID,IMAGENAME,IMAGEURL FROM IMAGES ";
        Cursor cursor=database.rawQuery(sql,null);
        ImageAdapter imageAdapter=new ImageAdapter(this,cursor,screenwidth,screenheigth);
        Message msg = new Message();
        msg.obj=imageAdapter;
        UIHandler.sendMessage(msg);
    }
}
