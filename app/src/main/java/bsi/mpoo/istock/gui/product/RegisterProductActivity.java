package bsi.mpoo.istock.gui.product;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.math.BigDecimal;
import bsi.mpoo.istock.R;
import bsi.mpoo.istock.domain.Administrator;
import bsi.mpoo.istock.domain.Producer;
import bsi.mpoo.istock.domain.Product;
import bsi.mpoo.istock.domain.Session;
import bsi.mpoo.istock.gui.AlertDialogGenerator;
import bsi.mpoo.istock.services.Constants;
import bsi.mpoo.istock.services.Exceptions.ProductAlreadyRegistered;
import bsi.mpoo.istock.services.product.ProductServices;
import bsi.mpoo.istock.services.Validations;

public class RegisterProductActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText priceEditText;
    private EditText quantityEditText;
    private EditText minimumEditText;
    private Object account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_product);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        nameEditText = findViewById(R.id.editTextNameRegisterProduct);
        priceEditText = findViewById(R.id.editTextPriceRegisterProduct);
        quantityEditText = findViewById(R.id.editTextQuantityRegisterProduct);
        minimumEditText = findViewById(R.id.editTextMinimumRegisterProduct);
    }

    public void register(View view) {
        Validations validations = new Validations(getApplicationContext());
        if (!isAllFieldsValid(validations)) return;
        account = Session.getInstance().getAccount();
        ProductServices productServices = new ProductServices(getApplicationContext());
        Product newProduct = new Product();
        if (account instanceof Administrator || account instanceof Producer){
            newProduct.setAdministrator(Session.getInstance().getAdministrator());
        } else { return; }
        newProduct.setName(nameEditText.getText().toString().trim().toUpperCase());
        newProduct.setPrice(new BigDecimal(priceEditText.getText().toString()));
        newProduct.setQuantity(Long.parseLong(quantityEditText.getText().toString()));

        if (minimumEditText.getText().toString().isEmpty()){
            newProduct.setMinimumQuantity(0);
        } else {
            newProduct.setMinimumQuantity(Long.parseLong(minimumEditText.getText().toString()));
        }

        newProduct.setStatus(Constants.Status.ACTIVE);

        try {
            productServices.registerProduct(newProduct, Session.getInstance().getAdministrator());
            String message = getString(R.string.register_done);
            new AlertDialogGenerator(this, message, true).invoke();

        } catch (ProductAlreadyRegistered error){
            String message = getString(R.string.product_already_registered);
            new AlertDialogGenerator(this, message, false).invoke();
            validations.clearFields(nameEditText);

        } catch (Exception error){
            String message = getString(R.string.unknow_error);
            new AlertDialogGenerator(this, message, false).invoke();

        }
    }

    private boolean isAllFieldsValid(Validations validations){
        boolean valid = validations.editValidate(nameEditText, quantityEditText, priceEditText);

        if (!validations.name(nameEditText.getText().toString())){
            validations.setErrorIfNull(nameEditText, getString(R.string.invalid_Name));
            valid = false;
        }

        if (!validations.price(priceEditText.getText().toString())){
            validations.setErrorIfNull(priceEditText, getString(R.string.invalid_value));
            valid = false;
        }

        if (!validations.quantity(quantityEditText.getText().toString())){
            validations.setErrorIfNull(quantityEditText, getString(R.string.invalid_value));
            valid = false;
        }

        if (!validations.minimum(minimumEditText.getText().toString())){
            minimumEditText.requestFocus();
            minimumEditText.setError(getString(R.string.invalid_quantity));
            valid = false;
        }
        return valid;
    }
}
