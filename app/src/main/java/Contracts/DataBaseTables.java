package Contracts;

import android.provider.BaseColumns;

/**
 * Created by cunfe on 2015-11-16.
 */
public final class DataBaseTables {

    public static final String[] CreateTables()
    {
        String[] create =new String[]{MainMenu.GetCreateTableSql(),Images.GetCreateTableSql()};
        return create;
    }
    public static final String DropTables()
    {
        String drop ="";
        drop+=MainMenu.GetDropTableSql()+Images.GetDropTableSql();
        return drop;
    }
    public static abstract class MainMenu implements BaseColumns{
        public static final String TABLE_NAME="MAINMENU";
        public static final String COLUMN_NAME_MENU_ID="MENUID";
        public static final String COLUMN_NAME_MENU_NAME="MENUNAME";
        public static final String COLUMN_NAME_MENU_URL="MENUURL";

        public static String GetCreateTableSql(){
            return "CREATE TABLE "+TABLE_NAME+" ("
                    +COLUMN_NAME_MENU_ID+"  INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +COLUMN_NAME_MENU_NAME+" VARCHAR(50),"
                    +COLUMN_NAME_MENU_URL+" VARCHAR(200)"
                    +");";
            }

        public static String GetDropTableSql(){
            return "DROP TABLE IF EXISTS "+TABLE_NAME+";";
            }

    }

    public  static  abstract class Images implements BaseColumns
    {
        public static final String TABLE_NAME="IMAGES";
        public static final String COLUMN_NAME_IMAGEID="IMAGEID";
        public static final String COLUMN_NAME_IMAGE_NAME="IMAGENAME";
        public static final String COLUMN_NAME_IMAGE_URL="IMAGEURL";

        public static String GetCreateTableSql(){
            return "CREATE TABLE "+TABLE_NAME+" ("
                    +COLUMN_NAME_IMAGEID+"  INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +COLUMN_NAME_IMAGE_NAME+" VARCHAR(200),"
                    +COLUMN_NAME_IMAGE_URL+" VARCHAR(1000)"
                    +");";
        }

        public static String GetDropTableSql(){
            return "DROP TABLE IF EXISTS "+TABLE_NAME+";";
        }

    }
}
