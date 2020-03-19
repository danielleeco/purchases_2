package vdk.purchases.purchases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int Database_VERSION=1;
    public static final String Database_name="list_puchases";
    public static final String Table = "table_purch";

    public static final String ID="_id";
    public static final String Name="name";

    public DBHelper(Context context) {
        super(context, Database_name,null, Database_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Table + "(" + ID + " integer primary key," + Name + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
