package base.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Model_BookingDetailsModel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2093683055378780503L;

    private String setOrderDetails = "";
    private String IsQuoted = "0";
    private double promoDiscountedValue = 0;
    private double ExtraCharges = 0;
    private double AgentFees = 0;
    private double AgentCharge = 0;
    private double Congestion = 0;
    private double Parking = 0;
    private double Waiting = 0;
    private double AgentCommission = 0;
    private double extraTotal = 0.0f;
    private double fareRate = 0;
    private double extraDropCharges = 0;
    private double meetAndGreet = 0;
    private double serviceCharges = 0;
    private double BookingFee = 0;
    private double SurgePrice = 0;
    private double DriverRating = 0;
    private String DriverName = "";
    private String VehicleNo="";
    private String VehicleType="";
    private String DriverImageUrl="";

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public void setDriverImageUrl(String driverImageUrl) {
        DriverImageUrl = driverImageUrl;
    }

    public void setDriverRating(double driverRating) {
        DriverRating = driverRating;
    }

    public double getDriverRating() {
        return DriverRating;
    }

    public String getDriverImageUrl() {
        return DriverImageUrl;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setPromoDiscountedValue(double promoDiscountedValue) {
        this.promoDiscountedValue = promoDiscountedValue;
    }

    public double getPromoDiscountedValue() {
        return promoDiscountedValue;
    }

    public void setSurgePrice(double surgePrice) {
        SurgePrice = surgePrice;
    }

    public double getSurgePrice() {
        return SurgePrice;
    }

    public void setBookingFee(double bookingFee) {
        BookingFee = bookingFee;
    }

    public double getBookingFee() {
        return BookingFee;
    }

    public double getServiceCharges() {
        return serviceCharges;
    }

    public void setServiceCharges(double serviceCharges) {
        this.serviceCharges = serviceCharges;
    }

    public double getMeetAndGreet() {
        return meetAndGreet;
    }

    public void setMeetAndGreet(double meetAndGreet) {
        this.meetAndGreet = meetAndGreet;
    }

    public double getExtraDropCharges() {
        return extraDropCharges;
    }

    public void setExtraDropCharges(double extraDropCharges) {
        this.extraDropCharges = extraDropCharges;
    }

    private double CompanyPrice = 0;


    public void setFareRate(double fareRate) {
        this.fareRate = fareRate;
    }

    public double getFareRate() {
        return fareRate;
    }

    public void setExtraTotal(double extraTotal) {
        this.extraTotal = extraTotal;
    }

    public double getExtraTotal() {
        return extraTotal;
    }

    public double getCompanyPrice() {
        return CompanyPrice;
    }

    public void setCompanyPrice(double companyPrice) {
        CompanyPrice = companyPrice;
    }

    public double getExtraCharges() {
        return ExtraCharges;
    }

    public double getAgentCommission() {
        return AgentCommission;
    }

    public void setAgentCommission(double agentCommission) {
        AgentCommission = agentCommission;
    }

    public void setExtraCharges(double extraCharges) {
        ExtraCharges = extraCharges;
    }

    public double getAgentFees() {
        return AgentFees;
    }

    public void setAgentFees(double agentFees) {
        AgentFees = agentFees;
    }

    public double getAgentCharge() {
        return AgentCharge;
    }

    public void setAgentCharge(double agentCharge) {
        AgentCharge = agentCharge;
    }

    public double getCongestion() {
        return Congestion;
    }

    public void setCongestion(double congestion) {
        Congestion = congestion;
    }

    public double getParking() {
        return Parking;
    }

    public void setParking(double parking) {
        Parking = parking;
    }

    public double getWaiting() {
        return Waiting;
    }

    public void setWaiting(double waiting) {
        Waiting = waiting;
    }

    public void setIsQuoted(String isQuoted) {
        IsQuoted = isQuoted;
    }

    public String getIsQuoted() {
        return IsQuoted;
    }

    public void setSetOrderDetails(String setOrderDetails) {
        this.setOrderDetails = setOrderDetails;
    }

    public String getSetOrderDetails() {
        return setOrderDetails;
    }

    private String orderSummary = "";

    public void setorderSummary(String orderSummary) {
        this.orderSummary = orderSummary;
    }

    public String getorderSummary() {
        return orderSummary;
    }

    private String orderName = "";

    public void setorderName(String orderName) {
        this.orderName = orderName;
    }

    public String getorderName() {
        return orderName;
    }

    private String FromAddress = "";
    private String FromAddressType = "";
    private String FromAddressDoorNO = "";
    private String FromAddressFlightNo = "";
    private String FromAddressCommingFrom = "";
    private String toAddress = "";
    private String toAddressType = "";
    private String toAddressDoorNO = "";
    private String Car = "";
    private String PickUpTime = "", PickUpDate = "";
    private int JournyType;
    private String OneWayFare = "";
    private String ReturnFare = "";
    private String ReturnTime = "", ReturnDate = "";
    private String SpecialNotes = "";
    private ArrayList<String> viaAddresses = new ArrayList<String>();
    private String PaymentType = "";
    private String Status = "";
    private String RefrenceNo = "";
    private String CusomerName = "";
    private String CusomerPhone = "";
    private String CusomerMobile = "";
    private String CusomerEmail = "";
    private String DriverId;
    private String DriverNo;
    private String plat = "0";
    private String plon = "0";
    private String dlat = "0";
    private String dlon = "0";
    private String Passengers = "0";
    private String Lugages = "0";
    private String orignalFares = "0";
    private String HandLuggages = "0";
    private String ViaAddString = "";
    private ArrayList<String> viaAddressesQuote = new ArrayList<String>();
    private String mTransactionId = "";
    /*
     *	@author: Kumail Raza Lakhani
     *	Date: 23-June-2016
     */
    private String PaymentID;
    private String CreateTime;
    private String PaymentStatus;
    private String fromLatLng;
    private String toLatLng;

    private String viaPointsAsString;

    public void setViaPointsAsString(String viaPointsAsString) {
        this.viaPointsAsString = viaPointsAsString;
    }

    public String getViaPointsAsString() {
        return viaPointsAsString;
    }

    public String getFromLatLng() {
        return fromLatLng;
    }

    public String getToLatLng() {
        return toLatLng;
    }

    public void setFromLatLng(String fromLatLng) {
        this.fromLatLng = fromLatLng;
    }

    public void setToLatLng(String toLatLng) {
        this.toLatLng = toLatLng;
    }

    /*
     *	Date: 23-June-2016
     *	END ->
     */

    public Model_BookingDetailsModel() {
    }

    public Model_BookingDetailsModel(Model_BookingDetailsModel other) {
        this.Car = other.Car;
        this.CusomerEmail = other.CusomerEmail;
        this.CusomerMobile = other.CusomerMobile;
        this.CusomerName = other.CusomerName;
        this.CusomerPhone = other.CusomerPhone;
        this.dlat = other.dlat;
        this.dlon = other.dlon;
        this.DriverId = other.DriverId;
        this.DriverNo = other.DriverNo;
        this.FromAddress = other.FromAddress;
        this.FromAddressCommingFrom = other.FromAddressCommingFrom;
        this.FromAddressDoorNO = other.FromAddressDoorNO;
        this.FromAddressFlightNo = other.FromAddressFlightNo;
        this.FromAddressType = other.FromAddressType;
        this.HandLuggages = other.HandLuggages;
        this.JournyType = other.JournyType;
        this.Lugages = other.Lugages;
        this.mTransactionId = other.mTransactionId;
        this.OneWayFare = other.OneWayFare;
        this.Passengers = other.Passengers;
        this.PaymentType = other.PaymentType;
        this.PickUpDate = other.PickUpDate;
        this.PickUpTime = other.PickUpTime;
        this.plat = other.plat;
        this.plon = other.plon;
        this.RefrenceNo = other.RefrenceNo;
        this.ReturnDate = other.ReturnDate;
        this.ReturnFare = other.ReturnFare;
        this.ReturnTime = other.ReturnTime;
        this.SpecialNotes = other.SpecialNotes;
        this.Status = other.Status;
        this.toAddress = other.toAddress;
        this.toAddressDoorNO = other.toAddressDoorNO;
        this.toAddressType = other.toAddressType;

        this.viaAddresses = new ArrayList<String>();
        this.viaAddresses.addAll(other.viaAddresses);

        this.viaAddressesQuote = new ArrayList<String>();
        this.viaAddressesQuote.addAll(other.viaAddressesQuote);

        this.ViaAddString = other.ViaAddString;
        /*
         *	@author: Kumail Raza Lakhani
         *	Date: 23-June-2016
         */
        this.PaymentID = other.PaymentID;
        this.CreateTime = other.CreateTime;
        this.PaymentStatus = other.PaymentStatus;
        /*
         *	Date: 23-June-2016
         *	END ->
         */
    }

    public void setCusomerEmail(String arg) {

        this.CusomerEmail = arg;
    }

    public String getCusomerEmail() {

        return CusomerEmail;
    }

    public void setPassengers(String arg) {

        this.Passengers = arg;
    }

    public String getPassengers() {

        return Passengers;
    }

    public void setOrignalFares(String arg) {

        this.orignalFares = arg;
    }

    public String getOrignalFares() {

        return orignalFares;
    }

    public void setluggages(String arg) {

        this.Lugages = arg;
    }

    public String getluggages() {

        return Lugages;
    }

    public void setHandluggages(String arg) {

        this.HandLuggages = arg;
    }

    public String getHandluggages() {

        return HandLuggages;
    }

    public void setPickupLat(String arg) {

        this.plat = arg;
    }

    public String getPickupLat() {

        return plat;
    }

    public void setPickupLon(String arg) {

        this.plon = arg;
    }

    public String getPickupLon() {

        return plon;
    }

    public void setDropLat(String arg) {

        this.dlat = arg;
    }

    public String getDropLat() {

        return dlat;
    }

    public void setDropLon(String arg) {

        this.dlon = arg;
    }

    public String getDropLon() {

        return dlon;
    }

    public void setFromAddress(String arg) {
        this.FromAddress = arg;
    }

    public String getFromAddress() {
        return FromAddress;
    }

    public void setFromAddressType(String arg) {
        this.FromAddressType = arg;
    }

    public String getFromAddressType() {

        return FromAddressType;
    }

    public void setFromAddressDoorNO(String arg) {

        this.FromAddressDoorNO = arg;
    }

    public String getFromAddressDoorNO() {

        return FromAddressDoorNO;
    }

    public void setFromAddressFlightNo(String arg) {

        this.FromAddressFlightNo = arg;
    }

    public String getFromAddressFlightNo() {

        return FromAddressFlightNo;
    }

    public void setFromAddressCommingFrom(String arg) {

        this.FromAddressCommingFrom = arg;
    }

    public String getFromAddressCommingFrom() {

        return FromAddressCommingFrom;
    }

    public void settoAddress(String arg) {

        this.toAddress = arg;
    }

    public String gettoAddress() {

        return toAddress;
    }

    public void settoAddressType(String arg) {

        this.toAddressType = arg;
    }

    public String gettoAddressType() {

        return toAddressType;
    }

    public void settoAddressDoorNO(String arg) {

        this.toAddressDoorNO = arg;
    }

    public String gettoAddressDoorNO() {

        return toAddressDoorNO;
    }

    public void setCar(String arg) {

        this.Car = arg;
    }

    public String getCar() {

        return Car;
    }

    public void setJournyType(int arg) {

        this.JournyType = arg;
    }

    public int getJournyType() {

        return JournyType;
    }

    public void setOneWayFare(String arg) {

        this.OneWayFare = arg;
    }

    public String getOneWayFare() {

        return OneWayFare;
    }

    public void setReturnFare(String arg) {

        this.ReturnFare = arg;
    }

    public String getReturnFare() {

        return ReturnFare;
    }

    public void setReturnTime(String arg) {

        this.ReturnTime = arg;
    }

    public String getReturnTime() {

        return ReturnTime;
    }

    public void setReturnDate(String arg) {

        this.ReturnDate = arg;
    }

    public String getReturnDate() {

        return ReturnDate;
    }

    public void setSpecialNotes(String arg) {

        this.SpecialNotes = arg;
    }

    public String getSpecialNotes() {

        return SpecialNotes;
    }

    public void setPickUpTime(String arg) {

        this.PickUpTime = arg;
    }

    public String getPickUpTime() {

        return PickUpTime;
    }

    public void setPickUpDate(String arg) {

        this.PickUpDate = arg;
    }

    public String getPickUpDate() {

        return PickUpDate;
    }

    public void setPaymentType(String arg) {

        this.PaymentType = arg;
    }

    public String getPaymentType() {

        return PaymentType;
    }

    public void setStatus(String arg) {

        this.Status = arg;
    }

    public String getStatus() {

        return Status;
    }

    public void setRefrenceNo(String arg) {

        this.RefrenceNo = arg;
    }

    public String getRefrenceNo() {

        return RefrenceNo;
    }

    public void setCusomerName(String arg) {

        this.CusomerName = arg;
    }

    public String getCusomerName() {

        return CusomerName;
    }

    public void setCusomerMobile(String arg) {

        this.CusomerMobile = arg;
    }

    public String getCusomerMobile() {

        return CusomerMobile;
    }

    public void setCusomerPhone(String arg) {

        this.CusomerPhone = arg;
    }

    public String getCusomerPhone() {

        return CusomerPhone;
    }

    public String getDriverNo() {
        return DriverNo;
    }

    public void setDriverNo(String driverNo) {
        DriverNo = driverNo;
    }

    public String getDriverId() {
        return DriverId;
    }

    public void setDriverId(String driverId) {
        DriverId = driverId;
    }

    public void setViaPoints(ArrayList<String> viaAddresses) {
        // TODO Auto-generated method stub

        this.viaAddresses = viaAddresses;
    }

    public ArrayList<String> getViaPoints() {
        // TODO Auto-generated method stub

        return viaAddresses;
    }

    public void setViaPointsQuote(ArrayList<String> viaAddressesQuote) {
        // TODO Auto-generated method stub

        this.viaAddressesQuote = viaAddressesQuote;
    }

    public ArrayList<String> getViaPointsQuote() {
        // TODO Auto-generated method stub

        return viaAddressesQuote;
    }

    public void setTransactionId(String id) {
        this.mTransactionId = id;
    }

    public String getTransactionId() {
        return mTransactionId;
    }


    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getPaymentID() {
        return PaymentID;
    }

    public void setPaymentID(String paymentID) {
        PaymentID = paymentID;
    }

    private float surchargeAmount = 0.0f;

    public void setsurchargeAmount(float arg) {
        this.surchargeAmount = arg;
    }

    public float getSurchargeAmount() {
        return surchargeAmount;
    }
}

