package bsi.mpoo.istock.gui.historic;

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
import bsi.mpoo.istock.domain.Order;
import bsi.mpoo.istock.domain.Producer;
import bsi.mpoo.istock.domain.Product;
import bsi.mpoo.istock.domain.Salesman;
import bsi.mpoo.istock.domain.Session;
import bsi.mpoo.istock.gui.MainActivity;
import bsi.mpoo.istock.gui.product.RegisterProductActivity;
import bsi.mpoo.istock.services.order.OrderListAdapter;
import bsi.mpoo.istock.services.order.OrderServices;
import bsi.mpoo.istock.services.product.ProductListAdapter;
import bsi.mpoo.istock.services.product.ProductServices;

public class HistoricActivity extends AppCompatActivity {

    private Object account;
    private OrderListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);
        setUpMenuToolbar();
    }

    private void setUpMenuToolbar() {
        View view = getLayoutInflater().inflate(R.layout.menu_actionbar_search, null);
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        ImageView imageViewBack = findViewById(R.id.back_toolbar);
        TextView textView = findViewById(R.id.title_toolbar);
        ImageView searchView = findViewById(R.id.search_toolbar);
        textView.setText(R.string.historic_sales);
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
        OrderServices orderServices = new OrderServices(this);
        ArrayList<Order> orderArrayList;
        if (account instanceof Administrator || account instanceof Salesman || account instanceof Producer){
            orderArrayList = orderServices.getAcitiveOrders(Session.getInstance().getAdministrator());
            RecyclerView recyclerView;
            recyclerView = findViewById(R.id.recyclerviewHistoric);
            adapter = new OrderListAdapter(this, orderArrayList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}
