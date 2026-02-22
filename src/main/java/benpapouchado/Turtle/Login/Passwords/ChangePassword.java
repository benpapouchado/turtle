package benpapouchado.Turtle.Login.Passwords;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "change_password", schema = "public")
@NoArgsConstructor
public class ChangePassword {

    @Id
    @Column(name = "id", columnDefinition = "serial")
    private int id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "new_password_hash", nullable = false)
    private String new_password_hash;

    @Column(name = "old_password_hash", nullable = false)
    private String old_password_hash;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "password_changed")
    private boolean password_changed;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updated_at;

    public ChangePassword(int id, String username, String new_password_hash, String old_password_hash,
                          String code, boolean code_matches, Timestamp updated_at) {
        this.id = id;
        this.username = username;
        this.new_password_hash = new_password_hash;
        this.old_password_hash = old_password_hash;
        this.code = code;
        this.password_changed = code_matches;
        this.updated_at = updated_at;
    }

    public ChangePassword(int id, String username, String new_password_hash, String old_password_hash, String code){
        this.id = id;
        this.username = username;
        this.new_password_hash = new_password_hash;
        this.old_password_hash = old_password_hash;
        this.code = code;
    }

    public ChangePassword(String new_password_hash, String old_password_hash){
        this.new_password_hash = new_password_hash;
        this.old_password_hash = old_password_hash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNew_password_hash() {
        return new_password_hash;
    }

    public void setNew_password_hash(String new_password_hash) {
        this.new_password_hash = new_password_hash;
    }

    public String getOld_password_hash() {
        return old_password_hash;
    }

    public void setOld_password(String old_password_hash) {
        this.old_password_hash = old_password_hash;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isPassword_changed() {
        return password_changed;
    }

    public void setPassword_changed(boolean password_changed) {
        this.password_changed = password_changed;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
}
