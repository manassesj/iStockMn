package bsi.mpoo.istock.services.product;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.NumberFormat;
import bsi.mpoo.istock.R;
import bsi.mpoo.istock.domain.Cart;
import bsi.mpoo.istock.domain.Item;

public class ProductCartListAdapter  extends RecyclerView.Adapter<ProductCartListAdapter.ProductCartViewHolder>{

    private LayoutInflater inflater;
    private Context context;
    private TextView total;


    public ProductCartListAdapter(Context context, TextView total){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.total = total;
    }

    class ProductCartViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        TextView nameItemView;
        TextView totalItemView;
        TextView quantityItemView;
        ProductCartListAdapter adapter;

        @Override
        public boolean onMenuItemClick(MenuItem item){
            int position = getLayoutPosition();
            Item itemCart = Cart.getInstance().getItems().get(position);
            final String removeOtion = context.getApplicationContext().getString(R.string.delete);

             if (item.getTitle().equals(removeOtion)){
                Cart.getInstance().removeItem(itemCart);
                adapter.notifyDataSetChanged();
                total.setText(NumberFormat.getCurrencyInstance().format(Cart.getInstance().getTotal()));
            }
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem deleteItem = menu.add(context.getApplicationContext().getString(R.string.delete));
            deleteItem.setOnMenuItemClickListener(this);
        }
        private ProductCartViewHolder(View itemView, ProductCartListAdapter adapter){
            super(itemView);
            itemView.setOnCreateContextMenuListener(this);
            nameItemView = itemView.findViewById(R.id.nameProductCartItemList);
            totalItemView = itemView.findViewById(R.id.priceProductCartItemList);
            quantityItemView = itemView.findViewById(R.id.quantityProductCartItemList);
            this.adapter = adapter;
        }
    }

    @NonNull
    @Override
    public ProductCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.product_cart_list_item, parent, false);
        return new ProductCartViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCartViewHolder cartViewHolder, int position) {
        String currentName = Cart.getInstance().getItems().get(position).getProduct().getName();
        String currentPrice = NumberFormat.getCurrencyInstance().format(Cart.getInstance().getItems().get(position).getPrice());
        String currentQuantity = String.valueOf(Cart.getInstance().getItems().get(position).getQuantity());
        cartViewHolder.nameItemView.setText(currentName);
        cartViewHolder.totalItemView.setText(currentPrice);
        cartViewHolder.quantityItemView.setText(currentQuantity);
    }

    @Override
    public int getItemCount() {
        return Cart.getInstance().getItems().size();
    }

}
