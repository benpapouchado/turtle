package benpapouchado.Turtle.Login;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name = "frog_details", schema = "public")
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;

    @Column(name = "name")
    private String name;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String password_hash; //add hashing

    @Column(name = "date_of_birth")
    private Date dob;

    @Column(nullable = true)
    private String salt;


    public UserDetails(String name, String username, String password_hash, Date dob) {
        this.name = name;
        this.username = username;
        this.password_hash = password_hash;
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public void setName(String firstname) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password_hash;
    }

    public void setPassword(String password_hash) {
        this.password_hash = password_hash;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = Date.valueOf(dob);
    }

}
