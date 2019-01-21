package bsi.mpoo.istock.gui.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import bsi.mpoo.istock.R;
import bsi.mpoo.istock.domain.Address;
import bsi.mpoo.istock.domain.Client;
import bsi.mpoo.istock.gui.AlertDialogGenerator;
import bsi.mpoo.istock.services.client.ClientServices;
import bsi.mpoo.istock.services.Constants;
import bsi.mpoo.istock.services.Exceptions.ClientNotRegistered;
import bsi.mpoo.istock.services.MaskGenerator;
import bsi.mpoo.istock.services.Validations;

public class EditClientActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText streetEditText;
    private EditText numberEditText;
    private EditText districtEditText;
    private EditText cityEditText;
    private EditText stateEditText;
    private EditText phoneEditText;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        this.client = bundle.getParcelable(Constants.BundleKeys.CLIENT);
        nameEditText = findViewById(R.id.editTextNameEditClient);
        streetEditText = findViewById(R.id.editTextStreetEditClient);
        numberEditText = findViewById(R.id.editTextNumberEditClient);
        districtEditText = findViewById(R.id.editTextDistrictEditClient);
        cityEditText = findViewById(R.id.editTextCityEditClient);
        stateEditText = findViewById(R.id.editTextStateEditClient);
        phoneEditText = findViewById(R.id.editTextPhoneEditClient);
        nameEditText.setText(client.getName());
        streetEditText.setText(client.getAddress().getStreet());
        numberEditText.setText(String.valueOf(client.getAddress().getNumber()));
        districtEditText.setText(client.getAddress().getDistrict());
        cityEditText.setText(client.getAddress().getCity());
        stateEditText.setText(client.getAddress().getState());
        MaskGenerator.mask(phoneEditText, Constants.MaskTypes.PHONE);
        phoneEditText.setText(MaskGenerator.unmask(client.getPhone()));

    }

    public void edit(View view) {
        Validations validations = new Validations(getApplicationContext());
        if (!isAllFieldsValid(validations)) return;
        ClientServices clientServices = new ClientServices(getApplicationContext());
        Client newClient = new Client();
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
        newAddress.setId(client.getAddress().getId());
        newClient.setAddress(newAddress);
        newClient.setAdministrator(client.getAdministrator());
        newClient.setId(client.getId());

        try {
            clientServices.updateClient(newClient);
            String message = getString(R.string.edit_successful);
            new AlertDialogGenerator(this, message, true).invoke();

        } catch (ClientNotRegistered error){
            String message = getString(R.string.client_not_registered);
            new AlertDialogGenerator(this, message, false).invoke();

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
            validations.setErrorIfNull(nameEditText, getString(R.string.invalid_Name));
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
            validations.setErrorIfNull(phoneEditText,getString(R.string.invalid_phone) );
            valid = false;
        }
        return valid;
    }
}
