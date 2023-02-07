package Bank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rates")
public class Rates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Currencies currency;
    private Double rate; // rate of currency to UAH

    @OneToMany(mappedBy = "rate", cascade = CascadeType.ALL)
    private List<Accounts> accounts = new ArrayList<>();

    public Rates(){}

    public Rates(Currencies currency, Double rate){
        this.currency = currency;
        this.rate = rate;
    }

    public void addAccount(Accounts account) {
        if (! accounts.contains(account)) {
            accounts.add(account);
            account.setRate(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Currencies getCurrency() {
        return currency;
    }

    public void setCurrency(Currencies currency) {
        this.currency = currency;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public List<Accounts> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Accounts> accounts) {
        this.accounts = accounts;
    }
}