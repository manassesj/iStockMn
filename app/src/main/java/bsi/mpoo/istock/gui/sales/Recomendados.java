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
import bsi.mpoo.istock.domain.Item;
import bsi.mpoo.istock.domain.Product;
import bsi.mpoo.istock.domain.Salesman;
import bsi.mpoo.istock.domain.Session;
import bsi.mpoo.istock.gui.MainActivity;
import bsi.mpoo.istock.services.order.OrderRecommend;
import bsi.mpoo.istock.services.product.ProductListAdapter;
import bsi.mpoo.istock.services.product.ProductServices;

public class Recomendados extends AppCompatActivity {

    private Object account;
    private RecyclerView recyclerView;
    private ProductListAdapter adapter;
    private ProductServices productServices;

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
            ArrayList<Long> listtId = new ArrayList<>();
            for(Item item:Cart.getInstance().getItems()){
                Product product = productServices.getProductByName(item.getProduct().getName(),Session.getInstance().getAdministrator());
                listtId.add(product.getId());
            }
            OrderRecommend orderRecommend = new OrderRecommend(getApplicationContext());
            String stringList = orderRecommend.arrayToString(listtId);
            ArrayList<String> result = new ArrayList<>();
            ArrayList<String> sub = orderRecommend.subLists("",stringList,result);
            ArrayList<Product> products = new ArrayList<>();
            sub.remove(sub.size() - 1);
            for(String sublList: sub){
                ArrayList<Long> subListCar = orderRecommend.stringToArray(sublList);
                products = orderRecommend.productForRecom(subListCar,listtId);
                if(products.size() >  0){
                    break;
                }
            }
            recyclerView = findViewById(R.id.recyclerviewRec);
            adapter = new ProductListAdapter(this, products);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


        }


    }
}
