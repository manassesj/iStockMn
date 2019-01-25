package bsi.mpoo.istock.services.order;

import android.content.Context;
import android.icu.util.LocaleData;
import android.widget.Toast;

import org.json.JSONException;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import bsi.mpoo.istock.data.order.OrderDAO;
import bsi.mpoo.istock.domain.Administrator;
import bsi.mpoo.istock.domain.Order;
import bsi.mpoo.istock.services.Exceptions;

public class OrderServices {

    private OrderDAO orderDAO;

    public OrderServices(Context context){
        this.orderDAO = new OrderDAO(context);
    }

    public boolean isOrderRegistered(Order order) throws JSONException {
        Order searchedOrder = orderDAO.getOrderById(order.getId());
        return searchedOrder != null;
    }

    public void registerOrder(Order order) throws Exception {
            orderDAO.insertOrder(order);
    }

    public void updateOrder(Order order) throws Exception{
        try {
            orderDAO.updateOrder(order);
        } catch (Exception error){
            throw new Exceptions.OrderNotRegistered();
        }
    }

    public void disableOrder(Order order) throws Exception{

        if (isOrderRegistered(order)){
            orderDAO.disableOrder(order);
        } else {
            throw  new Exceptions.OrderNotRegistered();
        }
    }

    public ArrayList<Order> getAcitiveOrders(Administrator administrator) {
        return (ArrayList<Order>) orderDAO.getActiveOrdersByAdm (administrator);
    }

    public ArrayList<Order> getOrders(Administrator administrator) {
        return (ArrayList<Order>) orderDAO.getOrdersByAdm (administrator);
    }

    public ArrayList<Integer> dateForMonth(Administrator administrator){
        ArrayList<Integer> months = new ArrayList<>();
        months.add(0);
        months.add(0);
        months.add(0);
        months.add(0);
        months.add(0);
        months.add(0);
        months.add(0);
        months.add(0);
        months.add(0);
        months.add(0);
        months.add(0);
        months.add(0);
        ArrayList<Order> orders = getOrders(administrator);

        for(Order order:orders){
            Month localeData = order.getDateCreation().getMonth();
            int total = (order.getTotal()).intValue();
            if (localeData == Month.JANUARY){
                months.add(0,months.get(0)+ total);
            }else if (localeData == Month.FEBRUARY){
                months.add(1,months.get(1)+total);
            }else if (localeData == Month.MARCH) {
                months.add(2, months.get(2)+total);
            }else if (localeData == Month.APRIL) {
                months.add(3, months.get(3)+total);
            }else if (localeData == Month.MAY){
                months.add(4,months.get(4)+total);
            }else if (localeData == Month.JUNE) {
                months.add(5, months.get(5)+total);
            }else if (localeData == Month.JULY) {
                months.add(6, months.get(6)+total);
            }else if (localeData == Month.AUGUST) {
                months.add(7, months.get(7)+total);
            }else if (localeData == Month.SEPTEMBER) {
                months.add(8, months.get(8)+total);
            }else if (localeData == Month.OCTOBER){
                months.add(9,months.get(9)+total);
            }else if (localeData == Month.NOVEMBER) {
                months.add(10,months.get(10)+ total);
            }else if (localeData == Month.DECEMBER){
                months.add(11,months.get(11)+total);
            }
        }
        return months;
    }
}
