package vdk.purchases.purchases;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.Collections;

import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> purch = new ArrayList();

    DBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);
        final SQLiteDatabase database=dbHelper.getWritableDatabase();


        Cursor cursor= database.query(DBHelper.Table, null,null,null,null,null,null);

        if (cursor.moveToFirst()){
            int nameIndex = cursor.getColumnIndex(DBHelper.Name);
            do{
                purch.add(cursor.getString(nameIndex));
            } while(cursor.moveToNext());
            Collections.reverse(purch);
        }
        else {
            Log.d("mLog","0");
        }


        findViewById(R.id.relativelayout).setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        final ArrayList<String> selectedCell = new ArrayList();

        //___убираем полоску состояния___
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //получаем эелемент ListView
        final ListView listView = findViewById(R.id.listview);

        // список будет хранить позиции выделенных элементов
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //адаптер для связи элементов списков с источниками данных
        final ArrayAdapter adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_multiple_choice, purch);
        listView.setAdapter(adapter);

        final TextView total_text = findViewById(R.id.total);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // получаем нажатый элемент
                String cell = (String) adapter.getItem(position);
                if (listView.isItemChecked(position)) {
                    selectedCell.add(cell);
                } else {
                    selectedCell.remove(cell);
                }
                total_text.setText("You chose: " + selectedCell.size() + " items");
            }

        });


        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
       // final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.scale);
        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);

        ImageButton add = findViewById(R.id.addPurchase);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues= new ContentValues();
                EditText CellEditText = findViewById(R.id.text);
                String txt = CellEditText.getText().toString();

                //database.delete(DBHelper.Table, null, null);
                if (!txt.isEmpty() && !purch.contains(txt)) {
                    contentValues.put(DBHelper.Name, txt);
                    database.insert(DBHelper.Table, null, contentValues);
                    adapter.insert(txt,0);
                    //или
                    //purch.add(0, txt);
                    //listView.setAdapter(adapter);

                    CellEditText.setText("");
                    adapter.notifyDataSetChanged();
                    v.startAnimation(animRotate);
                }
            }
        });

        ImageButton remove = findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // получаем и удаляем выделенные элементы
                for (int i = 0; i < selectedCell.size(); i++) {
                    adapter.remove(selectedCell.get(i));
                }
                // снимаем все ранее установленные отметки
                listView.clearChoices();
                // очищаем массив выбраных объектов
                selectedCell.clear();
                adapter.notifyDataSetChanged();
                total_text.setText("You chose: " + selectedCell.size() + " items");
                v.startAnimation(animAlpha);
                }
        });


    }
}

        /**
        @Override
        public boolean onItemLongClick (AdapterView < ? > parent, ListView view,
        int position, long id){
        String selectedItem = parent.getItemAtPosition(position).toString();

        ListView listView = findViewById(R.id.listview);

        // список будет хранить позиции выделенных элементов
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //адаптер для связи элементов списков с источниками данных
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(
                this, R.array.purchases,
                android.R.layout.simple_list_item_single_choice);
        listView.setAdapter(arrayAdapter);

        arrayAdapter.remove(selectedItem);
        arrayAdapter.notifyDataSetChanged();

        Toast.makeText(getApplicationContext(),
                selectedItem + " удалён.",
                Toast.LENGTH_SHORT).show();
        return true; */




