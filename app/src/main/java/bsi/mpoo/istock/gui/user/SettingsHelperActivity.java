package bsi.mpoo.istock.gui.user;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import bsi.mpoo.istock.R;
import bsi.mpoo.istock.domain.Session;
import bsi.mpoo.istock.domain.User;
import bsi.mpoo.istock.gui.AlertDialogGenerator;
import bsi.mpoo.istock.gui.LoginActivity;
import bsi.mpoo.istock.services.Constants;
import bsi.mpoo.istock.services.Encryption;
import bsi.mpoo.istock.services.Exceptions;
import bsi.mpoo.istock.services.SessionServices;
import bsi.mpoo.istock.services.user.UserServices;
import bsi.mpoo.istock.services.Validations;

public class SettingsHelperActivity extends AppCompatActivity {

    private TextView textView;
    private EditText companyEditTextShow;
    private EditText companyEditText;
    private EditText nameEditTextShow;
    private EditText nameEditText;
    private EditText emailEditTextShow;
    private EditText emailEditText;
    private EditText passwordEditTextOld;
    private EditText passwordEditText;
    private EditText passwordEditTextConfirmation;
    private TextInputLayout companyTextInputLayoutShow;
    private TextInputLayout companyTextInputLayout;
    private TextInputLayout nameTextInputLayoutShow;
    private TextInputLayout nameTextInputLayout;
    private TextInputLayout emailTextInputLayoutShow;
    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayoutOld;
    private TextInputLayout passwordTextInputLayout;
    private TextInputLayout passwordTextInputLayoutConfirmation;
    private User user;
    private int editOption;
    private UserServices userServices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_helper);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        editOption = bundle.getInt(Constants.BundleKeys.SETTINGS);
        userServices = new UserServices(getApplicationContext());
        textView = findViewById(R.id.textViewSettingsSettingsHelper);
        companyEditTextShow = findViewById(R.id.editCompanyNameSettingsHelperShow);
        companyEditText = findViewById(R.id.editCompanyNameSettingsHelper);
        nameEditTextShow = findViewById(R.id.editFullNameSettingsHelperShow);
        nameEditText = findViewById(R.id.editFullNameSettingsHelper);
        emailEditTextShow = findViewById(R.id.editEmailSettingsHelperShow);
        emailEditText = findViewById(R.id.editEmailSettingsHelper);
        passwordEditTextOld = findViewById(R.id.editPasswordSettingsHelperOld);
        passwordEditText = findViewById(R.id.editPasswordSettingsHelper);
        passwordEditTextConfirmation = findViewById(R.id.editPasswordSettingsHelperConfirmation);
        companyTextInputLayoutShow = findViewById(R.id.companyInputLayoutSettingsHelperShow);
        companyTextInputLayout = findViewById(R.id.companyInputLayoutSettingsHelper);
        nameTextInputLayoutShow = findViewById(R.id.nameInputLayoutSettingsHelperShow);
        nameTextInputLayout = findViewById(R.id.nameInputLayoutSettingsHelper);
        emailTextInputLayoutShow = findViewById(R.id.emailInputLayoutSettingsHelperShow);
        emailTextInputLayout = findViewById(R.id.emailInputLayoutSettingsHelper);
        passwordTextInputLayoutOld = findViewById(R.id.passwordInputLayoutSettingsHelperOld);
        passwordTextInputLayout = findViewById(R.id.passwordInputLayoutSettingsHelper);
        passwordTextInputLayoutConfirmation = findViewById(R.id.passwordInputLayoutSettingsHelperConfirmation);
        UserServices userServices = new UserServices(getApplicationContext());
        user = userServices.getUserFromDomainType(Session.getInstance().getAccount());


        switch (editOption){
            case Constants.SettingsHelper.COMPANY:
                showTextInputLayout(companyTextInputLayoutShow, companyTextInputLayout);
                textView.setText(getString(R.string.edition_of_company_name));
                companyEditTextShow.setText(user.getCompany());
                break;
            case Constants.SettingsHelper.EMAIL:
                showTextInputLayout(emailTextInputLayoutShow, emailTextInputLayout);
                textView.setText(getString(R.string.edition_of_email));
                emailEditTextShow.setText(user.getEmail());
                break;
            case Constants.SettingsHelper.NAME:
                showTextInputLayout(nameTextInputLayoutShow, nameTextInputLayout);
                textView.setText(getString(R.string.edition_of_name));
                nameEditTextShow.setText(user.getName());
                break;
            case Constants.SettingsHelper.PASSWORD:
                showTextInputLayout(passwordTextInputLayoutOld, passwordTextInputLayout, passwordTextInputLayoutConfirmation);
                textView.setText(getString(R.string.edition_of_password));
                break;
            case Constants.SettingsHelper.DELETE:
                showTextInputLayout(passwordTextInputLayout, passwordTextInputLayoutConfirmation);
                textView.setText(getString(R.string.delete_account));
                break;
        }
    }

    private void showTextInputLayout(TextInputLayout...textInputLayouts){
        for (TextInputLayout textInputLayout: textInputLayouts){
            textInputLayout.setVisibility(View.VISIBLE);
        }
    }

    public void cancel(View view) {
        finish();
    }

    public void save(View view) {
        Validations validations = new Validations(getApplicationContext());
        switch (editOption){
            case Constants.SettingsHelper.COMPANY:
                if (isFieldsValid(validations)){
                    user.setCompany(companyEditText.getText().toString().trim());
                    updateUser();
                }
                break;
            case Constants.SettingsHelper.EMAIL:
                if (isFieldsValid(validations)){
                    user.setEmail(emailEditText.getText().toString().trim().toUpperCase());
                    updateUser();
                }
                break;
            case Constants.SettingsHelper.NAME:
                if (isFieldsValid(validations)){
                    user.setName(nameEditText.getText().toString().trim().toUpperCase());
                    updateUser();
                }
                break;
            case Constants.SettingsHelper.PASSWORD:
                if (isFieldsValid(validations)){
                    user.setPassword(Encryption.encrypt(passwordEditText.getText().toString()));
                    updateUser();
                }
                break;
            case Constants.SettingsHelper.DELETE:
                if (isFieldsValid(validations)){
                    userServices.deleteCompany(user);
                    SessionServices sessionServices = new SessionServices(getApplicationContext());
                    sessionServices.clearSession();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    finishAffinity();
                    startActivity(intent);

                }
        }
    }

    private void updateUser() {
        try {
            userServices.updateUser(user);
            SessionServices sessionServices = new SessionServices(getApplicationContext());
            sessionServices.refreshSession();
            new AlertDialogGenerator(this,
                    getString(R.string.edit_successful), true).invoke();

        } catch (Exceptions.UserNotRegistered error){
            new AlertDialogGenerator(this,
                    getString(R.string.user_not_registered), false).invoke();

        } catch (Exception error){
            new AlertDialogGenerator(this,
                    getString(R.string.unknow_error), false).invoke();
        }
    }

    public boolean isFieldsValid(Validations validations){
        boolean valid = true;
        switch (editOption){
            case Constants.SettingsHelper.COMPANY:
                valid = validations.editValidate(companyEditText);
                if (!validations.name(companyEditText.getText().toString())){
                    validations.setErrorIfNull(companyEditText, getString(R.string.invalid_Name));
                    valid = false;
                }
                break;
            case Constants.SettingsHelper.NAME:
                valid = validations.editValidate(nameEditText);
                if (!validations.name(nameEditText.getText().toString())){
                    validations.setErrorIfNull(nameEditText, getString(R.string.invalid_Name));
                    valid = false;
                }
                break;
            case Constants.SettingsHelper.EMAIL:
                valid = validations.editValidate(emailEditText);
                if (!validations.email(emailEditText.getText().toString().toUpperCase())){
                    validations.setErrorIfNull(emailEditText, getString(R.string.invalid_email));
                    valid = false;
                }
                break;
            case Constants.SettingsHelper.PASSWORD:
                valid = validations.editValidate(passwordEditTextOld,
                        passwordEditText, passwordEditTextConfirmation);

                if (!validations.password(passwordEditText.getText().toString())){
                    validations.setErrorIfNull(passwordEditText, getString(R.string.invalid_password));
                    valid = false;
                }

                if (!validations.password(passwordEditTextConfirmation.getText().toString())){
                    validations.setErrorIfNull(passwordEditTextConfirmation, getString(R.string.invalid_password));
                    valid = false;
                }

                if (!validations.passwordEquals(passwordEditText.getText().toString(),
                        passwordEditTextConfirmation.getText().toString())){
                    validations.setErrorIfNull(passwordEditText, getString(R.string.invalid_password_not_equals));
                    validations.setErrorIfNull(passwordEditTextConfirmation, getString(R.string.invalid_password_not_equals));
                    valid = false;
                } else {
                    if (!validations.passwordEquals(Encryption.encrypt(passwordEditTextOld.getText().toString()),
                            user.getPassword())){
                        validations.setErrorIfNull(passwordEditTextOld, getString(R.string.invalid_currenty_password));
                        valid = false;
                    }
                }
                break;
            case Constants.SettingsHelper.DELETE:
                valid = validations.editValidate(passwordEditText,
                        passwordEditTextConfirmation);

                if (!validations.password(passwordEditText.getText().toString())){
                    validations.setErrorIfNull(passwordEditText, getString(R.string.invalid_password));
                    valid = false;
                }

                if (!validations.password(passwordEditTextConfirmation.getText().toString())){
                    validations.setErrorIfNull(passwordEditTextConfirmation, getString(R.string.invalid_password));
                    valid = false;
                }

                if (!validations.passwordEquals(passwordEditText.getText().toString(),
                        passwordEditTextConfirmation.getText().toString())) {
                    validations.setErrorIfNull(passwordEditText, getString(R.string.invalid_password_not_equals));
                    validations.setErrorIfNull(passwordEditTextConfirmation, getString(R.string.invalid_password_not_equals));
                    valid = false;
                } else {
                    if (!validations.passwordEquals(Encryption.encrypt(passwordEditText.getText().toString()),
                            user.getPassword())){
                        validations.setErrorIfNull(passwordEditText, getString(R.string.invalid_currenty_password));
                        valid = false;
                    }
                }
                break;

        }
        return valid;
    }
}
