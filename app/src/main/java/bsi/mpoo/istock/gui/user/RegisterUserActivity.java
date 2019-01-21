package bsi.mpoo.istock.gui.user;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.Normalizer;
import java.util.ArrayList;

import bsi.mpoo.istock.R;
import bsi.mpoo.istock.domain.Administrator;
import bsi.mpoo.istock.domain.Session;
import bsi.mpoo.istock.domain.User;
import bsi.mpoo.istock.gui.AlertDialogGenerator;
import bsi.mpoo.istock.services.Constants;
import bsi.mpoo.istock.services.Exceptions;
import bsi.mpoo.istock.services.RandomPassword;
import bsi.mpoo.istock.services.user.UserServices;
import bsi.mpoo.istock.services.Validations;

public class RegisterUserActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private EditText nameEditText;
    private EditText emailEditText;
    private int selectedOption = Constants.UserTypes.SALESMAN;
    private Object account;
    private String tempPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        account = Session.getInstance().getAccount();
        spinner = findViewById(R.id.spinnerRegisterUser);
        spinner.setOnItemSelectedListener(this);
        ArrayList<String> functions = new ArrayList<>();
        functions.add(getString(R.string.sales));
        functions.add(getString(R.string.production));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                this, R.layout.support_simple_spinner_dropdown_item, functions);
        dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        nameEditText = findViewById(R.id.editNameRegisterUser);
        emailEditText = findViewById(R.id.editEmailRegisterUser);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        String option = parent.getItemAtPosition(position).toString();
        if (option.equals(getString(R.string.production).intern())){
            selectedOption = Constants.UserTypes.PRODUCER;
        } else {
            selectedOption = Constants.UserTypes.SALESMAN;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0){}

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {}

    public void register(View view) {
        Validations validations = new Validations(getApplicationContext());
        if (!isAllFieldsValid(validations)){
            return;
        }
        UserServices userServices = new UserServices(getApplicationContext());
        User newUser = new User();
        if (account instanceof Administrator){
            newUser.setAdministrator(Session.getInstance().getAdministrator().getUser().getId());
        }
        newUser.setName(nameEditText.getText().toString());
        newUser.setCompany(Session.getInstance().getAdministrator().getUser().getCompany());
        newUser.setEmail(emailIsEmpty(emailEditText.getText().toString(), newUser.getName(), newUser.getCompany()));
        newUser.setStatus(Constants.Status.FIRST_ACCESS_FOR_USER);
        tempPassword = RandomPassword.generate();
        newUser.setPassword(tempPassword);
        newUser.setType(selectedOption);
        try {
            userServices.registerUser(newUser);
            String message = registeredMessage(newUser.getEmail(), tempPassword);
            new AlertDialogGenerator(this, getString(R.string.register_done), true).invokeShare(message);
        } catch (Exceptions.EmailAlreadyRegistered error){
            String message = getString(R.string.email_already_registered);
            new AlertDialogGenerator(this, message, false).invoke();
            validations.clearFields(emailEditText, nameEditText);
            nameEditText.requestFocus();
        } catch (Exception error){
            String message = getString(R.string.unknow_error);
            new AlertDialogGenerator(this, message, false).invoke();
            validations.clearFields(emailEditText, nameEditText);
            nameEditText.requestFocus();
        }
    }

    private boolean isAllFieldsValid(Validations validations){
        boolean valid = validations.editValidate(nameEditText);

        if (!validations.name(nameEditText.getText().toString())){
            validations.setErrorIfNull(nameEditText, getString(R.string.invalid_Name));
            valid = false;
        }

        if (!emailEditText.getText().toString().isEmpty()){
            if (!validations.email(emailEditText.getText().toString())){
                validations.setErrorIfNull(emailEditText,getString(R.string.invalid_email));
                valid = false;
            }
        }
        return valid;
    }

    private String emailIsEmpty(String email ,String name, String company){
        if (email.isEmpty()){
            return generateUserCompanyEmail(name, company);
        } else {
            return email;
        }
    }

    private String registeredMessage(String email, String tempPassword){
        return getString(R.string.email)+": "+email+"\n"+
                getString(R.string.password)+": "+tempPassword;
    }
    private String generateUserCompanyEmail(String name, String company) {
        String newEmail = name.replace(" ","_")+"."+company.replace(" ","_")+getString(R.string.domain_email_istock);
        return Normalizer.normalize(newEmail, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }
}
