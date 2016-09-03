package Tools;

import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cunfe on 2015-12-03.
 */
public class SpinnerHelper {
    public static ArrayAdapter<String> InitSpinnerWithCursor(Context context,Spinner spinner, Cursor cursor,List<String> columnnamelist) {
        List<String> spinnerlist = new ArrayList<>();
        while (cursor.moveToNext())
        {
            String item = "";
            for (String columnname:columnnamelist) {
                item+=cursor.getString(cursor.getColumnIndex(columnname))+"@";
            }
            spinnerlist.add(item.substring(0,item.length()-1));
        }
        return InitSpinnerWithList(context,spinner,spinnerlist);
    }

    private static ArrayAdapter<String> InitSpinnerWithList(Context context,Spinner spinner, List<String> spinnerlist) {
        ArrayAdapter<String> adapter = new ArrayAdapter(context,android.R.layout.simple_spinner_item,spinnerlist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return adapter;
    }
}
