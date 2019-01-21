package bsi.mpoo.istock.services.product;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.ArrayList;
import bsi.mpoo.istock.R;
import bsi.mpoo.istock.domain.Cart;
import bsi.mpoo.istock.domain.Item;
import bsi.mpoo.istock.domain.Product;
import bsi.mpoo.istock.gui.DialogDetails;
import bsi.mpoo.istock.gui.sales.DialogQuantity;

public class ProductOrderListAdapter extends RecyclerView.Adapter<ProductOrderListAdapter.ProductOrderViewHolder> {

    private final ArrayList<Product> productList;
    private LayoutInflater inflater;
    private Context context;
    private ImageView cart;


    public ProductOrderListAdapter(Context context, ArrayList<Product> productList, ImageView cart){
        inflater = LayoutInflater.from(context);
        this.productList = productList;
        this.context = context;
        this.cart = cart;
    }

    class ProductOrderViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        TextView nameItemView;
        TextView totalItemView;
        TextView quantityItemView;
        LinearLayout linearLayout;
        ProductOrderListAdapter adapter;

        @Override
        public boolean onMenuItemClick(MenuItem item){
            int position = getLayoutPosition();
            Product product = productList.get(position);
            final String detailOption = context.getApplicationContext().getString(R.string.details);
            final String addOption = context.getApplicationContext().getString(R.string.add);
            final String removeOtion = context.getApplicationContext().getString(R.string.delete);

            if (item.getTitle().toString().equals(addOption)){
                Item itemConverted = convertProductToItem(product);
                new DialogQuantity(context, linearLayout, cart).invokeQuantity(product.getQuantity(), itemConverted);



            } else if (item.getTitle().equals(detailOption)){
                DialogDetails dialogDetails = new DialogDetails(context);
                dialogDetails.invoke(product);

            } else if (item.getTitle().equals(removeOtion)){
                Cart.getInstance().removeItem(convertProductToItem(product));
                if (Cart.getInstance().getItems().size() == 0){
                    cart.setBackgroundResource(R.drawable.ic_sales_before);
                }
                linearLayout.setBackgroundColor(0x00000000);
            }
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem detailsItem = menu.add(context.getApplicationContext().getString(R.string.details));

            int position = getLayoutPosition();
            Product product = productList.get(position);

            if (Cart.getInstance().getItems().contains(convertProductToItem(product))){
                MenuItem deleteItem = menu.add(context.getApplicationContext().getString(R.string.delete));
                deleteItem.setOnMenuItemClickListener(this);
            } else{
                MenuItem addItem = menu.add(context.getApplicationContext().getString(R.string.add));
                addItem.setOnMenuItemClickListener(this);
            }
            detailsItem.setOnMenuItemClickListener(this);
        }


        private ProductOrderViewHolder(View itemView, ProductOrderListAdapter adapter){
            super(itemView);
            itemView.setOnCreateContextMenuListener(this);
            nameItemView = itemView.findViewById(R.id.nameProductOrderItemList);
            totalItemView = itemView.findViewById(R.id.priceProductOrderItemList);
            quantityItemView = itemView.findViewById(R.id.quantityProductOrderItemList);
            linearLayout = itemView.findViewById(R.id.linearLayoutProductOrderListItem);
            this.adapter = adapter;
        }
    }

    @NonNull
    @Override
    public ProductOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.product_order_list_item, parent, false);
        return new ProductOrderViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductOrderViewHolder orderViewHolder, int position) {
        String currentName = productList.get(position).getName();
        String currentPrice = NumberFormat.getCurrencyInstance().format(productList.get(position).getPrice());
        String currentQuantity = String.valueOf(productList.get(position).getQuantity());
        orderViewHolder.nameItemView.setText(currentName);
        orderViewHolder.totalItemView.setText(currentPrice);
        orderViewHolder.quantityItemView.setText(currentQuantity);
        if (Cart.getInstance().getItems().contains(convertProductToItem(productList.get(position)))){
            orderViewHolder.linearLayout.setBackgroundColor(context.getColor(R.color.greenLight));
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public Item convertProductToItem(Product product){
        Item item = new Item();
        item.setPrice(product.getPrice());
        item.setIdAdministrator(product.getAdministrator().getUser().getId());
        item.setStatus(product.getStatus());
        item.setProduct(product);
        return item;
    }
}
