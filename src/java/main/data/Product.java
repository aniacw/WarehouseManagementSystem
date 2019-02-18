package main.data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "products")
public class Product implements Serializable {

    @Id
    @Column(name = "product_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_name", nullable = false)
    private String name;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "unit_price", nullable = false, precision = 8, scale = 2)
    private Double unitPrice;

    @Column(name = "supplier_id", nullable = true)
    private Integer supplierId;

    @ManyToOne(fetch = FetchType.EAGER) //Exception in thread "JavaFX Application Thread" org.hibernate.LazyInitializationException: could not initialize proxy [main.data.Supplier#2] - no Session
    @JoinColumn(name = "supplier_id", insertable = false, updatable = false, nullable = true)
    private Supplier supplier;

    @Column(name = "quantity_on_stock")
    private Integer quantityOnStock;

    @OneToOne(fetch = FetchType.LAZY)//nie bylo
    private OrderedItems orderedItems;

    public Product() {
    }

    public Product(Integer id, String name, String category, double unitPrice, Integer supplierId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
        this.supplierId = supplierId;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantityOnStock() {
        return quantityOnStock;
    }

    public void setQuantityOnStock(Integer quantityOnStock) {
        this.quantityOnStock = quantityOnStock;
    }

    public OrderedItems getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(OrderedItems orderedItems) {
        this.orderedItems = orderedItems;
    }
}
