package benpapouchado.Turtle.Login;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {

    @Query("Select count(row) from UserDetails row where row.username = :username")
    int usernameExists(@RequestParam("username") String username);

}