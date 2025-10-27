package benpapouchado.Turtle;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import benpapouchado.Turtle.Login.UserDetails;
import benpapouchado.Turtle.Login.UserDetailsRepository;

@RestController
public class LoginController {

  private final UserDetailsRepository userDetailsRepository;

  public LoginController(UserDetailsRepository userDetailsRepository) {
    this.userDetailsRepository = userDetailsRepository;
  }
  
  @GetMapping("/people")
  public Iterable<UserDetails> findAllUsers() {
    return this.userDetailsRepository.findAll();
  }
}