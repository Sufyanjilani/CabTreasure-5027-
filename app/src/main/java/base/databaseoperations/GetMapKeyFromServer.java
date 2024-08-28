package base.databaseoperations;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class GetMapKeyFromServer extends AsyncTask<Void, Void, String> {

	private static String METHOD_NAME = "GetMapKey";

	private Activity mContext;
	private boolean isFinish = false;
	private ProgressDialog mProgress;

/**
 *  @author: Kumail Raza Lakhani
 *  Date: 09-August-2016
 *  SignIn Signup
 *  Manor Car
 */
	SharedPreferences sp;
	SharedPreferences.Editor edit;
/**
 *  Date: 09-August-2016
 *  END SignIn Signup
 *  Manor Car ->
 */

	public GetMapKeyFromServer(Activity context, boolean isFinish) {
		mContext = context;
		this.isFinish = isFinish;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		try {
			if (!isFinish) {
//				mProgress = new ProgressDialog(mContext);

//				mProgress.setTitle("Please Wait..");
//				mProgress.setMessage("Please Wait..");
//				mProgress.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}// End onPreExecute()

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		DatabaseOperations operations = new DatabaseOperations(new DatabaseHelper(mContext));

		if (result != null && !result.isEmpty()) {

			try {
				JSONObject jsonObject=new JSONObject(result);

				Toast.makeText(mContext, "Application Synced Successfully", Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(mContext, "Error fetching Key", Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(mContext, "Error fetching Key", Toast.LENGTH_SHORT).show();
		}
//if(new DatabaseOperations(new DatabaseHelper(mContext)).getAllVehiclesNames().length>0){
		if (isFinish) {



		}

		if (mProgress != null)
			mProgress.dismiss();

	}// End onPostExecute()

	@Override
	protected String doInBackground(Void... params) {



		return null;
	}// End doInBackground

}