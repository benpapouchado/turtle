package benpapouchado.Turtle.Login;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String, String>> usernameTaken(@PathVariable String username) {
        if(username == null){
            return ResponseEntity.ok(Map.of("message", "Username check cannot pass null values"));
        } else {
            int count = userDetailsRepository.usernameExists(username);
            return ResponseEntity.ok(Map.of("is_available", count == 0 ? "true" : "false"));
        }
    }

    @GetMapping("/password-is-strong/{password}")
    public ResponseEntity<Map<String, String>> passwordStrongEnough(@PathVariable String password) {
        if(password == null){
            return ResponseEntity.ok(Map.of("message", "Password check cannot pass null values"));
        } else {
            boolean password_strength = PasswordHandling.isStrongPassword(password);
            return ResponseEntity.ok(Map.of("password_is_strong", password_strength ? "true" : "false"));
        }
    }

    @PostMapping("/create-account")
    public ResponseEntity<Map<String, String>> accountCreated(@RequestBody UserDetails userDetails) throws Exception {
        if (userDetails.getUsername() != null || userDetails.getPasswordHash() != null) {
            userDetails.setPassword_hash(PasswordHandling.hashPassword(userDetails.getPasswordHash()));
            userDetailsRepository.save(userDetails);
            return ResponseEntity.ok(Map.of("message", "Account successfully created"));
        } else {
            return ResponseEntity.ok(Map.of("message", "Account creation failed"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticateUserDetails(@RequestBody Login login) throws Exception {
        UserDetails user = userDetailsRepository.findUserByUsername(login.getUsername().trim());
        if (user != null &&
                PasswordHandling.verifyPassword(login.getPassword(), user.getPassword_hash())) {
            return ResponseEntity.ok(Map.of("message", "Successful login"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("message", "Unauthorised login info"));
        }
    }
}