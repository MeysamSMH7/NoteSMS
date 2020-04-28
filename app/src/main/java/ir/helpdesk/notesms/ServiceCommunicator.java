package ir.helpdesk.notesms;

import android.app.Service;
import android.content.IntentFilter;
import android.widget.Toast;

public abstract class ServiceCommunicator extends Service {
    private SMSreceiver123 mSMSreceiver;
    private IntentFilter mIntentFilter;

    @Override
    public void onCreate() {
        super.onCreate();

        //SMS event receiver
        mSMSreceiver = new SMSreceiver123();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mSMSreceiver, mIntentFilter);
        Toast.makeText(this, "sadf3333", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister the SMS receiver
        unregisterReceiver(mSMSreceiver);
        Toast.makeText(this, "close", Toast.LENGTH_SHORT).show();
    }
}