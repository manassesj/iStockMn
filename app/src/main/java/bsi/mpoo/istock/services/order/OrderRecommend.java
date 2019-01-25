package bsi.mpoo.istock.services.order;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import bsi.mpoo.istock.domain.Item;
import bsi.mpoo.istock.domain.Order;
import bsi.mpoo.istock.domain.Product;
import bsi.mpoo.istock.domain.Session;
import bsi.mpoo.istock.services.product.ProductServices;

public class OrderRecommend {

    private ProductServices productServices;
    private Context context;
    private OrderServices orderServices;

    public OrderRecommend(Context context){
        this.context = context;
        this.orderServices = new OrderServices(context);
        this.productServices = new ProductServices(context);
    }

    public  ArrayList<Product> productForRecom(ArrayList<Long> itemInCar,ArrayList<Long> realIntemInCar){
        ArrayList<ArrayList> listOfProcutOrders  = new ArrayList<>();
        ArrayList<Order>  listOrders;
        listOrders = orderServices.getOrders(Session.getInstance().getAdministrator());
        for(Order order: listOrders){
            ArrayList<Long> itemsOrder;
            itemsOrder = listOfIdProducts(order.getItems());
            listOfProcutOrders.add(itemsOrder);
        }
        ArrayList<Long> idProducts = new ArrayList<>();
        for (ArrayList<ArrayList> idsProducts : listOfProcutOrders) {
            ArrayList<Long> itemCast = (ArrayList) idsProducts;
            Collections.sort(realIntemInCar);
            Collections.sort(itemCast);
            if (itemCast.containsAll(itemInCar) && realIntemInCar != itemCast && itemCast.size() > itemInCar.size()) {
                for (Long item : itemCast) {
                    if (!realIntemInCar.contains(item)) {
                        idProducts.add(item); }
                        }
                        break;
                    }
                }
        ArrayList<Product> listProducts = new ArrayList<>();
            if (itemInCar.size() > 0 && idProducts.size() > 0) {
                for (Long idItem : idProducts) {
                    listProducts.add(productServices.getProductById(idItem));
                }
            }
        return listProducts;
    }

    public ArrayList<Long> listOfIdProducts(ArrayList<Item> items){
        ArrayList<Long> listtId = new ArrayList<>();
        for(Item item: items){
            Product product = productServices.getProductByName(item.getProduct().getName(),Session.getInstance().getAdministrator());
            listtId.add(product.getId());
        }
        return  listtId;
    }

    public ArrayList<String> subLists(String result, String string,ArrayList<String> results){
        if(string.equals("")){
            results.add(result);
        }else{
            subLists(result + string.charAt(0),string.substring(1),results);
            subLists(result,string.substring(1),results);
        };
        return results;
    }

    public String arrayToString(ArrayList<Long> itemInCar){
        String stringItems = "";
        for(Long item: itemInCar){
            stringItems = stringItems + item.toString();
        }
        return stringItems;
    }

    public  ArrayList<Long> stringToArray(String string){
        int index = 0;
        ArrayList<Long> idLongItems = new ArrayList<>();
        while(index < string.length()){
            char caracter = string.charAt(index);
            long id = (long)(caracter - '0');
            index  = index + 1;
            idLongItems.add(id);
        }
        return idLongItems;
    }


}
