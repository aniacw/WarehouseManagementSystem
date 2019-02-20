package main.data;

import main.ConverterEnum;
import main.OrderStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_no", unique = true, nullable = false)
    private Integer orderNumber;

    @Convert(converter = ConverterEnum.class)
    @Column(name = "order_status", nullable = false)
    private OrderStatus status;

    @Column(name = "supplier_id")
    private Integer supplierId;

    @Column(name = "total_order_value", nullable = false)
    private Double totalOrderValue;

    @Column(name = "order_date", nullable = false)
    private Date date;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderedItems> orderedItems;

    private Supplier supplier;


    static DateFormat string = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);

    public Order() {
    }

    public Order(Integer orderNumber, OrderStatus status, Double totalOrderValue, Integer supplierId, Date date) {
        this.orderNumber = orderNumber;
        this.status = status;
        this.supplierId = supplierId;
        this.totalOrderValue = totalOrderValue;
        this.date = date;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Double getTotalOrderValue() {
        return totalOrderValue;
    }

    public void setTotalOrderValue(Double totalOrderValue) {
        this.totalOrderValue = totalOrderValue;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public List<OrderedItems> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<OrderedItems> orderedItems) {
        this.orderedItems = orderedItems;
    }

}
