package com.example.s528116.smartinventory;

import java.net.URL;
import java.util.Date;

public class ItemContainer {
  //  private int image;
    private String itemID;
    private String itemName;
    private long unitPrice;
    private long qntyNeeded;
    private Date requiredBy;
    private String documentId;
    private String userEmail;
    private String imageURL;

    public ItemContainer(String userEmail, String documentId,String imageURL, String itemID, String itemName, Long unitPrice, long qntyNeeded, Date requiredBy) {
        this.userEmail = userEmail;
        this.documentId = documentId;
        this.imageURL = imageURL;
        this.itemID = itemID;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.qntyNeeded = qntyNeeded;
        this.requiredBy = requiredBy;
    }

    public String getUserEmail() {return userEmail;}

    public String getDocumentId() {return documentId; }

    public String getImage() {
        return imageURL;
    }

    public String getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public long getQntyNeeded() {
        return qntyNeeded;
    }

    public Date getRequiredBy() {return requiredBy;}
}
