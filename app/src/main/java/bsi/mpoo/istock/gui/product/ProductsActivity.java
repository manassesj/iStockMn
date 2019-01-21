package bsi.mpoo.istock.gui.product;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import bsi.mpoo.istock.R;
import bsi.mpoo.istock.domain.Administrator;
import bsi.mpoo.istock.domain.Producer;
import bsi.mpoo.istock.domain.Product;
import bsi.mpoo.istock.domain.Salesman;
import bsi.mpoo.istock.domain.Session;
import bsi.mpoo.istock.gui.MainActivity;
import bsi.mpoo.istock.services.product.ProductListAdapter;
import bsi.mpoo.istock.services.product.ProductServices;

public class ProductsActivity extends AppCompatActivity {

    private Object account;
    private ProductListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        setUpMenuToolbar();
        setUpFloatingActionButton();

    }

    private void setUpFloatingActionButton() {
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingButtonProduct);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterProductActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpMenuToolbar() {
        View view = getLayoutInflater().inflate(R.layout.menu_actionbar_search, null);
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        ImageView imageViewBack = findViewById(R.id.back_toolbar);
        TextView textView = findViewById(R.id.title_toolbar);
        ImageView searchView = findViewById(R.id.search_toolbar);
        textView.setText(R.string.products);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                close();
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void close(){
        finishAffinity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        account = Session.getInstance().getAccount();
        ProductServices productServices = new ProductServices(this);
        ArrayList<Product> productArrayList;

        if (account instanceof Administrator || account instanceof Salesman || account instanceof Producer){
            productArrayList = productServices.getAcitiveProductsAsc(Session.getInstance().getAdministrator());
            RecyclerView recyclerView;
            recyclerView = findViewById(R.id.recyclerviewProduct);
            adapter = new ProductListAdapter(this, productArrayList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}
