package base.databaseoperations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

import base.models.LocAndField;
import base.utils.SharedPrefrenceHelper;

import com.eurosoft.customerapp.BuildConfig;
import com.eurosoft.customerapp.R;

public class AddressesDatabase {

    private static String DB_NAME = "db.sqllite";
    private SQLiteDatabase db;
    private final Context context;
    private String DB_PATH;

    private static final int COLUMN_ADDRESS = 1;
    private static final int COLUMN_LATITUDE = 3;
    private static final int COLUMN_LONGITUDE = 4;

    public AddressesDatabase(Context context) {
        this.context = context;
        DB_PATH = context.getDir("data", Context.MODE_PRIVATE).getAbsolutePath();
    }

    public void createDataBase() throws IOException {

        if (!checkDataBase()) {
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public float getDistanceInMiles(double lat1, double lng1, double lat2, double lng2) {

        float[] dist = new float[1];
        Location.distanceBetween(lat1, lng1, lat2, lng2, dist);
        return dist[0] * 0.000621371192f;

    }

    /*
     * public List<PostalCodeModel> getPostCodeModel(String PostCode, double FromLAT, double FromLON) { List<PostalCodeModel> pModel = new
     * ArrayList<PostalCodeModel>();
     *
     * String selectQuery = "SELECT address, postcodeNo, latitude, longitude FROM Gen_Addresses where postcodeNo like '" + PostCode + "%'";
     *
     * String myPath = DB_PATH + DB_NAME;
     *
     * db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
     *
     * Cursor cursor = db.rawQuery(selectQuery, null);
     *
     * if (cursor.moveToFirst()) { do { PostalCodeModel Model = new PostalCodeModel();
     *
     * double ToLAT = Double.parseDouble(cursor.getString(COLUMN_LATITUDE)); double ToLON = Double.parseDouble(cursor.getString(COLUMN_LONGITUDE));
     *
     * Float distance = getDistanceInMiles(FromLAT, FromLON, ToLAT, ToLON); if (distance < 0.1) { Model.setAddress(cursor.getString(0));
     * Model.setPostCodeNO(cursor.getString(1)); Model.setlatitude(String.valueOf(ToLAT)); Model.setlongitude(String.valueOf(ToLON));
     * Model.setDistance(distance);
     *
     * pModel.add(Model); }
     *
     * } while (cursor.moveToNext()); }
     *
     * cursor.close();
     *
     * return pModel;
     *
     * }
     */

    private boolean checkDataBase() {
        File dbFile = new File(
                DB_PATH + DB_NAME);
        if (new SharedPrefrenceHelper(context).getFirstRun().equals("2") || new SharedPrefrenceHelper(context).getFirstRun().equals("1") || new SharedPrefrenceHelper(context).getFirstRun().equals("0")) {
            if (dbFile.exists()) {
                dbFile.delete();
            }

            return false;
        } else if (dbFile.exists()) {
            return true;
        }
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {

        InputStream myInput = context.getResources().openRawResource(R.raw.db);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        new SharedPrefrenceHelper(context).putFirstRun();

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public List<LocAndField> getAirports() {

        List<LocAndField> AirportNames = new ArrayList<LocAndField>();

        Cursor cursor = null;
        try {

/**
 *    @author: KUMAIL RAZA LAKHANI
 *	Date: 25-August-2016
 *	Get results in Alphabetical order Select query changed.
 */
            String selectQuery = "SELECT  * FROM  AirportAddresses ORDER BY Id ASC";
//		// OLD CODE
//			String selectQuery = "SELECT  * FROM  AirportAddresses";

/**
 *	Date: 25-August-2016
 *	Get results in Alphabetical order Select query changed. END ->
 */

            String myPath = DB_PATH + DB_NAME;

            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);

            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    LocAndField Model = new LocAndField();

                    Model.setField(cursor.getString(COLUMN_ADDRESS));
                    Model.setLat(cursor.getString(COLUMN_LATITUDE));
                    Model.setLon(cursor.getString(COLUMN_LONGITUDE));

                    AirportNames.add(Model);
                } while (cursor.moveToNext());

            }

            db.close();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return AirportNames;
    }

    public List<LocAndField> getStations() {

        Cursor cursor = null;
        List<LocAndField> StationNames = new ArrayList<LocAndField>();
        try {

/**
 *    @author: KUMAIL RAZA LAKHANI
 *	Date: 25-August-2016
 *	Get results in Alphabetical order Select query changed.
 */
            String selectQuery = "SELECT  * FROM  Stations ORDER BY Id ASC";
//		// OLD CODE
//			String selectQuery = "SELECT  * FROM  Stations";

/**
 *	Date: 25-August-2016
 *	Get results in Alphabetical order Select query changed. END ->
 */
            String myPath = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);

            cursor = db.rawQuery(selectQuery, null);
//db.insert()
            if (cursor.moveToFirst()) {
                do {

                    LocAndField Model = new LocAndField();

                    Model.setField(cursor.getString(COLUMN_ADDRESS));
                    Model.setLat(cursor.getString(COLUMN_LATITUDE));
                    Model.setLon(cursor.getString(COLUMN_LONGITUDE));
                    StationNames.add(Model);
                } while (cursor.moveToNext());

            }

            db.close();
            cursor.close();

        } catch (Exception e) {

            Log.d("aa", e.getMessage());

        }
        return StationNames;
    }

    public void removeStations() {

        Cursor cursor = null;
//		ArrayList<LocAndField> arr = new ArrayList<LocAndField>();

        try {

            String selectQuery = "";
            /*
             * @author: Kumail Raza Lakhani
             * Date: 28-June-2016
             * Query changed for Whiz Cars.
             */

////		OLD CODE
//			selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
//			if(BuildConfig.FLAVOR.equals("whizcars")) {
            selectQuery = "DELETE  FROM  Stations";
//			} else {
//				selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
//			}
            String myPath = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
            cursor = db.rawQuery(selectQuery, null);

//			if (cursor.moveToFirst()) {
//				do {
//
//					LocAndField Model = new LocAndField();
//
//					Model.setField(cursor.getString(COLUMN_ADDRESS));
//					Model.setLat(cursor.getString(COLUMN_LATITUDE));
//					Model.setLon(cursor.getString(COLUMN_LONGITUDE));
//					arr.add(Model);
//				} while (cursor.moveToNext());
//
//			}

            db.close();
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
//		return arr;
    }

    public List<String> getAddresses() {

        Cursor cursor = null;
        List<String> AddressesName = new ArrayList<String>();
        try {

/**
 *    @author: KUMAIL RAZA LAKHANI
 *	Date: 25-August-2016
 *	Get results in Alphabetical order Select query changed.
 */
            String selectQuery = "SELECT  * FROM  Gen_Addresses ORDER BY address COLLATE NOCASE";
//		// OLD CODE
//			String selectQuery = "SELECT  * FROM  Gen_Addresses";

            String myPath = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    AddressesName.add(cursor.getString(COLUMN_ADDRESS));
                } while (cursor.moveToNext());

            }

            db.close();
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return AddressesName;
    }

    //	public void insertStations(String address, String postCode, float lat, float lng) {
//		ContentValues initialValues = new ContentValues();
//		String myPath = DB_PATH + DB_NAME;
//		db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY | SQLiteDatabase.OPEN_READWRITE);
////		db.W
//		initialValues.put(COLUMN_ADDRESS_TEXT, address);
//		initialValues.put(COLUMN_POSTCODE_TEXT, postCode);
//		initialValues.put(COLUMN_LATITUDE_TEXT, lat);
//		initialValues.put(COLUMN_LONGITUDE_TEXT, lng);
//
//
//		db.insert("Stations", null, initialValues);
//		db.close();// Closing database connection
//
//	}
    public List<LocAndField> SearchStations(String keyword) {

        Cursor cursor = null;
        ArrayList<LocAndField> arr = new ArrayList<LocAndField>();

        try {

            String selectQuery = "";
            /*
             * @author: Kumail Raza Lakhani
             * Date: 28-June-2016
             * Query changed for Whiz Cars.
             */

////		OLD CODE
//			selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
//			if(BuildConfig.FLAVOR.equals("whizcars")) {
            selectQuery = "SELECT  * FROM  Stations WHERE address LIKE '" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
//			} else {
//				selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
//			}
            String myPath = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    LocAndField Model = new LocAndField();

                    Model.setField(cursor.getString(COLUMN_ADDRESS));
                    Model.setLat(cursor.getString(COLUMN_LATITUDE));
                    Model.setLon(cursor.getString(COLUMN_LONGITUDE));
                    arr.add(Model);
                } while (cursor.moveToNext());

            }

            db.close();
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

    public List<LocAndField> SearchAirports(String keyword) {

        Cursor cursor = null;
        ArrayList<LocAndField> arr = new ArrayList<LocAndField>();

        try {

            String selectQuery = "";
            /*
             * @author: Kumail Raza Lakhani
             * Date: 28-June-2016
             * Query changed for Whiz Cars.
             */

////		OLD CODE
//			selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
//			if(BuildConfig.FLAVOR.equals("whizcars")) {
            selectQuery = "SELECT  * FROM  AirportAddresses WHERE address LIKE '" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
//			} else {
//				selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
//			}
            String myPath = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    LocAndField Model = new LocAndField();

                    Model.setField(cursor.getString(COLUMN_ADDRESS));
                    Model.setLat(cursor.getString(COLUMN_LATITUDE));
                    Model.setLon(cursor.getString(COLUMN_LONGITUDE));
                    arr.add(Model);
                } while (cursor.moveToNext());

            }

            db.close();
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }
    // **
    // Type 1 for no post code and space
    // Type 2 for five characters without space
    // Type 3 post code and space
    // **

    public List<LocAndField> SearchAddresses(String keyword) {

        Cursor cursor = null;
        ArrayList<LocAndField> arr = new ArrayList<LocAndField>();
        try {

            String selectQuery = "";

            String myPath = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
            selectQuery = "SELECT * FROM Gen_Addresses WHERE REPLACE(postcodeNo,' ','') = '" + keyword.toUpperCase(Locale.US) + "'";

            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.getCount() <= 0) {

////			OLD CODE
//				selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
                if (BuildConfig.FLAVOR.equals("whizcars")) {
                    selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
                } else {
                    selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
                }
                cursor = db.rawQuery(selectQuery, null);
            }


            if (cursor.moveToFirst()) {
                do {
                    LocAndField Model = new LocAndField();
                    Model.setField(cursor.getString(COLUMN_ADDRESS));
                    Model.setLat(cursor.getString(COLUMN_LATITUDE));
                    Model.setLon(cursor.getString(COLUMN_LONGITUDE));
                    arr.add(Model);

                } while (cursor.moveToNext());

            }

            db.close();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return arr;
    }
}
