package vdk.purchases.purchases;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {


    private static final int REQUEST_CODE = 2;
    private static final String TAG = "hd";
    private static final int PICK_IMAGE=100;
    Uri imageUri;
    ImageView imageView;


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Dialog dialog = new Dialog(Profile.this);

        dialog.setContentView(R.layout.dialog_set_name);

        final EditText set_name = (EditText) dialog.findViewById(R.id.set_name);
        final Button apply_name = (Button) dialog.findViewById(R.id.apply_name);
        final RelativeLayout profile_view = findViewById(R.id.prof_view);
        final View blue_view = findViewById(R.id.blue_view);
        final View name_1 = findViewById(R.id.name);
        final TextView str_1 = findViewById(R.id.str_1);
        final TextView str_2 = findViewById(R.id.str_2);
        final ImageView gallery_button =  findViewById(R.id.profile_pic);
        final TextView name = (TextView) findViewById(R.id.profile_name);
        final Button sign_out_button = (Button) findViewById(R.id.sign_out);

        SharedPreferences sPref = getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE);
        Boolean saved_state = sPref.getBoolean(getString(R.string.switch_loc), false);
        System.out.println(saved_state);
        if(saved_state) {
            // profile screen
            profile_view.setBackgroundColor(Color.parseColor("#302F2F"));
            blue_view.setBackgroundColor(Color.parseColor("#34484E"));
            gallery_button.setBackgroundResource(R.drawable.profile_pic_2);
            name_1.setBackgroundResource(R.drawable.profile_rect_2_2);
            name.setTextColor(Color.parseColor("#5F737A"));
            sign_out_button.setBackgroundResource(R.drawable.exit_btn_2);
            str_1.setTextColor(Color.parseColor("#5F737A"));
            str_2.setTextColor(Color.parseColor("#5F737A"));
        }

        name.setText("");
         name.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 System.out.println("you pressed");
                 System.out.println(name.getText());
                 dialog.show();
                 final String name_to_edit = name.getText().toString();
                 set_name.setText(name_to_edit);
                 apply_name.setOnClickListener(new View.OnClickListener() {
                     public void onClick(View v) {
                         name.setText(set_name.getText().toString());
                         dialog.cancel();
                     }
                 });
                 name.setGravity(Gravity.CENTER_HORIZONTAL);
             }
         });

         imageView = (ImageView) findViewById(R.id.profile_pic);
         imageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 openGallery();
             }
         });


        sign_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(Profile.this, EmailPasswordActivity.class);
                startActivity(i);
            }
        });
    }
    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK && requestCode==PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);

        }
    }
}