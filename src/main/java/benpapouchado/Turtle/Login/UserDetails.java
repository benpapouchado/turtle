package benpapouchado.Turtle.Login;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name = "frog_details", schema = "public")
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "serial")
    private BigInteger ID;

    @Column(name = "name")
    private String name;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String password_hash; //add hashing

    @Override
    public String toString() {
        return "UserDetails{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password_hash='" + password_hash + '\'' +
                ", date_of_birth=" + date_of_birth +
                '}';
    }

    @Column(name = "date_of_birth")
    private Date date_of_birth;

    public UserDetails(String name, String username, String password_hash, Date date_of_birth) {
        this.name = name;
        this.username = username;
        this.password_hash = password_hash;
        this.date_of_birth = date_of_birth;
    }

    public UserDetails(){}

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

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = Date.valueOf(date_of_birth);
    }

}
