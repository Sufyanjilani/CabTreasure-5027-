package base.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsBroadcastReceiver";

    private final String serviceProviderPrefix;
    private final String serviceProviderSmsCondition;

    private Listener listener;

    public SmsBroadcastReceiver() {
        this.serviceProviderPrefix = "your";
        this.serviceProviderSmsCondition = "app verification code";
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
//            Bundle extras = intent.getExtras();
//            if (extras != null) {
//                Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
//
//                if (status != null)
//                    switch (status.getStatusCode()) {
//                        case CommonStatusCodes.SUCCESS:
//                            // Get SMS message contents
//                            String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
//                            // Extract one-time code from the message and complete verification
//                            // by sending the code back to your server.
//                            if (!TextUtils.isEmpty(message)) {
//                                String activationCode = null;
////                                Pattern p = Pattern.compile("your pattern like \\b\\d{4}\\b");
////                                Matcher m = p.matcher(message);
//
//                                if (message.startsWith(serviceProviderPrefix)&&message.toLowerCase().contains(serviceProviderSmsCondition)&&message.contains(":")) {
//                                String[] splittedArr=message.split(":");
//                                    if(splittedArr.length==2){
//                                        activationCode=splittedArr[1].trim();
//                                        if (listener != null && !TextUtils.isEmpty(activationCode)) {
//                                            listener.onTextReceived(activationCode);
//                                        }
//                                    }
//
//                                }
//
//
//                            }
//                            break;
//                        case CommonStatusCodes.TIMEOUT:
//                            // Waiting for SMS timed out (5 minutes)
//                            // Handle the error ...
//                            break;
//                    }
//            }
//        }
    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
//            String smsSender = "";
//            String smsBody = "";
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
////                    smsSender = smsMessage.getDisplayOriginatingAddress();
//                    smsBody += smsMessage.getMessageBody();
//                }
//            } else {
//                Bundle smsBundle = intent.getExtras();
//                if (smsBundle != null) {
//                    Object[] pdus = (Object[]) smsBundle.get("pdus");
//                    if (pdus == null) {
//                        // Display some error to the user
//                        Log.e(TAG, "SmsBundle had no pdus key");
//                        return;
//                    }
//                    SmsMessage[] messages = new SmsMessage[pdus.length];
//                    for (int i = 0; i < messages.length; i++) {
//                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
//                        smsBody += messages[i].getMessageBody();
//                    }
//                    smsSender = messages[0].getOriginatingAddress();
//                }
//            }
//
//            if (smsBody.toLowerCase().startsWith(serviceProviderPrefix) && smsBody.contains(serviceProviderSmsCondition)) {
//                String[] splitSms=smsBody.split(":");
//                if (listener != null&&splitSms.length==2) {
//                    listener.onTextReceived(splitSms[1]);
//                }
//            }
//        }
//    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onTextReceived(String text);
    }
}