package Tools;

import android.app.ActionBar;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.cunfe.nexusapp.ShowPicFragment;

/**
 * Created by cunfe on 2016-01-11.
 */
public class ImageAdapter extends BaseAdapter{
    private Context context;
    private Cursor cursor;
    private int screenwidth;
    private int screenheigth;

    public ImageAdapter(Context mcontext, Cursor mcursor, int width, int heigth)
    {
        context=mcontext;
        cursor=mcursor;
        screenheigth=heigth;
        screenwidth=width;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return GetImageViewByCursorPosition(position,screenwidth,screenwidth);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private int columnNum=3;
    private int lineNum=3;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView ;
        if(convertView==null)
        {
            imageView=new ImageView(context);
            Size size=new Size(screenwidth/columnNum,screenheigth/lineNum);
            int width= (int) (size.getWidth()*0.9);
            int height= (int) (size.getHeight()*0.9);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width,height);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding((size.getWidth()-width*columnNum)/4,(size.getHeight()-height*lineNum)/4, (size.getWidth()-width*columnNum)/4, (size.getHeight()-height*lineNum)/4);
        }else
        {
            imageView=(ImageView)convertView;
        }

        Bitmap bitmap=GetImageViewByCursorPosition(position,imageView.getLayoutParams().width,imageView.getLayoutParams().height);
        if(bitmap!=null)
        {
            imageView.setImageBitmap(bitmap);
        }
        return imageView;
    }

    private Bitmap GetImageViewByCursorPosition(int position, int width, int height) {
        if(cursor.moveToPosition(position))
        {
            String url= cursor.getString(cursor.getColumnIndex("IMAGEURL"));
            Bitmap bitmap = ShowPicFragment.decodeSampledBitmapFromResource(url, width, height);
            return bitmap;
        }else {return null;}
    }
}
