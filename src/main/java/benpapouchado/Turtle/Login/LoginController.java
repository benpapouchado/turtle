package benpapouchado.Turtle.Login;

import org.springframework.web.bind.annotation.*;

import benpapouchado.Turtle.Login.UserDetails;
import benpapouchado.Turtle.Login.UserDetailsRepository;

import java.util.*;

@RestController
@RequestMapping("/users")
public class LoginController {
  @GetMapping("/health")
  public String index() {
    return "Looks good!";
  }

  private final UserDetailsRepository userDetailsRepository;

  public LoginController(UserDetailsRepository userDetailsRepository) {
    this.userDetailsRepository = userDetailsRepository;
  }

  @GetMapping("/people")
  public List<UserDetails> findAllUsers() {
    return this.userDetailsRepository.findAll();
  }

  @GetMapping("/username-exists/{username}")
  public Map<String, Boolean> usernameTaken(@PathVariable String username) {
    int count = userDetailsRepository.usernameExists(username);
    return Map.of("taken", count > 0);
  }

  @PostMapping("/create-account")
  public Map<String, Integer> accountCreated(@RequestBody UserDetails userDetails) {
    userDetailsRepository.save(userDetails);
    return Map.of("status", 200);
  }

}