package com.support.socket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * @author Shahnawaz
 */

public final class ClientSocket {
	public static final HashMap<String, Integer> REQUEST_MAPPING = new HashMap<String, Integer>();

	// TODO: Courier JOB
	// TODO: Ask Sir Danish about implementation of FOJ

	public static final String IO_ERROR = "error";

	// MAIN MENU
	public static final String REQUEST_LOGOUT = "request logout=";
	public static final String REQUEST_AUTH_LOGOUT = "request authorization logout=";
	public static final String REQUEST_DRIVER_STATUS = "request driver status=";
	public static final String REQUEST_ONROAD_JOB = "request onroad job=";

	// ADMIN MENU
	public static final String RQUEST_FARES = "request fares";
	public static final String REQUEST_ARRIVAL_WAITING = "request arrival waiting";
	public static final String REQUEST_SYNC_BIDDING = "request sync Bidding=";
	public static final String REQUEST_ALL_DRIVERS = "request all drivers=";
	public static final String REQUEST_ADMIN_MENU = "request admin menu";

	// BID SCHEMA
	public static final String REQUEST_DRIVER_BID = "driver bid=";
	public static final String REQUEST_BIDDING = "request bidding=";

	// Current Journey
	public static final String REQUEST_SHUTTLE_ACTION = "shuttle action button=";

	// Customer Message
	public static final String SEND_CUSTOMER_SMS = "sms=";

	// Driver Login
	public static final String REQUEST_DRIVERS = "request drivers";
	public static final String REQUEST_VEHICLES = "request vehicles";
	public static final String REQUEST_SHIFT_LOGIN = "request shift login,";
	// Driver Settings
	public static final String REQUEST_VERSION = "request version>>>";

	// Extra Charges
	public static final String SEND_EXTRA_CHARGE = "extra charge=";

	// FOJ
	public static final String REQUEST_FOJ_START_VERIFY = "verify start foj=";
	public static final String REQUSST_FOJ_ACTION = "foj action button=";
	public static final String REQUEST_FOJ_ACTION_SIMPLE = "action button=";

	// Future Job
	public static final String REQUEST_VERIFY_START_PREJOB = "verify start PreJob=";
	public static final String REQUEST_PREJOB_ACTION = "prejob action button=";

	// Group Notification
	public static final String REQUEST_SELECT_AS_DIRECTED = "select as directed=";
	public static final String REQUES_JOB_LATE = "joblate=";
	public static final String REQUEST_PANIC_UNPANIC = "request panic unpanic=";
	public static final String REQUEST_AS_DIRECTED = "request as directed";
	public static final String REQUEST_CHANGE_ADDRESS = "request change address=";
	public static final String REQUEST_AUTHORIZATION = "request auth=";

	// GroupPush Notification
	public static final String REQUES_REJECCT_LOGOUT = "request reject/logout=";

	// Lat Lng Service
	public static final String REQUEST_SENDING_MESSAGE = "request sending message=";

	// Plots
	public static final String REQUEST_PLOTS = "request plots=";

	// Home
	public static final String REQUEST_OFFICE_BASE = "request office base=";
	public static final String REQUEST_ZONE_AND_UPDATES = "request zone and updates=";
	private static final String THREAD_NAME = "ClientSocket";
	private static final int TIMEOUT = 5000;
	private static final int REDIRECTED_SERVER_PORT = 1101;

	static {
		// Start Mapping
		REQUEST_MAPPING.put(REQUEST_ADMIN_MENU, 1001);
	}

	private String mReadString;
	private Socket mClientSocket;
	private String mIpAddress;

	public ClientSocket(String ip) {
		mIpAddress = ip;
	}

	public interface ClientSoceketResponseNotifier {
		public abstract void responseFromServer(String response);
	}

	public static synchronized void process(final Activity context, final String stringToSend, final ClientSoceketResponseNotifier notifier, final String ip) {
		final StringBuilder builder = new StringBuilder();
		try {
			final ProgressDialog mDialog = new ProgressDialog(context);

			mDialog.setTitle("Fetching Data");
			mDialog.setMessage("Please Wait");
			mDialog.show();

			Thread th = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Socket clientsocket = new Socket();

						clientsocket.connect(new InetSocketAddress(ip, REDIRECTED_SERVER_PORT), TIMEOUT);
						clientsocket.setSoTimeout(6000);

						DataOutputStream dos = new DataOutputStream(clientsocket.getOutputStream());
						dos.write(stringToSend.getBytes());

						BufferedReader socketReadStream = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));

						String line = null;
						while ((line = socketReadStream.readLine()) != null) {
							builder.append(line).append("\n");
						}

						socketReadStream.close();
						clientsocket.close();

						context.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (notifier != null) {
									notifier.responseFromServer(builder.toString());
								}
								if (mDialog != null)
									mDialog.dismiss();
							}
						});
					} catch (IOException e) {
						e.printStackTrace();
						builder.setLength(0);
						builder.append(IO_ERROR);

						context.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (notifier != null)
									notifier.responseFromServer("Error connecting to server, Please check your internet connection");
								if (mDialog != null)
									mDialog.dismiss();
							}
						});
					}
				}
			}, THREAD_NAME);
			th.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized String process(final String stringToSend, final String ip) {
		final StringBuilder builder = new StringBuilder();
		Thread th = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Socket clientsocket = new Socket();

					clientsocket.connect(new InetSocketAddress(ip, REDIRECTED_SERVER_PORT), TIMEOUT);
					clientsocket.setSoTimeout(6000);

					DataOutputStream dos = new DataOutputStream(clientsocket.getOutputStream());
					dos.write(stringToSend.getBytes());

					BufferedReader socketReadStream = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));

					String line = null;
					while ((line = socketReadStream.readLine()) != null) {
						builder.append(line).append("\n");
					}

					socketReadStream.close();
					clientsocket.close();
				} catch (IOException e) {
					e.printStackTrace();
					builder.setLength(0);
					builder.append(IO_ERROR);
				}
			}
		}, THREAD_NAME);
		th.start();
		try {
			th.join(TIMEOUT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (builder.length() > 0)
			return builder.toString();
		return null;
	}

	public static synchronized String processSameThread(final String stringToSend, final String ip) {
		String ret = null;
		try {
			Socket clientsocket = new Socket();

			clientsocket.connect(new InetSocketAddress(ip, REDIRECTED_SERVER_PORT), TIMEOUT);
			clientsocket.setSoTimeout(6000);

			DataOutputStream dos = new DataOutputStream(clientsocket.getOutputStream());
			dos.write(stringToSend.getBytes());

			BufferedReader socketReadStream = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));

			ret = socketReadStream.readLine();

			socketReadStream.close();
			clientsocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public synchronized void connect(final int soTimeout) {
		Thread th = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					mClientSocket = new Socket();
					mClientSocket.connect(new InetSocketAddress(mIpAddress, REDIRECTED_SERVER_PORT), TIMEOUT);
					mClientSocket.setSoTimeout(soTimeout);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, THREAD_NAME);

		th.start();
		try {
			th.join(TIMEOUT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public synchronized String read() {
		mReadString = "";

		Thread th = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
					mReadString = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, THREAD_NAME);

		th.start();
		try {
			th.join(TIMEOUT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return mReadString;
	}

	public synchronized void write(final String stringToSend) {
		Thread th = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					DataOutputStream dos = new DataOutputStream(mClientSocket.getOutputStream());
					dos.write(stringToSend.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, THREAD_NAME);

		th.start();

		try {
			th.join(TIMEOUT);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public synchronized void close() {
		try {
			if (mClientSocket != null)
				mClientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			mClientSocket.close();
		} finally {
			super.finalize();
		}
	}

}