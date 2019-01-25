package bsi.mpoo.istock.gui.sales;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bsi.mpoo.istock.R;
import bsi.mpoo.istock.domain.Administrator;
import bsi.mpoo.istock.domain.Cart;
import bsi.mpoo.istock.domain.Product;
import bsi.mpoo.istock.domain.Session;
import bsi.mpoo.istock.gui.MainActivity;
import bsi.mpoo.istock.services.order.OrderRecommend;
import bsi.mpoo.istock.services.product.ProductOrderListAdapter;
import bsi.mpoo.istock.services.product.ProductServices;

public class ProductsOrderActivity extends AppCompatActivity {

    private Object account;
    private RecyclerView recyclerView;
    private ProductOrderListAdapter adapter;
    private ImageView cartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_order);
        setUpMenuToolbar();
    }


    private void setUpMenuToolbar() {
        View view = getLayoutInflater().inflate(R.layout.menu_actionbar_sales, null);
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        ImageView imageViewBack = findViewById(R.id.back_toolbar_sales);
        TextView textView = findViewById(R.id.title_toolbar_sales);
        ImageView searchView = findViewById(R.id.search_toolbar_sales);
        cartView = findViewById(R.id.cart_toolbar_sales);

        textView.setText(R.string.sales);
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
        cartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Cart.getInstance().getItems().size() > 0){
                    Intent intent = new Intent(getApplicationContext(), RegisterOrderActivity.class);
                    startActivity(intent);
                }

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
    }

    public void recommend(View view){
        Intent intent = new Intent(getApplicationContext(),Recomendados.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.onPostResume();
        if (Cart.getInstance().getItems().size() > 0){
            cartView.setBackgroundResource(R.drawable.ic_sales_after);
        } else {
            cartView.setBackgroundResource(R.drawable.ic_sales_before);
        }
        ProductServices productServices = new ProductServices(this);
        ArrayList<Product> productArrayList;

        if (account instanceof Administrator){
            productArrayList = productServices.getAcitiveProductsAsc(Session.getInstance().getAdministrator());
            recyclerView = findViewById(R.id.recyclerviewOrder);
            adapter = new ProductOrderListAdapter(this, productArrayList, cartView);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}
