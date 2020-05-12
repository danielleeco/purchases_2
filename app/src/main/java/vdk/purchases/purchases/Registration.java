package vdk.purchases.purchases;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.os.Bundle;

import java.util.Objects;

public class Registration extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuthr;
    private EditText ETemailr;
    private EditText ETpasswordr;
    private EditText ETpassword2r;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        mAuthr = FirebaseAuth.getInstance();


        ETemailr = (EditText) findViewById(R.id.email2);
        ETpasswordr = (EditText) findViewById(R.id.password2);
        ETpassword2r = (EditText) findViewById(R.id.confpassword);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                } else {
                    // User is signed out

                }

            }
        };
        findViewById(R.id.btn_sign_up).setOnClickListener(this);
        findViewById(R.id.sign_in2).setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_sign_up) {

            checking(ETpasswordr.getText().toString(), ETpassword2r.getText().toString());

        }

        if(view.getId() == R.id.sign_in2) {
            Intent i = new Intent(Registration.this, EmailPasswordActivity.class);
            startActivity(i);
        }
    }
    public void checking (String a , String b){
        if(a.equals(b)){
            registration(ETemailr.getText().toString(), ETpasswordr.getText().toString());
        }
        else {
            Toast.makeText(Registration.this, "НОРМАЛЬНО ВВОДИ", Toast.LENGTH_SHORT).show();
        }
    }


    public void registration (String email , String password){

            mAuthr.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Registration.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(Registration.this, "Регистрация провалена", Toast.LENGTH_SHORT).show();
                }
            });


    }


}
