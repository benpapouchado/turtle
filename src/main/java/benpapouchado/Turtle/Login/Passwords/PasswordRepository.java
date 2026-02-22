package benpapouchado.Turtle.Login.Passwords;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PasswordRepository extends JpaRepository<ChangePassword, Integer> {

    @Query("""
            SELECT code
            FROM ChangePassword
            WHERE username = :username
            AND id = :id
            """)
    int fetch_code(@Param("id") int id, @Param("username") String username);

    @Transactional
    @Modifying
    @Query("""
            UPDATE ChangePassword u
            SET u.password_changed = TRUE
            WHERE u.id = :id
            AND u.username = :username
            AND u.code = :code
            """)
    void update(@Param("id") int id, @Param("code") String code, @Param("username") String username);

    List<ChangePassword> findUserByUsername(@Param("username") String username);

}
