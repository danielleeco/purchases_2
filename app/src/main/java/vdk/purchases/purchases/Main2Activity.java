package vdk.purchases.purchases;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class Main2Activity extends AppCompatActivity {
    //переход на mainactivity
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

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
        share.getBackground().setAlpha(0);

        ImageButton profile = findViewById(R.id.profile);
        profile.getBackground().setAlpha(0);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main2Activity.this, Profile.class);
                startActivity(i);
            }
        });

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
}