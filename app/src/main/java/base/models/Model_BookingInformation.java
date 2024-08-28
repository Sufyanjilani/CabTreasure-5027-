package base.models;

import java.util.ArrayList;

import base.utils.CommonVariables;
import cn.pedant.SweetAlert.ViaAddresses;

/**
 * Created by ayu on 05/09/2017.
 */

public class Model_BookingInformation {
    public Model_BookingInformation() {
        // Via = new ViaAddresses();
    }

    public String DefaultFromAddress;
    public String PostedFrom = "Android";
    public String FromAddress = "";
    public String FromLatLng = "";
    public String ToLatLng = "";
    public String CustomerId = "";
    public String PromotionCode = "";
    public String RouteCoordinates = "";
    public String ToAddress = "";
    public String DefaultToAddress = "";
    public String FromType = "";
    public String ToType = "";
    public String Fares = "";
    public String CompanyId = "";
    public String vehicle = "";
    public String PickupDateTime = "";
    public String SubcompanyId = CommonVariables.SUB_COMPANY;
    public String PickupToDestinationTime ="";
    public float Mileage = 0;
    public String Miles = "";
    public String defaultclientId;

    public ArrayList<ViaAddresses> Via;
    public String FareType = "";
    public String DistanceType = "";
    public String isRebook = "";
    public String PaymentType = "";
    public String flightNo = "";
}

