package ir.helpdesk.notesms;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

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
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.helpdesk.notesms.DataBase.DataSource.tb_BillsDataSource;
import ir.helpdesk.notesms.DataBase.Tables.tb_Bills;
import ir.helpdesk.notesms.Fragment.frTab_item;

public class Activity_Main_NoteSMS extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context context = this;
    private Toolbar toolbar;

    private TabLayout tl_tabLayout;
    private ViewPager vp_viewPager;
    private List<Fragment> fragments;
    private int[] tabIcons = {R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher};

    private static final int Time_Between_Two_Back = 2000;
    private long TimeBackPressed;

    private SharedPreferences preferences;

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
        ArrayList<tb_Bills> tb_billsList = new ArrayList<>(new tb_BillsDataSource(context).GetList());
        ArrayList<tb_Bills> bills = new ArrayList<>();
        for (int i = 0; i < tb_billsList.size(); i++) {
            tb_Bills tbBills = tb_billsList.get(i);
            if (tbBills.senderSMS.equals(phonNum))
                bills.add(tbBills);
        }
        return bills;
    }

    private void initViewPager() {
        fragments = new ArrayList<>();

        ArrayList arrayListTtle = new ArrayList();
        arrayListTtle.add("اسنپ");
        arrayListTtle.add("زولا");
        arrayListTtle.add("همراه اول");
        arrayListTtle.add("همتا");
        arrayListTtle.add("ایرانسل");
        ArrayList arrayListPhoneNum = new ArrayList();
        arrayListPhoneNum.add("Snapp");
        arrayListPhoneNum.add("+9810007119");
        arrayListPhoneNum.add("HAMRAHAVAL");
        arrayListPhoneNum.add("HAMTA");
        arrayListPhoneNum.add(".IRANCELL.");


        for (int i = 0; i < arrayListPhoneNum.size(); i++)
            fragments.add(frTab_item.newInstance(getCustomData(arrayListPhoneNum.get(i) + "")));


        ViewPagerAdapterMain adapter = new ViewPagerAdapterMain(getSupportFragmentManager(), fragments, arrayListTtle);
        vp_viewPager.setAdapter(adapter);
        tl_tabLayout.setupWithViewPager(vp_viewPager);
        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
        vp_viewPager.setOffscreenPageLimit(limit);
        vp_viewPager.setCurrentItem(0);

        for (int i = 0; i < arrayListPhoneNum.size(); i++)
            tl_tabLayout.getTabAt(i).setIcon(tabIcons[i]);

        customIcon(arrayListTtle);
    }

    private void customIcon(ArrayList arrayListTtle) {
        for (int i = 0; i < tl_tabLayout.getTabCount(); i++) {
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_tablayout, null);
            TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
            ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);

            tab_label.setText(arrayListTtle.get(i) + "");

            tab_label.setTextColor(getResources().getColor(R.color.colorWhite));
            tab_icon.setImageResource(tabIcons[i]);


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
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

