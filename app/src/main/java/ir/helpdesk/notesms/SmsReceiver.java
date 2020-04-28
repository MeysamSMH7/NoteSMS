package ir.helpdesk.notesms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by Marjan on 09/12/2017.
 * Own WebSite is Tejariapp.com
 */

public class SmsReceiver extends BroadcastReceiver {

//    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Toast.makeText(context, "asdfasdf123", Toast.LENGTH_SHORT).show();

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = smsMessage.getDisplayOriginatingAddress();
            String messageBody = smsMessage.getMessageBody();
//            mListener.onMessageReceived(messageBody);
        }
    }

//    public static void bindListener(SmsListener listener) {
//        mListener = listener;
//    }
}