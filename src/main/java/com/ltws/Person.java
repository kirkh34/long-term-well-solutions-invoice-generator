package com.ltws;

abstract class Person {
    private int ID;
    private String firstName;
    private String lastName;
    private String email;
    private String street;
    private String city;
    private String state;
    private int zip;
    private long phone;

    public Person(int ID, String firstName, String lastName, String email, String street, String city, String state, int zip, long phone)
    {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phone = phone;
    }
    public int getID()
    {
        return this.ID;
    }
    public String getFirstName()
    {
        return this.firstName;
    }
    public String getLastName()
    {
        return this.lastName;
    }
    public String getEmail()
    {
        return this.email;
    }
    public String getStreet() { return this.street; }
    public String getCity() { return this.city; }
    public String getState() { return this.state; }
    public int getZip() { return this.zip; }
    public long getPhone() { return this.phone; }

    public void setID(int ID)
    {
        this.ID = ID;
    }
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public void setStreet(String street)
    {
        this.street = street;
    }
    public void setCity(String city) { this.city = city; }
    public void setState(String state) { this.state = state; }
    public void setZip(int zip)
    {
        this.zip = zip;
    }
    public void setPhone(long phone)
    {
        this.phone = phone;
    }
}
