package Tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import Contracts.DataBaseTables;

/**
 * Created by cunfe on 2015-11-15.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    //public  SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME,null);
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "MyNexusDb.db";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public SQLiteDatabase getWritableDatabase()
    {
         return super.getWritableDatabase();
    }
    public SQLiteDatabase getReadableDatabase()
    {
        return super.getReadableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String sql:DataBaseTables.CreateTables()) {
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DataBaseTables.DropTables());
        onCreate(db);
    }

}
