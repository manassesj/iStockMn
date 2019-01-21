package bsi.mpoo.istock.services.user;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import bsi.mpoo.istock.gui.user.EditUserActivity;
import bsi.mpoo.istock.R;
import bsi.mpoo.istock.domain.User;
import bsi.mpoo.istock.gui.DialogDetails;
import bsi.mpoo.istock.services.Constants;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> implements Filterable{
    private final ArrayList<User> userList;
    private ArrayList<User> userListfull;
    private LayoutInflater inflater;
    private Context context;

    public UserListAdapter(Context context, ArrayList<User> userList){
        inflater = LayoutInflater.from(context);
        this.userList = userList;
        this.context = context;
    }

    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        final TextView nameItemView;
        final TextView functionItemView;
        final UserListAdapter adapter;

        private UserViewHolder(View itemView, UserListAdapter adapter ){
            super(itemView);
            itemView.setOnCreateContextMenuListener(this);
            nameItemView = itemView.findViewById(R.id.nameUserItemList);
            functionItemView = itemView.findViewById(R.id.functionUserItemList);
            this.adapter = adapter;

        }

        @Override
        public boolean onMenuItemClick(MenuItem item){
            int position = getLayoutPosition();
            User user = userList.get(position);
            final String detailOption = context.getApplicationContext().getString(R.string.details);
            final String editOption = context.getApplicationContext().getString(R.string.edit);

            if (item.getTitle().equals(editOption)){
                Intent intent = new Intent(context, EditUserActivity.class);
                intent.putExtra(Constants.BundleKeys.USER, user);
                context.startActivity(intent);
            } else if (item.getTitle().equals(detailOption)){
                DialogDetails dialogDetails = new DialogDetails(context);
                dialogDetails.invoke(user);
            }
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem detailItem = menu.add(context.getApplicationContext().getString(R.string.details));
            MenuItem editItem = menu.add(context.getApplicationContext().getString(R.string.edit));
            detailItem.setOnMenuItemClickListener(this);
            editItem.setOnMenuItemClickListener(this);
        }

    }

    @NonNull
    @Override
    public UserListAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.user_list_item, parent, false);
        return new UserListAdapter.UserViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.UserViewHolder userViewHolder, int position) {
        String currentName = userList.get(position).getName();
        String currentFunction = null;
        switch (userList.get(position).getType()){
            case Constants.UserTypes.ADMINISTRATOR:
                currentFunction = context.getString(R.string.administration);
                break;
            case Constants.UserTypes.SALESMAN:
                currentFunction = context.getString(R.string.sales);
                break;
            case Constants.UserTypes.PRODUCER:
                currentFunction = context.getString(R.string.production);
                break;
        }
        userViewHolder.nameItemView.setText(currentName);
        userViewHolder.functionItemView.setText(currentFunction);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<User> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(userListfull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(User user : userListfull){
                    if(user.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(user);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            userList.clear();
            userList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
}
