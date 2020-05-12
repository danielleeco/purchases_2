package vdk.purchases.purchases;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final RelativeLayout sett_layout = findViewById(R.id.sett_layout);
        final View rect_set = findViewById(R.id.rect_set);
        final TextView your_custom = findViewById(R.id.your_custom);
        final TextView dark_theme = findViewById(R.id.dark_theme);

        Switch switch_theme = findViewById(R.id.switch_theme);
        switch_theme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if (isChecked) {
                    sett_layout.setBackgroundColor(Color.parseColor("#302F2F"));
                    rect_set.setBackgroundColor(Color.parseColor("#34484E"));
                    your_custom.setTextColor(Color.parseColor("#5F737A"));
                    dark_theme.setTextColor(Color.parseColor("#878787"));

                }
                if (!isChecked) {
                    sett_layout.setBackgroundColor(Color.parseColor("#ffffff"));
                    rect_set.setBackgroundColor(Color.parseColor("#91D0E4"));
                    your_custom.setTextColor(Color.parseColor("#ffffff"));
                    dark_theme.setTextColor(Color.parseColor("#91D0E4"));
                }

            }
        });
    }
}