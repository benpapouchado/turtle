package benpapouchado.Turtle.Login;

import benpapouchado.Turtle.Login.Passwords.ChangePassword;
import benpapouchado.Turtle.Login.Passwords.ForgotPassword;
import benpapouchado.Turtle.Login.Passwords.PasswordHandling;
import benpapouchado.Turtle.Login.Passwords.PasswordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Base64;
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
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    public LoginController(UserDetailsRepository userDetailsRepository, PasswordRepository passwordRepository) {
        this.userDetailsRepository = userDetailsRepository;
        this.passwordRepository = passwordRepository;
    }

    @GetMapping("/people")
    public List<FrogDetails> findAllUsers() {
        return this.userDetailsRepository.findAll();
    }

    @GetMapping("/username-exists")
    public ResponseEntity<Map<String, String>> usernameTaken(@RequestHeader (HttpHeaders.AUTHORIZATION) String auth) {
        String username = extract_basic_auth(auth)[0];

        if (auth == null) {
            logger.error("Username check cannot pass null values");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username check cannot pass null values");
        } else {
            int count = userDetailsRepository.usernameExists(username);
            if(count == 0) {
                logger.info("Username is available");
                return ResponseEntity.ok(Map.of("is_available","true"));
            } else {
                logger.info("Username is not available");
                return ResponseEntity.ok(Map.of("is_available","false"));
            }
        }
    }

    @GetMapping("/password-is-strong")
    public ResponseEntity<Map<String, String>> passwordStrongEnough(@RequestHeader (HttpHeaders.AUTHORIZATION) String auth) {
        String password = extract_basic_auth(auth)[0];
        if (auth == null) {
            logger.error("Null value entered");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Password check cannot pass null values");
        } else {
            boolean password_strength = PasswordHandling.isStrongPassword(password);

            if(password_strength){
                logger.info("Password entered is strong enough");
                return ResponseEntity.ok(Map.of("password_is_strong", "true"));
            } else {
                logger.info("Password is weak");
                return ResponseEntity.ok(Map.of("password_is_strong", "false"));
            }
        }
    }

    @PostMapping("/create-account")
    public ResponseEntity<Map<String, String>> accountCreated(@RequestHeader (HttpHeaders.AUTHORIZATION) String auth,
                                                              @RequestBody FrogDetails frogDetails) throws Exception {
        String[] decode = extract_basic_auth(auth);
        String username = decode[0];
        String password = decode[1];

        if (username != null || password != null) {
            logger.info(Arrays.toString(decode));
            frogDetails.setPassword_hash(PasswordHandling.hashPassword(password));
            frogDetails.setUsername(username);
            userDetailsRepository.save(frogDetails);
            return ResponseEntity.ok(Map.of("message", "Account successfully created"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( //400
                    (Map.of("message", "Account creation failed")));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticateUserDetails(@RequestHeader (HttpHeaders.AUTHORIZATION) String auth)
            throws Exception {
        String[] decode = extract_basic_auth(auth);
        String username = decode[0];
        String password = decode[1];
        FrogDetails user = userDetailsRepository.findUserByUsername(username.trim());

        if (user != null) {
            if(PasswordHandling.verifyPassword(password, user.getPassword_hash())) {
                return ResponseEntity.ok(Map.of("message", "Successful login"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("message", "Unauthorised login"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("message", "Frog does not exist"));
        }
    }

    @PostMapping("/update-password-request")
    public ResponseEntity<Map<String, String>> updatePasswordRequest(@RequestHeader (HttpHeaders.AUTHORIZATION) String auth)
            throws Exception {
        String[] decode = extract_basic_auth(auth);
        String username = decode[0];
        String password = decode[1];
        String confirm_password = decode[2];
        ForgotPassword forgotPassword = new ForgotPassword(username, password, confirm_password);

        if (username== null || username.isBlank()) {
            logger.error("Enter username");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Username is required"));
        }

        FrogDetails user = userDetailsRepository.findUserByUsername(username);

        if (user == null) {
            logger.error("Frog does not exist or incorrect password");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(Map.of("message", "Frog does not exist or incorrect password"));
        }

        if (password != null && confirm_password != null
                && forgotPassword.confirmPasswordsMatch()) {

            int id = forgotPassword.generateCode();
            String code =  String.format("%04d", forgotPassword.generateCode());
            String old_password_hash = userDetailsRepository.extract_old_password(username);
            String new_password_hash = PasswordHandling.hashPassword(password);

            //In a real system this code would be entered into and read from a cache rather than the database

            if(ForgotPassword.ensure_new_password(passwordRepository.findUserByUsername(username),
                    old_password_hash, new_password_hash)) {
                passwordRepository.save(new ChangePassword(id, username,
                        new_password_hash, old_password_hash, code));
            } else {
                logger.warn("Can't repeat an already used password.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED). //401
                        body(Map.of("message", "Password already used. Choose a different one!"));
            }

            logger.info("Enter Code to proceed");
            return ResponseEntity.ok(Map.of("message", "Deliver code ",
                    "code", code,
                    "id", String.valueOf(id)));
        } else {
            logger.error("Update Failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED). //401
                    body(Map.of("message", "Update Failed"));
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<Map<String, String>> updatePassword(@RequestBody ChangePassword changePassword,
                                                              @RequestHeader (HttpHeaders.AUTHORIZATION) String auth)
            throws Exception {
        String[] decode = extract_basic_auth(auth);
        String username = decode[0];
        String password = decode[1];
        String OTP_code = decode[2];
        String new_password_hash = PasswordHandling.hashPassword(password);
        logger.info(username + " " + changePassword.getId() + " " + OTP_code + changePassword.getCode());
        if (changePassword.getCode() != null) {

            String match_code = passwordRepository.fetch_code(changePassword.getId(), username);
            logger.info(username + " " + changePassword.getId() + " id " + match_code + " " + OTP_code);
            if (OTP_code.equals(match_code)) {
                userDetailsRepository.updatePassword(username, new_password_hash);
                passwordRepository.update(changePassword.getId(), changePassword.getCode(), username);
                logger.info("Password update successful");
                return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
            } else {
                logger.warn("Code didn't match. Try again");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED). //401
                        body(Map.of("message", "Code didn't match"));
            }
        }
        logger.error("Failed");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST). //400
                body(Map.of("message", "Update Failed"));
    }

    public static String base64_decode(String header){
        if(header != null){
            byte[] decodedBytes = Base64.getDecoder().decode(header);
            return new String(decodedBytes);
        }
        return "";
    }

    public static String[] extract_basic_auth(String header){
        if(header != null && header.startsWith("Basic ")){
            String base64Credentials = header.substring(6);
            String[] credentials = base64_decode(base64Credentials).split(":");

            for (int i = 0; i < credentials.length; i++) {
                credentials[i] = base64_decode(credentials[i]);
            }
            return credentials;
        }
        return new String[0];
    }
}