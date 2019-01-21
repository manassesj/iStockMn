package bsi.mpoo.istock.services.client;

import android.content.Context;

import java.util.ArrayList;

import bsi.mpoo.istock.data.client.ClientDAO;
import bsi.mpoo.istock.data.Contract;
import bsi.mpoo.istock.domain.Administrator;
import bsi.mpoo.istock.domain.Client;
import bsi.mpoo.istock.domain.User;
import bsi.mpoo.istock.services.Exceptions;
import bsi.mpoo.istock.services.Exceptions.ClientAlreadyRegistered;
import bsi.mpoo.istock.services.Exceptions.ClientNotRegistered;
public class ClientServices {

    private ClientDAO clientDAO;
    public ClientServices(Context context){
        this.clientDAO = new ClientDAO(context);
    }

    private boolean isClientRegistered(String name, Administrator administrator){
        Client searchedClient = clientDAO.getClientByName(name, administrator);
        return searchedClient != null;
    }

    public void registerClient(Client client, Administrator administrator) throws Exception {
        if (isClientRegistered(client.getName(), administrator)){
            throw new ClientAlreadyRegistered();
        } else {
            clientDAO.insertClient(client);
        }
    }

    public void updateClient(Client client) throws Exception{
            try {
                clientDAO.updateClient(client);
            } catch (Exception error){
                throw new ClientNotRegistered();
            }
    }

    public void disableClient(Client client, Administrator administrator) throws Exception{
        if (isClientRegistered(client.getName(), administrator)){
            clientDAO.disableClient(client);
        } else {
            throw  new ClientNotRegistered();
        }
    }

    public ArrayList<Client> getAcitiveClientsAsc(Administrator administrator){
        return (ArrayList<Client>) clientDAO.getActiveClientsByAdmId(administrator,Contract.ASC);
    }

    public ArrayList<Client> getAcitiveClientsDesc(Administrator administrator){
        return (ArrayList<Client>) clientDAO.getActiveClientsByAdmId(administrator,Contract.DESC);
    }

    public ArrayList<String> getActiveClientsName(Administrator administrator){
        ArrayList<Client> clients = getAcitiveClientsAsc(administrator);
        ArrayList<String> clientsName = new ArrayList<>();
        for (Client client: clients){
            clientsName.add(client.getName());
        }
        return clientsName;
    }

    public Client getClientById(long id_client) {
        return clientDAO.getClientById(id_client);
    }

    public Client getClientByName(String name, Administrator administrator) throws ClientNotRegistered {
        Client client = clientDAO.getClientByName(name, administrator);
        if (client == null){
            throw new Exceptions.ClientNotRegistered();
        } else {
            return client;
        }
    }

}
