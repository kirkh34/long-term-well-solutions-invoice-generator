package com.ltws;

public class Employee extends Person{
    private int ssn;
    private int dl;
    private String username;
    private String password;
    private boolean isAdmin;

    public Employee(int ID, String firstName, String lastName, String email, String street, String city, int zip, int phone, int ssn, int dl, String username, String password, boolean isAdmin)
    {
        super(ID, firstName, lastName, email, street, city, zip, phone);
        this.ssn = ssn;
        this.dl = dl;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }
    public String getUsername() { return this.username; }
    public String getPassword() { return this.password; }
    public int getSsn() { return this.ssn; }
    public int getDl() { return this.dl; }
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
    public void setDl(int dl)
    {
        this.dl = dl;
    }
    public void setAdmin(boolean isAdmin)
    {
        this.isAdmin = isAdmin;
    }

}
