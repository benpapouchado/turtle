package benpapouchado.Turtle.Login;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Frog_Account_Details", schema = "public")
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password; //add hashing

    @Column(name = "email")
    private String email;

    @Column(name = "date_of_birth")
    private String dob;
//    private String createdAt;
//    private String lastLogin;
//    private String firstname;
//    private String surname;

    public UserDetails() {
    }

    public UserDetails(int ID, String username, String password, String email, String dob){
    //, String createdAt, String lastLogin, String firstname, String surname) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.dob = dob;
//        this.createdAt = createdAt;
//        this.lastLogin = lastLogin;
//        this.firstname = firstname;
//        this.surname = surname;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

//    public String getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(String createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public String getLastLogin() {
//        return lastLogin;
//    }
//
//    public void setLastLogin(String lastLogin) {
//        this.lastLogin = lastLogin;
//    }
//
//    public String getFirstname() {
//        return firstname;
//    }
//
//    public void setFirstname(String firstname) {
//        this.firstname = firstname;
//    }
//
//    public String getSurname() {
//        return surname;
//    }
//
//    public void setSurname(String surname) {
//        this.surname = surname;
//    }
}
