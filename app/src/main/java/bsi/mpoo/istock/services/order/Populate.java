package bsi.mpoo.istock.services.order;

import android.content.Context;

import java.math.BigDecimal;

import bsi.mpoo.istock.domain.Address;
import bsi.mpoo.istock.domain.Client;
import bsi.mpoo.istock.domain.Order;
import bsi.mpoo.istock.domain.Product;
import bsi.mpoo.istock.domain.Session;
import bsi.mpoo.istock.services.Constants;
import bsi.mpoo.istock.services.client.ClientServices;
import bsi.mpoo.istock.services.product.ProductServices;

public class Populate {

    private ProductServices productServices;
    private ClientServices clientServices;


    public Populate(Context context){
        productServices = new ProductServices(context);
        clientServices = new ClientServices(context);

    }


    public void populateProductss() throws Exception {
        Product product1 = new Product();
        product1.setName("banana");
        product1.setAdministrator(Session.getInstance().getAdministrator());
        product1.setMinimumQuantity(10);
        product1.setPrice(new BigDecimal(5));
        product1.setQuantity(100);
        product1.setStatus(Constants.Status.ACTIVE);

        productServices.registerProduct(product1,Session.getInstance().getAdministrator());

        Product product2 = new Product();
        product2.setName("macã");
        product2.setAdministrator(Session.getInstance().getAdministrator());
        product2.setMinimumQuantity(10);
        product2.setPrice(new BigDecimal(5));
        product2.setQuantity(100);
        product2.setStatus(Constants.Status.ACTIVE);

        productServices.registerProduct(product2,Session.getInstance().getAdministrator());

        Product product3 = new Product();
        product3.setName("pera");
        product3.setAdministrator(Session.getInstance().getAdministrator());
        product3.setMinimumQuantity(10);
        product3.setPrice(new BigDecimal(5));
        product3.setQuantity(100);
        product3.setStatus(Constants.Status.ACTIVE);

        productServices.registerProduct(product3,Session.getInstance().getAdministrator());

        Product product4 = new Product();
        product4.setName("melão");
        product4.setAdministrator(Session.getInstance().getAdministrator());
        product4.setMinimumQuantity(10);
        product4.setPrice(new BigDecimal(5));
        product4.setQuantity(100);
        product4.setStatus(Constants.Status.ACTIVE);

        productServices.registerProduct(product4,Session.getInstance().getAdministrator());

        Product product5 = new Product();
        product5.setName("uva");
        product5.setAdministrator(Session.getInstance().getAdministrator());
        product5.setMinimumQuantity(10);
        product5.setPrice(new BigDecimal(5));
        product5.setQuantity(100);
        product5.setStatus(Constants.Status.ACTIVE);

        productServices.registerProduct(product4,Session.getInstance().getAdministrator());
    }

    public void populateClient() throws Exception {

        Client client = new Client();
        client.setName("oscar");

        Address address = new Address();
        address.setCity("l");
        address.setDistrict("l");
        address.setNumber(00);
        address.setState("l");
        address.setStreet("l");
        address.setStatus(Constants.Status.ACTIVE);

        client.setAddress(address);
        client.setAdministrator(Session.getInstance().getAdministrator());
        client.setPhone("00000000000");
        clientServices.registerClient(client,Session.getInstance().getAdministrator());

    }

}
