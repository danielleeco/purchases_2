package vdk.purchases.purchases;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class Dialog_notmain extends Dialog {

    public Dialog_notmain(@NonNull Context context) {
        super(context);
    }

    MainActivity main_act = new MainActivity();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

       // main_act.dialog.setTitle("Заголовок");
      //  main_act.dialog.setContentView(R.layout.edit_dialog);
      //  TextView text = (TextView) main_act.dialog.findViewById(R.id.heading);
    //    text.setText("You can edit here");

    }

}

