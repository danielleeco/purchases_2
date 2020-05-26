package vdk.purchases.purchases;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.bumptech.glide.Glide;

public class Profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final int REQUEST_CODE = 2;
    private static final String TAG = "hd";
    private static final int PICK_IMAGE=100;
    private DatabaseReference mynameRef;//для хранения имен пользователей

    Uri imageUri;
    ImageView imageView;



    FirebaseStorage storage;//для хранения аватара
    StorageReference storageReference;




    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Dialog dialog = new Dialog(Profile.this);

        dialog.setContentView(R.layout.dialog_set_name);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final TextView str_1 = findViewById(R.id.email);
        str_1.setText(user.getEmail());

        final EditText set_name = (EditText) dialog.findViewById(R.id.set_name);
        final Button apply_name = (Button) dialog.findViewById(R.id.apply_name);
        final RelativeLayout profile_view = findViewById(R.id.prof_view);
        final View blue_view = findViewById(R.id.blue_view);
        final View name_1 = findViewById(R.id.name);

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
        mynameRef = FirebaseDatabase.getInstance().getReference("Names");
        //получаем имя
        mynameRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String now_name = dataSnapshot.child("name").getValue(String.class);
                name.setText(now_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
                         mAuth = FirebaseAuth.getInstance();
                         FirebaseUser user = mAuth.getCurrentUser();
                         name.setText(set_name.getText().toString());
                         dialog.cancel();
                         mynameRef.child(user.getUid()).child("name").setValue(name.getText().toString());
                     }
                 });
                 name.setGravity(Gravity.CENTER_HORIZONTAL);
             }
         });



         imageView  = findViewById(R.id.profile_pic);
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
                Intent i = new Intent(Profile.this, Main2Activity.class);
                startActivity(i);
            }
        });
        str_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Profile.this, ListProducts.class);
                startActivity(i);
            }
        });

        //получаем аватарку

            storageReference.child("avatars/" + user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext()).load(uri).into((ImageView) findViewById(R.id.profile_pic));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Avatar wasn't downloaded", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Process of downloading profile...", Toast.LENGTH_SHORT);
                    toast.show();
                }
            })
            ;


    }
    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }
//загружаем аватар в бд
    private void uploadImage(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (imageUri != null) {
            StorageReference ref = storageReference.child("avatars/"+ user.getUid());
            ref.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(Profile.this, "Avatar is downloaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Profile.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(Profile.this, Main2Activity.class);
        startActivity(i);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK && requestCode==PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            uploadImage();

        }
    }


}