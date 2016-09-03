package Tools;

import android.content.Context;
import android.os.Environment;
import android.util.EventLogTags;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by cunfe on 2015-11-06.
 */
public  class  FileHelper {

    public static boolean SaveFileToInternalStorage(Context context, String file, String filename)
    {
        File filepath = new File(context.getFilesDir(),filename);
        return SaveFileToDir(filepath,file);
    }

    private static boolean SaveFileToDir(File filepath, String file) {
        try {
            if (filepath.exists())
                {
                    filepath.delete();
                }
            filepath.createNewFile();
            filepath.setWritable(true);
            FileWriter out = new FileWriter(filepath,false);
            out.write(file);
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean SaveFileToExternalStorage(Context context,String file,String filename,boolean isPublic)
    {
        if(!isExternalStorageWritable())
        {
            return false;
        }
        try {
            if(isPublic){
                SaveFileToExternalStoragePublic(file, filename);
            }else {
                SaveFileToExternalStoragePrivate(context,file,filename);
            }
        }catch (Exception e)
        {
            return  false;
        }
        return true;
    }
    //可共享的文件
    private static void SaveFileToExternalStoragePublic( String file, String filename) {
        File filepath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),filename);
        filepath.mkdirs();
        SaveFileToDir(filepath,file);
    }
    //不可共享的文件
    private static void SaveFileToExternalStoragePrivate(Context context, String file, String filename) {
        File filepath = new File(context.getExternalFilesDir(null), filename);
        filepath.mkdirs();
        SaveFileToDir(filepath,file);
    }

    //检查External Stirage 是否可写
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            return true;
        }
        return  false;
    }
    //检查External Stirage 是否可写
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)||Environment.MEDIA_MOUNTED.equals(state)){
            return true;
        }
        return  false;
    }
}
