package com.ltws;

public class Customer extends Person{
    private int rating;
    private int wellDepth;
    private String wellLocation;
    private String wellFlowRate;
    private long ccNumber;
    private int ccExp;
    private int ccCvc;
    private int ccZip;


    public Customer(int ID, String firstName, String lastName, String street, String city, String state, int zip, long phone, String email, int rating, int wellDepth, String wellLocation, String wellFlowRate, Long ccNumber, int ccExp, int ccCvc, int ccZip)
    {
        super(ID, firstName, lastName, email, street, city, state, zip, phone);
        this.rating = rating;
        this.wellDepth = wellDepth;
        this.wellLocation = wellLocation;
        this.wellFlowRate = wellFlowRate;
        this.ccNumber = ccNumber;
        this.ccExp = ccExp;
        this.ccCvc = ccCvc;
        this.ccZip = ccZip;
    }
    public int getRating() { return this.rating; }
    public int getWellDepth() { return this.wellDepth; }
    public String getWellLocation() { return this.wellLocation; }
    public String getWellFlowRate() { return this.wellFlowRate; }

    public long getCcNumber() { return this.ccNumber; }
    public int getCcExp() { return this.ccExp; }
    public int getCcCvc() { return this.ccCvc; }
    public int getCcZip() { return this.ccZip; }

    public void setRating(int rating) { this.rating = rating; }
    public void setWellDepth(int wellDepth)
    {
        this.wellDepth = wellDepth;
    }
    public void setWellLocation(String wellLocation)
    {
        this.wellLocation = wellLocation;
    }
    public void setWellFlowRate(String wellFlowRate)
    {
        this.wellFlowRate = wellFlowRate;
    }

    public void setCcNumber(long ccNumber) { this.ccNumber = ccNumber; }
    public void setCcExp(int ccExp) { this.ccExp = ccExp; }
    public void setCcCvc(int ccCvc) { this.ccCvc = ccCvc; }
    public void setCcZip(int billingZip) { this.ccZip = billingZip; }


}
