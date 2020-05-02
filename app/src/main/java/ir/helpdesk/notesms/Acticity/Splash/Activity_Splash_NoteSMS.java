package ir.helpdesk.notesms.Acticity.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import ir.helpdesk.notesms.Acticity.Main.Activity_Main_NoteSMS;
import ir.helpdesk.notesms.R;

public class Activity_Splash_NoteSMS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_notesms);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Activity_Splash_NoteSMS.this, Activity_Main_NoteSMS.class));
            }
        }, 3000);
    }
}
