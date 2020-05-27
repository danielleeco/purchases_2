package vdk.purchases.purchases;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseListAdapter;

import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListProducts extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;



    FirebaseUser user = mAuth.getInstance().getCurrentUser();
    ArrayList<String> arrayList=new ArrayList<> ();
    ArrayAdapter<String> arrayAdapter;
    List <String> keys=new ArrayList<> ();


    private EditText ET_new_task;
    private ImageButton Btn_new_task;

    ListView ListUserProducts;//и листвью, в котором он будет отображаться

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);
        myRef = FirebaseDatabase.getInstance().getReference("Test");
        ListUserProducts = (ListView) findViewById(R.id.products);
        arrayAdapter=new ArrayAdapter<String> (this,android.R.layout.simple_list_item_1,arrayList);
        ListUserProducts.setAdapter (arrayAdapter);


/////////////////////////////////////////////////////
        final Dialog dialog = new Dialog(ListProducts.this);

        dialog.setContentView(R.layout.edit_dialog);
        TextView heading = (TextView) dialog.findViewById(R.id.heading);
        //heading.setText("You can edit here");

        SharedPreferences sPref = getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE);
        Boolean saved_state = sPref.getBoolean(getString(R.string.switch_loc), false);


        if (saved_state){
            dialog.setContentView(R.layout.edit_dialog_dark);
        }

        if (!saved_state){
            dialog.setContentView(R.layout.edit_dialog);
        }

        final EditText edit_text = (EditText) dialog.findViewById(R.id.edit_text);
        final Button dialog_done = (Button) dialog.findViewById(R.id.dialog_done);
        final Button dialog_del = (Button) dialog.findViewById(R.id.dialog_del);

        final TextView total_text = findViewById(R.id.total);


///////////////////////////////////////////////////////////
        myRef.addValueEventListener (new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear ();
                keys.clear();

                for (DataSnapshot keynode: dataSnapshot.child (user.getUid ()).getChildren ()){
                    keys.add(keynode.getKey ());

                    String prod= keynode.getValue (product.class).getName ();
                    arrayList.add(prod);
                    arrayAdapter.notifyDataSetChanged ();
                    total_text.setText("left to buy: " + arrayList.size() + " items");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Btn_new_task = (ImageButton)findViewById(R.id.addPurchase);
        ET_new_task = (EditText)findViewById(R.id.text);



        //отправляем в бд данные
        final EditText text = findViewById(R.id.text);
        Btn_new_task.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(arrayList.contains(ET_new_task.getText().toString()))//проверяем, нет ли уже элемента в listview
                { Toast.makeText(ListProducts.this, "This value already exists", Toast.LENGTH_SHORT).show();}
                else{
                if (text.getText().toString().length() > 0) {
                    myRef.child(user.getUid()).push().child("name").setValue(ET_new_task.getText().toString());
                    ET_new_task.setText("");
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }}
            }
        });

        // обработчик долгого нажатия и редактирования
        ListUserProducts.setOnItemLongClickListener (new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView parent, final View view, final int position, long id) {
                dialog.show();
                final String text_to_edit = (String) arrayAdapter.getItem(position);
                edit_text.setText(text_to_edit);
                dialog_done.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        arrayList.set(position, edit_text.getText().toString());
                        arrayAdapter.notifyDataSetChanged();

                        myRef.child(user.getUid()).child(keys.get(position)).child("name").setValue(edit_text.getText().toString());
                        //myRef.child(user.getUid()).child(keys.get(position)).setValue(edit_text.getText().toString());
                        //dbHelper.updateByName(text_to_edit, edit_text.getText().toString());
                        dialog.cancel();
                        total_text.setText("left to buy: " + arrayList.size() + " items");
                    }
                });
                dialog_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String deleted_string = arrayList.get(position);
                        arrayList.remove(position);
                        arrayAdapter.notifyDataSetChanged();

                        myRef.child(user.getUid()).child(keys.get(position)).removeValue();//удаляем в бд
                        keys.remove(position);


                        //dbHelper.delete(deleted_string, "table_purch");
                        dialog.cancel();
                        total_text.setText("left to buy: " + arrayList.size() + " items");
                    }
                });
                return true;
            }
        });
    }


}






