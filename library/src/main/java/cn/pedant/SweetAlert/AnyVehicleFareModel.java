package cn.pedant.SweetAlert;

import java.io.Serializable;

public class AnyVehicleFareModel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 735743164162974251L;
    private String Name;
    private String HandLuggages;
    private String SuitCase;
    private String TotalPassengers;
    private int NoOfPassengers;
    private int NoOfDiffrencePassengers;

    public double CompanyPrice = 0;
    public double ReturnCompanyPrice = 0;
    private double ExtraCharges = 0;
    private double AgentFees = 0;
    private double AgentCharge = 0;
    private double Congestion = 0;
    private double Parking = 0;
    private double Waiting = 0;
    private double AgentCommission = 0;

    private float extraChargesForBabySeat; // SORTORDERNO

    private double finalFares;
    private boolean currentSelect=false;

    public void setCurrentSelect(boolean currentSelect) {
        this.currentSelect = currentSelect;
    }

    public boolean isCurrentSelect() {
        return currentSelect;
    }

    public void setFinalFares(double finalFares) {
        this.finalFares = finalFares;
    }

    public double getFinalFares() {
        return finalFares;
    }

    public float getExtraChargesForBabySeat() {
        return extraChargesForBabySeat;
    }

    public void setExtraChargesForBabySeat(float extraChargesForBabySeat) {
        this.extraChargesForBabySeat = extraChargesForBabySeat;
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

    public double getAgentCommission() {
        return AgentCommission;
    }

    public void setAgentCommission(double agentCommission) {
        AgentCommission = agentCommission;
    }

    public void setCompanyPrice(double companyPrice) {
        CompanyPrice = companyPrice;
    }

    public double getCompanyPrice() {
        return CompanyPrice;
    }


    public void setReturnCompanyPrice(double returnCompanyPrice) {
        ReturnCompanyPrice = returnCompanyPrice;
    }


    public double getReturnCompanyPrice() {
        return ReturnCompanyPrice;
    }

    private String SingleFare;
    private String totalFares;
    private String ReturnFare;
    private boolean ReturnFareCheaked = false;
    private boolean OneWayFareCheaked = true;
    private int serverId;
    private String IsQuoted = "0";
    private double BookingFee;

    public void setBookingFee(double bookingFee) {
        BookingFee = bookingFee;
    }

    public double getBookingFee() {
        return BookingFee;
    }

    public String getIsQuoted() {
        return IsQuoted;
    }

    public void setIsQuoted(String isQuoted) {
        IsQuoted = isQuoted;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public void setName(String arg) {
        this.Name = arg;
    }

    public String getName() {
        return Name;
    }

    public void setNoOfPassengers(int arg) {

        this.NoOfPassengers = arg;
    }

    public Integer getNoOfPassengers() {

        return NoOfPassengers;
    }

    public void setNoOfDifferencePassengers(Integer arg) {

        this.NoOfDiffrencePassengers = arg;
    }

    public Integer getNoOfDifferencePassengers() {

        return NoOfDiffrencePassengers;
    }

    public void setHandLuggages(String arg) {

        this.HandLuggages = arg;
    }

    public String getHandLuggagese() {

        return HandLuggages;
    }

    public void setSuitCase(String arg) {

        this.SuitCase = arg;
    }

    public String getSuitCase() {

        return SuitCase;
    }

    public void setTotalPassengers(String arg) {

        this.TotalPassengers = arg;
    }

    public String getTotalPassengers() {

        return TotalPassengers;
    }

    public void setSingleFare(String arg) {

        this.SingleFare = arg;
    }

    public String getSingleFare() {

        return SingleFare;
    }

    public void setReturnFare(String arg) {

        this.ReturnFare = arg;
    }

    public String getReturnFare() {

        return ReturnFare;
    }

    public void setOneWayFareCheaked(boolean arg) {

        this.OneWayFareCheaked = arg;
        this.ReturnFareCheaked = !arg;
    }

    public boolean isOneWayFareCheaked() {

        return OneWayFareCheaked;
    }

    public void setReturnFareCheaked(boolean arg) {

        this.ReturnFareCheaked = arg;
        this.OneWayFareCheaked = !arg;
    }

    public boolean isReturnFareCheaked() {

        return ReturnFareCheaked;
    }

    public double getExtraCharges() {
        return ExtraCharges;
    }

    public void setExtraCharges(double extraCharges) {
        ExtraCharges = extraCharges;
    }

    public String getTotalFares() {
        return totalFares;
    }

    public void setTotalFares(String totalFares) {
        this.totalFares = totalFares;
    }
}
