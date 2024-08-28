package base.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.support.parser.SoapHelper;

import base.listener.Listener_InquiryList;
import base.listener.Listener_List;
import base.utils.CommonVariables;

public class Manager_GetListFromServer extends AsyncTask<Void, Void, String> {
    private Context context;
    private String methodName;
    private Listener_List listener;

    public Manager_GetListFromServer(Context context, String methodName, Listener_List listener) {
        this.context = context;
        this.methodName = methodName;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {

            return new SoapHelper.Builder(CommonVariables.SERVICE, context)
                    .setMethodName(methodName, true)
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
