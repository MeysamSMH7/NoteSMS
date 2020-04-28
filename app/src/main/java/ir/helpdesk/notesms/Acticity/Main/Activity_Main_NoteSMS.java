package ir.helpdesk.notesms.Acticity.Main;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ir.helpdesk.notesms.Acticity.Main.Adapter.AdRecyclFilterPhone;
import ir.helpdesk.notesms.Acticity.Main.Adapter.ViewPagerAdapterMain;
import ir.helpdesk.notesms.Acticity.Main.Adapter.onClickInterface;
import ir.helpdesk.notesms.Classes.CalendarTool;
import ir.helpdesk.notesms.DataBase.DataSource.tb_BillsDataSource;
import ir.helpdesk.notesms.DataBase.Tables.tb_Bills;
import ir.helpdesk.notesms.Acticity.Main.Fragment.frTab_item;
import ir.helpdesk.notesms.Acticity.Main.Fragment.frTab_search;
import ir.helpdesk.notesms.R;

public class Activity_Main_NoteSMS extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context context = this;
    private Toolbar toolbar;

    private TabLayout tl_tabLayout;
    private ViewPager vp_viewPager;
    private List<Fragment> fragments;
//    private int[] tabIcons = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};

    private static final int Time_Between_Two_Back = 2000;
    private long TimeBackPressed;
    private ArrayList<tb_Bills> tb_billsList;

    private SharedPreferences preferences;
    private ArrayList arrayListTitle;
    private ArrayList arrayListPhoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_notesms);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        getSMSFromInbox();
        findViews();
        changeTabsFont(tl_tabLayout);
        initViewPager();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("FirstTime?", false);
        editor.apply();


    }

    private void findViews() {
        preferences = getSharedPreferences("TuRn", 0);
        toolbar = findViewById(R.id.toolbar);
        tl_tabLayout = (TabLayout) findViewById(R.id.tl_tabLayout);
        vp_viewPager = (ViewPager) findViewById(R.id.vp_viewPager);

        tb_billsList = new ArrayList<>(new tb_BillsDataSource(context).GetList());

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

    private ArrayList<tb_Bills> getCustomData(String phonNum) {

        ArrayList<tb_Bills> bills = new ArrayList<>();
        for (int i = 0; i < tb_billsList.size(); i++) {
            tb_Bills tbBills = tb_billsList.get(i);
            if (tbBills.senderSMS.equals(phonNum))
                bills.add(tbBills);
        }
        return bills;
    }

    private void initViewPager() {

        arrayListTitle = new ArrayList();
        arrayListPhoneNum = new ArrayList();

        arrayListTitle.add("همه");
        arrayListTitle.add("جستجو");

        String titles[] = preferences.getString("titles", "")
                .split(",");
        String phoneNum[] = preferences.getString("phoneNum", "")
                .split(",");

        if (!titles[0].equals("")) {
            for (int i = 0; i < titles.length; i++)
                arrayListTitle.add(titles[i]);
            for (int i = 0; i < phoneNum.length; i++)
                arrayListPhoneNum.add(titles[i]);
        }
        fragments = new ArrayList<>();

        fragments.add(frTab_item.newInstance(tb_billsList));
        fragments.add(frTab_search.newInstance());

        for (int i = 0; i < arrayListPhoneNum.size(); i++)
            fragments.add(frTab_item.newInstance(getCustomData(arrayListPhoneNum.get(i) + "")));

        ViewPagerAdapterMain adapter = new ViewPagerAdapterMain(getSupportFragmentManager(), fragments, arrayListTitle);
        vp_viewPager.setAdapter(adapter);
        tl_tabLayout.setupWithViewPager(vp_viewPager);
        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
        vp_viewPager.setOffscreenPageLimit(limit);
        vp_viewPager.setCurrentItem(0);

//        for (int i = 0; i < arrayListPhoneNum.size(); i++)
//            tl_tabLayout.getTabAt(i).setIcon(tabIcons[i]);

        customIcon(arrayListTitle);
    }

    private void customIcon(ArrayList arrayListTtle) {
        for (int i = 0; i < tl_tabLayout.getTabCount(); i++) {
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_tablayout, null);
            TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
            ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);

            tab_label.setText(arrayListTtle.get(i) + "");

            tab_label.setTextColor(getResources().getColor(R.color.colorWhite));
//            tab_icon.setImageResource(tabIcons[i]);


            // finally publish this custom view to navigation tab
            tl_tabLayout.getTabAt(i).setCustomView(tab);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (TimeBackPressed + Time_Between_Two_Back > System.currentTimeMillis()) {
                super.onBackPressed();
                return;
            } else
                Toast.makeText(context, "برای خروج دوباره کلیک کنید", Toast.LENGTH_SHORT).show();

            TimeBackPressed = System.currentTimeMillis();
        }
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
        if (id == R.id.nav_slideshow) {
            PersianCalendar persianCalendar = new PersianCalendar();
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                    null,
                    persianCalendar.getPersianYear(),
                    persianCalendar.getPersianMonth(),
                    persianCalendar.getPersianDay()
            );
            datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            alertDialogFilterPhone();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //----------------- filter
    private AlertDialog alertDialogFilterPhone;
    private AdRecyclFilterPhone adRecycPopUp;
    private androidx.appcompat.widget.SearchView editsearchSearchView;
    private List<ModFilterPhone> arraylistSearchView = new ArrayList<ModFilterPhone>();

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
        for (int i = 0; i < arrayList.size(); i++) {
            ModFilterPhone modAlerts = new ModFilterPhone(
                    arrayList.get(i) + "",
                    arrayList.get(i) + "");
            arraylistSearchView.add(modAlerts);
        }


        RecyclerView recycFitler = layout.findViewById(R.id.recycFitler);
        adRecycPopUp = new AdRecyclFilterPhone(context, arraylistSearchView, new onClickInterface() {
            @Override
            public void setClick(int position, View view) {

                TextView txttitle = ((LinearLayout) view).findViewById(R.id.txtTitle);
                TextView txtId = ((LinearLayout) view).findViewById(R.id.txtId);
                String titlePH = txttitle.getText().toString();
                String id = txtId.getText().toString();


                String titles = preferences.getString("titles", "");
                String phoneNum = preferences.getString("phoneNum", "");

                if (!arrayListPhoneNum.contains(titlePH)) {

                    if (!titlePH.equals("") && !titles.contains(titlePH)) {
                        titlePH += ","+titles;
//                        title += phoneNum;

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("titles", titlePH);
                        editor.putString("phoneNum", titlePH);
                        editor.apply();
                        alertDialogFilterPhone.dismiss();
                        initViewPager();

                    }else
                        Toast.makeText(context, "این رو قبلا زدی", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, "چنین شماره ای وجود داره", Toast.LENGTH_SHORT).show();
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

    //----------------------------------------------- SMS

    public void getSMSFromInbox() {

        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"),
                null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        int date = smsInboxCursor.getColumnIndex("date");
        int type = smsInboxCursor.getColumnIndex("type");

        tb_BillsDataSource source = new tb_BillsDataSource(context);

        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        do {
            String typeee = smsInboxCursor.getString(type);
            if (typeee.equals("1")) {
                String key = smsInboxCursor.getString(date);
                Date smsDayTime = new Date(Long.valueOf(key));

                Log.i("ASDF =========> ", smsInboxCursor.getString(indexAddress) + "");
//                String str = "SMS From: " + smsInboxCursor.getString(indexAddress) +
//                        "\n" + smsInboxCursor.getString(indexBody) +
//                        "\n" + smsDayTime +
//                        "\n" + smsInboxCursor.getString(type) +
//                        "\n";

                tb_Bills tb_bills = new tb_Bills();

                String dateMiladi = convertDate(smsDayTime.toString());
                String[] dateSamsiArr = dateMiladi.split("/");
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

                if (source.isARecordExist(key))
                    source.Add(tb_bills);
            }
        } while (smsInboxCursor.moveToNext());

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

        dateMiladi = year + "/" + numberMonth + "/" + day;

        return dateMiladi;
    }

}