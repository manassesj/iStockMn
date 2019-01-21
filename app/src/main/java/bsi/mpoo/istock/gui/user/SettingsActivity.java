package bsi.mpoo.istock.gui.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.media.ExifInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import bsi.mpoo.istock.R;
import bsi.mpoo.istock.domain.Administrator;
import bsi.mpoo.istock.domain.Producer;
import bsi.mpoo.istock.domain.Salesman;
import bsi.mpoo.istock.domain.Session;
import bsi.mpoo.istock.domain.User;
import bsi.mpoo.istock.gui.AlertDialogGenerator;
import bsi.mpoo.istock.services.Constants;
import bsi.mpoo.istock.services.Exceptions;
import bsi.mpoo.istock.services.ImageServices;
import bsi.mpoo.istock.services.SessionServices;
import bsi.mpoo.istock.services.user.UserServices;

public class SettingsActivity extends AppCompatActivity {

    private User user;
    private EditText companyNameEditText;
    private TextInputLayout companyTextInputLayout;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private ImageView imageView;
    private Button buttonDelete;
    private Bitmap reducedImageProfile;
    private UserServices userServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        userServices = new UserServices(getApplicationContext());
        companyNameEditText = findViewById(R.id.editCompanyNameSettings);
        companyTextInputLayout =findViewById(R.id.companyInputLayoutSettings);
        nameEditText = findViewById(R.id.editFullNameSettings);
        emailEditText = findViewById(R.id.editEmailSettings);
        passwordEditText = findViewById(R.id.editPasswordSettings);
        imageView = findViewById(R.id.pictureSettings);
        buttonDelete = findViewById(R.id.deleteButtonSettings);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Session.getInstance().getAccount() instanceof Salesman){
            user = ((Salesman) Session.getInstance().getAccount()).getUser();
            companyTextInputLayout.setVisibility(View.GONE);
            buttonDelete.setVisibility(View.GONE);

        } else if (Session.getInstance().getAccount() instanceof Producer){
            user = ((Producer) Session.getInstance().getAccount()).getUser();
            companyTextInputLayout.setVisibility(View.GONE);
            buttonDelete.setVisibility(View.GONE);
        } else {
            user = ((Administrator)Session.getInstance().getAccount()).getUser();
        }
        companyNameEditText.setText(user.getCompany());
        nameEditText.setText(user.getName());
        emailEditText.setText(user.getEmail());
        passwordEditText.setText(R.string.any_text);
        ImageServices imageServices = new ImageServices();
        if (user.getImage() != null){
            imageView.setImageBitmap(imageServices.byteToImage(user.getImage()));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    public void editCompany(View view) {
        Intent intent = new Intent(this, SettingsHelperActivity.class);
        Bundle bundle = new Bundle();
        int optionSelected = Constants.SettingsHelper.COMPANY;
        bundle.putInt(Constants.BundleKeys.SETTINGS,optionSelected);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void editName(View view) {
        Intent intent = new Intent(this, SettingsHelperActivity.class);
        Bundle bundle = new Bundle();
        int optionSelected = Constants.SettingsHelper.NAME;
        bundle.putInt(Constants.BundleKeys.SETTINGS,optionSelected);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void editEmail(View view) {
        Intent intent = new Intent(this, SettingsHelperActivity.class);
        Bundle bundle = new Bundle();
        int optionSelected = Constants.SettingsHelper.EMAIL;
        bundle.putInt(Constants.BundleKeys.SETTINGS,optionSelected);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void editPassword(View view) {
        Intent intent = new Intent(this, SettingsHelperActivity.class);
        Bundle bundle = new Bundle();
        int optionSelected = Constants.SettingsHelper.PASSWORD;
        bundle.putInt(Constants.BundleKeys.SETTINGS,optionSelected);
        intent.putExtras(bundle);
        startActivity(intent);
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
                    user.setImage(imageServices.imageToByte(reducedImageProfile));
                    updateUser();


                } catch (IOException error){
                    String message = getString(R.string.unknow_error);
                    new AlertDialogGenerator(this,message,false).invoke();

                }
            }
        }
    }

    private void setImageOnImageView() {
        imageView.setImageBitmap(reducedImageProfile);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    private void updateUser() {
        try {
            userServices.updateUser(user);
            SessionServices sessionServices = new SessionServices(getApplicationContext());
            sessionServices.refreshSession();

        } catch (Exceptions.UserNotRegistered error){
            new AlertDialogGenerator(this,
                    getString(R.string.user_not_registered), false).invoke();

        } catch (Exception error){
            new AlertDialogGenerator(this,
                    getString(R.string.unknow_error), false).invoke();
        }
    }

    public void delete(View view) {

        Intent intent = new Intent(this, SettingsHelperActivity.class);
        Bundle bundle = new Bundle();
        int optionSelected = Constants.SettingsHelper.DELETE;
        bundle.putInt(Constants.BundleKeys.SETTINGS,optionSelected);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}
