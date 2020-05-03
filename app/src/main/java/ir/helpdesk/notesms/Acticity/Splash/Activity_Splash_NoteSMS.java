package ir.helpdesk.notesms.Acticity.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import org.w3c.dom.Text;

import ir.helpdesk.notesms.Acticity.Main.Activity_Main_NoteSMS;
import ir.helpdesk.notesms.R;

public class Activity_Splash_NoteSMS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_notesms);

        TextView textView = findViewById(R.id.txtVersion);
        try {
            textView.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName + "");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Activity_Splash_NoteSMS.this, Activity_Main_NoteSMS.class));
            }
        }, 3000);
    }
}
