package com.ltws;

public class Employee {
    public int ID;
    public String firstName;
    public String lastName;
    public String email;
    public String address;

    public Employee(int ID, String firstName, String lastName, String email, String address)
    {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
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
    public String getAddress()
    {
        return this.address;
    }
}
