package base.databaseoperations;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String COLUMN_ADDRESS_TEXT = "address";
    public static final String COLUMN_POSTCODE_TEXT = "postcodeNo";
    public static final String COLUMN_LATITUDE_TEXT = "latitude";
    public static final String COLUMN_LONGITUDE_TEXT = "longitude";
    // All Static variables
    ArrayList<String> vnarr;
    private static final int DATABASE_VERSION = 99;

    // Database Name
    private static final String DATABASE_NAME = "a4DB.db";

    // **********************Table Strings******************//
    // **********************VEHICLE************************//

    // Vehicle table name
    public static final String VEHICLE_TABLE_NAME = "Vehicles";
    public static final String BOOKING_TABLE_NAME = "Booking";
    public static final String RECENT_TABLE_NAME = "RecentTable";
    public static final String FAVOURITES_TABLE_NAME = "Favourites";
    public static final String FAVOURITES_ADDRESS_TABLE_NAME = "FavouritesAddress";
    public static final String USERACCOUNT_TABEL = "ACCOUNT";
    public static final String USERACCOUNT_id = "id";
    public static final String USERACCOUNT_ISVerify = "isverify";
    public static final String USERACCOUNT_CODE = "code";
    public static final String USERACCOUNT_IP = "ip";
    public static final String USERACCOUNT_NAME = "name";
    public static final String USERACCOUNT_EMAIL = "email";
    public static final String USERACCOUNT_MOBILENO = "mobileno";
    public static final String USERACCOUNT_PASSWORD = "password";
    // **********************Table Users******************//

    public static final String USERS_TABLE_NAME = "UsersTable";

    public static final String USER_COLUMN_ID = "id";
    public static final String USER_COLUMN_NAME = "Name";
    public static final String USER_COLUMN_PASSWORD = "Password";
    public static final String USER_COLUMN_VERIFYPASSWORD = "VerifyPassword";
    public static final String USER_COLUMN_EMAIL = "Email";
    public static final String USER_COLUMN_PHONE_NUMBER = "PhoneNumber";

    // VEHICLEDATA Table Columns names

    public static final String VEHICLE_COLUMN_ID = "id"; // Id
    public static final String VEHICLE_COLUMN_SERVER_ID = "server_id"; // Id
    public static final String VEHICLE_COLUMN_NAME = "Name"; // name
    public static final String VEHICLE_COLUMN_TOTALPASSENGERS = "TotalPassengers"; //
    public static final String VEHICLE_COLUMN_TOTALHANDLUGGAGES = "TotalHandLuggages"; //
    public static final String VEHICLE_COLUMN_TOTALLUGGAGES = "TotalLugages"; //
    public static final String VEHICLE_COLUMN_SORTORDERNO = "SortOderNo"; //

    // Info Details Colum Names

    // public static final String VEHICLE_COLUMN_ID = "id"; // Id
    public static final String INFO_TABLE_NAME = "infoTable";
    public static final String PHONE_NUMBER = "PhoneNumber"; // name
    public static final String EMAIL_ADDRESS = "EmailAddress"; //
    public static final String WEBSITE = "Website"; //
    public static final String URLADDRESS = "UrlAddress"; //

    //promo table
    public static final String TABLE_PROMO = "promotable";
    public static final String PR_ID = "pr_id"; // Id
    public static final String PR_CODE = "pr_code"; // name
    public static final String PR_Title = "pr_title"; //
    public static final String PR_Type = "PR_Type";
    public static final String PR_ServerID = "PR_ServerID";
    public static final String PR_USED = "PR_USED";
    public static final String PR_PROMOTYPE = "PR_PROMOTYPE";
    public static final String PR_Max_Discount = "PR_Max_Discount";
    public static final String PR_MinFares = "PR_MinFares";
    public static final String PR_TOTAL = "PR_TOTAL";
    public static final String PR_Value = "PR_Value";
    public static final String PR_MSG = "pr_msg"; //
    public static final String PR_STRTDATE = "pr_strtdate";
    public static final String PR_ENDDATE = "pr_enddate";

    // **********************SETTINGS************************//
    // SETTINGS table name
    public static final String SETTINGS_TABLE_NAME = "Settings";

    // SETTINGS Table Columns names
    public static final String RING_BACK = "RingBack";
    public static final String TEXT_BACK = "TextBack";
    public static final String ADRESS = "Address";
    public static final String DEFAULT_VEHICLE_TYPE = "DefaultVehicleType";
    public static final String ZIP = "Zip";
    public static final String PAYMENT_TYPE = "PaymentType";

    // **********************BookingTable************************//

    public static final String BOOKING_ID = "id"; // Id
    public static final String BOOKING_REFNO = "Refno"; // name
    public static final String BOOKING_PICKUPDATE = "PickupDate"; //
    public static final String BOOKING_PICKUPTIME = "PickupTime"; //
    public static final String BOOKING_RETURNUPDATE = "ReturnDate";
    public static final String BOOKING_RETURNTIME = "ReturnTime";
    public static final String BOOKING_JOURNYTYPE = "JournyType";
    public static final String BOOKING_FROMDOORNO = "FromDoorNO";
    public static final String BOOKING_TODOORNO = "ToDoorNO";
    public static final String BOOKING_FLIGHTNO = "FlightNo";
    public static final String BOOKING_COMMINGFROM = "CommingFrom";
    public static final String BOOKING_PICKUPPOINT = "PickupPoint"; //
    public static final String BOOKING_DESTINATION = "Destination"; //
    public static final String BOOKING_CUSTOMERNAME = "CustomerName"; //
    public static final String BOOKING_CUSTOMERMOBILENO = "CustomerMobileNo"; //
    public static final String BOOKING_CUSTOMERTELEPHONENO = "CustomerTelephoneNo"; //
    public static final String BOOKING_VEHICLENAME = "VehicleName"; //
    public static final String BOOKING_PAYMENTTYPE = "PaymentType"; //
    public static final String BOOKING_STATUS = "Status"; //
    public static final String BOOKING_FARES = "Fares"; //
    public static final String BOOKING_RETURNFARES = "ReturnFares"; //
    public static final String BOOKING_DRIVERID = "DriverId"; //
    public static final String BOOKING_DRIVERNO = "DriverNo"; //
    public static final String BOOKING_FROMLOCTYPE = "FromLocType"; //
    public static final String BOOKING_TOLOCTYPE = "ToLocType"; //
    public static final String BOOKING_TOLOCLAT = "DropLat";
    public static final String BOOKING_TOLOCLON = "DropLon";
    public static final String BOOKING_FROMLOCLAT = "PickLat";
    public static final String BOOKING_FROMLOCLON = "PickLon";
    public static final String BOOKING_VIAADD = "ViaAdd";
    public static final String BOOKING_USERNAME = "bookingusername";
    public static final String BOOKING_TRANSACTION_ID = "bookingTransactionId";

    public static final String BOOKING_Waiting = "Waiting";
    public static final String BOOKING_Parking = "Parking";
    public static final String BOOKING_Congestion = "Congestion";
    public static final String BOOKING_AgentCharge = "AgentCharge";
    public static final String BOOKING_AgentFees = "AgentFees";
    public static final String BOOKING_ExtraDropCharges = "ExtraDropCharges";
    public static final String BOOKING_MeetAndGreetCharges = "MeetAndGreetCharges";
    public static final String BOOKING_ServicesCharges = "ServicesCharges";
    public static final String BOOKING_AgentCommission = "AgentCommission";
    public static final String BOOKING_CompanyPrice = "CompanyPrice";
    public static final String BOOKING_FareRate = "FareRate";


    // **********************FAVOURITES Table************************//

    public static final String FAVOURITES_ID = "id"; // Id
    public static final String FAVOURITES_REFNO = "Refno"; // name
    public static final String FAVOURITES_PICKUPDATE = "PickupDate"; //
    public static final String FAVOURITES_PICKUPTIME = "PickupTime"; //

    public static final String FAVOURITES_RETURNUPDATE = "ReturnDate";
    public static final String FAVOURITES_RETURNTIME = "ReturnTime";
    public static final String FAVOURITES_JOURNYTYPE = "JournyType";

    public static final String FAVOURITES_FROMDOORNO = "FromDoorNO";
    public static final String FAVOURITES_TODOORNO = "ToDoorNO";

    public static final String FAVOURITES_FLIGHTNO = "FlightNo";
    public static final String FAVOURITES_COMMINGFROM = "CommingFrom";

    public static final String FAVOURITES_PICKUPPOINT = "PickupPoint"; //
    public static final String FAVOURITES_DESTINATION = "Destination"; //
    public static final String FAVOURITES_CUSTOMERNAME = "CustomerName"; //
    public static final String FAVOURITES_CUSTOMERMOBILENO = "CustomerMobileNo"; //
    public static final String FAVOURITES_CUSTOMERTELEPHONENO = "CustomerTelephoneNo"; //
    public static final String FAVOURITES_VEHICLENAME = "VehicleName"; //
    public static final String FAVOURITES_PAYMENTTYPE = "PaymentType"; //
    public static final String FAVOURITES_STATUS = "Status"; //
    public static final String FAVOURITES_FARES = "Fares"; //
    public static final String FAVOURITES_RETURNFARES = "ReturnFares"; //
    public static final String FAVOURITES_DRIVERID = "DriverId"; //
    public static final String FAVOURITES_DRIVERNO = "DriverNo"; //
    public static final String FAVOURITES_FROMLOCTYPE = "FromLocType"; //
    public static final String FAVOURITES_TOLOCTYPE = "ToLocType"; //
    public static final String FAVOURITES_TOLOCLAT = "DropLat";
    public static final String FAVOURITES_TOLOCLON = "DropLon";
    public static final String FAVOURITES_FROMLOCLAT = "PickLat";
    public static final String FAVOURITES_FROMLOCLON = "PickLon";
    public static final String FAVOURITES_VIAADD = "ViaAdd";
    public static final String FAVOURITES_TRANSACTIONID = "transactionID";

    public static final String Google_LAT = "lats";
    public static final String Google_LON = "Lons";


    /**
     * Syed Bilal ali
     * Added new column favAddUserid for apps with login feature. (Like Hornet Cars app).
     */
    public static final String FAVOURITES_USERNAME = "favUsername";
// END ->

    // **********************FAVOURITE Address Table************************//

    public static final String FAVOURITES_ADDRESS_ID = "id"; // Id
    public static final String FAVOURITES_ADDRESS_ADDRESS = "Address"; // name
    public static final String FAVOURITES_ADDRESS_DOORNO = "DoorNo"; //
    public static final String FAVOURITES_ADDRESS_LAT = "Lat"; //
    public static final String FAVOURITES_ADDRESS_LON = "Lon";
    /**
     * @author: Kumail Raza Lakhani
     * Date: 30-August-2016
     * Added new column favAddUserid for apps with login feature. (Like Hornet Cars app).
     */
    public static final String FAVOURITES_ADDRESS_USERNAME = "favAddUsername";
// END ->

    public static final String INFO_ID = "id";
    public static final String INFO_PHONE = "phone";
    public static final String INFO_EMAIL = "email";
    public static final String INFO_WEBSITE = "web";
    public static final String INFO_POWERED_BY = "powered";
    public static final String TABLE_INFO = "info";

    public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }// End constructor

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }// End constructor

    public void onCreate(SQLiteDatabase db) {
        // Vehicle table create query
        CreateVehicleTable(db);
        BookingTable(db);
        FavouritesTable(db);
        FavAddressTable(db);
        CreateUSERACCOUNTTable(db);
        InfoTable(db);
        PromoTable(db);
        Stations(db);
        RecentLocations(db);
        Airports(db);
        Stores(db);

    }// End onCreate()

    private void CreateUSERACCOUNTTable(SQLiteDatabase mdb) {

        String query = "CREATE TABLE IF NOT EXISTS " + USERACCOUNT_TABEL + "(" + USERACCOUNT_id + " INTEGER" + " PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE,"
                + USERACCOUNT_ISVerify + " VARCHAR," + USERACCOUNT_CODE + " VARCHAR," + USERACCOUNT_IP + " VARCHAR," + USERACCOUNT_NAME + " VARCHAR,"
                + USERACCOUNT_EMAIL + " VARCHAR," + USERACCOUNT_MOBILENO + " VARCHAR," + USERACCOUNT_PASSWORD + " VARCHAR)";

        mdb.execSQL(query);
        Log.i("Table", "Vehicles Created Successfully...");

    }

    private void CreateVehicleTable(SQLiteDatabase mdb) {

        try {
            String query = "CREATE TABLE IF NOT EXISTS Vehicles"
                    + "(id INTEGER" + " PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE,"
                    + "Name VARCHAR,"
                    + "TotalPassengers VARCHAR,"
                    + "TotalHandLuggages VARCHAR,"
                    + "TotalLugages VARCHAR,"
                    + "SortOderNo VARCHAR,"
                    + "server_id INTEGER"
                    + ")";

            mdb.execSQL(query);
            Log.i("Table", "Vehicles Created Successfully...");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void FavAddressTable(SQLiteDatabase mdb) {

        String query = "CREATE TABLE IF NOT EXISTS " + FAVOURITES_ADDRESS_TABLE_NAME + "("
                + FAVOURITES_ADDRESS_ID + " INTEGER" + " PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE,"
                + FAVOURITES_ADDRESS_ADDRESS + " VARCHAR," + FAVOURITES_ADDRESS_DOORNO + " VARCHAR,"
                + FAVOURITES_ADDRESS_LAT + " VARCHAR," + FAVOURITES_ADDRESS_LON + " VARCHAR,"
                + FAVOURITES_ADDRESS_USERNAME + " VARCHAR)";

        mdb.execSQL(query);
        Log.i("Table", "FavAddressTable Created Successfully...");
    }

    private void BookingTable(SQLiteDatabase mdb) {

        String CREATE_BOOKING_TABLE = "CREATE TABLE IF NOT EXISTS " + BOOKING_TABLE_NAME +
                "(" + BOOKING_ID + " INTEGER" + " PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE,"
                + BOOKING_REFNO + " VARCHAR,"//1

                + BOOKING_PICKUPDATE + " VARCHAR,"//2
                + BOOKING_PICKUPTIME + " VARCHAR,"//3
                + BOOKING_RETURNUPDATE + " VARCHAR,"//4
                + BOOKING_RETURNTIME + " VARCHAR,"//5

                + BOOKING_JOURNYTYPE + " VARCHAR,"//6
                + BOOKING_FROMDOORNO + " VARCHAR,"//7
                + BOOKING_TODOORNO + " VARCHAR,"//8
                + BOOKING_FLIGHTNO + " VARCHAR,"//9
                + BOOKING_COMMINGFROM + " VARCHAR,"//10

                + BOOKING_PICKUPPOINT + " VARCHAR,"//11
                + BOOKING_DESTINATION + " VARCHAR,"//12
                + BOOKING_CUSTOMERNAME + " VARCHAR,"//13
                + BOOKING_CUSTOMERMOBILENO + "  VARCHAR,"//14
                + BOOKING_CUSTOMERTELEPHONENO + " VARCHAR,"//15

                + BOOKING_VEHICLENAME + " VARHCAR, "//16
                + BOOKING_PAYMENTTYPE + "  VARCHAR,"//17
                + BOOKING_STATUS + "  VARCHAR,"//18
                + BOOKING_FARES + " VARCHAR,"//19
                + BOOKING_RETURNFARES + "  VARCHAR,"//20

                + BOOKING_DRIVERID + "  VARCHAR,"//21
                + BOOKING_DRIVERNO + "  VARCHAR,"//22
                + BOOKING_FROMLOCTYPE + "  VARCHAR,"//23
                + BOOKING_TOLOCTYPE + "  VARCHAR,"//24
                + BOOKING_TOLOCLAT + "  VARCHAR,"//25


                + BOOKING_TOLOCLON + "  VARCHAR,"//26
                + BOOKING_FROMLOCLAT + "  VARCHAR,"//27
                + BOOKING_FROMLOCLON + "  VARCHAR,"//28
                + BOOKING_VIAADD + "  VARCHAR,"//29
                + BOOKING_USERNAME + "  VARCHAR,"//30
                + BOOKING_TRANSACTION_ID + " VARCHAR,"//31

                + BOOKING_Waiting + " VARCHAR,"//32
                + BOOKING_Parking + " VARCHAR,"//33
                + BOOKING_Congestion + " VARCHAR,"//34
                + BOOKING_AgentCharge + " VARCHAR,"//35

                + BOOKING_AgentFees + " VARCHAR,"//36
                + BOOKING_ExtraDropCharges + " VARCHAR,"//37
                + BOOKING_MeetAndGreetCharges + " VARCHAR,"//38
                + BOOKING_ServicesCharges + " VARCHAR,"//39
                + BOOKING_AgentCommission + " VARCHAR,"//40
                + BOOKING_CompanyPrice + " VARCHAR,"//41
                + BOOKING_FareRate + " VARCHAR,"//42


                + VEHICLE_COLUMN_TOTALPASSENGERS + " VARCHAR,"//43
                + VEHICLE_COLUMN_TOTALLUGGAGES + " VARCHAR,"//44
                + VEHICLE_COLUMN_TOTALHANDLUGGAGES + " VARCHAR" + ")";//45

        mdb.execSQL(CREATE_BOOKING_TABLE);
        Log.i("Table", "BOOKING_TABLE Created Successfully...");
    }


    private void FavouritesTable(SQLiteDatabase mdb) {

        String CREATE_BOOKING_TABLE = "CREATE TABLE IF NOT EXISTS " + FAVOURITES_TABLE_NAME + "("
                + FAVOURITES_ID + " INTEGER" + " PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE,"
                + FAVOURITES_REFNO + " VARCHAR,"
                + FAVOURITES_PICKUPDATE + " VARCHAR,"
                + FAVOURITES_PICKUPTIME + " VARCHAR,"
                + FAVOURITES_RETURNUPDATE + " VARCHAR,"
                + FAVOURITES_RETURNTIME + " VARCHAR,"
                + FAVOURITES_JOURNYTYPE + " VARCHAR,"
                + FAVOURITES_FROMDOORNO + " VARCHAR,"
                + FAVOURITES_TODOORNO + " VARCHAR,"
                + FAVOURITES_FLIGHTNO + " VARCHAR,"
                + FAVOURITES_COMMINGFROM + " VARCHAR,"
                + FAVOURITES_PICKUPPOINT + " VARCHAR,"
                + FAVOURITES_DESTINATION + " VARCHAR,"
                + FAVOURITES_CUSTOMERNAME + " VARCHAR,"
                + FAVOURITES_CUSTOMERMOBILENO + "  VARCHAR,"
                + FAVOURITES_CUSTOMERTELEPHONENO + " VARCHAR,"
                + FAVOURITES_VEHICLENAME + " VARHCAR, "
                + FAVOURITES_PAYMENTTYPE + "  VARCHAR,"
                + FAVOURITES_STATUS + "  VARCHAR,"
                + FAVOURITES_FARES + " VARCHAR,"
                + FAVOURITES_RETURNFARES + "  VARCHAR,"
                + FAVOURITES_DRIVERID + "  VARCHAR,"
                + FAVOURITES_DRIVERNO + "  VARCHAR,"
                + FAVOURITES_FROMLOCTYPE + "  VARCHAR,"
                + FAVOURITES_TOLOCTYPE + "  VARCHAR,"
                + FAVOURITES_TOLOCLAT + "  VARCHAR,"
                + FAVOURITES_TOLOCLON + "  VARCHAR,"
                + FAVOURITES_FROMLOCLAT + "  VARCHAR,"
                + FAVOURITES_FROMLOCLON + "  VARCHAR,"
                + FAVOURITES_VIAADD + "  VARCHAR,"
                + FAVOURITES_TRANSACTIONID + " VARCHAR,"
                + FAVOURITES_USERNAME + " VARCHAR" + ")";
        mdb.execSQL(CREATE_BOOKING_TABLE);
        Log.i("log", CREATE_BOOKING_TABLE);
    }

    private void InfoTable(SQLiteDatabase mDb) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_INFO + "(" + INFO_ID + " INTEGER" + " PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," + INFO_PHONE + " VARCHAR,"
                + INFO_EMAIL + " VARCHAR," + INFO_WEBSITE + " VARCHAR," + INFO_POWERED_BY + " VARCHAR" + ")";

        mDb.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

/*        try {
            String alter = "ALTER TABLE " + BOOKING_TABLE_NAME + "  ADD COLUMN server_id INTEGER default 0";
            db.execSQL(alter);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        try {
            String deleteQuery5 = "DROP TABLE IF EXISTS " + VEHICLE_TABLE_NAME;
            db.execSQL(deleteQuery5);
            String deleteQuery = "DROP TABLE IF EXISTS " + BOOKING_TABLE_NAME;
            db.execSQL(deleteQuery);
            String deleteQuery1 = "DROP TABLE IF EXISTS " + TABLE_PROMO;
            db.execSQL(deleteQuery1);
            String deleteQuery2 = "DROP TABLE IF EXISTS Airports";
            db.execSQL(deleteQuery2);
        } catch (Exception e) {
        }

        try {
            BookingTable(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            CreateVehicleTable(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            Airports(db);
//            PromoTable(db);
//            Stations(db);
//            RecentLocations(db);
//            FavAddressTable(db);
//
//            Stores(db);
//        } catch (Exception e) {
//
//        }
    }

    private void PromoTable(SQLiteDatabase mDb) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_PROMO + "(" + PR_ID + " INTEGER" + " PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," + PR_CODE + " VARCHAR,"
                + PR_Title + " VARCHAR," + PR_Type + " VARCHAR," + PR_USED + " VARCHAR," + PR_Max_Discount + " VARCHAR," + PR_MinFares + " VARCHAR," + PR_PROMOTYPE + " VARCHAR," + PR_TOTAL + " VARCHAR," + PR_ServerID + " VARCHAR," + PR_Value + " VARCHAR," + PR_MSG + " VARCHAR," + PR_STRTDATE + " VARCHAR," + PR_ENDDATE + " VARCHAR" + ")";

        mDb.execSQL(query);
    }

    private void Stations(SQLiteDatabase mDb) {
        try {
            String query = "CREATE TABLE IF NOT EXISTS Stations(Id INTEGER" + " PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," + COLUMN_ADDRESS_TEXT + " VARCHAR,"
                    + COLUMN_POSTCODE_TEXT + " VARCHAR," + COLUMN_LATITUDE_TEXT + " VARCHAR," + COLUMN_LONGITUDE_TEXT + " VARCHAR" + ")";

            mDb.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void RecentLocations(SQLiteDatabase mDb) {
        try {
            String query = "CREATE TABLE IF NOT EXISTS RecentTable(Id INTEGER" + " PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," + COLUMN_ADDRESS_TEXT + " VARCHAR NOT NULL UNIQUE,"
                    + BOOKING_TOLOCTYPE + " VARCHAR," + COLUMN_POSTCODE_TEXT + " VARCHAR," + COLUMN_LATITUDE_TEXT + " VARCHAR," + COLUMN_LONGITUDE_TEXT + " VARCHAR" + ")";

            mDb.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Airports(SQLiteDatabase mDb) {
        try {
            String query = "CREATE TABLE IF NOT EXISTS Airports(Id INTEGER" + " PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," + COLUMN_ADDRESS_TEXT + " VARCHAR,"
                    + COLUMN_POSTCODE_TEXT + " VARCHAR," + COLUMN_LATITUDE_TEXT + " VARCHAR," + COLUMN_LONGITUDE_TEXT + " VARCHAR" + ")";

            mDb.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    private void Stations(SQLiteDatabase mDb) {
//        try {
//            String query = "CREATE TABLE IF NOT EXISTS Stations(Id INTEGER" + " PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," + COLUMN_ADDRESS_TEXT + " VARCHAR,"
//                    + COLUMN_POSTCODE_TEXT + " VARCHAR," + COLUMN_LATITUDE_TEXT + " VARCHAR," + COLUMN_LONGITUDE_TEXT + " VARCHAR" + ")";
//
//            mDb.execSQL(query);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    private void Stores(SQLiteDatabase mDb) {
        try {
            String query = "CREATE TABLE IF NOT EXISTS supermarkets(Id INTEGER" + " PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," + COLUMN_ADDRESS_TEXT + " VARCHAR,"
                    + COLUMN_POSTCODE_TEXT + " VARCHAR," + COLUMN_LATITUDE_TEXT + " VARCHAR," + COLUMN_LONGITUDE_TEXT + " VARCHAR" + ")";

            mDb.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

