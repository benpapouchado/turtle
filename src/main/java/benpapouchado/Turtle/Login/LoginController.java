package benpapouchado.Turtle.Login;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Map<String, Boolean>> usernameTaken(@PathVariable String username) {
        int count = userDetailsRepository.usernameExists(username);
        return ResponseEntity.ok(Map.of("is_available", count == 0));
    }

    @PostMapping("/create-account")
    public ResponseEntity<Map<String, String>> accountCreated(@RequestBody UserDetails userDetails) {
        System.out.println(userDetails);
        if(userDetails != null) {
            userDetailsRepository.save(userDetails);
            return ResponseEntity.ok(Map.of("message", "Account successfully created"));
        } else {
            return ResponseEntity.ok(Map.of("message", "Account creation failed"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticateUserDetails(@RequestBody Login login) {
        UserDetails user = userDetailsRepository.findUserByUsername(login.getUsername().trim());
        if (user != null &&
                login.getPassword().trim().equals(user.getPassword().trim())) {
            return ResponseEntity.ok(Map.of("message", "Successful login"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("message", "Unauthorised login info"));
        }
    }
//TODO introduce hashing so password is not stored in plain text
}