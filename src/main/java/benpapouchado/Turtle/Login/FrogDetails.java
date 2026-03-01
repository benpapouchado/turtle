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
public class FrogDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "serial")
    private BigInteger ID;

    @Column(name = "name")
    private String name;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String password_hash;

    @Column(name = "date_of_birth")
    private Date date_of_birth;

    public FrogDetails(String name, String username, String password_hash, Date date_of_birth) {
        this.name = name;
        this.date_of_birth = date_of_birth;
    }

    public FrogDetails(String name, Date date_of_birth) {
        this.name = name;
        this.date_of_birth = date_of_birth;
    }

    public FrogDetails(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
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
