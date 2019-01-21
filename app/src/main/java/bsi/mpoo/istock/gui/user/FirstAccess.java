package bsi.mpoo.istock.gui.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.media.ExifInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import bsi.mpoo.istock.R;
import bsi.mpoo.istock.domain.User;
import bsi.mpoo.istock.gui.AlertDialogGenerator;
import bsi.mpoo.istock.gui.LoginActivity;
import bsi.mpoo.istock.services.Constants;
import bsi.mpoo.istock.services.Encryption;
import bsi.mpoo.istock.services.Exceptions;
import bsi.mpoo.istock.services.ImageServices;
import bsi.mpoo.istock.services.user.UserServices;
import bsi.mpoo.istock.services.Validations;

public class FirstAccess extends AppCompatActivity {
    private ImageView imageRegister;
    private Bitmap reducedImageProfile;
    private User user;
    private EditText passwordEditText;
    private EditText passwordConfirmationEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_access);
        ActionBar actionBar = getSupportActionBar();
        passwordEditText = findViewById(R.id.editPasswordFirstAccess);
        passwordConfirmationEditText = findViewById(R.id.editPasswordConfirmFirstAccess);
        actionBar.hide();
        Intent intent = getIntent();
        user = intent.getParcelableExtra(Constants.BundleKeys.USER);

    }

    public void confirm(View view) {
        Validations validations = new Validations(getApplicationContext());
        if (!isAllFieldsValid(validations)){
            return;
        }
        user.setPassword(Encryption.encrypt(passwordEditText.getText().toString()));
        ImageServices imageServices = new ImageServices();
        user.setImage(imageServices.imageToByte(reducedImageProfile));
        user.setStatus(Constants.Status.ACTIVE);
        UserServices userServices = new UserServices(getApplicationContext());
        try {
            userServices.updateUser(user);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } catch (Exceptions.UserNotRegistered error){
            String message = getString(R.string.user_not_registered);
            new AlertDialogGenerator(this, message, false).invoke();

        } catch (Exception error){
            String message = getString(R.string.unknow_error);
            new AlertDialogGenerator(this, message, false).invoke();
        }

    }

    private boolean isAllFieldsValid(Validations validations) {
        boolean valid = validations.editValidate(passwordEditText, passwordConfirmationEditText);
        if (!validations.password(passwordEditText.getText().toString())){
            validations.setErrorIfNull(passwordEditText, getString(R.string.invalid_password_weak));
            valid = false;
        }
        if (!validations.password(passwordConfirmationEditText.getText().toString())){
            validations.setErrorIfNull(passwordConfirmationEditText, getString(R.string.invalid_password_weak));
            valid = false;
        }
        if (!validations.passwordEquals(
                passwordEditText.getText().toString(),
                passwordConfirmationEditText.getText().toString())){
            validations.setErrorIfNull(passwordEditText, getString(R.string.invalid_password_not_equals));
            validations.setErrorIfNull(passwordConfirmationEditText, getString(R.string.invalid_password_not_equals));
            valid = false;
        }


        return valid;
    }

    public void pickImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.pick_a_image)),Constants.Image.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK){

            if (requestCode == Constants.Image.REQUEST_CODE){

                Uri pickedImage = data.getData();
                imageRegister = findViewById(R.id.editImageRegister);

                try {
                    ImageServices imageServices = new ImageServices();
                    int orientation = Constants.Image.ORIENTATION_OUT_OF_BOUNDS;

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        InputStream inputStream = getContentResolver().openInputStream(pickedImage);
                        ExifInterface exifInterface = new ExifInterface(inputStream);
                        orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    }
                    Bitmap imageProfile = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pickedImage);
                    byte[] imageProfileByte = imageServices.imageToByte(imageProfile);
                    reducedImageProfile = imageServices.byteToImage(imageServices.reduceBitmap(imageProfileByte));

                    if (orientation < Constants.Image.ORIENTATION_OUT_OF_BOUNDS){
                        reducedImageProfile = imageServices.rotate(reducedImageProfile, orientation);
                    }
                    setImageOnImageView();

                } catch (IOException error){
                    String message = getString(R.string.unknow_error);
                    new AlertDialogGenerator(this,message,false).invoke();

                }
            }
        }
    }

    private void setImageOnImageView() {
        imageRegister.setImageBitmap(reducedImageProfile);
        imageRegister.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }
}
