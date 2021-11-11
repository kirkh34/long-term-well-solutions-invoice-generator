package com.ltws;

public class Employee {
    public int ID;
    public String firstName;
    public String lastName;
    public String email;

    public Employee(int ID, String firstName, String lastName, String email)
    {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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
}
