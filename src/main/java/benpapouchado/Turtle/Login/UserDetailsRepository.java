package benpapouchado.Turtle.Login;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {

    @Query("""
            SELECT COUNT(u)
            FROM UserDetails u
            WHERE LOWER(u.username) = LOWER(:username)
            """)
    int usernameExists(@Param("username") String username);

    UserDetails findUserByUsername(String username);

    @Query("""
            UPDATE UserDetails u
            SET u.password_hash = :newPasswordHash
            WHERE u.username = :username
            """)
    void updatePassword(@Param("username") String username, @Param("password_hash") String password);
}