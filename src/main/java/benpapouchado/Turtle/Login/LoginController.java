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
  public Map<String, String> accountCreated(@RequestBody UserDetails userDetails) {
    userDetailsRepository.save(userDetails);
    return Map.of("status", "200",
            "message", "Account successfully created");
  }

  @PostMapping("/login")
  public Map<String, String> createNewSession(@RequestBody String username, @RequestBody String password){
    int id = userDetailsRepository.login(username, password);

    if (id != 0) {
      return Map.of("status", "200",
              "message", "Successful login");
    } else {
      return Map.of("status", "401",
              "message", "Unauthorized status code");
    }
  }
//TODO introduce hashing so password is not stored in plain text
}