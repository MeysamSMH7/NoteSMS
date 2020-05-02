package ir.helpdesk.notesms.Acticity.Main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ir.helpdesk.notesms.Acticity.Main.Adapter.ViewPagerAdapterMain;
import ir.helpdesk.notesms.Acticity.Setting.Activity_Setting_NoteSMS;
import ir.helpdesk.notesms.Classes.CalendarTool;
import ir.helpdesk.notesms.Classes.SQLiteToExcel;
import ir.helpdesk.notesms.DataBase.DataSource.tb_BillsDataSource;
import ir.helpdesk.notesms.DataBase.DatabaseManagement;
import ir.helpdesk.notesms.DataBase.Structure.tb_BillsStructure;
import ir.helpdesk.notesms.DataBase.Tables.tb_Bills;
import ir.helpdesk.notesms.Acticity.Main.Fragment.frTab_item;
import ir.helpdesk.notesms.Acticity.Main.Fragment.frTab_search;
import ir.helpdesk.notesms.R;

public class Activity_Main_NoteSMS extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context context = this;
    private Toolbar toolbar;

    private TabLayout tl_tabLayout;
    private ViewPager vp_viewPager;

    private static final int Time_Between_Two_Back = 2000;
    private long TimeBackPressed;
    private ArrayList<tb_Bills> tb_billsList;

    private SharedPreferences preferences;
    private AlertDialog alertDialogLoading;
    private ViewPagerAdapterMain viewPagerAdapterMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//        navigationView.setNavigationItemSelectedListener(this);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Activity_Main_NoteSMS.this, R.color.colorLogo));

        loading();
        findViews();
        changeTabsFont(tl_tabLayout);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("FirstTime?", false);
        editor.apply();

        if (Build.VERSION.SDK_INT > 23)
            requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS,
                    Manifest.permission.BIND_TELECOM_CONNECTION_SERVICE, Manifest.permission.BROADCAST_SMS}, 1);
        else
            getSMSFromInbox();

        if (Build.VERSION.SDK_INT > 23) {
            String requiredPermission = android.Manifest.permission.SEND_SMS;
            int checkVal = checkCallingOrSelfPermission(requiredPermission);
            if (checkVal == PackageManager.PERMISSION_GRANTED) {
                getSMSFromInbox();
            }
        }

    }

    private void findViews() {
        preferences = getSharedPreferences("nOtEsMs", 0);
        toolbar = findViewById(R.id.toolbar);

        tl_tabLayout = findViewById(R.id.tl_tabLayout);
        vp_viewPager = findViewById(R.id.vp_viewPager);
    }

    protected void changeTabsFont(TabLayout tabLayout) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof AppCompatTextView) {
                    Typeface type = Typeface.createFromAsset(this.getAssets(), "fonts/vazir.ttf");
                    TextView viewChild = (TextView) tabViewChild;
                    viewChild.setTypeface(type);
                    viewChild.setAllCaps(false);
                }
            }
        }
    }

//    private ArrayList<tb_Bills> getCustomData(String phoneNum) {
//        ArrayList<tb_Bills> bills = new ArrayList<>();
//        for (int i = 0; i < tb_billsList.size(); i++) {
//            tb_Bills tbBills = tb_billsList.get(i);
//            if (tbBills.senderSMS.equals(phoneNum))
//                bills.add(tbBills);
//        }
//        return bills;
//    }

    private void initViewPager() {

        tb_billsList = new ArrayList<>(new tb_BillsDataSource(context).GetList());

        String defaultTabs = "جستجو,همه";

        String titlesPre = preferences.getString("phoneNum", "");
        String[] titles;


        if (titlesPre.equals(""))
            titles = defaultTabs.split(",");
        else {
            String temp = defaultTabs + "," + titlesPre;
            titles = temp.split(",");
        }


        List<Fragment> fragments = new ArrayList<>();

        fragments.add(frTab_search.newInstance());
        fragments.add(frTab_item.newInstance("all"));

        if (!titlesPre.equals(""))
            for (int i = 2; i < titles.length; i++)
                fragments.add(frTab_item.newInstance(titles[i]));

        viewPagerAdapterMain = new ViewPagerAdapterMain(getSupportFragmentManager(), fragments, titles);
        vp_viewPager.setAdapter(viewPagerAdapterMain);
        tl_tabLayout.setupWithViewPager(vp_viewPager);
        int limit = (viewPagerAdapterMain.getCount() > 1 ? viewPagerAdapterMain.getCount() - 1 : 1);
        vp_viewPager.setOffscreenPageLimit(limit);
        vp_viewPager.setCurrentItem(1);
        customIcon(titles);

//        for (int i = 0; i < arrayListPhoneNum.size(); i++)
//            tl_tabLayout.getTabAt(i).setIcon(tabIcons[i]);

    }

    private void customIcon(String[] arrayListTtle) {
        for (int i = 0; i < tl_tabLayout.getTabCount(); i++) {
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_tablayout, null);
            TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
            ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);

            tab_label.setText(arrayListTtle[i] + "");

            tab_label.setTextColor(getResources().getColor(R.color.colorWhite));
//            tab_icon.setImageResource(tabIcons[i]);

            // finally publish this custom view to navigation tab
            tl_tabLayout.getTabAt(i).setCustomView(tab);
        }
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
        if (TimeBackPressed + Time_Between_Two_Back > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else
            Toast.makeText(context, "برای خروج دوباره کلیک کنید", Toast.LENGTH_SHORT).show();

        TimeBackPressed = System.currentTimeMillis();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            startActivity(new Intent(Activity_Main_NoteSMS.this, Activity_Setting_NoteSMS.class));
            return true;
        } else if (id == R.id.action_exel) {
            if (Build.VERSION.SDK_INT > 23)
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            else
                exportDBToEx();

            if (Build.VERSION.SDK_INT > 23) {
                String requiredPermission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
                int checkVal = checkCallingOrSelfPermission(requiredPermission);
                if (checkVal == PackageManager.PERMISSION_GRANTED) {
                    exportDBToEx();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            finish();
            startActivity(new Intent(Activity_Main_NoteSMS.this, Activity_Setting_NoteSMS.class));
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        }


//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getSMSFromInbox();
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("رد کردن دسترسی");
                builder.setMessage("این برنامه نیاز به دسترسی به پیامک های شما رو داره");
                builder.setPositiveButton("درخواست دوباره", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(Activity_Main_NoteSMS.this, new String[]{
                                Manifest.permission.SEND_SMS,
                                Manifest.permission.READ_SMS,
                                Manifest.permission.BIND_TELECOM_CONNECTION_SERVICE,
                                Manifest.permission.BROADCAST_SMS}, 1);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("بستن برنامه", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                builder.create();
                builder.show();
            }
        } else if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               exportDBToEx();
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("رد کردن دسترسی");
                builder.setMessage("این برنامه نیاز به دسترسی به حافظه های شما رو داره");
                builder.setPositiveButton("درخواست دوباره", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(Activity_Main_NoteSMS.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("بستن برنامه", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.create();
                builder.show();
            }
        }
    }

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

    private void exportDBToEx() {
        SQLiteToExcel sqLiteToExcel = new SQLiteToExcel(this, DatabaseManagement.databaseName);
        sqLiteToExcel.exportSingleTable(tb_BillsStructure.tableName, "noteSMS.xls", new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {
                alertDialogLoading.show();
            }

            @Override
            public void onCompleted(String filePath) {
                alertDialogLoading.dismiss();
                Toast.makeText(context, "فایل شما با موفقیت در حافظه ی گوشی شما ذخیره شد.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                alertDialogLoading.dismiss();
                Toast.makeText(context, "ذخیره کردن با مشکل مواجع شد!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }
    //----------------------------------------------- SMS

    public void getSMSFromInbox() {
        alertDialogLoading.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ContentResolver contentResolver = getContentResolver();
                Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"),
                        null, null, null, null);
                int indexBody = smsInboxCursor.getColumnIndex("body");
                int indexAddress = smsInboxCursor.getColumnIndex("address");
                int date = smsInboxCursor.getColumnIndex("date");
                int type = smsInboxCursor.getColumnIndex("type");

                tb_BillsDataSource source = new tb_BillsDataSource(context);

                int counter = 0;
                boolean ifBreak = false;

                if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
                do {
                    String typeee = smsInboxCursor.getString(type);
                    if (typeee.equals("1")) {
                        String key = smsInboxCursor.getString(date);
                        Date smsDayTime = new Date(Long.valueOf(key));

//                Log.i("ASDF =========> ", smsInboxCursor.getString(indexAddress) + "");
//                String str = "SMS From: " + smsInboxCursor.getString(indexAddress) +
//                        "\n" + smsInboxCursor.getString(indexBody) +
//                        "\n" + smsDayTime +
//                        "\n" + smsInboxCursor.getString(type) +
//                        "\n";
                        tb_Bills tb_bills = new tb_Bills();

                        String dateMiladi = convertDate(smsDayTime.toString());
                        String[] dateSamsiArr = dateMiladi.split("-");
                        CalendarTool tool = new CalendarTool();
                        tool.setGregorianDate(Integer.parseInt(dateSamsiArr[0]), Integer.parseInt(dateSamsiArr[1]), Integer.parseInt(dateSamsiArr[2]));
                        String dataSamsi = tool.getIranianDate();

                        tb_bills.PK_key = key;
                        tb_bills.txtSMS = smsInboxCursor.getString(indexBody);
                        tb_bills.senderSMS = smsInboxCursor.getString(indexAddress);
                        tb_bills.dateSMSMiladi = dateMiladi;
                        tb_bills.dateSMSJalali = dataSamsi;
                        tb_bills.txtNote = "";
                        tb_bills.dateNoteMiladi = "";
                        tb_bills.dateNoteJalali = "";
                        tb_bills.temp = "";

                        if (counter == 0) {
                            tb_billsList = new ArrayList<>(new tb_BillsDataSource(context).GetList());
                            if (tb_billsList.size() != 0)
                                if (key.equals(tb_billsList.get(0).PK_key))
                                    ifBreak = true;
                        }
                        counter++;

                        if (ifBreak)
                            break;

                        if (source.isARecordExist(key))
                            source.Add(tb_bills);
                    }
                } while (smsInboxCursor.moveToNext());
                initViewPager();
            }
        }, 2000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialogLoading.dismiss();
            }
        }, 1000);
    }

    private String convertDate(String date) {
        String dateMiladi = "";

        String[] dateArr = date.split("\\s+");
        String month = dateArr[1];
        String day = dateArr[2];
        String year = dateArr[5];

        String numberMonth = "01";
        if (month.equals("Jan"))
            numberMonth = "01";
        else if (month.equals("Feb"))
            numberMonth = "02";
        else if (month.equals("Mar"))
            numberMonth = "03";
        else if (month.equals("Apr"))
            numberMonth = "04";
        else if (month.equals("May"))
            numberMonth = "05";
        else if (month.equals("Jun"))
            numberMonth = "06";
        else if (month.equals("Jul"))
            numberMonth = "07";
        else if (month.equals("Aug"))
            numberMonth = "08";
        else if (month.equals("Sep"))
            numberMonth = "09";
        else if (month.equals("Oct"))
            numberMonth = "10";
        else if (month.equals("Nov"))
            numberMonth = "11";
        else if (month.equals("Dec"))
            numberMonth = "12";

        dateMiladi = year + "-" + numberMonth + "-" + day;

        return dateMiladi;
    }

}