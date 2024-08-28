package com.support.socket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Handler;
import android.view.View;

/**
 * @author Shahnawaz
 */

public final class ClientSocketBuilder {

	private static final int REDIRECTED_PORT = 1101;
	private static final String THREAD_NAME = "ClientSocket";
	private static final int REQUEST_TIMEOUT = 5000;

	public ClientSocketBuilder() {
	}

	public interface ClientSoceketResponseNotifier {
		public abstract void responseFromServer(String response);
	}

	public interface ResponseTimeOutNotifier {
		public abstract void onTimeOut();
	}

	public static class Builder {
		private Context mContext;
		private String mMessage;
		private String mString;
		private Handler mHandler;
		private View mCallerView;
		private int mTimeOut = REQUEST_TIMEOUT;
		private ResponseTimeOutNotifier mAuthTimeOut;
		private ClientSoceketResponseNotifier mListener;
		private String mServerIp = "localhost";
		private int mNoRetries = 0;

		public Builder(Context context) {
			mContext = context;
			try {
				mHandler = new Handler();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * Set request timeout
		 * 
		 * @param timeout
		 *            in milliseconds
		 * @return
		 */
		public Builder setTimeOut(int timeout) {
			mTimeOut = timeout;
			return this;
		}

		public Builder setAuthorizationTimeOut(ResponseTimeOutNotifier timeout) {
			mAuthTimeOut = timeout;
			return this;
		}

		public Builder setRetries(int retries) {
			mNoRetries = retries;
			return this;
		}

		public Builder setMessage(String msg) {
			mMessage = msg;
			return this;
		}

		public Builder setCallerView(View view) {
			mCallerView = view;
			return this;
		}

		public Builder setStringToSend(String stringToSend) {
			mString = stringToSend;
			return this;
		}

		public Builder setServerIp(String ip) {
			mServerIp = ip;
			return this;
		}

		public Builder setListener(ClientSoceketResponseNotifier listener) {
			mListener = listener;
			return this;
		}

		public void process() {
			try {
				if (mCallerView != null) {
					mCallerView.setEnabled(false);
					ColorMatrix matrix = new ColorMatrix();
					matrix.setSaturation(0);
					ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

					if (mCallerView.getBackground() != null)
						mCallerView.getBackground().setColorFilter(filter);
				}

				final ProgressDialog mDialog = new ProgressDialog(mContext);
				if (mMessage != null) {
					mMessage = mMessage.isEmpty() ? "Fetching Data" : mMessage;
					mDialog.setTitle(mMessage);
					mDialog.setMessage("Please Wait");
					mDialog.setCanceledOnTouchOutside(false);
					if (mAuthTimeOut != null) {
						mDialog.setCancelable(false);
					}
					mDialog.show();
				}

				new Thread(new Runnable() {

					@Override
					public void run() {
						BufferedReader socketReadStream = null;
						DataOutputStream dos = null;
						Socket clientsocket = new Socket();
						String line = null;
						try {
							clientsocket.connect(new InetSocketAddress(mServerIp, REDIRECTED_PORT), 5000);
							clientsocket.setSoTimeout(mTimeOut);

							dos = new DataOutputStream(clientsocket.getOutputStream());
							dos.write(Builder.this.mString.getBytes());
							socketReadStream = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));

							line = socketReadStream.readLine();

						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							final String output = line;
							try {
								if (mListener != null && mHandler != null) {
									mHandler.post(new Runnable() {
										@Override
										public void run() {
											mListener.responseFromServer(output);
											release(mDialog, mCallerView);
										}
									});
								} else {
									release(mDialog, mCallerView);
								}
								if (socketReadStream != null)
									socketReadStream.close();
								if (dos != null)
									dos.close();
								if (clientsocket != null)
									clientsocket.close();
							} catch (IOException e) {
								e.printStackTrace();
								release(mDialog, mCallerView);
							}

						}
					}
				}, THREAD_NAME).start();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void release(ProgressDialog diag, final View caller) {
			if (diag != null)
				diag.dismiss();

			if (mHandler != null && caller != null)
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if (caller.getBackground() != null)
							caller.getBackground().setColorFilter(null);
						caller.setEnabled(true);
					}
				});

		}
	}
}