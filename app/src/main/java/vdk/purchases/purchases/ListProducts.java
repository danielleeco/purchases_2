package vdk.purchases.purchases;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.List;


public class ListProducts extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;


    FirebaseUser user = mAuth.getInstance().getCurrentUser();
    ArrayList<String> arrayList=new ArrayList<> ();
    ArrayAdapter<String> arrayAdapter;



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

        final EditText edit_text = (EditText) dialog.findViewById(R.id.edit_text);
        final Button dialog_done = (Button) dialog.findViewById(R.id.dialog_done);
        final Button dialog_del = (Button) dialog.findViewById(R.id.dialog_del);

///////////////////////////////////////////////////////////
        myRef.addValueEventListener (new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear ();
                List <String> keys=new ArrayList<> ();
                for (DataSnapshot keynode: dataSnapshot.child (user.getUid ()).getChildren ()){
                    keys.add(keynode.getKey ());
                    String prod= keynode.getValue (product.class).getName ();
                    arrayList.add(prod);
                    arrayAdapter.notifyDataSetChanged ();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Btn_new_task = (ImageButton)findViewById(R.id.addPurchase);
        ET_new_task = (EditText)findViewById(R.id.text);



        //отправляем в бд данные
        Btn_new_task.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                myRef.child(user.getUid()).push().child ("name").setValue(ET_new_task.getText().toString());
                ET_new_task.setText("");
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

}






