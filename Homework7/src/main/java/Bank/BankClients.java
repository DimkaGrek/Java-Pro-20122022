package Bank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
public class BankClients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private String email;
    private String address;
    private String phone;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Accounts> accounts = new ArrayList<>();

    public BankClients(){}

    public BankClients(String name, String email, String address, String phone) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public void addAccount(Accounts account) {
        if (! accounts.contains(account)) {
            accounts.add(account);
            account.setClient(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Accounts> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Accounts> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return "Client{ id= " + id
                + "; name= " + name
                + "; email= " + email
                + "; address= " + address
                + "; phone= " + phone
                + " }";
    }
}