package benpapouchado.Turtle;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import benpapouchado.Turtle.Login.UserDetails;
import benpapouchado.Turtle.Login.UserDetailsRepository;

import java.util.*;

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
  public Map<String, Boolean> usernameTaken(@PathVariable String username){
        int count = this.userDetailsRepository.usernameExists(username);
        Map<String, Boolean> exists = new HashMap<>();
        exists.put("username-exists", count > 0);
        return exists;
  }

  @PostMapping("/account-created")
  public void accountCreated(@RequestBody UserDetails extraUserDetails){
        this.userDetailsRepository.save(extraUserDetails);
  }
}