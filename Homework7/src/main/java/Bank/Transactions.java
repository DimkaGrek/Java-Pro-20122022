package Bank;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private LocalDateTime date; // LocalDateTime now = LocalDateTime.now();

    private Double amount;

    @Enumerated(EnumType.STRING)
    private Currencies currency;

    private String fromAccount;

    private String toAccount;

    private String description;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Accounts account;

    public Transactions(){}

    public Transactions(LocalDateTime date, Double amount, Currencies currency, String fromAccount, String toAccount, String description) {
        this.date = date;
        this.amount = amount;
        this.currency = currency;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Currencies getCurrency() {
        return currency;
    }

    public void setCurrency(Currencies currency) {
        this.currency = currency;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Accounts getAccount() {
        return account;
    }

    public void setAccount(Accounts account) {
        this.account = account;
    }
}