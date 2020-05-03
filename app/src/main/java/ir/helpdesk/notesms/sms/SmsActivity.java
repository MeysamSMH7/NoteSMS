package ir.helpdesk.notesms.sms;


import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ir.helpdesk.notesms.Classes.CalendarTool;
import ir.helpdesk.notesms.DataBase.DataSource.tb_BillsDataSource;
import ir.helpdesk.notesms.DataBase.Tables.tb_Bills;
import ir.helpdesk.notesms.R;

public class SmsActivity extends AppCompatActivity implements OnItemClickListener {

    private static SmsActivity inst;
    ArrayList<String> smsMessagesList = new ArrayList<String>();
    ListView smsListView;
    ArrayAdapter arrayAdapter;
    TextView textView;
    Context context = this;
    tb_BillsDataSource source;

    public static SmsActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        textView = findViewById(R.id.textView);
        smsListView = (ListView) findViewById(R.id.SMSList);
        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, smsMessagesList);
        smsListView.setAdapter(arrayAdapter);
        smsListView.setOnItemClickListener(this);


//        ActivityCompat.requestPermissions(this, new String[]{
//                Manifest.permission.SEND_SMS,
//                Manifest.permission.READ_SMS,
//                Manifest.permission.BIND_TELECOM_CONNECTION_SERVICE,
//                Manifest.permission.BROADCAST_SMS}, 1234);

        CalendarTool tool = new CalendarTool();
        Log.i("DATE Number:", tool.getIranianDate());
        tool.previousDay(30);
        Log.i("DATE Number:", tool.getIranianDate());


        source = new tb_BillsDataSource(context);

//        refreshSmsInbox();

    }

    private ArrayList<tb_Bills> getCustomData(String phoneNum) {
        ArrayList<tb_Bills> tb_billsList = new ArrayList<>(new
                tb_BillsDataSource(context).GetList());

        ArrayList<tb_Bills> bills = new ArrayList<>();

        for (int i = 0; i < tb_billsList.size(); i++) {
            tb_Bills tbBills = tb_billsList.get(i);
            if (tbBills.senderSMS.equals(phoneNum))
                bills.add(tbBills);
        }
        return bills;
    }


    public void refreshSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"),
                null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        int date = smsInboxCursor.getColumnIndex("date");
        int type = smsInboxCursor.getColumnIndex("type");

        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        arrayAdapter.clear();
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

        ArrayList<tb_Bills> tb_billsList = new ArrayList<>(source.GetList());

        arrayAdapter.clear();
        for (int i = 0; i < tb_billsList.size(); i++) {
            tb_Bills tb_bills = tb_billsList.get(i);
            String str = "پیامک از طرف: " + tb_bills.senderSMS
                    + "\n" + "تاریخ دریافت" + tb_bills.dateSMSJalali
                    + "\n" + "متن پیام:" + tb_bills.txtSMS
                    + "\n" + "کلید:" + tb_bills.PK_key
                    + "\n\n" + "متن نوت:" + tb_bills.txtNote
                    + "\n" + "تاریخ یادداشت:" + tb_bills.dateNoteMiladi
                    + "\n";
            arrayAdapter.add(str);
        }

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

    public void updateList(final String smsMessage) {
        arrayAdapter.insert(smsMessage, 0);
        arrayAdapter.notifyDataSetChanged();
        Toast.makeText(inst, "UUUPPPP", Toast.LENGTH_SHORT).show();
    }

    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        try {
            String[] smsMessages = smsMessagesList.get(pos).split("\n");
            String address = smsMessages[0];
            String smsMessage = "";
            for (int i = 1; i < smsMessages.length; ++i) {
                smsMessage += smsMessages[i];
            }

            String smsMessageStr = address + "\n";
            smsMessageStr += smsMessage;
            Toast.makeText(this, smsMessageStr, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
