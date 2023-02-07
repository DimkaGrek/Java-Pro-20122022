package Orders;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String status;

    private String name;
    private String email;
    private String address;
    private String phone;

    public Orders(){}

    public Orders(String name, String email, String address, String phone) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.status = "new";
    }

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Clients client;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "order_product",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Products> products = new HashSet<>();

    public void addProduct(Products product){
        products.add(product);
        product.getOrders().add(this);
    }

    public Clients getClient() {
        return client;
    }

    public void setClient(Clients client) {
        this.client = client;
    }

    public Set<Products> getProducts() {
        return products;
    }

    public void setProducts(Set<Products> products) {
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}