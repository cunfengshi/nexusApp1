package com.example.cunfe.nexusapp;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import Contracts.DataBaseTables;

import com.example.cunfe.nexusapp.Receivers.RemoteControlReceiver;
import com.example.cunfe.nexusapp.Services.SoundService;
import Tools.DataBaseHelper;
import Tools.FileHelper;
import Tools.SoundHelper;

public class MainActivity extends AppCompatActivity
            implements NavigationView.OnNavigationItemSelectedListener {

    private boolean isPlay=false;
    RemoteControlReceiver receiver=new RemoteControlReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
  //      Intent intent = getIntent();
       // String message = intent.getStringExtra(FullscreenActivity.Extra_Message);
        //final String[] logonInfo = message.split("\\|");

       // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                       // .setAction("Action", null).show();
              //  Intent intent = new Intent(MainActivity.this,FullscreenActivity.class);
             //   startActivity(intent);
            //}
        //});

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
         //       this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        //toggle.syncState();


//        final DataBaseHelper Helper = new DataBaseHelper(this, DataBaseTables.DATABASE_NAME,null,DataBaseTables.DATABASE_VERSION);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Helper.getWritableDatabase();
//            }
//        }).start();

        //设置音频流类型 调节音量用 可不使用
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
//        IntentFilter filter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
//        filter.setPriority(100);
//        registerReceiver(receiver,filter);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        //unregisterReceiver(receiver);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(receiver);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
//        IntentFilter filter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
//        filter.setPriority(100);
//        registerReceiver(receiver, filter);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            TakePhoto();
        } else if (id == R.id.nav_gallery) {
            TurnOnMyCamera();
        } else if (id == R.id.nav_music) {
            PlayMusic();
        } else if (id == R.id.nav_manage) {
            ClickNavManage();
        } else if (id == R.id.nav_share) {
            Share();
        } else if (id == R.id.nav_send) {
            //SaveFile();
            final boolean[] isWrite = {false,false};// 0 保存是否写入成功 1 是否执行完成
            final boolean[] isRead ={false};
            final String filename = "myNexusAppTestFile.txt";
            final Context context=this;
            Thread filewrite = new Thread(new Runnable() {
                @Override
                public void run() {
                    isWrite[0] =FileHelper.SaveFileToExternalStorage(context,"this is my test file!",filename,false);
                    isWrite[1] = true;
                }
            });
            filewrite.start();
            final String[] content = {""};
            Thread fileread = new Thread(new Runnable() {
                @Override
                public void run() {
                        if (isWrite[0]&&isWrite[1]){
                            File filepath = new File(context.getExternalFilesDir(null),filename);
                            content[0] =ReadFile(filepath);
                            isRead[0]=true;
                        }
                    }
                }
            );
            while (!isWrite[1])
            {
            }
            fileread.start();
            AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.email);
            while (!isRead[0]){
            }
            textView.append(content[0]);
            textView.setError(content[0]);

        } else if  (id == R.id.nav_pass){
            Intent intent = new Intent(MainActivity.this,GridViewTestActivity.class);
            intent.putExtra("msg",id);
            startActivity(intent);
        } else if (id==R.id.nav_sendEmail) {
            CreateAndSendEmail("cunfengshi@164.com","Email subject","this is my first email","content://path/to/email/attachment");
        }else if(id==R.id.nav_showPic) {
            Intent intent = new Intent(this,PicShowActivity.class);
            startActivity(intent);
        }else if(id==R.id.nav_OpenGL){
            Intent intent = new Intent(this,OpenGLTestActivity.class);
            startActivity(intent);
        }else if(id==R.id.nav_contacts)
        {
            Intent intent = new Intent(this,ContactListActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void TurnOnMyCamera() {
        Intent myCamera = new Intent(this,MyCameraActivity.class);
        startActivity(myCamera);
    }

    private void TakePhoto() {
        Intent takePhoto = new Intent(MainActivity.this,CameraActivity.class);
        startActivity(takePhoto);
    }


    Intent playmusic;
    private void PlayMusic() {
        if(playmusic==null) playmusic = new Intent(MainActivity.this, SoundService.class);
        SoundHelper soundHelper = new SoundHelper(this,playmusic);
        if(!isPlay)
        {
            soundHelper.Play(R.raw.shorttriphome);
        }
        else
        {
            soundHelper.Pause();
        }
        isPlay=!isPlay;
    }


    private void Share() {
        ///拨打电话
//        Uri number = Uri.parse("tel:15842647903");
//        Intent callintent = new Intent(Intent.ACTION_DIAL,number);
        //调用地图
        Uri location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        if(CheckIntentActivitise(mapIntent)) {
            startActivity(mapIntent);
        }
    }


    private void ClickNavManage() {
        DataBaseHelper helper = new DataBaseHelper(this);
        final SQLiteDatabase db = helper.getWritableDatabase();
        new AlertDialog.Builder(MainActivity.this).setTitle("询问")
                .setMessage("是否清空MAINMENU表中数据？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try
                        {
                            db.execSQL(DataBaseTables.MainMenu.GetCreateTableSql());
                        }
                        catch (Exception e)
                        {
                            db.execSQL(DataBaseTables.MainMenu.GetDropTableSql());
                            db.execSQL(DataBaseTables.MainMenu.GetCreateTableSql());
                        }
                    }
                } )
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this,DbTestActivity.class);
                        startActivity(intent);
                    }
                }).show();
    }

    private String ReadFile(File file) {
        //File file = new File(getFilesDir(),filename);
        String content = "";
        try {
            String encoding="GBK";
            if(file.isFile() && file.exists()){
                InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String buffer;
                while((buffer=bufferedReader.readLine()) != null){
                    content +=buffer;
                }
                read.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            content+=e.getMessage();
        }
        return content;
    }

    private void SaveFile()  {

        String filename = "myNexusAppTestFile.txt";
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(this.getClass().toString().getBytes());
            outputStream.close();


        } catch (Exception e) {
        e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 处理动作按钮的点击事件
        switch (item.getItemId()) {
            case R.id.action_search:
                openSearch();
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    ///检查是否有可用应用
    private boolean CheckIntentActivitise(Intent intent) {
        return getPackageManager().queryIntentActivities(intent,0).size()>0;
    }
    //创建Email
    private void CreateAndSendEmail(String recipients,String subject,String content,String stream) {
        Intent emailintent = new Intent(Intent.ACTION_SEND);
        emailintent.setType("text/plain");
        emailintent.putExtra(Intent.EXTRA_EMAIL, recipients);
        emailintent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailintent.putExtra(Intent.EXTRA_TEXT, content);
        emailintent.putExtra(Intent.EXTRA_STREAM, Uri.parse(stream));

        if(CheckIntentActivitise(emailintent)){
            startActivity(emailintent);
        }
    }
    private void openSettings() {
    }

    private void openSearch() {
        
    }

}
