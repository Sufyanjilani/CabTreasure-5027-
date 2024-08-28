package com.support.parser;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.AttributeInfo;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParserException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SoapHelper {

    public static class Builder {//"http://favouritehatfield.co.uk/Service1.asmx?
        /*
         ~~ Windsor URL ~~*/
        private static final String mURL1 = "http://favouritehatfield.co.uk/Service1.asmx?"; //http://cabtreasureappapi.co.uk/cusAPI/AppAPI.asmx
        private static final String mURL2 = "https://cabtreasureappapi.co.uk/cusAPI/AppAPI.asmx";
        Context context;
        private String mURL = "https://cabtreasureappapi.co.uk/cusAPI/AppAPI.asmx";
        private String mMethod = "GetServerVehicles";
        private String mNameSpace = "http://tempuri.org/";
        private String mSoapAction = "http://tempuri.org/GetServerVehicles";

        /* Windsor URL    */


        /* CROWN CARS URL
        private static final String mURL1 = "http://favouritehatfield.co.uk/Service1.asmx?"; //http://cabtreasureappapi.co.uk/cusAPI/AppAPI.asmx
        //        private static final String mURL2 = "https://cabtreasureappapi.co.uk/cusAPI/AppAPI.asmx";
        private static final String mURL2 = "https://cabtreasureonlineapi.co.uk/appapi.asmx";
        Context context;
        private String mURL = "https://cabtreasureonlineapi.co.uk/appapi.asmx";
        private String mMethod = "GetServerVehicles";
        private String mNameSpace = "http://tempuri.org/";
        private String mSoapAction = "http://tempuri.org/GetServerVehicles";
        /* CROWN CARS URL */


        private SoapSerializationEnvelope mEnvelope;

        private ArrayList<PropertyInfo> mInfoArray = new ArrayList<PropertyInfo>();

        public Builder(int service, Context context) {
            switch (service) {
                case 1:
                    mURL = mURL1;
                    break;
                case 2:
                    mURL = mURL2;
                    break;
                default:
                    break;
            }
            this.context = context;
            mEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            mEnvelope.dotNet = true;
            buildAuthHeader();

        }

        public Builder setMethodName(String method, boolean concatinate) {
            if (concatinate) {
                mMethod = method;
                mSoapAction = mNameSpace + method;
            } else
                mMethod = method;

            return this;
        }

        public Builder setMethodName(String method) {
            mMethod = method;
            return this;
        }

        public Builder setSoapAction(String action) {
            mSoapAction = action;
            return this;
        }

        public static String encryptAndEncode(String raw) {
            try {
//            Cipher c = getCipher(Cipher.ENCRYPT_MODE);
//            byte[] encryptedVal = c.doFinal(getBytes(raw));
//            String s = getString(Base64.encode(encryptedVal,Base64.NO_PADDING));
//
//            String originalInput = "test input";
                String result = Base64.encodeToString(raw.getBytes("UTF-8"), Base64.DEFAULT);
                return result;
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }

        public Builder addProperty(String name, Object value, Object clazz) {
            PropertyInfo info = new PropertyInfo();
            info.setName(name);
            info.setType(clazz);
            info.setValue(value);

            mInfoArray.add(info);
            return this;
        }

        ArrayList<HeaderProperty> headerProperty;

        private void buildAuthHeader() {
            if (context == null) {
                return;
            }
            String rawValue = PreferenceManager.getDefaultSharedPreferences(context).getString("ParamId", "");
            if (headerProperty == null) {
                headerProperty = new ArrayList<HeaderProperty>();
            } else {
                headerProperty.clear();
            }
            headerProperty.add(new HeaderProperty("Authentication", encryptAndEncode(rawValue)));

        }

        public SoapSerializationEnvelope executeEnvelope() throws IOException, XmlPullParserException {
//          mEnvelope.a

            HttpTransportSE transport = new HttpTransportSE(mURL, 12000);
//            transport.getConnection().se
            SoapObject soapObject = new SoapObject(mNameSpace, mMethod);
//            soapObject.addAttribute(new AttributeInfo().h);
            for (PropertyInfo info : mInfoArray)
                soapObject.addProperty(info);

            mEnvelope.setOutputSoapObject(soapObject);


            transport.call(mSoapAction, mEnvelope, headerProperty);
//            String dump=mEnvelope.toString();
//            Log.e("dump Request: " ,androidHttpTransport.requestDump);
            return mEnvelope;
        }

        public String getResponse() throws IOException, XmlPullParserException {
            if (context == null) {
                return null;
            }
            return executeEnvelope().getResponse().toString();
        }

        public Builder addHeader(String headerString) throws IOException, XmlPullParserException {
//           buildAuthHeader(headerString);
            return this;
        }

    }

//    public  static String  encodedText(String input){
//        try {
//            final String key = "=abcd!#Axd*G!pxP";
//            final javax.crypto.spec.SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
//            final javax.crypto.Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
//            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
//            byte[] encryptedValue = cipher.doFinal(input.getBytes());
//            return new String(org.apache.commons.codec.binary.Hex.encodeHex(encryptedValue));
//        }catch (Exception e){
//            return  input;
//        }
//    }

}
