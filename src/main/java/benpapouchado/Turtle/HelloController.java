package benpapouchado.Turtle;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import benpapouchado.Turtle.Login.UserDetails;
import benpapouchado.Turtle.Login.UserDetailsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class HelloController {
    @GetMapping("/health")
    public String index() {
        return "Looks good!";
    }

    private final UserDetailsRepository userDetailsRepository;

    public HelloController(UserDetailsRepository userDetailsRepository) {
    this.userDetailsRepository = userDetailsRepository;
    }

    @GetMapping("/people")
    public List<UserDetails> findAllUsers() {
    return this.userDetailsRepository.findAll();
  }

  @GetMapping("/username-exists/{username}")
  public boolean usernameTaken(@PathVariable String username){
        int count = this.userDetailsRepository.usernameExists(username);
        return count > 0;
  }

  @PostMapping("/account-created")
  public void accountCreated(@RequestBody UserDetails extraUserDetails){
        this.userDetailsRepository.save(extraUserDetails);
  }
}