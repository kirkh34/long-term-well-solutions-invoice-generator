package com.ltws;

public class Employee extends Person{
    private int ssn;
    private long dln;
    private String username;
    private String password;
    private boolean isAdmin;

    public Employee(int ID, String firstName, String lastName, String email, String street, String city, String state, int zip, int phone, int ssn, long dln, String username, String password, boolean isAdmin)
    {
        super(ID, firstName, lastName, email, street, city, state, zip, phone);
        this.ssn = ssn;
        this.dln = dln;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }
    public String getUsername() { return this.username; }
    public String getPassword() { return this.password; }
    public int getSsn() { return this.ssn; }
    public long getDln() { return this.dln; }
    public boolean getIsAdmin() { return this.isAdmin; }

    public void setUsername(String username)
    {
        this.username = username;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
    public void setSsn(int ssn)
    {
        this.ssn = ssn;
    }
    public void setDln(long dln)
    {
        this.dln = dln;
    }
    public void setAdmin(boolean isAdmin)
    {
        this.isAdmin = isAdmin;
    }

}
