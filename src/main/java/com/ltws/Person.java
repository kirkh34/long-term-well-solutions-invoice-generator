package com.ltws;

abstract class Person {
    private int ID;
    private String firstName;
    private String lastName;
    private String email;
    private String street;
    private String city;
    private int zip;
    private int phone;

    public Person(int ID, String firstName, String lastName, String email, String street, String city, int zip, int phone)
    {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.street = street;
        this.city = city;
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
    public int getZip() { return this.zip; }
    public int getPhone() { return this.phone; }

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
    public void setZip(int zip)
    {
        this.zip = zip;
    }
    public void setPhone(int phone)
    {
        this.phone = phone;
    }
}
