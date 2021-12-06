package com.ltws;
import com.ui.LoginPageController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class Job {
    private int ID;
    private int custID;
    private String custName;
    private ArrayList<Integer> jobEmployees;
    private LocalDate jobStart;
    private LocalDate jobEnd;
    private String jobDesc;
    private Boolean invoicePaid;
    private ObservableList<Material> materialOList;
    private ObservableList<Labor> laborOList;
    private ObservableList<Fee> feeOList;

    public Job(int ID, int custID, ArrayList<Integer> jobEmployees, LocalDate jobStart, LocalDate jobEnd, String jobDesc, Boolean invoicePaid, ObservableList<Material> materialOList, ObservableList<Labor> laborOList, ObservableList<Fee> feeOList) {
        this.ID = ID;
        this.custID = custID;
        this.jobEmployees = jobEmployees;
        this.jobStart = jobStart;
        this.jobEnd = jobEnd;
        this.jobDesc = jobDesc;
        this.invoicePaid = invoicePaid;
        this.materialOList = materialOList;
        this.laborOList = laborOList;
        this.feeOList = feeOList;
        setCustomerName();
    }

    public int getID() {
        return ID;
    }

    public int getCustID() {
        return custID;
    }

    public String getCustName() {
        return custName;
    }

    public ArrayList<Integer> getJobEmployees() {
        return jobEmployees;
    }

    public LocalDate getJobStart() {
        return jobStart;
    }

    public LocalDate getJobEnd() {
        return jobEnd;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public Boolean getInvoicePaid() {
        return invoicePaid;
    }

    public ObservableList<Material> getMaterialOList() {
        return materialOList;
    }

    public ObservableList<Labor> getLaborOList() {
        return laborOList;
    }

    public ObservableList<Fee> getFeeOList() {
        return feeOList;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public void setJobEmployees(ArrayList<Integer> jobEmployees) {
        this.jobEmployees = jobEmployees;
    }

    public void setJobStart(LocalDate jobStart) {
        this.jobStart = jobStart;
    }

    public void setJobEnd(LocalDate jobEnd) {
        this.jobEnd = jobEnd;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public void setInvoicePaid(Boolean invoicePaid) {
        this.invoicePaid = invoicePaid;
    }

    public void setMaterialOList(ObservableList<Material> materialOList) {
        this.materialOList = materialOList;
    }

    public void setLaborOList(ObservableList<Labor> laborOList) {
        this.laborOList = laborOList;
    }

    public void setFeeOList(ObservableList<Fee> feeOList) {
        this.feeOList = feeOList;
    }

    public void setCustomerName(){
        for(Customer cust : LoginPageController.allCustomers) {
            if (cust.getID() == this.custID) {
                this.custName = cust.getFirstName() + " " + cust.getLastName();
            }
        }
    }

    public static class Material {
        private String itemDesc;
        private double itemPrice;

        public Material(){
        }

        public Material(String itemDesc, double itemPrice){
            this.itemDesc = itemDesc;
            this.itemPrice = itemPrice;
        }

        public double getItemPrice() {
            return itemPrice;
        }
        public String getItemDesc() {
            return itemDesc;
        }

        public void setItemDesc(String itemDesc) {
            this.itemDesc = itemDesc;
        }
        public void setItemPrice(double itemPrice) {
            this.itemPrice = itemPrice;
        }

        @Override
        public boolean equals ( Object obj ) {
            if ( obj == this ) return true;
            if ( obj == null || obj.getClass () != this.getClass () ) return false;
            var that = ( Material ) obj;
            return  Objects.equals ( this.itemPrice , that.itemPrice ) &&
                    Objects.equals ( this.itemDesc , that.itemDesc );
        }

        @Override
        public int hashCode () {
            return Objects.hash (  itemPrice , itemDesc );
        }
    }

    public static class Labor {
        private String itemDesc;
        private double itemPrice;

        public Labor(String itemDesc, double itemPrice){
            this.itemDesc = itemDesc;
            this.itemPrice = itemPrice;
        }

        public double getItemPrice() {
            return itemPrice;
        }
        public String getItemDesc() {
            return itemDesc;
        }

        public void setItemDesc(String itemDesc) {
            this.itemDesc = itemDesc;
        }
        public void setItemPrice(double itemPrice) {
            this.itemPrice = itemPrice;
        }

        @Override
        public boolean equals ( Object obj ) {
            if ( obj == this ) return true;
            if ( obj == null || obj.getClass () != this.getClass () ) return false;
            var that = ( Labor ) obj;
            return  Objects.equals ( this.itemPrice , that.itemPrice ) &&
                    Objects.equals ( this.itemDesc , that.itemDesc );
        }

        @Override
        public int hashCode () {
            return Objects.hash (  itemPrice , itemDesc );
        }
    }

    public static class Fee {
        private String itemDesc;
        private double itemPrice;

        public Fee(String itemDesc, double itemPrice){
            this.itemDesc = itemDesc;
            this.itemPrice = itemPrice;
        }

        public double getItemPrice() {
            return itemPrice;
        }
        public String getItemDesc() {
            return itemDesc;
        }

        public void setItemDesc(String itemDesc) {
            this.itemDesc = itemDesc;
        }
        public void setItemPrice(double itemPrice) {
            this.itemPrice = itemPrice;
        }

        @Override
        public boolean equals ( Object obj ) {
            if ( obj == this ) return true;
            if ( obj == null || obj.getClass () != this.getClass () ) return false;
            var that = ( Fee ) obj;
            return  Objects.equals ( this.itemPrice , that.itemPrice ) &&
                    Objects.equals ( this.itemDesc , that.itemDesc );
        }

        @Override
        public int hashCode () {
            return Objects.hash (  itemPrice , itemDesc );
        }
    }
}

