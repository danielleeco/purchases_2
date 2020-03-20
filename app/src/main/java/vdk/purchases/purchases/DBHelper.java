package vdk.purchases.purchases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class DBHelper extends SQLiteOpenHelper {

    public static final int Database_VERSION=1; //версия дб
    public static final String Database_name="list_puchases"; //имя дб
    public static final String Table = "table_purch"; //название таблицы с именами

    public static final String ID="_id"; //столбец id таблицы table_purch
    public static final String Name="name";//столбец name таблицы table_purch

    public DBHelper(Context context) {
        super(context, Database_name,null, Database_VERSION); //конструктор
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Table + "(" + ID + " integer primary key," + Name + " text" + ")"); //при создании бд создается талица
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void add(String txt){
        ContentValues contentValues= new ContentValues();
        contentValues.put(Name, txt);
        SQLiteDatabase database=getWritableDatabase();
        //добавление элемента
        database.insert(Table, null, contentValues);
        database.close();
    }
    public  void delete(String txt){
        //удаление элемента с таким-то именем
//        SQLiteDatabase database=getWritableDatabase();
////       // txt="'"+txt+"'";//без этого не работает
////        database.delete(Table, Name+ "='" + txt+"'", null);
////        database.close();
        SQLiteDatabase database=getWritableDatabase();
        // txt="'"+txt+"'";//без этого не работает
        database.delete(Table, Name+ "=:txt", new String[]{txt});
        database.close();
    }

    public ArrayList<String> OnLoad(){
        ArrayList<String> purch = new ArrayList();
        final SQLiteDatabase database=getWritableDatabase();
        Cursor cursor= database.query(DBHelper.Table, null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            int nameIndex = cursor.getColumnIndex(DBHelper.Name);
            int idIndex = cursor.getColumnIndex(DBHelper.ID);
            do{
                purch.add(cursor.getString(nameIndex)); //проходит построчно и добавляет новые покупки
                System.out.println(cursor.getString(idIndex)+ " " + cursor.getString(nameIndex));
            } while(cursor.moveToNext());
        }
        else {
            Log.d("mLog","0"); //если ничего нет в консоль пишет 0
        }
        Collections.reverse(purch);
        database.close();
        return purch;
    }
}

