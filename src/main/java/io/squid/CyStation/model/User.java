package io.squid.CyStation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String passWord;
    private String email;

    public User(){

    }

    public User(Long id, String firstName, String lastName, String passWord, String email){

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passWord = passWord;
        this.email = email;

    }

    public Long getID(){ return id;}
    public void setId(Long id){ this.id = id;}

    public String getFirstName(){ return firstName;}
    public void setFirstName(String firstName){ this.firstName = firstName;}

    public String getLastName(){ return lastName;}
    public void setLastName(String firstName){ this.lastName = lastName;}

    public String getPassWord(){ return passWord;}
    public void setPassWord(String passWord){ this.passWord = passWord;}

    public String getEmail(){ return email;}
    public void setEmail(String email){ this.email = email;}

}
