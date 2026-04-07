package io.squid.CyStation.model;

import io.squid.CyStation.enums.Gender;
import io.squid.CyStation.enums.Role;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private int experience;
    private Role role;
    private Date birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;


    public User(){

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

    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    @Override
    public boolean equals(Object obj){

        if(obj instanceof User){
            User user = (User) obj;

            return this.email.equalsIgnoreCase(user.email);
        }
        return false;
    }


    public Date getBirthday() { return birthday; }
    public void setBirthday(Date birthday) { this.birthday = birthday; }

}
