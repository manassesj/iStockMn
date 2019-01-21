package bsi.mpoo.istock.gui.user;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import bsi.mpoo.istock.R;
import bsi.mpoo.istock.domain.Session;
import bsi.mpoo.istock.domain.User;
import bsi.mpoo.istock.gui.AlertDialogGenerator;
import bsi.mpoo.istock.services.Constants;
import bsi.mpoo.istock.services.Exceptions;
import bsi.mpoo.istock.services.user.UserServices;

public class EditUserActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private User user;
    private Spinner spinner;
    private Object account;
    private TextView nameTextView;
    private int selectedOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user = bundle.getParcelable(Constants.BundleKeys.USER);
        account = Session.getInstance().getAccount();
        spinner = findViewById(R.id.spinnerEditUser);
        spinner.setOnItemSelectedListener(this);
        ArrayList<String> functions = new ArrayList<>();
        functions.add(getString(R.string.sales));
        functions.add(getString(R.string.production));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                this, R.layout.support_simple_spinner_dropdown_item, functions);
        dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        nameTextView = findViewById(R.id.nameViewEditUser);
        nameTextView.setText(user.getName());
        selectedOption = user.getType();
        if (selectedOption == Constants.UserTypes.SALESMAN){
            spinner.setSelection(dataAdapter.getPosition(getString(R.string.sales)));
        } else {
            spinner.setSelection(dataAdapter.getPosition(getString(R.string.production)));
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String option = parent.getItemAtPosition(position).toString();
        if (option.equals(getString(R.string.production).intern())){
            selectedOption = Constants.UserTypes.PRODUCER;
        } else {
            selectedOption = Constants.UserTypes.SALESMAN;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void save(View view) {
        UserServices userServices = new UserServices(getApplicationContext());
        user.setType(selectedOption);
        try {
            userServices.updateUser(user);
            String message = getString(R.string.edit_successful);
            new AlertDialogGenerator(this, message, true).invoke();

        } catch (Exceptions.UserNotRegistered error){
        String message = getString(R.string.user_not_registered);
        new AlertDialogGenerator(this, message, false).invoke();

        } catch (Exception error){
            String message = getString(R.string.unknow_error);
            new AlertDialogGenerator(this, message, false).invoke();

        }

    }

    public void delete(View view) {
        UserServices userServices = new UserServices(getApplicationContext());
        try {
            userServices.disableUser(user);
            String message = getString(R.string.edit_successful);
            new AlertDialogGenerator(this, message, true).invoke();
        } catch (Exceptions.UserNotRegistered error){
            String message = getString(R.string.user_not_registered);
            new AlertDialogGenerator(this, message, false).invoke();

        } catch (Exception error){
            String message = getString(R.string.unknow_error);
            new AlertDialogGenerator(this, message, false).invoke();
        }

    }
}
