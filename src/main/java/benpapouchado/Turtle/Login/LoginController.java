package benpapouchado.Turtle.Login;

import benpapouchado.Turtle.Login.Passwords.ChangePassword;
import benpapouchado.Turtle.Login.Passwords.ForgotPassword;
import benpapouchado.Turtle.Login.Passwords.PasswordHandling;
import benpapouchado.Turtle.Login.Passwords.PasswordRepository;
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
    private final PasswordRepository passwordRepository;

    public LoginController(UserDetailsRepository userDetailsRepository, PasswordRepository passwordRepository) {
        this.userDetailsRepository = userDetailsRepository;
        this.passwordRepository = passwordRepository;
    }

    @GetMapping("/people")
    public List<UserDetails> findAllUsers() {
        return this.userDetailsRepository.findAll();
    }

    @GetMapping("/username-exists/{username}")
    public ResponseEntity<Map<String, String>> usernameTaken(@PathVariable String username) {
        if (username == null) {
            return ResponseEntity.ok(Map.of("message", "Username check cannot pass null values"));
        } else {
            int count = userDetailsRepository.usernameExists(username);
            return ResponseEntity.ok(Map.of("is_available", count == 0 ? "true" : "false"));
        }
    }

    @GetMapping("/password-is-strong/{password}")
    public ResponseEntity<Map<String, String>> passwordStrongEnough(@PathVariable String password) {
        if (password == null) {
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

    @PostMapping("/update-password-request")
    public ResponseEntity<Map<String, String>> updatePasswordRequest(@RequestBody ForgotPassword forgotPassword)
            throws Exception {

        if (forgotPassword.getUsername() == null || forgotPassword.getUsername().isBlank()) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Username is required"));
        }

        UserDetails user = userDetailsRepository.findUserByUsername(forgotPassword.getUsername());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(Map.of("message", "Frog does not exist"));
        }

        if (forgotPassword.getPassword() != null && forgotPassword.getConfirmPassword() != null
                && forgotPassword.confirmPasswordsMatch()) {

            int id = forgotPassword.generateCode();
            String code =  String.format("%04d", forgotPassword.generateCode());
            String old_password_hash = userDetailsRepository.extract_old_password(forgotPassword.getUsername());
            String new_password_hash = PasswordHandling.hashPassword(forgotPassword.getPassword());

            //In a real system this code would be entered into and read from a cache rather than the database

            if(ForgotPassword.ensure_new_password(passwordRepository.findUserByUsername(forgotPassword.getUsername()),
                    old_password_hash, new_password_hash)) {
                passwordRepository.save(new ChangePassword(id, forgotPassword.getUsername(),
                        new_password_hash, old_password_hash, code));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                        body(Map.of("message", "Password already used. Choose a different one!"));
            }

            return ResponseEntity.ok(Map.of("message", "Deliver code ",
                    "code", code,
                    "id", String.valueOf(id)));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                    body(Map.of("message", "Update Failed"));
        }
    }

    @PostMapping("/update-password/{id}/{code}")
    public ResponseEntity<Map<String, String>> updatePassword(@PathVariable String code, @PathVariable String id,
                                                              @RequestBody ForgotPassword forgotPassword) throws Exception {
        String new_password_hash = PasswordHandling.hashPassword(forgotPassword.getPassword());
        if (code != null) {
            String match_code = String.valueOf(passwordRepository.fetch_code(Integer.parseInt(id), forgotPassword.getUsername()));
            if (code.equals(match_code)) {
                userDetailsRepository.updatePassword(forgotPassword.getUsername(), new_password_hash);
                passwordRepository.update(Integer.parseInt(id), code, forgotPassword.getUsername());
                return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
            }
        }
        return ResponseEntity.ok(Map.of("message", "Password update failed."));
    }

}