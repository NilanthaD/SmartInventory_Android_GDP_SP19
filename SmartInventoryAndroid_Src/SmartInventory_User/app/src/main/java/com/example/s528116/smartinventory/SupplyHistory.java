package com.example.s528116.smartinventory;

import java.util.Date;

public class SupplyHistory {

    private String imageURL;
    private String status;
    private String itemName;
    private long unitPrice;
    private long numberOfUnits;
    private Date requestCreatedDate;
    private String userEmail;
    private String message;
    private long totalValue;
    private String paymentStatus;
    private String itemDocId;
    private String supplyDocId;

    public SupplyHistory(String userEmail, String imageURL, String status, String itemName, long unitPrice, long numberOfUnits, Date requestCreatedDate
            , String message, long totalValue, String paymentStatus, String itemDocId, String supplyDocId) {
        this.imageURL = imageURL;
        this.status = status;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.numberOfUnits = numberOfUnits;
        this.requestCreatedDate = requestCreatedDate;
        this.userEmail = userEmail;
        this.message = message;
        this.totalValue = totalValue;
        this.paymentStatus = paymentStatus;
        this.itemDocId = itemDocId;
        this.supplyDocId = supplyDocId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getStatus() {
        return status;
    }

    public String getItemName() {
        return itemName;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public long getNumberOfUnits() {
        return numberOfUnits;
    }

    public Date getRequestCreatedDate() {
        return requestCreatedDate;
    }

    public String getMessage() {
        return message;
    }

    public long getTotalValue() {
        return totalValue;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getItemDocId() {
        return itemDocId;
    }

    public String getSupplyDocId() { return supplyDocId; }
}


