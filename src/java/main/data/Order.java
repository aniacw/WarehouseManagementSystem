package main.data;

import main.ConverterEnum;
import main.OrderStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_no", unique = true, nullable = false)
    private Integer orderNumber;

    //@Enumerated(EnumType.STRING)
    @Convert(converter = ConverterEnum.class)
    @Column(name = "order_status", nullable = false)
    private OrderStatus status;

    @Column(name = "supplier_id")
    private Integer supplierId;

    @Column(name = "total_order_value", nullable = false)
    private Double totalOrderValue;

    @Column(name = "order_date", nullable = false)
    private Date date;

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
}
