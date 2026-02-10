package benpapouchado.Turtle.Login;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {

    @Query("""
            SELECT COUNT(u)
            FROM UserDetails u
            WHERE LOWER(u.username) = LOWER(:username)
            """)
    int usernameExists(@Param("username") String username);

}