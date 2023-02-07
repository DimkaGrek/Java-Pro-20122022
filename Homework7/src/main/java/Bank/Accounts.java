package Bank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Accounts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String number; // account number

    @Enumerated(EnumType.STRING)
    private Currencies currency; // currency of account

    private Double amount; // amount of account

    @ManyToOne
    @JoinColumn(name = "client_id")
    private BankClients client;

    @ManyToOne
    @JoinColumn(name = "rate_id")
    private Rates rate;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transactions> transactions = new ArrayList<>();

    public Accounts(){}

    public Accounts(String number, Currencies currency, Double amount) {
        this.number = number;
        this.currency = currency;
        this.amount = amount;
    }

    public void addTransaction(Transactions transaction) {
        if (! transactions.contains(transaction)) {
            transactions.add(transaction);
            transaction.setAccount(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Currencies getCurrency() {
        return currency;
    }

    public void setCurrency(Currencies currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public BankClients getClient() {
        return client;
    }

    public void setClient(BankClients client) {
        this.client = client;
    }

    public Rates getRate() {
        return rate;
    }

    public void setRate(Rates rate) {
        this.rate = rate;
    }

    public List<Transactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transactions> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "Account{ id= " + id
                + "; number= " + number
                + "; currency= " + currency
                + "; amount= " + String.format("%.2f",amount)
                + "; client= " + client.getName()
                + " }";
    }
}