package main.data;

import javax.persistence.*;

@Entity
@Table(name = "ordered_items")
public class OrderedItems {

    @Id
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "total_item_value")
    private Double totalItemValue;

    public OrderedItems() {
    }

    public OrderedItems(Integer orderId, Integer productId, Integer quantity, Double discount, Double totalItemValue) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.discount = discount;
        this.totalItemValue = totalItemValue;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTotalItemValue() {
        return totalItemValue;
    }

    public void setTotalItemValue(Double totalItemValue) {
        this.totalItemValue = totalItemValue;
    }
}
