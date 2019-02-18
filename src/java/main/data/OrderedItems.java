package main.data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "ordered_items")
public class OrderedItems implements Serializable {


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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_order_id", unique = true, nullable = false)
    private Integer productOrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false, nullable = false)
    private Order order;


    @OneToOne(fetch = FetchType.LAZY)//zmienilam z manytoone
    @JoinColumn(name = "product_id", insertable = false, updatable = false, nullable = true)
    private Product product;


    public OrderedItems() {
    }

    public OrderedItems(Integer orderId, Integer productId, Integer quantity, Double discount, Double totalItemValue, Integer productOrderId) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.discount = discount;
        this.totalItemValue = totalItemValue;
        this.productOrderId = productOrderId;
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

    public Integer getProductOrderId() {
        return productOrderId;
    }

    public void setProductOrderId(Integer productOrderId) {
        this.productOrderId = productOrderId;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
