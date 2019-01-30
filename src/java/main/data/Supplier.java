package main.data;


import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "suppliers")
public class Supplier implements Serializable {

    @Column(name = "supplier_name", nullable = false)
    private String name;

    @Id
    @Column(name = "supplier_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private Integer phoneNumber;

    public Supplier() {
    }

    public Supplier(String name, Integer id, String email, Integer phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    @OneToMany(mappedBy = "supplier",
            cascade = {CascadeType.ALL},
            fetch = FetchType.EAGER)
    private List<Product> products;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return name;
//        return "Supplier{" +
//                "name='" + name + '\'' +
//                ", id=" + id +
//                ", email='" + email + '\'' +
//                ", phoneNumber=" + phoneNumber +
//                ", products=" + products +
//                '}';
    }
}
