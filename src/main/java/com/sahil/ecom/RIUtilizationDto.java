package com.sahil.ecom;

public class RIUtilizationDto {
    String reservation;
    String region;
    String operatingSystem;
    String instanceType;
    double reservedHours;
    double usedHours;
    double unusedHours;
    double quantity;
    double netSavings;
    Double usedPercentage;

    String productName;
    Double onDemandCost;
    Integer multiAzVal;


    public RIUtilizationDto() {
    }

    public String getReservation() {
        return reservation;
    }

    public void setReservation(String reservation) {
        this.reservation = reservation;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public double getReservedHours() {
        return reservedHours;
    }

    public void setReservedHours(double reservedHours) {
        this.reservedHours = reservedHours;
    }

    public double getUsedHours() {
        return usedHours;
    }

    public void setUsedHours(double usedHours) {
        this.usedHours = usedHours;
    }

    public double getUnusedHours() {
        return unusedHours;
    }

    public void setUnusedHours(double unusedHours) {
        this.unusedHours = unusedHours;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getNetSavings() {
        return netSavings;
    }

    public void setNetSavings(double netSavings) {
        this.netSavings = netSavings;
    }

    public Double getUsedPercentage() {
        return usedPercentage;
    }

    public void setUsedPercentage(Double usedPercentage) {
        this.usedPercentage = usedPercentage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getOnDemandCost() {
        return onDemandCost;
    }

    public void setOnDemandCost(Double onDemandCost) {
        this.onDemandCost = onDemandCost;
    }

    public Integer getMultiAzVal() {
        return multiAzVal;
    }

    public void setMultiAzVal(Integer multiAzVal) {
        this.multiAzVal = multiAzVal;
    }
}
