package Bank;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class App {
    static EntityManagerFactory emf;
    static EntityManager em;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try{
            emf = Persistence.createEntityManagerFactory("JPABank");
            em = emf.createEntityManager();

            try{
                while(true) {
                    System.out.println("1: add client");
                    System.out.println("2: add bank account");
                    System.out.println("3: add rate");
                    System.out.println("4: top up bank account");
                    System.out.println("5: transfer from one account to another");
                    System.out.println("6: show client's total amount");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addClient(sc);
                            break;
                        case "2":
                            addBankAccount(sc);
                            break;
                        case "3":
                            addRate(sc);
                            break;
                        case "4":
                            topUpAccount(sc);
                            break;
                        case "5":
                            transferFromToAccount(sc);
                            break;
                        case "6":
                            showTotalAmount(sc);
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                em.close();
                emf.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void addClient(Scanner sc) {
        System.out.print("Enter client's name: ");
        String name = sc.nextLine();
        System.out.print("Enter client's email: ");
        String email = sc.nextLine();
        System.out.print("Enter client's address: ");
        String address = sc.nextLine();
        System.out.print("Enter client's phone: ");
        String phone = sc.nextLine();

        em.getTransaction().begin();
        try {
            BankClients c = new BankClients(name, email, address, phone);
            em.persist(c);
            em.getTransaction().commit();

            System.out.println(c.getId());
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    public static void addBankAccount(Scanner sc) {
        int countClients = showClients();
        System.out.print("Enter client id: ");
        String sId = sc.nextLine();
        try{
            Long clientId = Long.parseLong(sId);
            if (clientId == 0 || clientId > countClients) throw new Exception();
            else {
                System.out.print("Enter account number: ");
                String number = sc.nextLine();
                System.out.print("Enter currency of account(USD, EUR, UAH): ");
                String sCurrency = sc.nextLine();
                Currencies currency = Currencies.valueOf(sCurrency);
                System.out.print("Enter start amount: ");
                String sAmount = sc.nextLine();
                Double amount = Double.parseDouble(sAmount);

                em.getTransaction().begin();
                try {
                    Accounts a = new Accounts(number, currency, amount);
                    a.setClient(em.getReference(BankClients.class, clientId));

                    // get rate for this account
                    Query query = em.createQuery("SELECT r FROM Rates r WHERE currency = :value");
                    query.setParameter("value", currency);
                    Rates r = (Rates) query.getSingleResult();
                    System.out.println("rates = " + r);
                    a.setRate(r);

                    em.persist(a);
                    em.getTransaction().commit();

                    System.out.println("Bank account with id = " + a.getId() + " created.");
                } catch (Exception ex) {
                    em.getTransaction().rollback();
                    System.out.println("Bank account is not created. " + ex.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println("No client with this id");
            return;
        }
    }

    public static int showClients() {
        Query query = em.createQuery("SELECT c FROM BankClients c", BankClients.class);
        List<BankClients> list = (List<BankClients>) query.getResultList();

        System.out.println("---------Clients----------");
        for (BankClients c: list) {
            System.out.println(c);
        }
        System.out.println("Count of clients = " + list.size());
        System.out.println("--------------------------");
        return list.size();
    }

    public static void addRate(Scanner sc) {
        System.out.print("Enter currency (USD, EUR, UAH): ");
        String sCurrency = sc.nextLine();
        Currencies currency = Currencies.valueOf(sCurrency);
        System.out.print("Enter rate: ");
        String sRate = sc.nextLine();
        Double rate = Double.parseDouble(sRate);

        em.getTransaction().begin();
        try {
            Rates r = new Rates(currency, rate);
            em.persist(r);
            em.getTransaction().commit();

            System.out.println(r.getId());
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    public static void topUpAccount(Scanner sc) {
        int countClients = showClients();
        System.out.print("Enter client id: ");
        String sId = sc.nextLine();
        try{
            Long clientId = Long.parseLong(sId);
            if (clientId == 0 || clientId > countClients) throw new Exception();
            else {
                BankClients client = em.getReference(BankClients.class, clientId);
                System.out.println(client);
                System.out.println("------Accounts------");
                List<Accounts> accounts = client.getAccounts();
                for (Accounts account :  accounts) {
                   System.out.println(account);
                }
                System.out.println("------------------");

                System.out.println("Select account id, which you want to top up: ");
                sId = sc.nextLine();
                Long accountId = Long.parseLong(sId);
                System.out.println("Select amount, which you want to top up: ");
                String sAmount = sc.nextLine();
                Double amount = Double.parseDouble(sAmount);
                Accounts account = em.getReference(Accounts.class, accountId);
                em.getTransaction().begin();
                try {
                    Transactions t = new Transactions(LocalDateTime.now(), amount, account.getCurrency(),
                            "Cash", account.getNumber(), "Cash replenishment"); // add transaction
                    t.setAccount(account);
                    em.persist(t);
                    account.setAmount(account.getAmount() + amount); // add amount to account's amount
                    em.persist(account);
                    em.getTransaction().commit();
                    System.out.println("account has been topped up.");
                } catch (Exception ex) {
                    em.getTransaction().rollback();
                }
            }
        } catch (Exception ex) {
            System.out.println("No client with this id");
            return;
        }
    }

    public static void transferFromToAccount(Scanner sc) {
        int countAccounts = showAccounts();
        System.out.print("Enter FROM account id: ");
        String sFromId = sc.nextLine();
        System.out.print("Enter TO account id: ");
        String sToId = sc.nextLine();
        try{
            Long fromId = Long.parseLong(sFromId);
            if (fromId < 1 || fromId > countAccounts) throw new Exception("No account with this id");
            Long toId = Long.parseLong(sToId);
            if (toId < 1 || toId > countAccounts) throw new Exception("No account with this id");

            Accounts fromAccount = em.getReference(Accounts.class, fromId);
            Accounts toAccount = em.getReference(Accounts.class, toId);

            System.out.print("Enter amount: ");
            String sAmount = sc.nextLine();
            Double amount = Double.parseDouble(sAmount);
            em.getTransaction().begin();
            try {
                Currencies currFrom = fromAccount.getCurrency();
                Currencies currTo = toAccount.getCurrency();
                // add transaction for account FROM
                Transactions tFrom = new Transactions(LocalDateTime.now(), -amount, currFrom, fromAccount.getNumber(),
                        toAccount.getNumber(), "Transfer from one account to another");
                tFrom.setAccount(fromAccount);
                fromAccount.setAmount(fromAccount.getAmount() - amount);
                em.persist(tFrom);

                if (!currFrom.equals(currTo)) {
                    amount = amount * fromAccount.getRate().getRate() / toAccount.getRate().getRate();
                }
                System.out.println(amount);
                // add transaction for account TO
                Transactions tTo = new Transactions(LocalDateTime.now(), amount, currTo, fromAccount.getNumber(),
                        toAccount.getNumber(), "Transfer from one account to another");
                tTo.setAccount(toAccount);
                toAccount.setAmount(toAccount.getAmount() + amount);
                em.persist(tTo);

                em.getTransaction().commit();
                System.out.println("Transfer is successful.");
            } catch (Exception ex) {
                em.getTransaction().rollback();
                System.out.println("Transfer is NOT successful!!!");
            }

        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return;
        }
    }

    public static int showAccounts(){
        Query query = em.createQuery("SELECT a FROM Accounts a", Accounts.class);
        List<Accounts> list = (List<Accounts>) query.getResultList();

        System.out.println("---------Accounts----------");
        for (Accounts a: list) {
            System.out.println(a);
        }
        System.out.println("Count of accounts = " + list.size());
        System.out.println("--------------------------");
        return list.size();
    }

    public static void showTotalAmount(Scanner sc) {
        int countClients = showClients();
        System.out.print("Enter client id: ");
        String sId = sc.nextLine();
        try{
            Long clientId = Long.parseLong(sId);
            if (clientId == 0 || clientId > countClients) throw new Exception();
            else {
                BankClients client = em.getReference(BankClients.class, clientId);
                System.out.println(client);
                System.out.println("------Accounts------");
                List<Accounts> accounts = client.getAccounts();
                Double totalAmount = 0.0;
                for (Accounts account :  accounts) {
                    System.out.println(account + " rate = " + account.getRate().getRate());
                    totalAmount += account.getAmount() * account.getRate().getRate();
                }
                System.out.println("---- Total amount: " + String.format("%.2f", totalAmount) + " UAH -----");

            }
        } catch (Exception ex) {
            System.out.println("No client with this id");
            return;
        }
    }
}
