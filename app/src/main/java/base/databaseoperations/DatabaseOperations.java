package base.databaseoperations;

import static base.databaseoperations.DatabaseHelper.BOOKING_TOLOCTYPE;
import static base.databaseoperations.DatabaseHelper.COLUMN_ADDRESS_TEXT;
import static base.databaseoperations.DatabaseHelper.COLUMN_LATITUDE_TEXT;
import static base.databaseoperations.DatabaseHelper.COLUMN_LONGITUDE_TEXT;
import static base.databaseoperations.DatabaseHelper.COLUMN_POSTCODE_TEXT;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import base.models.LocAndField;
import base.models.Model_BookingDetailsModel;
import base.models.PromoModel;
import base.models.UserModel;
import base.models.VehicleModel;
import base.utils.CommonVariables;


public class DatabaseOperations {

    public DatabaseHelper mDatabaseHelper;
    public SQLiteDatabase mSQLiteDatabase;
    public static final String COL_USERNAME = "name";
    public static final String COL_PASSWORD = "password";

    public DatabaseOperations(DatabaseHelper mDHB) {

        this.mDatabaseHelper = mDHB;
    }

    boolean FirstRun = true;

    /**
     * @author: Kumail Raza Lakhani
     * Date: 09-August-2016
     * SignIn SignUp
     * Manor Cars
     */
    @SuppressLint("Range")
    public List<UserModel> getUsers() {
        List<UserModel> vehicles = new ArrayList<UserModel>();
        String selectQuery = "SELECT  * FROM "
                + DatabaseHelper.USERACCOUNT_TABEL;
        // SELECT * FROM Booking where Status IN ('Cancelled','Completed') AND
        // bookinguserid like 'fasiha%'ORDER BY Refno DESC
        mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                UserModel vehicleModel = new UserModel();
                vehicleModel.setmobileno(cursor.getString(cursor
                        .getColumnIndex(DatabaseHelper.USERACCOUNT_MOBILENO)));
                vehicleModel.setip(cursor.getString(cursor
                        .getColumnIndex(DatabaseHelper.USERACCOUNT_IP)));
                vehicleModel.setname(cursor.getString(cursor
                        .getColumnIndex(DatabaseHelper.USERACCOUNT_NAME)));
                vehicleModel.setEmail(cursor.getString(cursor
                        .getColumnIndex(DatabaseHelper.USERACCOUNT_EMAIL)));
                vehicles.add(vehicleModel);

            } while (cursor.moveToNext());
        }

        cursor.close();
        mSQLiteDatabase.close();

        return vehicles;

    }

    @SuppressLint("Range")
    public List<UserModel> loginUser(String email, String pass) {

        //	pass="'"+pass+"'";
        List<UserModel> vehicles = new ArrayList<UserModel>();
        String selectQuery = "SELECT  * FROM "
                + DatabaseHelper.USERACCOUNT_TABEL + " where " + DatabaseHelper.USERACCOUNT_NAME + "='" + email + "' AND " + DatabaseHelper.USERACCOUNT_PASSWORD + "='" + pass + "'";
        Log.e("TAG", "query is " + selectQuery);
        // / SELECT * FROM Booking where Status IN ('Cancelled','Completed') AND
        // bookinguserid like 'fasiha%'ORDER BY Refno DESC
        mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                UserModel vehicleModel = new UserModel();
                vehicleModel.setmobileno(cursor.getString(cursor
                        .getColumnIndex(DatabaseHelper.USERACCOUNT_MOBILENO)));
                vehicleModel.setip(cursor.getString(cursor
                        .getColumnIndex(DatabaseHelper.USERACCOUNT_IP)));
                vehicleModel.setname(cursor.getString(cursor
                        .getColumnIndex(DatabaseHelper.USERACCOUNT_NAME)));
                vehicleModel.setEmail(cursor.getString(cursor
                        .getColumnIndex(DatabaseHelper.USERACCOUNT_EMAIL)));

                vehicles.add(vehicleModel);

            } while (cursor.moveToNext());
        }

        cursor.close();
        mSQLiteDatabase.close();

        return vehicles;

    }

//    public boolean Login(String username) throws SQLException {
//
//        try {
//            mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
//
//            Cursor mCursor = mSQLiteDatabase.rawQuery(
//                    "SELECT * FROM  " + DatabaseHelper.USERACCOUNT_TABEL
//                            + " WHERE name=? ", new String[]{username}
//            );
//
//
//            if (mCursor != null) {
//                if (mCursor.getCount() > 0) {
//                    return true;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }

    /**
     * Date: 09-August-2016
     * END SignIn SignUp
     * Manor Cars ->
     */


    public void insertVehicle(int Id, String Name, String totalPassengers, String totalHandLuggages, String totalLuggages, String sortOrderNo) {
        ContentValues initialValues = new ContentValues();
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();

        initialValues.put(DatabaseHelper.VEHICLE_COLUMN_NAME, Name);
        initialValues.put(DatabaseHelper.VEHICLE_COLUMN_TOTALPASSENGERS, totalPassengers);
        initialValues.put(DatabaseHelper.VEHICLE_COLUMN_TOTALHANDLUGGAGES, totalHandLuggages);
        initialValues.put(DatabaseHelper.VEHICLE_COLUMN_TOTALLUGGAGES, totalLuggages);
        initialValues.put(DatabaseHelper.VEHICLE_COLUMN_SORTORDERNO, sortOrderNo);
        initialValues.put(DatabaseHelper.VEHICLE_COLUMN_SERVER_ID, Id);

        mSQLiteDatabase.insert(DatabaseHelper.VEHICLE_TABLE_NAME, null, initialValues);
        mSQLiteDatabase.close();// Closing database connection

    }// End insertVehicel()


//    public void insertAirports(List<LocAndField> locAndFields) {
//        ContentValues initialValues = new ContentValues();
////		String myPath = DB_PATH + DB_NAME;
//        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
////		db.W
//
////        mSQLiteDatabase.close();// Closing database connection
//        mSQLiteDatabase.beginTransaction();
//        try {
//
//            for (LocAndField locAndField : locAndFields) {
//
//                initialValues.put(COLUMN_ADDRESS_TEXT, locAndField.getField());
//                try {
//                    initialValues.put(COLUMN_POSTCODE_TEXT, getPostalCodeFromAddress(locAndField.getField()));
//                } catch (Exception e) {
//                    initialValues.put(COLUMN_POSTCODE_TEXT, "");
//                }
//                initialValues.put(COLUMN_LATITUDE_TEXT, locAndField.getLat());
//                initialValues.put(COLUMN_LONGITUDE_TEXT, locAndField.getLon());
//
//
//                mSQLiteDatabase.insert("Airports", null, initialValues);
//            }
//            mSQLiteDatabase.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            mSQLiteDatabase.endTransaction();
//        }
//    }

//    public void insertStations(List<LocAndField> locAndFields) {
//        ContentValues initialValues = new ContentValues();
////		String myPath = DB_PATH + DB_NAME;
//        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
////		db.W
//
////        mSQLiteDatabase.close();// Closing database connection
//        mSQLiteDatabase.beginTransaction();
//        try {
//
//            for (LocAndField locAndField : locAndFields) {
//
//                initialValues.put(COLUMN_ADDRESS_TEXT, locAndField.getField());
//                try {
//                    initialValues.put(COLUMN_POSTCODE_TEXT, getPostalCodeFromAddress(locAndField.getField()));
//                } catch (Exception e) {
//                    initialValues.put(COLUMN_POSTCODE_TEXT, "");
//                }
//                initialValues.put(COLUMN_LATITUDE_TEXT, locAndField.getLat());
//                initialValues.put(COLUMN_LONGITUDE_TEXT, locAndField.getLon());
//
//
//                mSQLiteDatabase.insert("Stations", null, initialValues);
//            }
//            mSQLiteDatabase.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            mSQLiteDatabase.endTransaction();
//        }
//    }

    public void insertRecentLocation(LocAndField locAndField) {
        try {
            ContentValues initialValues = new ContentValues();
//		String myPath = DB_PATH + DB_NAME;
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//		db.W

//        mSQLiteDatabase.close();// Closing database connection

            try {


                initialValues.put(COLUMN_ADDRESS_TEXT, locAndField.getField());
                try {
                    initialValues.put(COLUMN_POSTCODE_TEXT, getPostalCodeFromAddress(locAndField.getField()));
                } catch (Exception e) {
                    initialValues.put(COLUMN_POSTCODE_TEXT, "");
                }
                initialValues.put(COLUMN_LATITUDE_TEXT, locAndField.getLat());
                initialValues.put(COLUMN_LONGITUDE_TEXT, locAndField.getLon());
                initialValues.put(BOOKING_TOLOCTYPE, locAndField.getLocationType());


                try {
//                    mSQLiteDatabase.insert("RecentTable", null, initialValues);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPostalCodeFromAddress(String address) throws IOException {
        String[] split = address.split(" ");
        String result = "";
        for (int i = split.length - 1; i >= 0; i--) {
            result += (split[i] + " ");
        }
        String reverseAddress = result.trim();
        //  System.out.println(result.trim());
        String[] arr = reverseAddress.split(" ");

        String PostalCode = arr[1] + " " + arr[0];
        if (isAlphaNumeric(PostalCode)) {
            return PostalCode;
        } else {
            return "";
        }
    }

    private boolean isNumericValue(String address) {

        boolean found = false;
        Pattern p = Pattern.compile("^[a-zA-Z0-9]*$");//<-- compile( not Compile(
        Matcher m = p.matcher(address.toLowerCase());  //<-- matcher( not Matcher
        if (m.find()) {  //<-- m not matcher

            found = true;
            return found;
        } else {
            return found;
        }
    }

    public boolean isAlphaNumeric(String postCode) {
//boolean islength=false;
        boolean[] validcode = {false, false};

        if (postCode != null) {
            String[] text = postCode.split(" ");
            for (int j = 0; j < text.length; j++) {
                if (text[j].length() > 4) {
                    return false;
                } else {
//                for (int i = 0; i < text[j].length(); ++i) {
//                    char c = text[j].charAt(i);
//                    if (Character.isDigit(c)) {
//                        numeric = true;
//                    } else if (Character.isLetter(c)) {
//                        alpha = true;
//                    } else {
//                        accepted = false;
//                        break;
//                    }
//                }
                    validcode[j] = isNumericValue(text[j]);// Then it is correct


                }
            }
            return validcode[0] && validcode[1];// Then it is correct
        } else {
            return false;
        }
    }

//    public void insertStores(String address, String postCode, float lat, float lng) {
//        ContentValues initialValues = new ContentValues();
////		String myPath = DB_PATH + DB_NAME;
//        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
////		db.W
//        initialValues.put(COLUMN_ADDRESS_TEXT, address);
//        initialValues.put(COLUMN_POSTCODE_TEXT, postCode);
//        initialValues.put(COLUMN_LATITUDE_TEXT, lat);
//        initialValues.put(COLUMN_LONGITUDE_TEXT, lng);
//
//
//        mSQLiteDatabase.insert("supermarkets", null, initialValues);
//        mSQLiteDatabase.close();// Closing database connection
//
//    }

//    public List<LocAndField> SearchStations(String keyword) {
//
//        Cursor cursor = null;
//        ArrayList<LocAndField> arr = new ArrayList<LocAndField>();
//
//        try {
//
//            String selectQuery = "";
//            /*
//             * @author: Kumail Raza Lakhani
//             * Date: 28-June-2016
//             * Query changed for Whiz Cars.
//             */
//
//////		OLD CODE
////			selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
////			if(BuildConfig.FLAVOR.equals("whizcars")) {
////			selectQuery = "SELECT  * FROM  Stations WHERE address LIKE '" + keyword + "' LIMIT 50";
////			} else {
////				selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
////			}
//            selectQuery = "SELECT  * FROM  Stations WHERE address LIKE '" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
//            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//            cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
//
//            if (cursor.moveToFirst()) {
//                do {
//
//                    LocAndField Model = new LocAndField();
//
//                    Model.setField(cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS_TEXT)));
//                    Model.setLat(cursor.getString(cursor.getColumnIndex(COLUMN_LATITUDE_TEXT)));
//                    Model.setLon(cursor.getString(cursor.getColumnIndex(COLUMN_LONGITUDE_TEXT)));
//                    arr.add(Model);
//                } while (cursor.moveToNext());
//
//            }
//
//            mSQLiteDatabase.close();
//            cursor.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return arr;
//    }

//    public List<LocAndField> SearchAirports(String keyword) {
//
//        Cursor cursor = null;
//        ArrayList<LocAndField> arr = new ArrayList<LocAndField>();
//
//        try {
//
//            String selectQuery = "";
//            /*
//             * @author: Kumail Raza Lakhani
//             * Date: 28-June-2016
//             * Query changed for Whiz Cars.
//             */
//
//////		OLD CODE
////			selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
////			if(BuildConfig.FLAVOR.equals("whizcars")) {
////
////			} else {
////				selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
////			}
//            selectQuery = "SELECT  * FROM  Airports WHERE address LIKE '" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
//            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//            cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
//
//            if (cursor.moveToFirst()) {
//                do {
//
//                    LocAndField Model = new LocAndField();
//
//                    Model.setField(cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS_TEXT)));
//                    Model.setLat(cursor.getString(cursor.getColumnIndex(COLUMN_LATITUDE_TEXT)));
//                    Model.setLon(cursor.getString(cursor.getColumnIndex(COLUMN_LONGITUDE_TEXT)));
//                    arr.add(Model);
//                } while (cursor.moveToNext());
//
//            }
//
//            mSQLiteDatabase.close();
//            cursor.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return arr;
//    }

    @SuppressLint("Range")
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
//			String myPath = DB_PATH + DB_NAME;
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();

            cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
//db.insert()
            if (cursor.moveToFirst()) {
                do {

                    LocAndField Model = new LocAndField();

                    Model.setField(cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS_TEXT)));
                    Model.setLat(cursor.getString(cursor.getColumnIndex(COLUMN_LATITUDE_TEXT)));
                    Model.setLon(cursor.getString(cursor.getColumnIndex(COLUMN_LONGITUDE_TEXT)));
                    StationNames.add(Model);
                } while (cursor.moveToNext());

            }

            mSQLiteDatabase.close();
            cursor.close();

        } catch (Exception e) {

            Log.d("aa", e.getMessage());

        }
        return StationNames;
    }

    @SuppressLint("Range")
    public List<LocAndField> getRecentLocation() {

        Cursor cursor = null;
        List<LocAndField> StationNames = new ArrayList<LocAndField>();
        try {

/**
 *    @author: KUMAIL RAZA LAKHANI
 *	Date: 25-August-2016
 *	Get results in Alphabetical order Select query changed.
 */
            String selectQuery = "SELECT  * FROM  RecentTable ORDER BY Id ASC";
//		// OLD CODE
//			String selectQuery = "SELECT  * FROM  Stations";

/**
 *	Date: 25-August-2016
 *	Get results in Alphabetical order Select query changed. END ->
 */
//			String myPath = DB_PATH + DB_NAME;
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();

            cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
//db.insert()
            if (cursor.moveToFirst()) {
                do {

                    LocAndField Model = new LocAndField();

                    Model.setField(cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS_TEXT)));
                    Model.setLat(cursor.getString(cursor.getColumnIndex(COLUMN_LATITUDE_TEXT)));
                    Model.setLon(cursor.getString(cursor.getColumnIndex(COLUMN_LONGITUDE_TEXT)));
                    Model.setLocationType(cursor.getString(cursor.getColumnIndex(BOOKING_TOLOCTYPE)));
                    StationNames.add(Model);
                } while (cursor.moveToNext());

            }

            mSQLiteDatabase.close();
            cursor.close();

        } catch (Exception e) {

//            Log.d("aa", e.getMessage());

        }
        return StationNames;
    }

//    public List<LocAndField> getAirports() {
//
//        Cursor cursor = null;
//        List<LocAndField> StationNames = new ArrayList<LocAndField>();
//        try {
//
///**
// *    @author: KUMAIL RAZA LAKHANI
// *	Date: 25-August-2016
// *	Get results in Alphabetical order Select query changed.
// */
//            String selectQuery = "SELECT  * FROM  Airports ORDER BY Id ASC";
////		// OLD CODE
////
//
///**
// *	Date: 25-August-2016
// *	Get results in Alphabetical order Select query changed. END ->
// */
////			String myPath = DB_PATH + DB_NAME;
//            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//
//            cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
////db.insert()
//            if (cursor.moveToFirst()) {
//                do {
//
//                    LocAndField Model = new LocAndField();
//
//                    Model.setField(cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS_TEXT)));
//                    Model.setLat(cursor.getString(cursor.getColumnIndex(COLUMN_LATITUDE_TEXT)));
//                    Model.setLon(cursor.getString(cursor.getColumnIndex(COLUMN_LONGITUDE_TEXT)));
//                    StationNames.add(Model);
//                } while (cursor.moveToNext());
//
//            }
//
//            mSQLiteDatabase.close();
//            cursor.close();
//
//        } catch (Exception e) {
//
//            Log.d("aa", e.getMessage());
//
//        }
//        return StationNames;
//    }

//    public void removeRecentLocations() {
//
//        Cursor cursor = null;
////		ArrayList<LocAndField> arr = new ArrayList<LocAndField>();
//
//        try {
//
////			String selectQuery = "";
//            /*
//             * @author: Kumail Raza Lakhani
//             * Date: 28-June-2016
//             * Query changed for Whiz Cars.
//             */
//
//////		OLD CODE
////			selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
////			if(BuildConfig.FLAVOR.equals("whizcars")) {
////			selectQuery = "DELETE  FROM  Stations";
////			} else {
////				selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
////			}
////			String myPath = DB_PATH + DB_NAME;
//            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//            mSQLiteDatabase.delete("RecentTable", null, null);
////			cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
//
////			if (cursor.moveToFirst()) {
////				do {
////
////					LocAndField Model = new LocAndField();
////
////					Model.setField(cursor.getString(COLUMN_ADDRESS));
////					Model.setLat(cursor.getString(COLUMN_LATITUDE));
////					Model.setLon(cursor.getString(COLUMN_LONGITUDE));
////					arr.add(Model);
////				} while (cursor.moveToNext());
////
////			}
//
//            mSQLiteDatabase.close();
////			cursor.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////		return arr;
//    }

//    public void removeStations() {
//
//        Cursor cursor = null;
////		ArrayList<LocAndField> arr = new ArrayList<LocAndField>();
//
//        try {
//
////			String selectQuery = "";
//            /*
//             * @author: Kumail Raza Lakhani
//             * Date: 28-June-2016
//             * Query changed for Whiz Cars.
//             */
//
//////		OLD CODE
////			selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
////			if(BuildConfig.FLAVOR.equals("whizcars")) {
////			selectQuery = "DELETE  FROM  Stations";
////			} else {
////				selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
////			}
////			String myPath = DB_PATH + DB_NAME;
//            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//            mSQLiteDatabase.delete("Stations", null, null);
////			cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
//
////			if (cursor.moveToFirst()) {
////				do {
////
////					LocAndField Model = new LocAndField();
////
////					Model.setField(cursor.getString(COLUMN_ADDRESS));
////					Model.setLat(cursor.getString(COLUMN_LATITUDE));
////					Model.setLon(cursor.getString(COLUMN_LONGITUDE));
////					arr.add(Model);
////				} while (cursor.moveToNext());
////
////			}
//
//            mSQLiteDatabase.close();
////			cursor.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////		return arr;
//    }

//    public void removeAirports() {
//
//        Cursor cursor = null;
////		ArrayList<LocAndField> arr = new ArrayList<LocAndField>();
//
//        try {
//
////			String selectQuery = "";
//            /*
//             * @author: Kumail Raza Lakhani
//             * Date: 28-June-2016
//             * Query changed for Whiz Cars.
//             */
//
//////		OLD CODE
////			selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
////
////			} else {
////				selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
////			}
////			String myPath = DB_PATH + DB_NAME;
//            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//            mSQLiteDatabase.delete("Airports", null, null);
////			cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
//
////			if (cursor.moveToFirst()) {
////				do {
////
////					LocAndField Model = new LocAndField();
////
////					Model.setField(cursor.getString(COLUMN_ADDRESS));
////					Model.setLat(cursor.getString(COLUMN_LATITUDE));
////					Model.setLon(cursor.getString(COLUMN_LONGITUDE));
////					arr.add(Model);
////				} while (cursor.moveToNext());
////
////			}
//
//            mSQLiteDatabase.close();
////			cursor.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////		return arr;
//    }

//    public List<LocAndField> SearchStops(String keyword) {
//
//        Cursor cursor = null;
//        ArrayList<LocAndField> arr = new ArrayList<LocAndField>();
//
//        try {
//
//            String selectQuery = "";
//            /*
//             * @author: Kumail Raza Lakhani
//             * Date: 28-June-2016
//             * Query changed for Whiz Cars.
//             */
//
//////		OLD CODE
////			selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
////			if(BuildConfig.FLAVOR.equals("whizcars")) {
////			selectQuery = "SELECT  * FROM  Stations WHERE address LIKE '" + keyword + "' LIMIT 50";
////			} else {
////				selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
////			}
//            selectQuery = "SELECT  * FROM  supermarkets WHERE address LIKE '" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
//            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//            cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
//
//            if (cursor.moveToFirst()) {
//                do {
//
//                    LocAndField Model = new LocAndField();
//
//                    Model.setField(cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS_TEXT)));
//                    Model.setLat(cursor.getString(cursor.getColumnIndex(COLUMN_LATITUDE_TEXT)));
//                    Model.setLon(cursor.getString(cursor.getColumnIndex(COLUMN_LONGITUDE_TEXT)));
//                    arr.add(Model);
//                } while (cursor.moveToNext());
//
//            }
//
//            mSQLiteDatabase.close();
//            cursor.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return arr;
//    }

    @SuppressLint("Range")
    public List<LocAndField> getStores() {

        Cursor cursor = null;
        List<LocAndField> StationNames = new ArrayList<LocAndField>();
        try {

/**
 *    @author: KUMAIL RAZA LAKHANI
 *	Date: 25-August-2016
 *	Get results in Alphabetical order Select query changed.
 */
            String selectQuery = "SELECT  * FROM  supermarkets ORDER BY Id ASC";
//		// OLD CODE
//			String selectQuery = "SELECT  * FROM  Stations";

/**
 *	Date: 25-August-2016
 *	Get results in Alphabetical order Select query changed. END ->
 */
//			String myPath = DB_PATH + DB_NAME;
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();

            cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
//db.insert()
            if (cursor.moveToFirst()) {
                do {

                    LocAndField Model = new LocAndField();
                    Model.setField(cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS_TEXT)));
                    Model.setLat(cursor.getString(cursor.getColumnIndex(COLUMN_LATITUDE_TEXT)));
                    Model.setLon(cursor.getString(cursor.getColumnIndex(COLUMN_LONGITUDE_TEXT)));
                    StationNames.add(Model);
                } while (cursor.moveToNext());
            }

            mSQLiteDatabase.close();
            cursor.close();

        } catch (Exception e) {
            Log.d("aa", e.getMessage());
        }
        return StationNames;
    }

//    public void removeStores() {
//
//        Cursor cursor = null;
////		ArrayList<LocAndField> arr = new ArrayList<LocAndField>();
//
//        try {
//
////			String selectQuery = "";
//            /*
//             * @author: Kumail Raza Lakhani
//             * Date: 28-June-2016
//             * Query changed for Whiz Cars.
//             */
//
//////		OLD CODE
////			selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
////			if(BuildConfig.FLAVOR.equals("whizcars")) {
////			selectQuery = "DELETE  FROM  Stations";
////			} else {
////				selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
////			}
////			String myPath = DB_PATH + DB_NAME;
//            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//            mSQLiteDatabase.delete("supermarkets", null, null);
////			cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
//
////			if (cursor.moveToFirst()) {
////				do {
////
////					LocAndField Model = new LocAndField();
////
////					Model.setField(cursor.getString(COLUMN_ADDRESS));
////					Model.setLat(cursor.getString(COLUMN_LATITUDE));
////					Model.setLon(cursor.getString(COLUMN_LONGITUDE));
////					arr.add(Model);
////				} while (cursor.moveToNext());
////
////			}
//
//            mSQLiteDatabase.close();
////			cursor.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////		return arr;
//    }

//    public void insertInfo(String PhoneNumber, String EmailAddress, String Website, String UrlAddress) {
//        ContentValues initialValues = new ContentValues();
//        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//
//        initialValues.put(DatabaseHelper.PHONE_NUMBER, PhoneNumber);
//        initialValues.put(DatabaseHelper.EMAIL_ADDRESS, EmailAddress);
//        initialValues.put(DatabaseHelper.WEBSITE, Website);
//        initialValues.put(DatabaseHelper.URLADDRESS, UrlAddress);
//
//        mSQLiteDatabase.insert(DatabaseHelper.INFO_TABLE_NAME, null, initialValues);
//        mSQLiteDatabase.close();// Closing database connection
//
//    }// End insertVehicel()

    public static final String USERS_TABLE_NAME = "UsersTable";
    public static final String USER_COLUMN_ID = "id";
    public static final String USER_COLUMN_NAME = "Name";
    public static final String USER_COLUMN_PASSWORD = "Password";
    public static final String USER_COLUMN_VERIFYPASSWORD = "VerifyPassword";
    public static final String USER_COLUMN_EMAIL = "Email";
    public static final String USER_COLUMN_PHONE_NUMBER = "PhoneNumber";

//    public void insertUsers(String Name, String Password, String VerifyPassword,
//                            String Email, String PhoneNumber) {
//
//        try {
//            ContentValues initialValues = new ContentValues();
//            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//
//            initialValues.put(DatabaseHelper.USER_COLUMN_NAME, Name);
//            initialValues.put(DatabaseHelper.USER_COLUMN_PASSWORD, Password);
//            initialValues.put(DatabaseHelper.USER_COLUMN_VERIFYPASSWORD, VerifyPassword);
//            initialValues.put(DatabaseHelper.USER_COLUMN_EMAIL, Email);
//            initialValues.put(DatabaseHelper.USER_COLUMN_PHONE_NUMBER, PhoneNumber);
//
//            mSQLiteDatabase.insert(DatabaseHelper.USERS_TABLE_NAME, null, initialValues);
//
//            Log.i("check1", "check1");
//
//            mSQLiteDatabase.close();// Closing database connection
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//
//        }
//
//    }// End insertVehicel()

//    public boolean Login(String username, String password) throws SQLException {
//
//        try {
//            mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
//
//            Cursor mCursor = mSQLiteDatabase.rawQuery("SELECT * FROM  " + DatabaseHelper.USERS_TABLE_NAME + " WHERE name=? AND password=?", new String[]{
//                    username, password});
//
//            if (mCursor != null) {
//                if (mCursor.getCount() > 0) {
//                    return true;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }

    public void INSERTAccount(String Name, String isverify, String code, String ip,
                              String email, String mobileno, String password) {
        ContentValues initialValues = new ContentValues();
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();

        initialValues.put(DatabaseHelper.USERACCOUNT_NAME, Name);
        initialValues.put(DatabaseHelper.USERACCOUNT_ISVerify, isverify);
        initialValues.put(DatabaseHelper.USERACCOUNT_CODE, code);
        initialValues.put(DatabaseHelper.USERACCOUNT_IP, ip);
        initialValues.put(DatabaseHelper.USERACCOUNT_EMAIL, email);
        initialValues.put(DatabaseHelper.USERACCOUNT_MOBILENO, mobileno);
        initialValues.put(DatabaseHelper.USERACCOUNT_PASSWORD, password);

        mSQLiteDatabase.insert(DatabaseHelper.USERACCOUNT_TABEL, null, initialValues);

        mSQLiteDatabase.close();
    }

//    public String getPhoneNoByUser(String username, String pwd) {
//
//        String phoneNo = "";
//        String selectQuery = "SELECT  * FROM " +
//                DatabaseHelper.USERACCOUNT_TABEL + " where " + DatabaseHelper.USERACCOUNT_NAME + "='" + username + "'";
//        mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
//        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
//        if (cursor.moveToFirst()) {
//            do {
//
//                phoneNo = cursor.getString(
//                        cursor.getColumnIndex(DatabaseHelper.USERACCOUNT_MOBILENO)
//                );
//
//                break;
//
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        mSQLiteDatabase.close();
//
//        return phoneNo;
//
//    }

//    public boolean geUserExst(String username) {
//
//        boolean exist = false;
//        String selectQuery = "SELECT  * FROM " + DatabaseHelper.USERACCOUNT_TABEL + " where " + DatabaseHelper.USERACCOUNT_NAME + "='" + username + "'";
//        mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
//        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
//        if (cursor.moveToFirst()) {
//            do {
//
//                exist = true;
//
//                break;
//
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        mSQLiteDatabase.close();
//
//        return exist;
//
//    }

//    public void deleteUsers() {
//
//        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//        mSQLiteDatabase.delete(DatabaseHelper.USERS_TABLE_NAME, null, null);
//        mSQLiteDatabase.close();
//    }// E

    public void deleteVehicle() {

        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        mSQLiteDatabase.delete(DatabaseHelper.VEHICLE_TABLE_NAME, null, null);
        mSQLiteDatabase.close();
    }// End deleteVehicle(...) method

    public int getvehiclesCount() {
        String query = "SELECT  * FROM " + DatabaseHelper.VEHICLE_TABLE_NAME;

        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = mSQLiteDatabase.rawQuery(query, null);

        int count = cursor.getCount();
        cursor.close();

        mSQLiteDatabase.close();
        return count;
    }

    public List<VehicleModel> getAllVehicles() {
        List<VehicleModel> vehicles = new ArrayList<VehicleModel>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + DatabaseHelper.VEHICLE_TABLE_NAME;

        mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                VehicleModel vehicleModel = new VehicleModel();
                try {
                    vehicleModel.setId(cursor.getInt(0));
                }catch (Exception ex){}
                //
                try {
                vehicleModel.setName(cursor.getString(1)); //
            }catch (Exception ex){}
                try {
                vehicleModel.setTotalPassengers(cursor.getString(2));
        }catch (Exception ex){}
                try {
                vehicleModel.setTotalHandLuggages(cursor.getString(3));
    }catch (Exception ex){}
                try {
                vehicleModel.setTotalLugages(cursor.getString(4));
}catch (Exception ex){}
                try {
                vehicleModel.setSortOderNo(Integer.parseInt(cursor.getString(5)));
        }catch (Exception ex){}
                vehicles.add(vehicleModel);

            } while (cursor.moveToNext());
        }

        cursor.close();
        mSQLiteDatabase.close();

        return vehicles;

    }// End getAllVehicles()

    public VehicleModel getVehicleByName(String CarName) {
        VehicleModel vehicles = new VehicleModel();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + DatabaseHelper.VEHICLE_TABLE_NAME + " where Name = '" + CarName + "'";

        mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
        VehicleModel vehicleModel = new VehicleModel();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                vehicleModel.setName(cursor.getString(1)); //
                vehicleModel.setTotalPassengers(cursor.getString(2));
                vehicleModel.setTotalHandLuggages(cursor.getString(3));
                vehicleModel.setTotalLugages(cursor.getString(4));
                vehicleModel.setSortOderNo(Integer.parseInt(cursor.getString(5)));


            } while (cursor.moveToNext());
        }

        cursor.close();
        mSQLiteDatabase.close();

        return vehicleModel;

    }

    public String[] getAllVehiclesNames() {
        int i = 0;
        String[] vehicles = new String[]{};

        // Select All Query
        String selectQuery = "SELECT  * FROM " + DatabaseHelper.VEHICLE_TABLE_NAME;

        mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        ArrayList<String> tempList = new ArrayList<>();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

//				VehicleModel vehicleModel = new VehicleModel();
//				vehicleModel.setName(cursor.getString(1)); //
//				vehicleModel.setTotalPassengers(cursor.getString(2));
//				vehicleModel.setTotalHandLuggages(cursor.getString(3));
//				vehicleModel.setTotalLugages(cursor.getString(4));
//				vehicleModel.setSortOderNo(Integer.parseInt(cursor.getString(5)));
//				vehicles[i]=cursor.getString(1);
                tempList.add(cursor.getString(1));


            } while (cursor.moveToNext());
            vehicles = tempList.toArray(vehicles);
        }

        cursor.close();
        mSQLiteDatabase.close();

        return vehicles;

    }

    public List<String> getInfo() {
        List<String> infodata = new ArrayList<String>();

        try {
            String selectQuery = "SELECT  * FROM " + DatabaseHelper.INFO_TABLE_NAME;

            mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
            Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    infodata.add(cursor.getString(1));
                    infodata.add(cursor.getString(2));
                    infodata.add(cursor.getString(3));
                    infodata.add(cursor.getString(4));

                } while (cursor.moveToNext());
            }

            cursor.close();
            mSQLiteDatabase.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return infodata;

    }

//    public void insertFavAdress(final String Address, final String DoorNo, final String Lat, final String Lon, String userId) {
//
//        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//        ContentValues initialValues = new ContentValues();
//
//        initialValues.put(DatabaseHelper.FAVOURITES_ADDRESS_ADDRESS, Address);
//        initialValues.put(DatabaseHelper.FAVOURITES_ADDRESS_DOORNO, DoorNo);
//        initialValues.put(DatabaseHelper.FAVOURITES_ADDRESS_LAT, Lat);
//        initialValues.put(DatabaseHelper.FAVOURITES_ADDRESS_LON, Lon);
//        initialValues.put(DatabaseHelper.FAVOURITES_ADDRESS_USERNAME, userId);
//
//        mSQLiteDatabase.insert(DatabaseHelper.FAVOURITES_ADDRESS_TABLE_NAME, null, initialValues);
//        mSQLiteDatabase.close();
//
//    }

//    public List<LocAndField> searchFav(String keyword, String userId) {
//
//        Cursor cursor = null;
//        ArrayList<LocAndField> arr = new ArrayList<LocAndField>();
//
//        try {
//
//            String selectQuery = "";
//            /*
//             * @author: Kumail Raza Lakhani
//             * Date: 28-June-2016
//             * Query changed for Whiz Cars.
//             */
//
//////		OLD CODE
////			selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
////			if(BuildConfig.FLAVOR.equals("whizcars")) {
////			selectQuery = "SELECT  * FROM  Stations WHERE address LIKE '" + keyword + "' LIMIT 50";
////			} else {
////				selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
////			}
//
////            String selectQuery = "SELECT  * FROM " + DatabaseHelper.FAVOURITES_ADDRESS_TABLE_NAME   + " WHERE favAddUsername = '" + userId + "' ORDER BY " + DatabaseHelper.FAVOURITES_ADDRESS_ID + " DESC";
//            selectQuery = "SELECT  * FROM  FavouritesAddress WHERE Address LIKE '" + keyword + "%' AND favAddUsername = '" + userId + "' LIMIT 50";
//            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//            cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
//
//            if (cursor.moveToFirst()) {
//                do {
//                    LocAndField obj = new LocAndField();
//                    obj.setField(cursor.getString(1));
//                    obj.setDoorNo(cursor.getString(2));
//                    obj.setLat(cursor.getString(3));
//                    obj.setLon(cursor.getString(4));
//
//                    arr.add(obj);
//
//                } while (cursor.moveToNext());
//            }
//
//            mSQLiteDatabase.close();
//            cursor.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return arr;
//    }

//    public ArrayList<LocAndField> getFavAdress(String userId) {
//
//        final ArrayList<LocAndField> FavAdd = new ArrayList<LocAndField>();
//
//        String selectQuery = "SELECT  * FROM " + DatabaseHelper.FAVOURITES_ADDRESS_TABLE_NAME + " WHERE favAddUsername = '" + userId + "' ORDER BY " + DatabaseHelper.FAVOURITES_ADDRESS_ID + " DESC";
//
//        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                LocAndField obj = new LocAndField();
//                obj.setField(cursor.getString(1));
//                obj.setDoorNo(cursor.getString(2));
//                obj.setLat(cursor.getString(3));
//                obj.setLon(cursor.getString(4));
//
//                FavAdd.add(obj);
//
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        mSQLiteDatabase.close();
//
//        return FavAdd;
//
//    }

//    public ArrayList<LocAndField> searchFav(String keyword) {
//
//        final ArrayList<LocAndField> FavAdd = new ArrayList<LocAndField>();
//
////        String selectQuery = "SELECT  * FROM " + DatabaseHelper.FAVOURITES_ADDRESS_TABLE_NAME + " ORDER BY " + DatabaseHelper.FAVOURITES_ADDRESS_ID + " DESC";
//        String selectQuery = "SELECT  * FROM  FavouritesAddress WHERE Address LIKE '" + keyword + "%' LIMIT 50";
//        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                LocAndField obj = new LocAndField();
//                obj.setField(cursor.getString(1));
//                obj.setDoorNo(cursor.getString(2));
//                obj.setLat(cursor.getString(3));
//                obj.setLon(cursor.getString(4));
//
//                FavAdd.add(obj);
//
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        mSQLiteDatabase.close();
//
//        return FavAdd;
//    }

//    public ArrayList<String> getFavAdressNames() {
//
//        final ArrayList<String> FavAdd = new ArrayList<String>();
//
//        try {
//            String selectQuery = "SELECT  * FROM " + DatabaseHelper.FAVOURITES_ADDRESS_TABLE_NAME + " ORDER BY " + DatabaseHelper.FAVOURITES_ADDRESS_ID + " DESC";
//
//            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//            Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
//
//            // looping through all rows and adding to list
//            if (cursor.moveToFirst()) {
//                do {
//
//
//                    FavAdd.add(cursor.getString(1));
//
//                } while (cursor.moveToNext());
//            }
//            cursor.close();
//            mSQLiteDatabase.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return FavAdd;
//    }

    public void insertBooking(String RefNo, String PickupDate, String PickupTime,
                              String ReturnDate, String ReturnTime, String JournyType,
                              String FromDoorNO, String ToDoorNO, String FlightNo,
                              String CommingFrom, String PickupPoint, String Destination,
                              String CustomerName, String CustomerMobileNo,
                              String CustomerTelephoneNo, String VehicleName, String PaymentType,
                              String Status, String OneWayFare, String ReturnFareFare,
                              String FromLocType, String ToLocType, String tlat, String tlon,
                              String flat, String flon, String ViaAdd,
                              String transactionid,
                              String passenger, String Lugauge, String handLaguage) {

        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase(); //
        ContentValues initialValues = new ContentValues();

        // initialValues.put(BOOKING_ID, Id);
        initialValues.put(DatabaseHelper.BOOKING_REFNO, RefNo);
        initialValues.put(DatabaseHelper.BOOKING_PICKUPDATE, PickupDate);
        initialValues.put(DatabaseHelper.BOOKING_PICKUPTIME, PickupTime);
        initialValues.put(DatabaseHelper.BOOKING_RETURNUPDATE, ReturnDate);
        initialValues.put(DatabaseHelper.BOOKING_RETURNTIME, ReturnTime);
        initialValues.put(DatabaseHelper.BOOKING_JOURNYTYPE, JournyType);
        initialValues.put(DatabaseHelper.BOOKING_FROMDOORNO, FromDoorNO);
        initialValues.put(DatabaseHelper.BOOKING_TODOORNO, ToDoorNO);
        initialValues.put(DatabaseHelper.BOOKING_FLIGHTNO, FlightNo);
        initialValues.put(DatabaseHelper.BOOKING_COMMINGFROM, CommingFrom);
        initialValues.put(DatabaseHelper.BOOKING_PICKUPPOINT, PickupPoint);
        initialValues.put(DatabaseHelper.BOOKING_DESTINATION, Destination);
        initialValues.put(DatabaseHelper.BOOKING_CUSTOMERNAME, CustomerName);
        initialValues.put(DatabaseHelper.BOOKING_CUSTOMERMOBILENO, CustomerMobileNo);
        initialValues.put(DatabaseHelper.BOOKING_CUSTOMERTELEPHONENO, CustomerTelephoneNo);
        initialValues.put(DatabaseHelper.BOOKING_VEHICLENAME, VehicleName);
        initialValues.put(DatabaseHelper.BOOKING_PAYMENTTYPE, PaymentType);
        initialValues.put(DatabaseHelper.BOOKING_STATUS, Status);
        initialValues.put(DatabaseHelper.BOOKING_FARES, OneWayFare);
        initialValues.put(DatabaseHelper.BOOKING_RETURNFARES, ReturnFareFare);
        initialValues.put(DatabaseHelper.BOOKING_FROMLOCTYPE, FromLocType);
        initialValues.put(DatabaseHelper.BOOKING_TOLOCTYPE, ToLocType);
        initialValues.put(DatabaseHelper.BOOKING_TOLOCLAT, tlat);
        initialValues.put(DatabaseHelper.BOOKING_TOLOCLON, tlon);
        initialValues.put(DatabaseHelper.BOOKING_FROMLOCLAT, flat);
        initialValues.put(DatabaseHelper.BOOKING_FROMLOCLON, flon);
        initialValues.put(DatabaseHelper.BOOKING_VIAADD, ViaAdd);
        initialValues.put(DatabaseHelper.BOOKING_TRANSACTION_ID, transactionid);
        initialValues.put(DatabaseHelper.VEHICLE_COLUMN_TOTALPASSENGERS, passenger);
        initialValues.put(DatabaseHelper.VEHICLE_COLUMN_TOTALLUGGAGES, Lugauge);
        initialValues.put(DatabaseHelper.VEHICLE_COLUMN_TOTALHANDLUGGAGES, handLaguage);
        mSQLiteDatabase.insert(DatabaseHelper.BOOKING_TABLE_NAME, null, initialValues);
        Log.i("log", DatabaseHelper.BOOKING_TABLE_NAME);
        mSQLiteDatabase.close();
        LocAndField locAndField = new LocAndField();
        locAndField.setField(PickupPoint);
        locAndField.setLat(flat);
        locAndField.setLon(flon);
        locAndField.setLocationType(FromLocType);
        insertRecentLocation(locAndField);


        LocAndField locAndField2 = new LocAndField();
        locAndField2.setField(Destination);
        locAndField2.setLat(tlat);
        locAndField2.setLon(tlon);
        locAndField.setLocationType(ToLocType);
        insertRecentLocation(locAndField2);

    }

    public void insertBulkBookings(ArrayList<Model_BookingDetailsModel> bookingsList) {

        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase(); //
        ContentValues initialValues = new ContentValues();
        mSQLiteDatabase.beginTransaction();
        try {

            for (Model_BookingDetailsModel modelBookingDetailsModel : bookingsList) {
                initialValues.put(DatabaseHelper.BOOKING_REFNO, modelBookingDetailsModel.getRefrenceNo());//1


                initialValues.put(DatabaseHelper.BOOKING_PICKUPDATE, modelBookingDetailsModel.getPickUpDate());//2


                initialValues.put(DatabaseHelper.BOOKING_PICKUPTIME, modelBookingDetailsModel.getPickUpTime());//3
                initialValues.put(DatabaseHelper.BOOKING_RETURNUPDATE, modelBookingDetailsModel.getReturnDate());//4
                initialValues.put(DatabaseHelper.BOOKING_RETURNTIME, modelBookingDetailsModel.getReturnTime());//5
                initialValues.put(DatabaseHelper.BOOKING_JOURNYTYPE, modelBookingDetailsModel.getJournyType());//6
                initialValues.put(DatabaseHelper.BOOKING_FROMDOORNO, modelBookingDetailsModel.getFromAddressDoorNO());//7
                initialValues.put(DatabaseHelper.BOOKING_TODOORNO, modelBookingDetailsModel.gettoAddressDoorNO());//8
                initialValues.put(DatabaseHelper.BOOKING_FLIGHTNO, modelBookingDetailsModel.getFromAddressFlightNo());//9
                initialValues.put(DatabaseHelper.BOOKING_COMMINGFROM, modelBookingDetailsModel.getFromAddressCommingFrom());//10
                initialValues.put(DatabaseHelper.BOOKING_PICKUPPOINT, modelBookingDetailsModel.getFromAddress());//11
                initialValues.put(DatabaseHelper.BOOKING_DESTINATION, modelBookingDetailsModel.gettoAddress());//12
                initialValues.put(DatabaseHelper.BOOKING_CUSTOMERNAME, modelBookingDetailsModel.getCusomerName());//13
                initialValues.put(DatabaseHelper.BOOKING_CUSTOMERMOBILENO, modelBookingDetailsModel.getCusomerMobile());//14
                initialValues.put(DatabaseHelper.BOOKING_CUSTOMERTELEPHONENO, "");//15
                initialValues.put(DatabaseHelper.BOOKING_VEHICLENAME, modelBookingDetailsModel.getCar());//16
                initialValues.put(DatabaseHelper.BOOKING_PAYMENTTYPE, modelBookingDetailsModel.getPaymentType());//17
                initialValues.put(DatabaseHelper.BOOKING_STATUS, modelBookingDetailsModel.getStatus());//18
                initialValues.put(DatabaseHelper.BOOKING_FARES, modelBookingDetailsModel.getOneWayFare());//19
                initialValues.put(DatabaseHelper.BOOKING_RETURNFARES, modelBookingDetailsModel.getReturnFare());//20
                initialValues.put(DatabaseHelper.BOOKING_FROMLOCTYPE, modelBookingDetailsModel.getFromAddressType());//21
                initialValues.put(DatabaseHelper.BOOKING_TOLOCTYPE, modelBookingDetailsModel.gettoAddressType());//22
                initialValues.put(DatabaseHelper.BOOKING_TOLOCLAT, modelBookingDetailsModel.getDropLat());//23
                initialValues.put(DatabaseHelper.BOOKING_TOLOCLON, modelBookingDetailsModel.getDropLon());//24
                initialValues.put(DatabaseHelper.BOOKING_FROMLOCLAT, modelBookingDetailsModel.getPickupLat());//25
                initialValues.put(DatabaseHelper.BOOKING_FROMLOCLON, modelBookingDetailsModel.getPickupLon());//26
                String via = modelBookingDetailsModel.getViaPointsAsString();//27
                initialValues.put(DatabaseHelper.BOOKING_VIAADD, via);//getViaAddString(modelBookingDetailsModel.getViaPoints()));//28
                initialValues.put(DatabaseHelper.BOOKING_TRANSACTION_ID, modelBookingDetailsModel.getTransactionId());//29

                initialValues.put(DatabaseHelper.BOOKING_Waiting, modelBookingDetailsModel.getWaiting());//30
                initialValues.put(DatabaseHelper.BOOKING_Parking, modelBookingDetailsModel.getParking());//31
                initialValues.put(DatabaseHelper.BOOKING_Congestion, modelBookingDetailsModel.getCongestion());//32
                initialValues.put(DatabaseHelper.BOOKING_AgentCharge, modelBookingDetailsModel.getAgentCharge());//33
                initialValues.put(DatabaseHelper.BOOKING_AgentFees, modelBookingDetailsModel.getAgentFees());//34
                initialValues.put(DatabaseHelper.BOOKING_ExtraDropCharges, modelBookingDetailsModel.getExtraDropCharges());//35
                initialValues.put(DatabaseHelper.BOOKING_MeetAndGreetCharges, modelBookingDetailsModel.getMeetAndGreet());//36
                initialValues.put(DatabaseHelper.BOOKING_ServicesCharges, modelBookingDetailsModel.getServiceCharges());//37
                initialValues.put(DatabaseHelper.BOOKING_AgentCommission, modelBookingDetailsModel.getAgentCommission());//38
                initialValues.put(DatabaseHelper.BOOKING_CompanyPrice, modelBookingDetailsModel.getCompanyPrice());//39
                initialValues.put(DatabaseHelper.BOOKING_FareRate, modelBookingDetailsModel.getFareRate());//40


                initialValues.put(DatabaseHelper.VEHICLE_COLUMN_TOTALPASSENGERS, modelBookingDetailsModel.getPassengers());//41
                initialValues.put(DatabaseHelper.VEHICLE_COLUMN_TOTALLUGGAGES, modelBookingDetailsModel.getluggages());//42
                initialValues.put(DatabaseHelper.VEHICLE_COLUMN_TOTALHANDLUGGAGES, modelBookingDetailsModel.getHandluggages());//43


                mSQLiteDatabase.insert(DatabaseHelper.BOOKING_TABLE_NAME, null, initialValues);

            }
            mSQLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mSQLiteDatabase.endTransaction();
        }

        int size = getHistoryBookingsDetails().size();
        Log.d("TAG", "insertBulkBookings:  " + size);
    }

    public ArrayList<Model_BookingDetailsModel> getbookingbyRefference(String ReferenceNo) {
        ArrayList<Model_BookingDetailsModel> bookings = new ArrayList<Model_BookingDetailsModel>();
        String selectQuery = "SELECT  * FROM " + DatabaseHelper.BOOKING_TABLE_NAME + " where " + DatabaseHelper.BOOKING_REFNO + " = '" + ReferenceNo + "'";
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Model_BookingDetailsModel obj = new Model_BookingDetailsModel();
                obj.setRefrenceNo(cursor.getString(1));
                obj.setPickUpDate(cursor.getString(2));
                obj.setPickUpTime(cursor.getString(3));

                obj.setReturnDate(cursor.getString(4));
                obj.setReturnTime(cursor.getString(5));

                obj.setJournyType(Integer.parseInt(cursor.getString(6)));
                obj.setFromAddressDoorNO(cursor.getString(7));
                obj.settoAddressDoorNO(cursor.getString(8));
                obj.setFromAddressFlightNo(cursor.getString(9));
                obj.setFromAddressCommingFrom(cursor.getString(10));

                obj.setFromAddress(cursor.getString(11));
                obj.settoAddress(cursor.getString(12));
                obj.setCusomerName(cursor.getString(13));
                obj.setCusomerMobile(cursor.getString(14));
                obj.setCusomerPhone(cursor.getString(15));
                obj.setCar(cursor.getString(16));
                obj.setPaymentType(cursor.getString(17));
                obj.setStatus(cursor.getString(18));

                obj.setOneWayFare(cursor.getString(19));
                obj.setReturnFare(cursor.getString(20));

                obj.setDriverId(cursor.getString(21));
                obj.setDriverNo(cursor.getString(22));
                obj.setFromAddressType(cursor.getString(23));
                obj.settoAddressType(cursor.getString(24));
                obj.setDropLat(cursor.getString(25));
                obj.setDropLon(cursor.getString(26));
                obj.setPickupLat(cursor.getString(27));
                obj.setPickupLon(cursor.getString(28));
                String viaS = cursor.getString(29);

                if (!viaS.equals("")) {
                    obj.setViaPointsAsString(viaS);
                }

                obj.setTransactionId(cursor.getString(31));
                obj.setPassengers(cursor.getString(32));
                obj.setluggages(cursor.getString(33));
                obj.setHandluggages(cursor.getString(34));
                bookings.add(obj);

            } while (cursor.moveToNext());
        }

        cursor.close();
        mSQLiteDatabase.close();

        return bookings;

    }

    public ArrayList<Model_BookingDetailsModel> swapAddressToRecent() {
        ArrayList<Model_BookingDetailsModel> bookings = new ArrayList<Model_BookingDetailsModel>();

        /*
         *	@author: Kumail Raza Lakhani.
         *	Date: 01-July-2016
         *	Query updated due to Booking Status JSON response.
         */

////		OLD Code
//		String selectQuery = "SELECT  * FROM " + DatabaseHelper.BOOKING_TABLE_NAME + " where " + DatabaseHelper.BOOKING_STATUS + " IN ('"
//				+ CommonVariables.STATUS_WAITING + "','" + CommonVariables.STATUS_CONFIRMED + "','" + CommonVariables.STATUS_ARRIVED + "','"
//				+ CommonVariables.STATUS_ONROUTE + "') ORDER BY " + DatabaseHelper.BOOKING_REFNO + " DESC";

        String selectQuery;

//		if(BuildConfig.FLAVOR.equals("hydeparkcars") ||
//				BuildConfig.FLAVOR.equals("churchillcars") ||
//			// Date: 09-August-2016
//				BuildConfig.FLAVOR.equals("manorcar") ||
//			// Date: 11-August-2016
//				BuildConfig.FLAVOR.equals("soniccars") ||
//			// Date: 25-August-2016
//				BuildConfig.FLAVOR.equals("atlascars") ||
//			// Date: 12-July-2016
//				BuildConfig.FLAVOR.equals("simplycabs")||
//				BuildConfig.FLAVOR.equals("MayfairCars")
//				) {
        selectQuery = "SELECT * FROM " + DatabaseHelper.BOOKING_TABLE_NAME + " where LOWER("
                + DatabaseHelper.BOOKING_STATUS + ") IN ('" + CommonVariables.STATUS_WAITING.toLowerCase() + "','"
                + CommonVariables.STATUS_CONFIRMED.toLowerCase() + "','" + CommonVariables.STATUS_ARRIVED.toLowerCase() + "','"
                + CommonVariables.STATUS_POB.toLowerCase() + "','" + CommonVariables.STATUS_POB2.toLowerCase() + "','" + CommonVariables.STATUS_STC.toLowerCase() + "','" + CommonVariables.STATUS_STC2.toLowerCase()
                + "','" + CommonVariables.STATUS_ONROUTE.toLowerCase() + "') ORDER BY " + DatabaseHelper.BOOKING_REFNO
                + " DESC";
        //	}
//		else {
//			selectQuery = "SELECT  * FROM " + DatabaseHelper.BOOKING_TABLE_NAME + " where " + DatabaseHelper.BOOKING_STATUS + " IN ('"
//					+ CommonVariables.STATUS_WAITING + "','" + CommonVariables.STATUS_CONFIRMED + "','" + CommonVariables.STATUS_ARRIVED + "','"
//					+ CommonVariables.STATUS_ONROUTE + "') ORDER BY " + DatabaseHelper.BOOKING_REFNO + " DESC";
//		}
        /*
         *	Date: 01-July-2016
         *	END ->
         */

        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Model_BookingDetailsModel obj = new Model_BookingDetailsModel();
                try {
                    obj.setRefrenceNo(cursor.getString(1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setPickUpDate(cursor.getString(2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setPickUpTime(cursor.getString(3));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    obj.setReturnDate(cursor.getString(4));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setReturnTime(cursor.getString(5));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    obj.setJournyType(Integer.parseInt(cursor.getString(6)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setFromAddressDoorNO(cursor.getString(7));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.settoAddressDoorNO(cursor.getString(8));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setFromAddressFlightNo(cursor.getString(9));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setFromAddressCommingFrom(cursor.getString(10));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    obj.setFromAddress(cursor.getString(11));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.settoAddress(cursor.getString(12));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setCusomerName(cursor.getString(13));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setCusomerMobile(cursor.getString(14));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setCusomerPhone(cursor.getString(15));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setCar(cursor.getString(16));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setPaymentType(cursor.getString(17));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setStatus(cursor.getString(18));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    obj.setOneWayFare(cursor.getString(19));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setReturnFare(cursor.getString(20));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    obj.setDriverId(cursor.getString(21));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setDriverNo(cursor.getString(22));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setFromAddressType(cursor.getString(23));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.settoAddressType(cursor.getString(24));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setDropLat(cursor.getString(25));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setDropLon(cursor.getString(26));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setPickupLat(cursor.getString(27));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setPickupLon(cursor.getString(28));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setViaPointsAsString(cursor.getString(29));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    obj.setTransactionId(cursor.getString(31));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setPassengers(cursor.getString(32));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setluggages(cursor.getString(33));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setHandluggages(cursor.getString(34));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bookings.add(obj);

            } while (cursor.moveToNext());
        }

        cursor.close();
//		mSQLiteDatabase.close();

        return bookings;

    }

//    public boolean getAllBookings() {
//
//        String selectQuery;
//
//        selectQuery = "SELECT * FROM " + DatabaseHelper.BOOKING_TABLE_NAME;
//
//
//        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//
//
//                LocAndField locAndField = new LocAndField();
//                locAndField.setField(cursor.getString(11));
//                locAndField.setLat(cursor.getString(27));
//                locAndField.setLon(cursor.getString(28));
//                locAndField.setLocationType(cursor.getString(cursor.getColumnIndex(DatabaseHelper.BOOKING_FROMLOCTYPE)));
//
//                insertRecentLocation(locAndField);
//
//
//                LocAndField locAndField2 = new LocAndField();
//                locAndField2.setField(cursor.getString(12));
//                locAndField2.setLat(cursor.getString(25));
//                locAndField2.setLon(cursor.getString(26));
//                locAndField.setLocationType(cursor.getString(cursor.getColumnIndex(DatabaseHelper.BOOKING_TOLOCTYPE)));
//                insertRecentLocation(locAndField2);
//
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
////		mSQLiteDatabase.close();
//
//        return true;
//
//    }

    /**
     * @author: Kumail Raza Lakhani
     * Date: 30-August-2016
     * Get ActiveBookings by usedId for apps with login feature. (Like Hornet Cars app).
     */
//    public ArrayList<Model_BookingDetailsModel> getActiveBookings(String usedId) {
//        ArrayList<Model_BookingDetailsModel> bookings = new ArrayList<Model_BookingDetailsModel>();
//
//        String selectQuery;
//
//        selectQuery = "SELECT  * FROM " + DatabaseHelper.BOOKING_TABLE_NAME + " where "
//                + DatabaseHelper.BOOKING_USERNAME + "='" + usedId + "' AND LOWER("
//                + DatabaseHelper.BOOKING_STATUS + ") IN ('"
//                + CommonVariables.STATUS_WAITING.toLowerCase() + "','"
//                + CommonVariables.STATUS_CONFIRMED.toLowerCase() + "','"
//                + CommonVariables.STATUS_ARRIVED.toLowerCase()
//                + "','" + CommonVariables.STATUS_ONROUTE.toLowerCase()
//                + "') ORDER BY " + DatabaseHelper.BOOKING_REFNO + " DESC";
//
//        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Model_BookingDetailsModel obj = new Model_BookingDetailsModel();
//                obj.setRefrenceNo(cursor.getString(1));
//                obj.setPickUpDate(cursor.getString(2));
//                obj.setPickUpTime(cursor.getString(3));
//
//                obj.setReturnDate(cursor.getString(4));
//                obj.setReturnTime(cursor.getString(5));
//
//                obj.setJournyType(Integer.parseInt(cursor.getString(6)));
//                obj.setFromAddressDoorNO(cursor.getString(7));
//                obj.settoAddressDoorNO(cursor.getString(8));
//                obj.setFromAddressFlightNo(cursor.getString(9));
//                obj.setFromAddressCommingFrom(cursor.getString(10));
//
//                obj.setFromAddress(cursor.getString(11));
//                obj.settoAddress(cursor.getString(12));
//                obj.setCusomerName(cursor.getString(13));
//                obj.setCusomerMobile(cursor.getString(14));
//                obj.setCusomerPhone(cursor.getString(15));
//                obj.setCar(cursor.getString(16));
//                obj.setPaymentType(cursor.getString(17));
//                obj.setStatus(cursor.getString(18));
//
//                obj.setOneWayFare(cursor.getString(19));
//                obj.setReturnFare(cursor.getString(20));
//
//                obj.setDriverId(cursor.getString(21));
//                obj.setDriverNo(cursor.getString(22));
//                obj.setFromAddressType(cursor.getString(23));
//                obj.settoAddressType(cursor.getString(24));
//                obj.setDropLat(cursor.getString(25));
//                obj.setDropLon(cursor.getString(26));
//                obj.setPickupLat(cursor.getString(27));
//                obj.setPickupLon(cursor.getString(28));
//                String viaS = cursor.getString(29);
//
//                if (!viaS.equals("")) {
//                    obj.setViaPoints(new ArrayList<String>(Arrays.asList(viaS.split(">>>"))));
//                }
//
//                obj.setTransactionId(cursor.getString(30));
//                bookings.add(obj);
//
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        mSQLiteDatabase.close();
//
//        return bookings;
//
//    }

    /**
     * Date: 30-August-2016
     * Get ActiveBookings by usedId for apps with login feature. (Like Hornet Cars app). END ->
     */
    public ArrayList<Model_BookingDetailsModel> getCompletedBookings() {
        ArrayList<Model_BookingDetailsModel> bookings = new ArrayList<Model_BookingDetailsModel>();

        String selectQuery = "SELECT * FROM " + DatabaseHelper.BOOKING_TABLE_NAME + " where LOWER(" + DatabaseHelper.BOOKING_STATUS + ") IN ('"
                + CommonVariables.STATUS_COMPLETED.toLowerCase() + "') ORDER BY " + DatabaseHelper.BOOKING_REFNO + " DESC LIMIT 6";

//		String selectQuery = "SELECT  * FROM " + DatabaseHelper.BOOKING_TABLE_NAME +" ORDER BY " + DatabaseHelper.BOOKING_REFNO + " DESC";


        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Model_BookingDetailsModel obj = new Model_BookingDetailsModel();
                obj.setRefrenceNo(cursor.getString(1));
                obj.setPickUpDate(cursor.getString(2));
                obj.setPickUpTime(cursor.getString(3));

                obj.setReturnDate(cursor.getString(4));
                obj.setReturnTime(cursor.getString(5));

                obj.setJournyType(Integer.parseInt(cursor.getString(6)));
                obj.setFromAddressDoorNO(cursor.getString(7));
                obj.settoAddressDoorNO(cursor.getString(8));
                obj.setFromAddressFlightNo(cursor.getString(9));
                obj.setFromAddressCommingFrom(cursor.getString(10));

                obj.setFromAddress(cursor.getString(11));
                obj.settoAddress(cursor.getString(12));
                obj.setCusomerName(cursor.getString(13));
                obj.setCusomerMobile(cursor.getString(14));
                obj.setCusomerPhone(cursor.getString(15));
                obj.setCar(cursor.getString(16));
                obj.setPaymentType(cursor.getString(17));
                obj.setStatus(cursor.getString(18));

                obj.setOneWayFare(cursor.getString(19));
                obj.setReturnFare(cursor.getString(20));

                obj.setDriverId(cursor.getString(21));
                obj.setDriverNo(cursor.getString(22));
                obj.setFromAddressType(cursor.getString(23));
                obj.settoAddressType(cursor.getString(24));
                obj.setDropLat(cursor.getString(25));
                obj.setDropLon(cursor.getString(26));
                obj.setPickupLat(cursor.getString(27));
                obj.setPickupLon(cursor.getString(28));
                String viaS = cursor.getString(29);

                if (!viaS.equals("")) {
                    obj.setViaPointsAsString(viaS);
                }

                obj.setTransactionId(cursor.getString(31));
                obj.setPassengers(cursor.getString(32));
                obj.setluggages(cursor.getString(33));
                obj.setHandluggages(cursor.getString(34));
                bookings.add(obj);

            } while (cursor.moveToNext());
        }

        cursor.close();
        mSQLiteDatabase.close();

        return bookings;

    }

    public ArrayList<Model_BookingDetailsModel> getHistoryBookingsDetails() {
        ArrayList<Model_BookingDetailsModel> bookings = new ArrayList<Model_BookingDetailsModel>();

        String selectQuery = "SELECT * FROM " + DatabaseHelper.BOOKING_TABLE_NAME + " where LOWER(" + DatabaseHelper.BOOKING_STATUS + ") IN ('"
                + CommonVariables.STATUS_CANCLED.toLowerCase() + "','" + CommonVariables.STATUS_COMPLETED.toLowerCase() + "') ORDER BY " + DatabaseHelper.BOOKING_REFNO + " DESC LIMIT 6";


//		String selectQuery = "SELECT  * FROM " + DatabaseHelper.BOOKING_TABLE_NAME +" ORDER BY " + DatabaseHelper.BOOKING_REFNO + " DESC";


        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Model_BookingDetailsModel obj = new Model_BookingDetailsModel();
                obj.setRefrenceNo(cursor.getString(1));
                obj.setPickUpDate(cursor.getString(2));
                obj.setPickUpTime(cursor.getString(3));

                obj.setReturnDate(cursor.getString(4));
                obj.setReturnTime(cursor.getString(5));

                obj.setJournyType(Integer.parseInt(cursor.getString(6)));
                obj.setFromAddressDoorNO(cursor.getString(7));
                obj.settoAddressDoorNO(cursor.getString(8));
                obj.setFromAddressFlightNo(cursor.getString(9));
                obj.setFromAddressCommingFrom(cursor.getString(10));

                obj.setFromAddress(cursor.getString(11));
                obj.settoAddress(cursor.getString(12));
                obj.setCusomerName(cursor.getString(13));
                obj.setCusomerMobile(cursor.getString(14));
                obj.setCusomerPhone(cursor.getString(15));
                obj.setCar(cursor.getString(16));
                obj.setPaymentType(cursor.getString(17));
                obj.setStatus(cursor.getString(18));

                obj.setOneWayFare(cursor.getString(19));
                obj.setReturnFare(cursor.getString(20));

                obj.setDriverId(cursor.getString(21));
                obj.setDriverNo(cursor.getString(22));
                obj.setFromAddressType(cursor.getString(23));
                obj.settoAddressType(cursor.getString(24));
                obj.setDropLat(cursor.getString(25));
                obj.setDropLon(cursor.getString(26));
                obj.setPickupLat(cursor.getString(27));
                if (obj.getPickupLat() == null || obj.getPickupLat().equals("") || obj.getPickupLat().equals("0") || obj.getPickupLat().equals("0.0") || obj.getPickupLat().equals("null")) {
                    continue;
                }
                obj.setPickupLon(cursor.getString(28));
                String viaS = cursor.getString(29);

                if (!viaS.equals("")) {
                    obj.setViaPointsAsString(viaS);
                }

                obj.setTransactionId(cursor.getString(31));
                obj.setPassengers(cursor.getString(32));
                obj.setluggages(cursor.getString(33));
                obj.setHandluggages(cursor.getString(34));
                bookings.add(obj);

            } while (cursor.moveToNext());
        }

        cursor.close();
        mSQLiteDatabase.close();

        return bookings;

    }

    public ArrayList<Model_BookingDetailsModel> getHistoryBookings() {
        ArrayList<Model_BookingDetailsModel> bookings = new ArrayList<Model_BookingDetailsModel>();

        String selectQuery = "SELECT * FROM " + DatabaseHelper.BOOKING_TABLE_NAME + " where LOWER(" + DatabaseHelper.BOOKING_STATUS + ") IN " +
                "('" + CommonVariables.STATUS_CANCLED.toLowerCase() + "','" + CommonVariables.STATUS_COMPLETED.toLowerCase() + "') ORDER BY " + DatabaseHelper.BOOKING_REFNO
                + " DESC";

//		String selectQuery = "SELECT  * FROM " + DatabaseHelper.BOOKING_TABLE_NAME +" ORDER BY " + DatabaseHelper.BOOKING_REFNO + " DESC";


        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Model_BookingDetailsModel obj = new Model_BookingDetailsModel();
                try {
                    obj.setRefrenceNo(cursor.getString(1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setPickUpDate(cursor.getString(2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setPickUpTime(cursor.getString(3));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    obj.setReturnDate(cursor.getString(4));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setReturnTime(cursor.getString(5));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    obj.setJournyType(Integer.parseInt(cursor.getString(6)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setFromAddressDoorNO(cursor.getString(7));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.settoAddressDoorNO(cursor.getString(8));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setFromAddressFlightNo(cursor.getString(9));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setFromAddressCommingFrom(cursor.getString(10));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    obj.setFromAddress(cursor.getString(11));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.settoAddress(cursor.getString(12));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setCusomerName(cursor.getString(13));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setCusomerMobile(cursor.getString(14));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setCusomerPhone(cursor.getString(15));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setCar(cursor.getString(16));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setPaymentType(cursor.getString(17));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setStatus(cursor.getString(18));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    obj.setOneWayFare(cursor.getString(19));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setReturnFare(cursor.getString(20));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    obj.setDriverId(cursor.getString(21));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setDriverNo(cursor.getString(22));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setFromAddressType(cursor.getString(23));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.settoAddressType(cursor.getString(24));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setDropLat(cursor.getString(25));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setDropLon(cursor.getString(26));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setPickupLat(cursor.getString(27));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setPickupLon(cursor.getString(28));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String viaS = "";
                try {
                    viaS = cursor.getString(29);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!viaS.equals("")) {
                    obj.setViaPointsAsString(viaS);
                }
                try {
                    obj.setTransactionId(cursor.getString(31));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setWaiting(Double.parseDouble(cursor.getString(32)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setParking(Double.parseDouble(cursor.getString(33)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setCongestion(Double.parseDouble(cursor.getString(34)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setAgentCharge(Double.parseDouble(cursor.getString(35)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setAgentFees(Double.parseDouble(cursor.getString(36)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setExtraDropCharges(Double.parseDouble(cursor.getString(37)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setMeetAndGreet(Double.parseDouble(cursor.getString(38)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setServiceCharges(Double.parseDouble(cursor.getString(39)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setAgentCommission(Double.parseDouble(cursor.getString(40)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setCompanyPrice(Double.parseDouble(cursor.getString(41)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setFareRate(Double.parseDouble(cursor.getString(42)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setPassengers(cursor.getString(43));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setluggages(cursor.getString(44));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setHandluggages(cursor.getString(45));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bookings.add(obj);

            } while (cursor.moveToNext());
        }

        cursor.close();
        mSQLiteDatabase.close();

        return bookings;
    }

    public void insertStores(String address, String postCode, float lat, float lng) {
        ContentValues initialValues = new ContentValues();
//		String myPath = DB_PATH + DB_NAME;
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//		db.W
        initialValues.put(COLUMN_ADDRESS_TEXT, address);
        initialValues.put(COLUMN_POSTCODE_TEXT, postCode);
        initialValues.put(COLUMN_LATITUDE_TEXT, lat);
        initialValues.put(COLUMN_LONGITUDE_TEXT, lng);


        mSQLiteDatabase.insert("supermarkets", null, initialValues);
        mSQLiteDatabase.close();// Closing database connection

    }

    public void UpdateBookingStatus(String Status, String RefNo) {
        if (Status == null || Status.equals("")) {
            return;
        }
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.BOOKING_STATUS, Status);
        mSQLiteDatabase.update(DatabaseHelper.BOOKING_TABLE_NAME, values, DatabaseHelper.BOOKING_REFNO + "='" + RefNo + "'", null);
        mSQLiteDatabase.close();
    }//


    public void deletePromoByCode(String Code) {

        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        mSQLiteDatabase.delete(DatabaseHelper.TABLE_PROMO, DatabaseHelper.PR_CODE + "='" + Code + "'", null);
        mSQLiteDatabase.close();
    }

    public void UpdateBookingStatusAndFares(String Fares, String Status, String RefNo) {

        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.BOOKING_STATUS, Status);
        values.put(DatabaseHelper.BOOKING_FARES, Fares);
        mSQLiteDatabase.update(DatabaseHelper.BOOKING_TABLE_NAME, values, DatabaseHelper.BOOKING_REFNO + "='" + RefNo + "'", null);
        mSQLiteDatabase.close();
    }


    public void DeleteBooking(String refno) {

        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        mSQLiteDatabase.delete(DatabaseHelper.BOOKING_TABLE_NAME, DatabaseHelper.BOOKING_REFNO + "='" + refno + "'", null);

        mSQLiteDatabase.close();
    }//

    public void DeleteAllBooking() {
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        mSQLiteDatabase.delete(DatabaseHelper.BOOKING_TABLE_NAME, null, null);
        mSQLiteDatabase.close();
    }


    public void DeleteAllSchedule() {
        try {
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
            mSQLiteDatabase.delete(DatabaseHelper.BOOKING_TABLE_NAME, "LOWER("
                    + DatabaseHelper.BOOKING_STATUS + ") IN ('" + CommonVariables.STATUS_WAITING.toLowerCase() + "','"
                    + CommonVariables.STATUS_CONFIRMED.toLowerCase() + "','" + CommonVariables.STATUS_ARRIVED.toLowerCase() + "','"
                    + CommonVariables.STATUS_POB.toLowerCase() + "','" + CommonVariables.STATUS_POB2.toLowerCase() + "','" + CommonVariables.STATUS_STC.toLowerCase() + "','" + CommonVariables.STATUS_STC2.toLowerCase()
                    + "','" + CommonVariables.STATUS_ONROUTE.toLowerCase() + "')", null);
            mSQLiteDatabase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DeleteAllHistory() {
        try {
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();

            mSQLiteDatabase.delete(DatabaseHelper.BOOKING_TABLE_NAME, "LOWER(" + DatabaseHelper.BOOKING_STATUS + ") IN ('"
                    + CommonVariables.STATUS_CANCLED.toLowerCase() + "','" + CommonVariables.STATUS_COMPLETED.toLowerCase() + "')", null);

            mSQLiteDatabase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getViaAddString(ArrayList<String> l) {
        String ret = "";
        if (l != null && !l.isEmpty()) {

            for (String via : l)
                ret += via + ">>>";
        }

        return ret;
    }

    public void inserInfo(String number, String email, String website, String poweredby) {
        ContentValues initialValues = new ContentValues();
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        mSQLiteDatabase.delete(DatabaseHelper.TABLE_INFO, null, null);

        initialValues.put(DatabaseHelper.INFO_PHONE, number); // 1
        initialValues.put(DatabaseHelper.INFO_EMAIL, email);  // 2
        initialValues.put(DatabaseHelper.INFO_WEBSITE, website);  // 3
        initialValues.put(DatabaseHelper.INFO_POWERED_BY, poweredby);  // 4

        mSQLiteDatabase.insert(DatabaseHelper.TABLE_INFO, null, initialValues);

        mSQLiteDatabase.close();
    }

    public String[] getInfoValues() {
        String[] values = null;

        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_INFO;
        mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            values = new String[4];
            values[0] = cursor.getString(1);
            values[1] = cursor.getString(2);
            values[2] = cursor.getString(3);
            values[3] = cursor.getString(4);
        }
        // closing connection
        cursor.close();
        mSQLiteDatabase.close();

        return values;
    }

    /**
     * @author: Kumail Raza Lakhani
     * Date: 20-September-2016
     * Get user details.
     */
    @SuppressLint("Range")
    public ArrayList<String> getUser(String username) throws SQLException {
        ArrayList<String> userDetailList = new ArrayList<>();
        try {
            mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
            String selectQuery = "SELECT  * FROM "
                    + DatabaseHelper.USERACCOUNT_TABEL + " WHERE name='" + username + "'";
//			Cursor mCursor = mSQLiteDatabase.rawQuery(
//					"SELECT * FROM  "+ DatabaseHelper.USERACCOUNT_TABEL
//							+" WHERE name='"+username+"'";
//			);
            mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
            Cursor mCursor = mSQLiteDatabase.rawQuery(selectQuery, null);
            if (mCursor.moveToFirst()) {
                do {
                    userDetailList.add(mCursor.getString(mCursor
                            .getColumnIndex(DatabaseHelper.USERACCOUNT_NAME)));
                    userDetailList.add(mCursor.getString(mCursor
                            .getColumnIndex(DatabaseHelper.USERACCOUNT_EMAIL)));
                    userDetailList.add(mCursor.getString(mCursor
                            .getColumnIndex(DatabaseHelper.USERACCOUNT_MOBILENO)));

                } while (mCursor.moveToNext());
            }

            mCursor.close();
            mSQLiteDatabase.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return userDetailList;
    }

    public void insertPromoCode(String promocode, String title, String msg, String st_date, String end_date, String promotype, String promoval, String serverid, String total, String used, String PromotionType, String MaxDisount, String minFares) {
        ContentValues initialValues = new ContentValues();
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();

        initialValues.put(DatabaseHelper.PR_CODE, promocode);
        initialValues.put(DatabaseHelper.PR_Title, title);
        initialValues.put(DatabaseHelper.PR_MSG, msg);
        initialValues.put(DatabaseHelper.PR_STRTDATE, st_date);
        initialValues.put(DatabaseHelper.PR_ENDDATE, end_date);
        initialValues.put(DatabaseHelper.PR_Type, promotype);
        initialValues.put(DatabaseHelper.PR_Value, promoval);
        initialValues.put(DatabaseHelper.PR_ServerID, serverid);
        initialValues.put(DatabaseHelper.PR_TOTAL, total);
        initialValues.put(DatabaseHelper.PR_USED, used);
        initialValues.put(DatabaseHelper.PR_PROMOTYPE, PromotionType);
        initialValues.put(DatabaseHelper.PR_Max_Discount, MaxDisount);
        initialValues.put(DatabaseHelper.PR_MinFares, minFares);

        mSQLiteDatabase.insert(DatabaseHelper.TABLE_PROMO, null, initialValues);
        mSQLiteDatabase.close();// Closing database connection

    }

    public void deletePromo() {

        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        mSQLiteDatabase.delete(DatabaseHelper.TABLE_PROMO, null, null);
        mSQLiteDatabase.close();
    }

    @SuppressLint("Range")
    public ArrayList<PromoModel> getPromoDetails() {
        ArrayList<PromoModel> values = new ArrayList<>();


        try {
            String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_PROMO;
            mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
            Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                PromoModel promoModel = new PromoModel();
                //			promoModel.setPROMOID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_ID)));
                promoModel.setPromoCode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_CODE)));
                promoModel.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_Title)));
                promoModel.setMsg(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_MSG)));
                promoModel.setStrtDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_STRTDATE)));
                promoModel.setEndDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_ENDDATE)));
                promoModel.setPromType(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_Type)));
                promoModel.setPromoValue(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_Value)));
                promoModel.setPromoServerId(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_ServerID)));
                promoModel.setTotal(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_TOTAL)));
                promoModel.setUsed(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_USED)));
                promoModel.setPROMOTIONTYPEID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_PROMOTYPE)));
                promoModel.setMaxDiscount(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_Max_Discount)));
                promoModel.setMinFares(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_MinFares)));
                values.add(promoModel);

                cursor.moveToNext();
            }
            // closing connection
            cursor.close();
            mSQLiteDatabase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return values;
    }

    @SuppressLint("Range")
    public PromoModel getPromoDetailsByCode(String code) {
        ArrayList<PromoModel> values = new ArrayList<>();

        PromoModel promoModel = new PromoModel();
        try {
            String selectQuery = "";
            selectQuery = "SELECT  * FROM  promotable WHERE pr_code = '" + code + "'";
            mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
            Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
//            PromoModel promoModel = new PromoModel();
            // looping through all rows and adding to list
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                //			promoModel.setPROMOID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_ID)));
                promoModel.setPromoCode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_CODE)));
                promoModel.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_Title)));
                promoModel.setMsg(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_MSG)));
                promoModel.setStrtDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_STRTDATE)));
                promoModel.setEndDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_ENDDATE)));
                promoModel.setPromType(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_Type)));
                promoModel.setPromoValue(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_Value)));
                promoModel.setPromoServerId(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_ServerID)));
                promoModel.setTotal(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_TOTAL)));
                promoModel.setUsed(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_USED)));
                promoModel.setPROMOTIONTYPEID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_PROMOTYPE)));
                promoModel.setMaxDiscount(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_Max_Discount)));
                promoModel.setMinFares(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PR_MinFares)));
                values.add(promoModel);

                cursor.moveToNext();
            }
            // closing connection
            cursor.close();
            mSQLiteDatabase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return promoModel;
    }

    public ArrayList<Model_BookingDetailsModel> getActiveBookings() {
        ArrayList<Model_BookingDetailsModel> bookings = new ArrayList<Model_BookingDetailsModel>();
        String selectQuery;
        selectQuery = "SELECT * FROM " + DatabaseHelper.BOOKING_TABLE_NAME + " where LOWER("
                + DatabaseHelper.BOOKING_STATUS + ")" +
                " IN " +
                "('"
                + CommonVariables.STATUS_WAITING.toLowerCase() + "','"
                + CommonVariables.STATUS_CONFIRMED.toLowerCase() + "','"
                + CommonVariables.STATUS_ARRIVED.toLowerCase() + "','"
                + CommonVariables.STATUS_POB.toLowerCase() + "','"
                + CommonVariables.STATUS_POB2.toLowerCase() + "','"
                + CommonVariables.STATUS_STC.toLowerCase() + "','"
                + CommonVariables.STATUS_STC2.toLowerCase() + "','"
                + CommonVariables.STATUS_ONROUTE.toLowerCase()
                + "') ORDER BY "
                + DatabaseHelper.BOOKING_REFNO
                + " DESC";
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Model_BookingDetailsModel obj = new Model_BookingDetailsModel();
                try {
                    obj.setRefrenceNo(cursor.getString(1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setPickUpDate(cursor.getString(2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setPickUpTime(cursor.getString(3));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setReturnDate(cursor.getString(4));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setReturnTime(cursor.getString(5));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setJournyType(Integer.parseInt(cursor.getString(6)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setFromAddressDoorNO(cursor.getString(7));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.settoAddressDoorNO(cursor.getString(8));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setFromAddressFlightNo(cursor.getString(9));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setFromAddressCommingFrom(cursor.getString(10));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setFromAddress(cursor.getString(11));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.settoAddress(cursor.getString(12));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setCusomerName(cursor.getString(13));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setCusomerMobile(cursor.getString(14));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setCusomerPhone(cursor.getString(15));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setCar(cursor.getString(16));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setPaymentType(cursor.getString(17));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    String status = cursor.getString(18);
//                    if (status.equalsIgnoreCase("POB")) {
//                        status = status.replace("POB", "Onboard");
//                    }
//                    if (status.equalsIgnoreCase("PassengerOnBoard")) {
//                        status = status.replace("PassengerOnBoard", "Onboard");
//                    }
                    obj.setStatus(status);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setOneWayFare(cursor.getString(19));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setReturnFare(cursor.getString(20));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setDriverId(cursor.getString(21));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setDriverNo(cursor.getString(22));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setFromAddressType(cursor.getString(23));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.settoAddressType(cursor.getString(24));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setDropLat(cursor.getString(25));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setDropLon(cursor.getString(26));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setPickupLat(cursor.getString(27));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setPickupLon(cursor.getString(28));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setViaPointsAsString(cursor.getString(29));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setTransactionId(cursor.getString(31));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setPassengers(cursor.getString(32));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setluggages(cursor.getString(33));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    obj.setHandluggages(cursor.getString(34));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bookings.add(obj);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return bookings;

    }

    public int getActiveBookingsCount() {

        String selectQuery1;

        selectQuery1 = "SELECT * FROM " + DatabaseHelper.BOOKING_TABLE_NAME + " where LOWER("
                + DatabaseHelper.BOOKING_STATUS + ") IN ('"
                + CommonVariables.STATUS_WAITING.toLowerCase() + "','"
                + CommonVariables.STATUS_CONFIRMED.toLowerCase() + "','"
                + CommonVariables.STATUS_ARRIVED.toLowerCase() + "','"
                + CommonVariables.STATUS_POB.toLowerCase() + "','"
                + CommonVariables.STATUS_POB2.toLowerCase() + "','"
                + CommonVariables.STATUS_STC.toLowerCase() + "','"
                + CommonVariables.STATUS_STC2.toLowerCase() + "','"
                + CommonVariables.STATUS_ONROUTE.toLowerCase() + "') ORDER BY " + DatabaseHelper.BOOKING_REFNO
                + " DESC";


        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery1, null);
        int count = cursor.getCount();
        return count;
    }

    public ArrayList<Model_BookingDetailsModel> getLastCompletedBookings() {
        ArrayList<Model_BookingDetailsModel> bookings = new ArrayList<Model_BookingDetailsModel>();

        String selectQuery = "SELECT * FROM " + DatabaseHelper.BOOKING_TABLE_NAME + " where LOWER(" + DatabaseHelper.BOOKING_STATUS + ") IN ('"
                + CommonVariables.STATUS_COMPLETED.toLowerCase() + "') ORDER BY " + DatabaseHelper.BOOKING_REFNO + " DESC LIMIT 1";

//		String selectQuery = "SELECT  * FROM " + DatabaseHelper.BOOKING_TABLE_NAME +" ORDER BY " + DatabaseHelper.BOOKING_REFNO + " DESC";

        try {
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
            Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    Model_BookingDetailsModel obj = new Model_BookingDetailsModel();
                    obj.setRefrenceNo(cursor.getString(1));
                    obj.setPickUpDate(cursor.getString(2));
                    obj.setPickUpTime(cursor.getString(3));

                    obj.setReturnDate(cursor.getString(4));
                    obj.setReturnTime(cursor.getString(5));

                    obj.setJournyType(Integer.parseInt(cursor.getString(6)));
                    obj.setFromAddressDoorNO(cursor.getString(7));
                    obj.settoAddressDoorNO(cursor.getString(8));
                    obj.setFromAddressFlightNo(cursor.getString(9));
                    obj.setFromAddressCommingFrom(cursor.getString(10));

                    obj.setFromAddress(cursor.getString(11));
                    obj.settoAddress(cursor.getString(12));
                    obj.setCusomerName(cursor.getString(13));
                    obj.setCusomerMobile(cursor.getString(14));
                    obj.setCusomerPhone(cursor.getString(15));
                    obj.setCar(cursor.getString(16));
                    obj.setPaymentType(cursor.getString(17));
                    obj.setStatus(cursor.getString(18));

                    obj.setOneWayFare(cursor.getString(19));
                    obj.setReturnFare(cursor.getString(20));

                    obj.setDriverId(cursor.getString(21));
                    obj.setDriverNo(cursor.getString(22));
                    obj.setFromAddressType(cursor.getString(23));
                    obj.settoAddressType(cursor.getString(24));
                    obj.setDropLat(cursor.getString(25));
                    obj.setDropLon(cursor.getString(26));
                    obj.setPickupLat(cursor.getString(27));
                    obj.setPickupLon(cursor.getString(28));
                    String viaS = cursor.getString(29);

                    if (!viaS.equals("")) {
                        obj.setViaPointsAsString(viaS);
                    }

                    obj.setTransactionId(cursor.getString(31));
                    obj.setPassengers(cursor.getString(32));
                    obj.setluggages(cursor.getString(33));
                    obj.setHandluggages(cursor.getString(34));
                    bookings.add(obj);

                } while (cursor.moveToNext());
            }
            cursor.close();
            mSQLiteDatabase.close();
        } catch (Exception e) {

        }


        return bookings;

    }


    @SuppressLint("Range")
    public List<LocAndField> getAirports() {

        Cursor cursor = null;
        List<LocAndField> StationNames = new ArrayList<LocAndField>();
        try {

/**
 *    @author: KUMAIL RAZA LAKHANI
 *	Date: 25-August-2016
 *	Get results in Alphabetical order Select query changed.
 */
            String selectQuery = "SELECT  * FROM  Airports ORDER BY Id ASC";
//		// OLD CODE
//

/**
 *	Date: 25-August-2016
 *	Get results in Alphabetical order Select query changed. END ->
 */
//			String myPath = DB_PATH + DB_NAME;
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();

            cursor = mSQLiteDatabase.rawQuery(selectQuery, null);
//db.insert()
            if (cursor.moveToFirst()) {
                do {

                    LocAndField Model = new LocAndField();

                    Model.setField(cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS_TEXT)));
                    Model.setLat(cursor.getString(cursor.getColumnIndex(COLUMN_LATITUDE_TEXT)));
                    Model.setLon(cursor.getString(cursor.getColumnIndex(COLUMN_LONGITUDE_TEXT)));
                    StationNames.add(Model);
                } while (cursor.moveToNext());

            }

            mSQLiteDatabase.close();
            cursor.close();

        } catch (Exception e) {

            Log.d("aa", e.getMessage());

        }
        return StationNames;
    }

    public void removeRecentLocations() {

        Cursor cursor = null;
//		ArrayList<LocAndField> arr = new ArrayList<LocAndField>();

        try {

//			String selectQuery = "";
            /*
             * @author: Kumail Raza Lakhani
             * Date: 28-June-2016
             * Query changed for Whiz Cars.
             */

////		OLD CODE
//			selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
//			if(BuildConfig.FLAVOR.equals("whizcars")) {
//			selectQuery = "DELETE  FROM  Stations";
//			} else {
//				selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
//			}
//			String myPath = DB_PATH + DB_NAME;
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
            mSQLiteDatabase.delete("RecentTable", null, null);
//			cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

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

            mSQLiteDatabase.close();
//			cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
//		return arr;
    }

    public void insertStations(List<LocAndField> locAndFields) {
        ContentValues initialValues = new ContentValues();
//		String myPath = DB_PATH + DB_NAME;
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//		db.W

//        mSQLiteDatabase.close();// Closing database connection
        mSQLiteDatabase.beginTransaction();
        try {

            for (LocAndField locAndField : locAndFields) {

                initialValues.put(COLUMN_ADDRESS_TEXT, locAndField.getField());
                try {
                    initialValues.put(COLUMN_POSTCODE_TEXT, getPostalCodeFromAddress(locAndField.getField()));
                } catch (Exception e) {
                    initialValues.put(COLUMN_POSTCODE_TEXT, "");
                }
                initialValues.put(COLUMN_LATITUDE_TEXT, locAndField.getLat());
                initialValues.put(COLUMN_LONGITUDE_TEXT, locAndField.getLon());


                mSQLiteDatabase.insert("Stations", null, initialValues);
            }
            mSQLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mSQLiteDatabase.endTransaction();
        }
    }

    public void removeStations() {

        Cursor cursor = null;
//		ArrayList<LocAndField> arr = new ArrayList<LocAndField>();

        try {

//			String selectQuery = "";
            /*
             * @author: Kumail Raza Lakhani
             * Date: 28-June-2016
             * Query changed for Whiz Cars.
             */

////		OLD CODE
//			selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
//			if(BuildConfig.FLAVOR.equals("whizcars")) {
//			selectQuery = "DELETE  FROM  Stations";
//			} else {
//				selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
//			}
//			String myPath = DB_PATH + DB_NAME;
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
            mSQLiteDatabase.delete("Stations", null, null);
//			cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

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

            mSQLiteDatabase.close();
//			cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
//		return arr;
    }

    public void removeAirports() {

        Cursor cursor = null;
//		ArrayList<LocAndField> arr = new ArrayList<LocAndField>();

        try {

//			String selectQuery = "";
            /*
             * @author: Kumail Raza Lakhani
             * Date: 28-June-2016
             * Query changed for Whiz Cars.
             */

////		OLD CODE
//			selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
//
//			} else {
//				selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
//			}
//			String myPath = DB_PATH + DB_NAME;
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
            mSQLiteDatabase.delete("Airports", null, null);
//			cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

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

            mSQLiteDatabase.close();
//			cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
//		return arr;
    }

    public void insertAirports(List<LocAndField> locAndFields) {
        ContentValues initialValues = new ContentValues();
//		String myPath = DB_PATH + DB_NAME;
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
//		db.W

//        mSQLiteDatabase.close();// Closing database connection
        mSQLiteDatabase.beginTransaction();
        try {

            for (LocAndField locAndField : locAndFields) {

                initialValues.put(COLUMN_ADDRESS_TEXT, locAndField.getField());
                try {
                    initialValues.put(COLUMN_POSTCODE_TEXT, getPostalCodeFromAddress(locAndField.getField()));
                } catch (Exception e) {
                    initialValues.put(COLUMN_POSTCODE_TEXT, "");
                }
                initialValues.put(COLUMN_LATITUDE_TEXT, locAndField.getLat());
                initialValues.put(COLUMN_LONGITUDE_TEXT, locAndField.getLon());


                mSQLiteDatabase.insert("Airports", null, initialValues);
            }
            mSQLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mSQLiteDatabase.endTransaction();
        }
    }

    public void removeStores() {

        Cursor cursor = null;
//		ArrayList<LocAndField> arr = new ArrayList<LocAndField>();

        try {

//			String selectQuery = "";
            /*
             * @author: Kumail Raza Lakhani
             * Date: 28-June-2016
             * Query changed for Whiz Cars.
             */

////		OLD CODE
//			selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
//			if(BuildConfig.FLAVOR.equals("whizcars")) {
//			selectQuery = "DELETE  FROM  Stations";
//			} else {
//				selectQuery = "SELECT  * FROM  Gen_Addresses WHERE address LIKE '%" + keyword + "%' OR postcodeNo LIKE '%" + keyword + "%' LIMIT 50";
//			}
//			String myPath = DB_PATH + DB_NAME;
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
            mSQLiteDatabase.delete("supermarkets", null, null);
//			cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

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

            mSQLiteDatabase.close();
//			cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
//		return arr;
    }
}

