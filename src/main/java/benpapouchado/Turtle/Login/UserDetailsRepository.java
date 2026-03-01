package benpapouchado.Turtle.Login;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDetailsRepository extends JpaRepository<FrogDetails, Integer> {

    @Query("""
            SELECT COUNT(u)
            FROM FrogDetails u
            WHERE LOWER(u.username) = LOWER(:username)
            """)
    int usernameExists(@Param("username") String username);

    FrogDetails findUserByUsername(String username);

    @Query("""
            SELECT password_hash
            FROM FrogDetails
            WHERE username = :username
            """)
    String extract_old_password(@Param("username") String username);

    @Transactional
    @Modifying
    @Query("""
            UPDATE FrogDetails u
            SET u.password_hash = :password_hash
            WHERE u.username = :username
            """)
    void updatePassword(@Param("username") String username, @Param("password_hash") String password);
}