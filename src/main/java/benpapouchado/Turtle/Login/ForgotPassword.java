package benpapouchado.Turtle.Login;

public class ForgotPassword extends Login{
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
        return PasswordHandling.hashPassword(getPassword());
    }

    public boolean confirmPasswordsMatch() throws Exception{
        String hashPassword = forgotPasswordHashPassword(getPassword());
        String hashConfirmPassword = forgotPasswordHashPassword(getConfirmPassword());

        boolean stringPasswordsMatch = getPassword().equals(getConfirmPassword());
        boolean hashPasswordsMatch = PasswordHandling.verifyPassword(getPassword(), hashConfirmPassword);
        boolean hashPasswordConfirmsMatch = PasswordHandling.verifyPassword(getConfirmPassword(), hashPassword);

        return stringPasswordsMatch && hashPasswordsMatch && hashPasswordConfirmsMatch;
    }

    //TODO unit tests
}
