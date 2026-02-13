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

    @Query("""
            SELECT id
            FROM UserDetails u
            WHERE LOWER(u.username) = LOWER(:username)
            AND LOWER(u.password) = LOWER(:password)
            """)
    int login(@Param("username") String username, @Param("password") String password);
}