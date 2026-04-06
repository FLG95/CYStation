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
    private String password;
    private String email;
    public enum Role{
        ADMIN, VISITOR, PASSENGER, SCIENTIST
    }
    Role role;

    public User(){

    }

    public User(Long id, String firstName, String lastName, String password, String email){

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    public Long getID(){ return id;}
    public void setId(Long id){ this.id = id;}

    public String getFirstName(){ return firstName;}
    public void setFirstName(String firstName){ this.firstName = firstName;}

    public String getLastName(){ return lastName;}
    public void setLastName(String lastName){ this.lastName = lastName;}

    public String getPassword(){ return password;}
    public void setPassword(String passWord){ this.password = passWord;}

    public String getEmail(){ return email;}
    public void setEmail(String email){ this.email = email;}

    public Role getRole(){ return role;}
    public void setRole(Role role){ this.role = role;}

    @Override
    public boolean equals(Object obj){

        if(obj instanceof User){
            User user = (User) obj;

            return this.email.equalsIgnoreCase(user.email);
        }
        return false;
    }

}
