package ir.helpdesk.notesms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

class SMSreceiver123 extends BroadcastReceiver {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        Toast.makeText(context, "asdf222222", Toast.LENGTH_SHORT).show();

        String strMessage = "";

        if (extras != null) {
            Object[] smsextras = (Object[]) extras.get("pdus");

            for (int i = 0; i < smsextras.length; i++) {
                SmsMessage smsmsg = SmsMessage.createFromPdu((byte[]) smsextras[i]);
                String strMsgBody = smsmsg.getMessageBody().toString();
                String strMsgSrc = smsmsg.getOriginatingAddress();
                strMessage += "SMS from " + strMsgSrc + " : " + strMsgBody;
                Toast.makeText(context, strMessage+"", Toast.LENGTH_SHORT).show();
                Log.i("asdfasdfasdf", strMessage);
            }

        }

    }

}
