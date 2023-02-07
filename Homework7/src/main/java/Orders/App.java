package Orders;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;
import java.util.Scanner;

public class App {
    static EntityManagerFactory emf;

    static EntityManager em;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try{
            emf = Persistence.createEntityManagerFactory("JPATest");
            em = emf.createEntityManager();

            try{
                while(true) {
                    System.out.println("1: add product");
                    System.out.println("2: add client");
                    System.out.println("3: create order");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addProduct(sc);
                            break;
                        case "2":
                            addClient(sc);
                            break;
                        case "3":
                            createOrder(sc);
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

    public static void addProduct(Scanner sc) {
        System.out.print("Enter product name: ");
        String product = sc.nextLine();
        System.out.print("Enter price: ");
        String sPrice = sc.nextLine();
        Double price = Double.parseDouble(sPrice);

        em.getTransaction().begin();
        try {
            Products p = new Products(product, price);
            em.persist(p);
            em.getTransaction().commit();

            System.out.println(p.getId());
        } catch (Exception ex) {
            em.getTransaction().rollback();
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
            Clients c = new Clients(name, email, address, phone);
            em.persist(c);
            em.getTransaction().commit();

            System.out.println(c.getId());
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    public static void createOrder(Scanner sc) {
        // show all products
        int countProducts = showProducts();

        // Show all clients
        int countClients = showClients();

        System.out.print("Enter client id: ");
        String sId = sc.nextLine();
        long clientId = 0;
        try{
            clientId = Long.parseLong(sId);
            if (clientId == 0 || clientId > countClients) throw new Exception();
        } catch (Exception ex) {
            System.out.println("No client with this id");
            return;
        }
        // get client info by id
        Clients c = em.getReference(Clients.class, clientId);

        // create order in database
        long orderId = 0;
        em.getTransaction().begin();
        try {
            Orders order = new Orders(c.getName(), c.getEmail(), c.getAddress(), c.getPhone());
            System.out.println(order);
            order.setClient(c); // add client_id

            em.persist(order);
            em.getTransaction().commit();

            orderId = order.getId();
            System.out.println("orderId = " + orderId);
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return;
        }
        //System.out.println(orderId);
        Orders order = em.getReference(Orders.class, orderId);

        // add products to order
        Boolean check = true;
        while (check) {
            System.out.print("Enter product id ('0' for end): ");
            String sProdId = sc.nextLine();
            try{
                long prodId = Long.parseLong(sProdId);
                if (prodId == 0) check = false;
                else if(prodId > countProducts) throw new Exception();
                else {
                    em.getTransaction().begin();
                    try {
                        Products product = em.getReference(Products.class, prodId);
                        System.out.println(product);
                        order.addProduct(product);
                        em.persist(order);
                        em.getTransaction().commit();
                        System.out.println("Product added.");
                    } catch (Exception ex) {
                        em.getTransaction().rollback();
                    }
                }
            } catch (Exception ex) {
                System.out.println("No this product");
            }
        }
    }

    public static int showProducts() {
        Query query = em.createQuery("SELECT p FROM Products p", Products.class);
        List<Products> list = (List<Products>) query.getResultList();

        System.out.println("---------Products----------");
        for (Products p: list) {
            System.out.println(p.getId() + ". " + p.getProductName() + " - " + p.getPrice());
        }
        System.out.println("Count of products = " + list.size());
        System.out.println("--------------------------");
        return list.size();
    }

    public static int showClients() {
        Query query = em.createQuery("SELECT c FROM Clients c", Clients.class);
        List<Clients> list = (List<Clients>) query.getResultList();

        System.out.println("---------Clients----------");
        for (Clients c: list) {
            System.out.println(c);
        }
        System.out.println("Count of clients = " + list.size());
        System.out.println("--------------------------");
        return list.size();
    }
}
