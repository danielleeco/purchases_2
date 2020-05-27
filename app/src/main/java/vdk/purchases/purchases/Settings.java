package vdk.purchases.purchases;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    public static String SAVED_STATE;
    RelativeLayout sett_layout;
    View rect_set;
    TextView your_custom;
    TextView dark_theme;

    // checking

    @Override
    public void onCreate(Bundle savedInstanceState) {

        System.out.println(SAVED_STATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // settings screen
        sett_layout = findViewById(R.id.sett_layout);
        rect_set = findViewById(R.id.rect_set);
        your_custom = findViewById(R.id.your_custom);
        dark_theme = findViewById(R.id.dark_theme);

        // profile screen
        //
        //
        //
        final RelativeLayout profile_view = findViewById(R.id.prof_view);
        final View blue_view = findViewById(R.id.blue_view);
        final Button profile_pic = findViewById(R.id.profile_pic);
        final View name = findViewById(R.id.name);
        final TextView profile_name = findViewById(R.id.profile_name);
        final Button sign_out = findViewById(R.id.sign_out);
        final TextView str_1 = findViewById(R.id.email);
        final TextView str_2 = findViewById(R.id.str_2);


        final Switch switch_theme = findViewById(R.id.switch_theme);
        // loading the switch state from settings
        SharedPreferences sPref = getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE);
        boolean saveState = sPref.getBoolean(getString(R.string.switch_loc), false);

        saving_state_func(saveState, switch_theme);
        switch_theme.setOnClickListener(new CompoundButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                saving_state_func(switch_theme.isChecked(), switch_theme);
            }

        });
    }

    // saving state of switch and changing background colors
    void saving_state_func(boolean check, Switch s){
        // saving the state
        boolean isChecked = check;
        s.setChecked(check);
        SharedPreferences sPref = getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(getString(R.string.switch_loc), isChecked);
        ed.commit();
        //System.out.println(sPref.getBoolean(getString(R.string.shared_pref),false));

        // switch on click listener
        int intChecked = isChecked ? 1 : -1;
        switch (intChecked) {
            case 1: // settings screen
                sett_layout.setBackgroundColor(Color.parseColor("#302F2F"));
                rect_set.setBackgroundColor(Color.parseColor("#34484E"));
                your_custom.setTextColor(Color.parseColor("#5F737A"));
                dark_theme.setTextColor(Color.parseColor("#878787"));
                break;

            case -1: // settings screen
                sett_layout.setBackgroundColor(Color.parseColor("#ffffff"));
                rect_set.setBackgroundColor(Color.parseColor("#8DA8B4"));
                your_custom.setTextColor(Color.parseColor("#ffffff"));
                dark_theme.setTextColor(Color.parseColor("#8DA8B4"));
//              profile_view.setBackgroundColor(Color.parseColor("#ffffff"));

                break;
        }
    }



}