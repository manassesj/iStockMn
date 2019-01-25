package bsi.mpoo.istock.gui.sales;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bsi.mpoo.istock.R;
import bsi.mpoo.istock.domain.Administrator;
import bsi.mpoo.istock.domain.Cart;
import bsi.mpoo.istock.domain.Product;
import bsi.mpoo.istock.domain.Salesman;
import bsi.mpoo.istock.domain.Session;
import bsi.mpoo.istock.services.order.SlopeOne;
import bsi.mpoo.istock.services.product.ProductListAdapter;
import bsi.mpoo.istock.services.product.ProductServices;

public class Recommend extends AppCompatActivity {

    private Object account;
    private RecyclerView recyclerView;
    private ProductListAdapter adapter;
    private ProductServices productServices;
    static String[] products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_rec);
        setUpMenuToolbar();
    }

    private void setUpMenuToolbar() {
        View view = getLayoutInflater().inflate(R.layout.menu_actionbar_rec, null);
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        ImageView imageViewBack = findViewById(R.id.back_toolbar_rec);
        TextView textView = findViewById(R.id.title_toolbar_rec);
        ImageView searchView = findViewById(R.id.search_toolbar_rec);

        textView.setText(R.string.rec);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductsOrderActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        account = Session.getInstance().getAccount();
        if (account instanceof Administrator || account instanceof Salesman){
            productServices = new ProductServices(getApplicationContext());

            Map<String, Map<String, Double>> data = new HashMap<>();

            String banana = "BANANA";
            String pera = "PERA";
            String uva = "UVA";
            String melao = "MELÃO";
            String maca = "MACÃ";

            products = new String[] {banana, pera,uva ,melao, maca};

            HashMap<String, Double> user1 = new HashMap<>();
            HashMap<String, Double> user2 = new HashMap<>();
            HashMap<String, Double> user3 = new HashMap<>();
            HashMap<String, Double> user4 = new HashMap<>();
            //Os elementos são o nome do produto e a quantidade que foi comprada(convertida para double).
            user1.put(banana, 2.0);
            user1.put(pera, 3.0);
            user1.put(melao, 4.0);
            data.put("Oscar", user1);
            user2.put(banana, 1.0);
            user2.put(uva, 3.0);
            user2.put(melao, 2.0);
            data.put("Elise", user2);
            user3.put(banana, 9.0);
            user3.put(pera, 4.0);
            user3.put(uva, 5.0);
            user3.put(melao, 1.0);
            data.put("Katarina", user3);
            user4.put(banana ,1.0);
            user4.put(melao, 1.0);
            user4.put(maca, 4.0);
            data.put("Milena", user4);

            SlopeOne slopeOne = new SlopeOne(data,products);
            HashMap<String, Double> user = new HashMap<>();
            ArrayList<String> productInCar = new ArrayList<>();
            user.put(maca, 2.0);
            productInCar.add(maca);
            Map<String, Double> productsForRec = slopeOne.predict(user);
            ArrayList<Product> products = new ArrayList<>();

            for (String j : productsForRec.keySet())
            {
                if(!productInCar.contains(j)) {
                    Product product = productServices.getProductByName(j, Session.getInstance().getAdministrator());
                    products.add(product);
                }
            }

            recyclerView = findViewById(R.id.recyclerviewRec);
            if(products != null){
            adapter = new ProductListAdapter(this, products);
            recyclerView.setAdapter(adapter);
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


        }


    }

}
