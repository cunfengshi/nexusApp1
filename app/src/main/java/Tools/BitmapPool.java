package Tools;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by cunfe on 2016-01-17.
 */
public class BitmapPool {
    public static LruCache<String, Bitmap> mMemoryCache;

    public static void InitCache()
    {
        //可用内存的8分之一用来缓存
        int cacheSize  = (int) (Runtime.getRuntime().maxMemory() / 1024)/8;
        mMemoryCache=new LruCache<String,Bitmap>(cacheSize)
        {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }
    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if(mMemoryCache==null)InitCache();
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }
    public static Bitmap getBitmapFromMemCache(String key) {
        if(mMemoryCache==null)InitCache();
        return mMemoryCache.get(key);
    }
}
