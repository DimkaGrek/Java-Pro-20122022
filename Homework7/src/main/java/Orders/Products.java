package Orders;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String productName;
    private Double price;

    public Products(){}

    public Products(String productName, Double price) {
        this.productName = productName;
        this.price = price;
    }

    @ManyToMany(mappedBy = "products")
    private Set<Orders> orders = new HashSet<>();

    public void addOrders(Orders order) {
        orders.add(order);
        order.getProducts().add(this);
    }

    public Set<Orders> getOrders() {
        return orders;
    }

    public void setOrders(Set<Orders> orders) {
        this.orders = orders;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}