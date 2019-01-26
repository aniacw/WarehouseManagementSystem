package main.data;

import javax.persistence.*;


@Entity
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "product_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnNameAnnotation(name = "id" )
    private Integer id;

    @Column(name = "product_name", nullable = false)
    @ColumnNameAnnotation(name = "name" )
    private String name;

    @Column(name = "category", nullable = false)
    @ColumnNameAnnotation(name = "category" )
    private String category;

    @Column(name = "unit_price", nullable = false, precision = 8, scale = 2)
    @ColumnNameAnnotation(name = "unitPrice" )
    private Double unitPrice;

    @Column(name = "supplier_id", nullable = true)
    @ColumnNameAnnotation(name = "supplierId" )
    private Integer supplierId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "supplier_id", insertable = true, updatable = true, nullable = true)
    //nullabel false bylo. not-null property references a null or transient value
    private Supplier supplier;

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
}
