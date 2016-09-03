package com.example.cunfe.nexusapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RemoteController;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Contracts.DataBaseTables;
import Tools.CameraHelper;
import Tools.DataBaseHelper;
import Tools.FileHelper;
import Tools.ImageViewHelper;
import Tools.SpinnerHelper;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        final DataBaseHelper dbhelper = new DataBaseHelper(this);
        final SQLiteDatabase db = dbhelper.getWritableDatabase();
//        new AlertDialog.Builder(this).setTitle("询问").setMessage("是否升级数据库？")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
                        //String createTableSql = DataBaseTables.Images.GetCreateTableSql();
                        //db.execSQL(DataBaseTables.Images.GetDropTableSql());
                       // db.execSQL(createTableSql);
//                    }
//                }).setNegativeButton("取消",null).show();
        InitSpinner();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
    }


    static final int REQUEST_IMAGE_CAPTURE = 1;
    public void dispatchTakePictureIntent(View view)
    {
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
        {
            return;
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    //接受activity返回的结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==REQUEST_IMAGE_CAPTURE&&resultCode==RESULT_OK)
        {
            Bundle extras = data.getExtras();
            //获取缩略图
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imageView = (ImageView)findViewById(R.id.cameraresult);
            imageView.setImageBitmap(imageBitmap);
        }
        if(requestCode==REQUEST_PHOTO_CAPTURE&&resultCode==RESULT_OK)
        {
            SavePicToDb();
            galleryAddPic();
            InitSpinner();
        }
    }

    private String ItemSelected;
    private ArrayAdapter<String> adapter;
    private void InitSpinner() {
        DataBaseHelper dbhelper = new DataBaseHelper(this);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String query = "SELECT IMAGEID,IMAGENAME,IMAGEURL FROM IMAGES";
        Cursor cursor = db.rawQuery(query, null);
        List<String> spinnerlist = Arrays.asList("IMAGEURL");
        Spinner photos = (Spinner)findViewById(R.id.photospinner);
        adapter=SpinnerHelper.InitSpinnerWithCursor(this, photos, cursor, spinnerlist);

        photos.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                ItemSelected = adapter.getItem(arg2);
                if(ItemSelected!=null)
                {
                    ShowImageByViewShape();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ItemSelected = null;
            }
        });
    }


    private void ShowImage()  {
        //String[] imageinfo = ItemSelected.split("@");
        Bitmap bitmap = ImageViewHelper.getLoacalBitmap(ItemSelected);
        if(bitmap!=null){
        ImageView imageview = (ImageView)findViewById(R.id.cameraresult);
        imageview.setImageBitmap(bitmap);
        }
    }

    private void ShowImageByViewShape()
    {
        ImageView imageview = (ImageView)findViewById(R.id.cameraresult);
        int width=imageview.getWidth();
        int height = imageview.getHeight();

        BitmapFactory.Options bmOption = new BitmapFactory.Options();
        bmOption.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(ItemSelected, bmOption);
        int photwidth = bmOption.outWidth;
        int photheight = bmOption.outHeight;

        int scaleFactor = Math.min(photwidth/width, photheight/height);

        bmOption.inJustDecodeBounds=false;
        bmOption.inSampleSize=scaleFactor;
        Bitmap bitmap = BitmapFactory.decodeFile(ItemSelected, bmOption);
        imageview.setImageBitmap(bitmap);

    }
    public void TakeOrDelPhoto(View view)
    {
        CheckBox add = (CheckBox)findViewById(R.id.checkBoxAdd);
        if(add.isChecked())
        {
            TakePhotoIntent();
        }else
        {
            DelPhoto();
        }
    }

    private void DelPhoto() {
        if(ItemSelected!=null)
        {
            DataBaseHelper helper = new DataBaseHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            String sql = "DELETE FROM IMAGES WHERE IMAGEURL='"+ItemSelected+"'";
            db.execSQL(sql);
            InitSpinner();
        }
    }

    static final int REQUEST_PHOTO_CAPTURE = 2;
    CameraHelper helper;
    public void TakePhotoIntent()
    {
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
        {
            return;
        }
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePhotoIntent.resolveActivity(getPackageManager())!=null)
        {
            File photoFile = null;
            try{
                helper = new CameraHelper();
                photoFile=helper.CreateImageFile();
                if(photoFile!=null){
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(takePhotoIntent,REQUEST_PHOTO_CAPTURE);
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(helper.mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void SavePicToDb() {
        final DataBaseHelper dbhelper = new DataBaseHelper(this);
        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        String sql = "INSERT INTO IMAGES VALUES(NULL,'"+helper.imageFileName+"','"+helper.mCurrentPhotoPath+"')";
        db.execSQL(sql);
    }

    public void TurnToVideo(View view)
    {
        Intent intent = new Intent(this,VideoActivity.class);
        startActivity(intent);
    }

}
