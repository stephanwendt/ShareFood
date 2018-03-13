package de.htwds.mada.foodsharing;

import java.util.Calendar;

class Transaction {

    private int id;
    private boolean dealDone;
    private boolean dealReserved;
    private Calendar dealDoneTime; //timestamp
    private boolean dealAborted;
    private User customer; // ist m.E. sinnvoller
    private User offerer; //koennen wir aber auch aendern
    private int customerUID; // ich mach einfach beide varianten rin
    private int offererUID;
    private int ratingID;


    public Transaction() {    }

    public int getId() {        return id;    }
    public void setId(int id) {
        if (id < 0) {
            throw new NumberFormatException(Constants.NOT_NEGATIVE);
        }
        this.id = id;
    }

    public boolean isDealDone() {        return dealDone;    }
    public void setDealDone(boolean dealDone) {
        this.dealDone = dealDone;
    }

    public boolean isDealReserved() {        return dealReserved;    }
    public void setDealReserved(boolean dealReserved) {
        this.dealReserved = dealReserved;
    }

    public Calendar getDealDoneTime() {        return dealDoneTime;    }
    public void setDealDoneTime(Calendar dealDoneTime) {
        this.dealDoneTime = dealDoneTime;
    }

    public boolean isDealAborted() {        return dealAborted;    }
    public void setDealAborted(boolean dealAborted) {
        this.dealAborted = dealAborted;
    }

    public User getCustomer() {        return customer;    }
    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public User getOfferer() {        return offerer;    }
    public void setOfferer(User offerer) {
        this.offerer = offerer;
    }

    public int getCustomerUID() {        return customerUID;    }
    public void setCustomerUID(int customerUID) {
        if (customerUID < 0) {
            throw new NumberFormatException(Constants.NOT_NEGATIVE);
        }
        this.customerUID = customerUID;
    }

    public int getOffererUID() {        return offererUID;    }
    public void setOffererUID(int offererUID) {
        if (offererUID < 0) {
            throw new NumberFormatException(Constants.NOT_NEGATIVE);
        }
        this.offererUID = offererUID;
    }

    public int getRatingID() {        return ratingID;    }
    public void setRatingID(int ratingID) {
        if (ratingID < 0) {
            throw new NumberFormatException(Constants.NOT_NEGATIVE);
        }
        this.ratingID = ratingID;
    }
}
