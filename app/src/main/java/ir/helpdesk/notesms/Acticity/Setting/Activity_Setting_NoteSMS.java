package ir.helpdesk.notesms.Acticity.Setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import ir.helpdesk.notesms.Acticity.Main.Activity_Main_NoteSMS;
import ir.helpdesk.notesms.Acticity.Setting.Adapter.AdRecyclFilterPhoneCheckBox;
import ir.helpdesk.notesms.Acticity.Setting.Adapter.onClickInterface;
import ir.helpdesk.notesms.Acticity.Main.ModFilterPhone;
import ir.helpdesk.notesms.DataBase.DataSource.tb_BillsDataSource;
import ir.helpdesk.notesms.DataBase.Tables.tb_Bills;
import ir.helpdesk.notesms.R;

public class Activity_Setting_NoteSMS extends AppCompatActivity {
    Context context = this;
    //----------------- filter
    private AlertDialog alertDialogFilterPhone;
    private AdRecyclFilterPhoneCheckBox adRecycPopUp;
    private androidx.appcompat.widget.SearchView editsearchSearchView;
    private List<ModFilterPhone> arraylistSearchView = new ArrayList<ModFilterPhone>();
    private ArrayList<tb_Bills> tb_billsList;
    private SharedPreferences preferences;

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(Activity_Setting_NoteSMS.this, Activity_Main_NoteSMS.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_notesms);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences = getSharedPreferences("nOtEsMs", 0);
        tb_billsList = new ArrayList<>(new tb_BillsDataSource(context).GetList());
        loading();
        Button btnChooseFilter = findViewById(R.id.btnChooseFilter);
        btnChooseFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogFilterPhone();
            }
        });

        Button btnQuickReplay = findViewById(R.id.btnQuickReplay);
        btnQuickReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "این قابلیت درحال حاضر غیرفعال می باشد", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogLoading.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        startActivity(new Intent(Activity_Setting_NoteSMS.this, Activity_Main_NoteSMS.class));
                    }
                }, 2000);
            }
        });

        checkBoxWeeks();


    }

    private void checkBoxWeeks() {

        final CheckBox checkbox_today = findViewById(R.id.checkbox_today);
        final CheckBox checkbox_lastWeek = findViewById(R.id.checkbox_lastWeek);
        final CheckBox checkbox_thisWeek = findViewById(R.id.checkbox_thisWeek);
        final CheckBox checkbox_thisMonth = findViewById(R.id.checkbox_thisMonth);
        final CheckBox checkbox_lastMonth = findViewById(R.id.checkbox_lastMonth);

        checkbox_today.setChecked(false);
        checkbox_lastWeek.setChecked(false);
        checkbox_thisWeek.setChecked(false);
        checkbox_thisMonth.setChecked(false);
        checkbox_lastMonth.setChecked(false);

        final String timeRange = preferences.getString("timeRange", "");
        String[] titles = timeRange.split(",");
        if (!titles[0].equals(""))
            for (int i = 0; i < titles.length; i++) {
                String tag = titles[i];
                if (tag.equals("امروز")) checkbox_today.setChecked(true);
                else if (tag.equals("7 روز گذشته")) checkbox_lastWeek.setChecked(true);
                else if (tag.equals("این هفته")) checkbox_thisWeek.setChecked(true);
                else if (tag.equals("این ماه")) checkbox_thisMonth.setChecked(true);
                else if (tag.equals("30 روز گذشته")) checkbox_lastMonth.setChecked(true);

            }

        checkbox_today.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getDataFromCheckBox(isChecked, checkbox_today);
            }
        });
        checkbox_lastWeek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getDataFromCheckBox(isChecked, checkbox_lastWeek);
            }
        });
        checkbox_thisWeek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getDataFromCheckBox(isChecked, checkbox_thisWeek);
            }
        });
        checkbox_thisMonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getDataFromCheckBox(isChecked, checkbox_thisMonth);
            }
        });
        checkbox_lastMonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getDataFromCheckBox(isChecked, checkbox_lastMonth);
            }
        });


    }

    private void getDataFromCheckBox(boolean isChecked, CheckBox checkbox) {

        final String titlePH = checkbox.getText().toString();
        final String titles = preferences.getString("timeRange", "");
        if (isChecked) {
            if (!titles.contains(titlePH)) {
                if (!titlePH.equals("") && !titles.contains(titlePH)) {
                    SharedPreferences.Editor editor = preferences.edit();
                    if (titles.equals("")) {
                        editor.putString("timeRange", titlePH);
                    } else {
                        editor.putString("timeRange", titles + "," + titlePH);
                    }
                    editor.apply();

                } else
                    Toast.makeText(context, "این رو قبلا زدی", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(context, "چنین شماره ای وجود داره", Toast.LENGTH_SHORT).show();
        } else {
            final String[] titles2 = preferences.getString("timeRange", "").split(",");
            String temp = "";
            for (int i = 0; i < titles2.length; i++) {
                String tempInPer = titles2[i];
                if (!tempInPer.equals(titlePH)) {
                    if (i == 0)
                        temp = tempInPer;
                    else if (temp.equals(""))
                        temp = tempInPer;
                    else
                        temp += "," + tempInPer;
                }
            }

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("timeRange", temp);
            editor.apply();
        }

 }

    private void alertDialogFilterPhone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.item_listview_chooser, null, false);

        Button btnFr = layout.findViewById(R.id.btnFr);
        btnFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogFilterPhone.dismiss();
            }
        });

        if (arraylistSearchView.size() != 0)
            arraylistSearchView.clear();
        ArrayList arrayList = new ArrayList();

        for (int i = 0; i < tb_billsList.size(); i++)
            arrayList.add(tb_billsList.get(i).senderSMS);

        LinkedHashSet<String> lhs = new LinkedHashSet<String>();
        lhs.addAll(arrayList);
        arrayList.clear();
        arrayList.addAll(lhs);

        String[] titles = preferences.getString("phoneNum", "").split(",");

//        if (!titles[0].equals("")) {
//            ArrayList titlesArr = new ArrayList();
//            for (int i = 0; i < titles.length; i++)
//                titlesArr.add(titles[i] + "");
//
//            arrayList.retainAll(titlesArr);
//        }

        for (int i = 0; i < arrayList.size(); i++) {
            ModFilterPhone modAlerts = new ModFilterPhone(
                    arrayList.get(i) + "",
                    arrayList.get(i) + "");
            arraylistSearchView.add(modAlerts);
        }

//        for (int i = 0; i < titles.length; i++) {
//            String phoneSaved = titles[i];
//            for (int j = 0; j < arraylistSearchView.size(); j++) {
//                String phoneAll = arraylistSearchView.get(j).getId() + "";
//                if (phoneSaved.equals(phoneAll)) {
//                    arraylistSearchView.remove(j);
//                    break;
//                }
//            }
//        }

        RecyclerView recycFitler = layout.findViewById(R.id.recycFitler);
        adRecycPopUp = new AdRecyclFilterPhoneCheckBox(context, arraylistSearchView, new onClickInterface() {
            @Override
            public void setClick(int position, View view, String s) {

                final CheckBox txttitle = ((LinearLayout) view).findViewById(R.id.checkboxTitle);
                TextView txtId = ((LinearLayout) view).findViewById(R.id.txtId);


                txttitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            }
        });
        recycFitler.setAdapter(adRecycPopUp);

        editsearchSearchView = layout.findViewById(R.id.searchFr);
        editsearchSearchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                adRecycPopUp.filter(text);
                return true;
            }
        });

        builder.setView(layout);
        alertDialogFilterPhone = builder.create();
        alertDialogFilterPhone.show();
        alertDialogFilterPhone.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    private AlertDialog alertDialogLoading;

    private void loading() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.loading, null, false);

            builder.setView(layout);
            alertDialogLoading = builder.create();
            alertDialogLoading.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
