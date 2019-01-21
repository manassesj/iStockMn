package bsi.mpoo.istock.gui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import bsi.mpoo.istock.R;
import bsi.mpoo.istock.domain.Administrator;
import bsi.mpoo.istock.domain.Producer;
import bsi.mpoo.istock.domain.Salesman;
import bsi.mpoo.istock.domain.Session;
import bsi.mpoo.istock.domain.User;
import bsi.mpoo.istock.gui.user.FirstAccess;
import bsi.mpoo.istock.gui.user.RegisterActivity;
import bsi.mpoo.istock.services.Constants;
import bsi.mpoo.istock.services.SessionServices;
import bsi.mpoo.istock.services.user.UserServices;
import bsi.mpoo.istock.services.Validations;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Switch switchButton;
    private Object account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        emailEditText = findViewById(R.id.editEmailLogin);
        passwordEditText = findViewById(R.id.editPasswordLogin);
        switchButton = findViewById(R.id.switchButtonLogin);
        SessionServices sessionServices = new SessionServices(getApplicationContext());
        Session session =  sessionServices.getSession();
        if (session != null){
            if (session.getRemember() == Constants.Session.NOT_TO_REMEMBER){
                sessionServices.clearSession();
            } else {
                Session.getInstance().setRemember(session.getRemember());
                Session.getInstance().setId_user(session.getId_user());
                account = session.getAccount();
                if (account instanceof Administrator){
                    Session.getInstance().setAccount((Administrator) session.getAccount());
                } else if (session.getAccount() instanceof Salesman){
                    Session.getInstance().setAccount((Salesman) session.getAccount());
                    Session.getInstance().setAdministrator(session.getAdministrator());
                    if (((Salesman) session.getAccount()).getUser().getStatus() == Constants.Status.FIRST_ACCESS_FOR_USER){
                        sessionServices.clearSession();
                        return;
                    }
                } else {
                    Session.getInstance().setAccount((Producer) session.getAccount());
                    Session.getInstance().setAdministrator(session.getAdministrator());
                    if (((Salesman) session.getAccount()).getUser().getStatus() == Constants.Status.FIRST_ACCESS_FOR_USER){
                        sessionServices.clearSession();
                        return;
                    }
                }
                Intent intent = new Intent(this, MainActivity.class);
                finish();
                startActivity(intent);

            }
        }
    }

    public void login(View view){

        Validations validations = new Validations(getApplicationContext());
        if (!isAllFieldsValid(validations)) return;
        UserServices userServices = new UserServices(getApplicationContext());
        String email = emailEditText.getText().toString().trim().toUpperCase();
        String password = passwordEditText.getText().toString();
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        User searchedUser = userServices.login(user);

        if (searchedUser == null){
            emailEditText.setError(getString(R.string.invalid_email_or_password));
            passwordEditText.setError(getString(R.string.invalid_email_or_password));
        } else {
            if (searchedUser.getStatus() == Constants.Status.FIRST_ACCESS_FOR_USER){
                Intent intent = new Intent(this, FirstAccess.class);
                intent.putExtra(Constants.BundleKeys.USER, searchedUser);
                startActivity(intent);
                finish();
                return;
            }
            int remember;
            boolean switchState = switchButton.isChecked();
            if (switchState){
                remember = Constants.Session.REMEMBER;
            } else {
                remember = Constants.Session.NOT_TO_REMEMBER;
            }
            SessionServices sessionServices = new SessionServices(getApplicationContext());
            account = userServices.getUserInDomainType(searchedUser);
            sessionServices.updateSession(account,remember );
            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);
        }
    }

    private boolean isAllFieldsValid(Validations validations) {
        boolean valid = validations.editValidate(emailEditText, passwordEditText);

        if (!validations.email(emailEditText.getText().toString())){
            validations.setErrorIfNull(emailEditText, getString(R.string.invalid_email));
            valid = false;
        }

        return valid;
    }

    public void CreateAccount(View view) {

        Intent intent = new Intent(this, RegisterActivity.class);
        Bundle bundle = new Bundle();
        String email = emailEditText.getText().toString();
        bundle.putString(Constants.BundleKeys.EMAIL,email);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void forgotPassword(View view) {
        Intent intent = new Intent(this, ForgotActivity.class);
        startActivity(intent);
    }
}
