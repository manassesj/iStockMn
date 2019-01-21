package bsi.mpoo.istock.services.client;

import android.app.Activity;
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
import bsi.mpoo.istock.R;
import bsi.mpoo.istock.domain.Client;
import bsi.mpoo.istock.domain.Session;
import bsi.mpoo.istock.gui.AlertDialogGenerator;
import bsi.mpoo.istock.gui.DialogDetails;
import bsi.mpoo.istock.gui.client.EditClientActivity;
import bsi.mpoo.istock.services.Constants;
import bsi.mpoo.istock.services.MaskGenerator;

public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.ClientViewHolder> implements Filterable{

    private final ArrayList<Client> clientList;
    private  ArrayList<Client> clientListFull;
    private LayoutInflater inflater;
    private Context context;

    public ClientListAdapter(Context context, ArrayList<Client> clientList){
        inflater = LayoutInflater.from(context);
        this.clientList = clientList;
        clientListFull = new ArrayList<>(clientList);
        this.context = context;

    }

    class ClientViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        final TextView nameItemView;
        final TextView phoneItemView;
        final ClientListAdapter adapter;

        private ClientViewHolder(View itemView, ClientListAdapter adapter ){
            super(itemView);
            itemView.setOnCreateContextMenuListener(this);
            nameItemView = itemView.findViewById(R.id.nameClientItemList);
            phoneItemView = itemView.findViewById(R.id.phoneClientItemList);
            this.adapter = adapter;

        }

        @Override
        public boolean onMenuItemClick(MenuItem item){
            ClientServices clientServices = new ClientServices(context);
            int position = getLayoutPosition();
            Client client = clientList.get(position);
            final String detailOption = context.getApplicationContext().getString(R.string.details);
            final String deleteOption = context.getApplicationContext().getString(R.string.delete);
            final String editOption = context.getApplicationContext().getString(R.string.edit);

            if (item.getTitle().toString().equals(deleteOption)){

                try {
                    clientServices.disableClient(client, Session.getInstance().getAdministrator());
                    clientList.remove(position);
                    adapter.notifyDataSetChanged();

                } catch (Exception error) {
                    new AlertDialogGenerator((Activity) context, error.getMessage(),false).invoke();

                }

            } else if (item.getTitle().equals(editOption)){
                Intent intent = new Intent(context, EditClientActivity.class);
                intent.putExtra(Constants.BundleKeys.CLIENT, client);
                context.startActivity(intent);

            } else if (item.getTitle().equals(detailOption)){
                DialogDetails dialogDetails = new DialogDetails(context);
                dialogDetails.invoke(client);

            }
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem detailItem = menu.add(context.getApplicationContext().getString(R.string.details));
            MenuItem editItem = menu.add(context.getApplicationContext().getString(R.string.edit));
            MenuItem deleteItem = menu.add(context.getApplicationContext().getString(R.string.delete));
            detailItem.setOnMenuItemClickListener(this);
            editItem.setOnMenuItemClickListener(this);
            deleteItem.setOnMenuItemClickListener(this);
        }
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.client_list_item, parent, false);
        return new ClientViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder clientViewHolder, int position) {
        String currentName = clientList.get(position).getName();
        String currentPhone = MaskGenerator.unmaskedTextToStringMasked(clientList.get(position).getPhone(), Constants.MaskTypes.PHONE);
        clientViewHolder.nameItemView.setText(currentName);
        clientViewHolder.phoneItemView.setText(currentPhone);
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    @Override
    public Filter getFilter() {
        return clientFilter;
    }

    private Filter clientFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Client> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(clientListFull);
            } else {
                String filteredPattern = constraint.toString().toLowerCase().trim();
                for(Client client : clientListFull){
                    if(client.getName().toLowerCase().contains(filteredPattern)){
                        filteredList.add(client);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clientList.clear();
            clientList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
}
