package bsi.mpoo.istock.gui;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;

import bsi.mpoo.istock.R;
import bsi.mpoo.istock.domain.Client;
import bsi.mpoo.istock.domain.Order;
import bsi.mpoo.istock.domain.Product;
import bsi.mpoo.istock.domain.User;
import bsi.mpoo.istock.services.Constants;
import bsi.mpoo.istock.services.ImageServices;
import bsi.mpoo.istock.services.MaskGenerator;

public class DialogDetails extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private Context context;

    public DialogDetails(Context context) {
        this.context = context;
        builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("OK", null);
    }

    public void invoke(Client client){
        View view = LayoutInflater.from(context).inflate(R.layout.details_client_dialog, null);
        TextView nameTextView = view.findViewById(R.id.nameDetailsDialogClient);
        TextView streetTextView = view.findViewById(R.id.streetDetailsDialogClient);
        TextView numberTextView = view.findViewById(R.id.numberDetailsDialogClient);
        TextView districtTextView = view.findViewById(R.id.districtDetailsDialogClient);
        TextView cityTextView = view.findViewById(R.id.cityDetailsDialogClient);
        TextView stateTextView = view.findViewById(R.id.stateDetailsDialogClient);
        TextView phoneTextView = view.findViewById(R.id.phoneDetailsDialogClient);
        nameTextView.setText(client.getName());
        streetTextView.setText(client.getAddress().getStreet());
        numberTextView.setText(String.valueOf(client.getAddress().getNumber()));
        districtTextView.setText(client.getAddress().getDistrict());
        cityTextView.setText(client.getAddress().getCity());
        stateTextView.setText(client.getAddress().getState());
        phoneTextView.setText(MaskGenerator.unmaskedTextToStringMasked(client.getPhone(), Constants.MaskTypes.PHONE));
        builder.setView(view);
        builder.show();
    }

    public void invoke(Product product){
        View view = LayoutInflater.from(context).inflate(R.layout.details_product_dialog, null);
        TextView nameTextView = view.findViewById(R.id.nameDetailsDialogProduct);
        TextView priceTextView = view.findViewById(R.id.priceDetailsDialogProduct);
        TextView quantityTextView = view.findViewById(R.id.quantityDetailsDialogProduct);
        TextView minimumTextView = view.findViewById(R.id.minimumDetailsDialogProduct);
        nameTextView.setText(product.getName());
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(product.getPrice()));
        quantityTextView.setText(String.valueOf(product.getQuantity()));
        if (product.getMinimumQuantity() == 0) {
            minimumTextView.setText(R.string.without_info);
        } else {
            minimumTextView.setText(String.valueOf(product.getMinimumQuantity()));
        }
        builder.setView(view);
        builder.show();
    }

    public void invoke(User user){
        View view = LayoutInflater.from(context).inflate(R.layout.details_user_dialog, null);
        ImageView imageView = view.findViewById(R.id.imageDetailsDialogUser);
        TextView nameTextView = view.findViewById(R.id.nameDetailsDialogUser);
        TextView functionTextView = view.findViewById(R.id.functionDetailsDialogUser);
        TextView emailTextView = view.findViewById(R.id.emailDetailsDialogUser);
        TextView statusTextView = view.findViewById(R.id.statusDetailsDialogUser);
        ImageServices imageServices = new ImageServices();
        if (user.getImage() != null){
            imageView.setImageBitmap(imageServices.byteToImage(user.getImage()));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        nameTextView.setText(user.getName());
        switch (user.getType()){
            case Constants.UserTypes.ADMINISTRATOR:
                functionTextView.setText(context.getString(R.string.administration));
                break;
            case Constants.UserTypes.SALESMAN:
                functionTextView.setText(context.getString(R.string.sales));
                break;
            case Constants.UserTypes.PRODUCER:
                functionTextView.setText(context.getString(R.string.production));
                break;
        }
        switch (user.getStatus()){
            case Constants.Status.ACTIVE:
                statusTextView.setText(context.getString(R.string.active));
                break;
            case Constants.Status.INACTIVE:
                statusTextView.setText(context.getString(R.string.inactive));
                break;
            case Constants.Status.FIRST_ACCESS_FOR_USER:
                statusTextView.setText(context.getString(R.string.at_first_access));
                break;
        }
        emailTextView.setText(user.getEmail());
        builder.setView(view);
        builder.show();

    }

    public void invoke(Order order){

    }
}
