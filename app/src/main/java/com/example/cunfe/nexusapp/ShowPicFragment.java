package com.example.cunfe.nexusapp;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;

import Tools.BitmapPool;
import Tools.ImageViewHelper;

public  class ShowPicFragment extends Fragment {

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pic_show,container,false);
        ImageView imageView=(ImageView)view.findViewById(R.id.ShowPicFra);

        if(imageView!=null){
              String imageurl = getArguments().getString(SECTION_URL);
//            ShowImageByViewShape(imageurl,imageView);

            BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            task.execute(imageurl);
        }
        return view;
    }
    private static final String SECTION_URL="PIC_SECTION_URL";

    public ShowPicFragment()
    {

    }
    public static Fragment newInstance(String url) {
        ShowPicFragment showPicFragment = new ShowPicFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SECTION_URL, url);
        showPicFragment.setArguments(bundle);
        return showPicFragment;
    }

    private void ShowImageByViewShape(String url,ImageView imageView)
    {
        int width=imageView.getWidth();
        int height = imageView.getHeight();

        BitmapFactory.Options bmOption = new BitmapFactory.Options();
        bmOption.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(url, bmOption);
        int photwidth = bmOption.outWidth;
        int photheight = bmOption.outHeight;
        if(width<=0)width=photwidth;
        if(height<=0)height=photheight;
        int scaleFactor = Math.min(photwidth/width, photheight/height);

        bmOption.inJustDecodeBounds=false;
        bmOption.inSampleSize=scaleFactor;
        Bitmap bitmap = BitmapFactory.decodeFile(url, bmOption);
        imageView.setImageBitmap(bitmap);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(100,100));
    }

    public static Bitmap decodeSampledBitmapFromResource( String imageurl,
                                                         int reqWidth, int reqHeight) {

        Bitmap bitmap=BitmapPool.getBitmapFromMemCache(imageurl);
        if(bitmap!=null){
            return bitmap;
        }
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageurl, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bitmap=BitmapFactory.decodeFile(imageurl, options);
        BitmapPool.addBitmapToMemoryCache(imageurl,bitmap);
        return bitmap;
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private Handler UIHandler = new Handler(){
        WeakReference imageViewReference = null;//弱引用 允许被系统回收
        @Override
        public void handleMessage(Message msg)
        {
            Bitmap bitmap =(Bitmap) msg.obj;
            if(bitmap!=null)
            {

                if (imageViewReference == null)
                {
                    ImageView imageView=(ImageView)view.findViewById(R.id.ShowPicFra);
                    imageViewReference = new WeakReference(imageView);
                }
                    final ImageView View = (ImageView) imageViewReference.get();
                    if (View != null) {
                        View.setImageBitmap(bitmap);
                 }
            }
        }
    };
    private LruCache<String, Bitmap> mMemoryCache;

        public class BitmapWorkerTask extends AsyncTask {


        private String data = "";

        public BitmapWorkerTask(ImageView imageView) {
        }

        @Override
        protected Bitmap doInBackground(Object[] params) {
            data = (String) params[0];
            Bitmap bitmap =decodeSampledBitmapFromResource(data, 100, 100);
            Message message = new Message();
            message.obj=bitmap;
            UIHandler.sendMessage(message);
            // addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
            return bitmap;
        }
    }

        public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
            if (getBitmapFromMemCache(key) == null) {
                mMemoryCache.put(key, bitmap);
            }
        }
        public Bitmap getBitmapFromMemCache(String key) {
            return mMemoryCache.get(key);
        }
    }

