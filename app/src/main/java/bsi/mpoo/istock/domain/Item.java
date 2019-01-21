package bsi.mpoo.istock.domain;

import java.math.BigDecimal;

public class Item {
    private long id;
    private Product product;
    private BigDecimal price;
    private long quantity;
    private long idOrder;
    private long idAdministrator;
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(long id_order) {
        this.idOrder = id_order;
    }

    public long getIdAdministrator() {
        return idAdministrator;
    }

    public void setIdAdministrator(long idAdministrator) {
        this.idAdministrator = idAdministrator;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Item){
            if (getProduct().equals(((Item) obj).getProduct()) ){
                return true;
            }
        }
        return false;
    }

    public BigDecimal getTotalPrice(){
        BigDecimal qtd = new BigDecimal(getQuantity());
        return qtd.multiply(getPrice());
    }
}
