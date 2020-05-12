package vdk.purchases.purchases;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {

    private static final int REQUEST_CODE = 2;
    private static final String TAG = "hd";

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

        final TextView name = (TextView) findViewById(R.id.profile_name);

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

         final Button gallery_button = (Button) findViewById(R.id.profile_pic);
         final int GET_FROM_GALLERY = 3;
         gallery_button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 System.out.println("pressed gallery button");
                 startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
             }
         });
        final Button sign_out_button = (Button) findViewById(R.id.sign_out);
        sign_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Profile.this, EmailPasswordActivity.class);
                startActivity(i);
            }
        });
    }
}