package bsi.mpoo.istock.gui.client;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import bsi.mpoo.istock.R;
import bsi.mpoo.istock.domain.Address;
import bsi.mpoo.istock.domain.Administrator;
import bsi.mpoo.istock.domain.Client;
import bsi.mpoo.istock.domain.Salesman;
import bsi.mpoo.istock.domain.Session;
import bsi.mpoo.istock.gui.AlertDialogGenerator;
import bsi.mpoo.istock.services.client.ClientServices;
import bsi.mpoo.istock.services.Constants;
import bsi.mpoo.istock.services.Exceptions.ClientAlreadyRegistered;
import bsi.mpoo.istock.services.MaskGenerator;
import bsi.mpoo.istock.services.Validations;

public class RegisterClientActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText streetEditText;
    private EditText numberEditText;
    private EditText districtEditText;
    private EditText cityEditText;
    private EditText stateEditText;
    private EditText phoneEditText;
    private Object account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        nameEditText = findViewById(R.id.editTextNameRegisterClient);
        streetEditText = findViewById(R.id.editTextStreetRegisterClient);
        numberEditText = findViewById(R.id.editTextNumberRegisterClient);
        districtEditText = findViewById(R.id.editTextDistrictRegisterClient);
        cityEditText = findViewById(R.id.editTextCityRegisterClient);
        stateEditText = findViewById(R.id.editTextStateRegisterClient);
        phoneEditText = findViewById(R.id.editTextPhoneRegisterClient);
        MaskGenerator.mask(phoneEditText, Constants.MaskTypes.PHONE);
    }

    public void register(View view) {
        Validations validations = new Validations(getApplicationContext());
        if (!isAllFieldsValid(validations)) return;
        account = Session.getInstance().getAccount();
        ClientServices clientServices = new ClientServices(getApplicationContext());
        Client newClient = new Client();
        if (account instanceof Administrator || account instanceof Salesman){
            newClient.setAdministrator(Session.getInstance().getAdministrator());
        } else { return; }
        newClient.setName(nameEditText.getText().toString().trim().toUpperCase());
        newClient.setPhone(MaskGenerator.unmask(phoneEditText.getText().toString()));
        newClient.setStatus(Constants.Status.ACTIVE);
        Address newAddress = new Address();
        newAddress.setStreet(streetEditText.getText().toString().trim().toUpperCase());
        newAddress.setNumber(Integer.parseInt(numberEditText.getText().toString()));
        newAddress.setDistrict(districtEditText.getText().toString().trim().toUpperCase());
        newAddress.setCity(cityEditText.getText().toString().trim().toUpperCase());
        newAddress.setState(stateEditText.getText().toString().trim().toUpperCase());
        newAddress.setStatus(Constants.Status.ACTIVE);
        newClient.setAddress(newAddress);

        try {
            clientServices.registerClient(newClient, Session.getInstance().getAdministrator());
            String message = getString(R.string.register_done);
            new AlertDialogGenerator(this, message, true ).invoke();

        } catch (ClientAlreadyRegistered error){
            String message = getString(R.string.client_already_registered);
            new AlertDialogGenerator(this, message, false ).invoke();
            validations.clearFields(nameEditText);

        } catch (Exception error){
            String message = getString(R.string.unknow_error);
            new AlertDialogGenerator(this, message, false).invoke();

        }

    }

    private boolean isAllFieldsValid(Validations validations) {
        boolean valid = validations.editValidate(nameEditText, streetEditText,
                numberEditText, districtEditText, cityEditText, stateEditText, phoneEditText
        );

        if (!validations.name(nameEditText.getText().toString())){
            validations.setErrorIfNull(nameEditText,getString(R.string.invalid_Name));
            valid = false;
        }

        if (!validations.name(streetEditText.getText().toString())){
            validations.setErrorIfNull(streetEditText, getString(R.string.invalid_Name));
            valid = false;
        }

        if (!validations.name(districtEditText.getText().toString())){
            validations.setErrorIfNull(districtEditText, getString(R.string.invalid_Name));
            valid = false;
        }

        if (!validations.name(cityEditText.getText().toString())){
            validations.setErrorIfNull(cityEditText, getString(R.string.invalid_Name));
            valid = false;
        }

        if (!validations.name(stateEditText.getText().toString())){
            validations.setErrorIfNull(stateEditText, getString(R.string.invalid_Name));
            valid = false;
        }

        if (!validations.phone(phoneEditText.getText().toString())){
            validations.setErrorIfNull(phoneEditText, getString(R.string.invalid_phone));
            valid = false;
        }
        return valid;
    }
}
