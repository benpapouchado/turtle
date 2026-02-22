package benpapouchado.Turtle.Login.Passwords;

import benpapouchado.Turtle.Login.Login;

import java.security.SecureRandom;
import java.util.List;

public class ForgotPassword extends Login {
    private String confirmPassword;

    public ForgotPassword(String username, String password, String confirmPassword) {
        super(username, password);
        this.confirmPassword = confirmPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String forgotPasswordHashPassword(String password) throws Exception{
        return PasswordHandling.hashPassword(password);
    }

    public boolean confirmPasswordsMatch() throws Exception{
        String hashPassword = forgotPasswordHashPassword(getPassword());
        String hashConfirmPassword = forgotPasswordHashPassword(getConfirmPassword());

        boolean stringPasswordsMatch = getPassword().equals(getConfirmPassword());
        boolean hashPasswordsMatch = PasswordHandling.verifyPassword(getPassword(), hashConfirmPassword);
        boolean hashPasswordConfirmsMatch = PasswordHandling.verifyPassword(getConfirmPassword(), hashPassword);

        return stringPasswordsMatch && hashPasswordsMatch && hashPasswordConfirmsMatch;
    }

    public int generateCode(){
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextInt(10_000);
    }

    public static boolean ensure_new_password(List<ChangePassword> passwordList, String current_password, String new_password){
        if(current_password.equals(new_password)){
            return false;
        }

        for(ChangePassword changePassword :  passwordList){
            if (changePassword.getOld_password_hash().equals(current_password) ||
                    changePassword.getNew_password_hash().equals(current_password)){
                return false;
            }

            if (changePassword.getOld_password_hash().equals(new_password) ||
                    changePassword.getNew_password_hash().equals(new_password)){
                return false;
            }
        }
        return true;
    }

    //TODO unit tests
}
