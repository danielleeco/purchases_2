package vdk.purchases.purchases;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Main2Activity extends AppCompatActivity {
    //переход на mainactivity
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ImageView menu = findViewById(R.id.im1);

        SharedPreferences sPref = getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE);
        Boolean saved_state = sPref.getBoolean(getString(R.string.switch_loc), false);
        System.out.println("saved state " + saved_state);
        if(saved_state) {
            menu.setImageResource(R.drawable.dark_theme);
        }
        if(!saved_state)
        {
            menu.setImageResource(R.drawable.activity2_21);
        }

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ImageButton btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(i);
            }
        });
        btn.getBackground().setAlpha(0);

        ImageButton share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main2Activity.this, EmailPasswordActivity.class);
                startActivity(i);
            }
        });
        share.getBackground().setAlpha(0);

        /*ImageButton profile = findViewById(R.id.profile);
        profile.getBackground().setAlpha(0);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main2Activity.this, Profile.class);
                startActivity(i);
            }
        });*/

        ImageButton setup = findViewById(R.id.setup);
        setup.getBackground().setAlpha(0);
        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main2Activity.this, Settings.class);
                startActivity(i);
            }
        });
    }

    protected void onRestart() {
        super.onRestart();
        Log.d("Log", "onRestart");
        System.out.println("restarted");

        ImageView menu = findViewById(R.id.im1);

        SharedPreferences sPref = getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE);
        Boolean saved_state = sPref.getBoolean(getString(R.string.switch_loc), false);
        System.out.println("saved state " + saved_state);
        if(saved_state) {
            menu.setImageResource(R.drawable.dark_theme);
        }
        if(!saved_state)
        {
            menu.setImageResource(R.drawable.activity2_21);
        }
    }
}