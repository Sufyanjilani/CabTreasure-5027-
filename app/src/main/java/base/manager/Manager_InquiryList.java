package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.support.parser.SoapHelper;

import base.listener.Listener_InquiryList;
import base.utils.CommonVariables;

public class Manager_InquiryList extends AsyncTask<Void, Void, String> {
    private Context context;
    private Listener_InquiryList listener;

    public Manager_InquiryList(Context context, Listener_InquiryList listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            return new SoapHelper.Builder(CommonVariables.SERVICE, context)
                    .setMethodName("InquiryList", true)
                    .getResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (listener != null) {
            listener.onComplete(result);
        }
    }// End onPostExecute()
}// End class PrefetchData
