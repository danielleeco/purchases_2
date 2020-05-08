package vdk.purchases.purchases;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> purch = new ArrayList();
    ArrayList<String> purch_done = new ArrayList();

    DBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);
        purch = dbHelper.OnLoad(); //Заполняем список покупок из базы
        purch_done = dbHelper.OnLoad2();

        final Dialog dialog = new Dialog(MainActivity.this);

        dialog.setTitle("Editing");
        dialog.setContentView(R.layout.edit_dialog);
        TextView heading = (TextView) dialog.findViewById(R.id.heading);
        //heading.setText("You can edit here");
        final EditText edit_text = (EditText) dialog.findViewById(R.id.edit_text);
        final Button dialog_done = (Button) dialog.findViewById(R.id.dialog_done);
        final Button dialog_del = (Button) dialog.findViewById(R.id.dialog_del);

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
        //
        final ListView listView_done = findViewById(R.id.listview_done);

        final TextView total_text = findViewById(R.id.total);
        total_text.setText("left to buy: " + purch.size() + " items");

        // список будет хранить позиции выделенных элементов
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        //
        listView_done.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //адаптер для связи элементов списков с источниками данных
        final ArrayAdapter adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1, purch);
        listView.setAdapter(adapter);

        final ArrayAdapter adapter_done = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1, purch_done){
            //установка цвета для текста из второго listview
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                TextView textView=(TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.LTGRAY);
                return view;
            }
        };

        listView_done.setAdapter(adapter_done);

        // moving the item
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
                dbHelper.delete(cell, "table_purch");
                dbHelper.add(cell, "table_purch2");
                purch.remove(cell);
                purch_done.add(cell);
                adapter.notifyDataSetChanged();
                adapter_done.notifyDataSetChanged();
                System.out.println(purch_done);
                total_text.setText("left to buy: " + purch.size() + " items");
            }
        });

        listView_done.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // получаем нажатый элемент
                String cell = (String) adapter_done.getItem(position);
                if (listView_done.isItemChecked(position)) {
                    selectedCell.add(cell);
                } else {
                    selectedCell.remove(cell);
                }

                // doesnt work
                dbHelper.delete(cell, "table_purch2");
                dbHelper.add(cell, "table_purch");
                adapter_done.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
                purch_done.remove(cell);
                purch.add(cell);
                System.out.println(dbHelper);
                total_text.setText("left to buy: " + purch.size() + " items");
            }

        });

        // обработчик долгого нажатия и редактирования
        listView.setOnItemLongClickListener (new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView parent, View view, final int position, long id) {
                dialog.show();
                final String text_to_edit = (String) adapter.getItem(position);
                edit_text.setText(text_to_edit);
                dialog_done.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        purch.set(position, edit_text.getText().toString());
                        adapter.notifyDataSetChanged();
                        dbHelper.updateByName(text_to_edit, edit_text.getText().toString());
                        dialog.cancel();
                        total_text.setText("left to buy: " + purch.size() + " items");
                    }
                });
                dialog_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String deleted_string = purch.get(position);
                        purch.remove(position);
                        adapter.notifyDataSetChanged();
                        dbHelper.delete(deleted_string, "table_purch");
                        dialog.cancel();
                        total_text.setText("left to buy: " + purch.size() + " items");
                    }
                });
                return true;
            }
        });


        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
       // final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.scale);
        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);

        ImageButton add = findViewById(R.id.addPurchase);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText CellEditText = findViewById(R.id.text);
                String txt = CellEditText.getText().toString();
                //database.delete(DBHelper.Table, null, null);
                if (!txt.isEmpty() && !purch.contains(txt)) {
                    dbHelper.add(txt, "table_purch");//добавление в бд покупки
                    adapter.insert(txt, 0);
                    //или
                    //purch.add(0, txt);
                    //listView.setAdapter(adapter);
                    CellEditText.setText("");
                    adapter.notifyDataSetChanged();
                    v.startAnimation(animRotate);

                    // hide the keyboard when the item added
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    total_text.setText("left to buy: " + purch.size() + " items");
                }
            }
        });

        /* ImageButton remove = findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // получаем и удаляем выделенные элементы
                for (int i = 0; i < selectedCell.size(); i++) {
                    adapter.remove(selectedCell.get(i));
                    dbHelper.delete(selectedCell.get(i));//удаление из бд покупки
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
         */

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




