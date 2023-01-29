package flats;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.lang3.RandomStringUtils;


public class App {
    static EntityManagerFactory emf;

    static EntityManager em;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            // create connection
            emf = Persistence.createEntityManagerFactory("JPATest");
            em = emf.createEntityManager();

            try{
                while(true) {
                    System.out.println("1: add flat");
                    System.out.println("2: add random flats");
                    System.out.println("3: delete flat");
                    System.out.println("4: change flat");
                    System.out.println("5: view flats");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addFlat(sc);
                            break;
                        case "2":
                            insertRandomFlats(sc);
                            break;
                        case "3":
                            deleteFlat(sc);
                            break;
                        case "4":
                            changeFlat(sc);
                            break;
                        case "5":
                            viewFlats(sc);
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
            return;
        }
    }

    public static void addFlat(Scanner sc) {
        System.out.print("Enter region: ");
        String region = sc.nextLine();
        System.out.print("Enter address: ");
        String address = sc.nextLine();
        System.out.print("Enter area (m2): ");
        String sAria = sc.nextLine();
        int area = Integer.parseInt(sAria);
        System.out.print("Enter number of rooms: ");
        String sRooms = sc.nextLine();
        int nRooms = Integer.parseInt(sRooms);
        System.out.print("Enter price: ");
        String sPrice = sc.nextLine();
        Double price = Double.parseDouble(sPrice);

        em.getTransaction().begin();
        try {
            Flats f = new Flats(region, address, area, nRooms, price);
            em.persist(f);
            em.getTransaction().commit();

            System.out.println(f.getId());
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    public static void deleteFlat(Scanner sc) {
        System.out.print("Enter flat id: ");
        String sId = sc.nextLine();
        long id = Long.parseLong(sId);

        Flats f = em.getReference(Flats.class, id);
        if (f == null) {
            System.out.println("Flat not found!");
            return;
        }

        em.getTransaction().begin();
        try {
            em.remove(f);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    public static void changeFlat(Scanner sc) {
        System.out.print("Enter flat id: ");
        String sId = sc.nextLine();
        try {
            long id = Long.parseLong(sId);

            try {
                Flats f = em.getReference(Flats.class, id);
                System.out.println(f);
                System.out.println("Choose what would you like to change:");
                showMenu();

                Boolean checker = true;
                while(checker) {
                    System.out.print("-> ");
                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            System.out.println("Input region:");
                            String reg = sc.nextLine();
                            f.setRegion(reg);
                            break;
                        case "2":
                            System.out.println("Input address:");
                            String address = sc.nextLine();
                            f.setAddress(address);
                            break;
                        case "3":
                            System.out.println("Input area:");
                            String sArea = sc.nextLine();
                            f.setArea(Integer.parseInt(sArea));
                            break;
                        case "4":
                            System.out.println("Input number of rooms:");
                            String sRooms = sc.nextLine();
                            f.setnRooms(Integer.parseInt(sRooms));
                            break;
                        case "5":
                            System.out.println("Input price:");
                            String sPrice = sc.nextLine();
                            f.setPrice(Double.parseDouble(sPrice));
                            break;
                        default:
                            checker = false;
                    }
                }

                em.getTransaction().begin();
                try {
                    em.getTransaction().commit();
                } catch (Exception ex) {
                    em.getTransaction().rollback();
                }
            } catch (EntityNotFoundException ex) {
                System.out.println("Flat not found!");
                return;
            }
        } catch (NumberFormatException ex) {
            System.out.println("Flat not found!");
        }

    }

    public static void insertRandomFlats(Scanner sc) {
        System.out.print("Enter flats count: ");
        String sCount = sc.nextLine();
        int count = Integer.parseInt(sCount);

        em.getTransaction().begin();
        try {
            for (int i = 0; i < count; i++) {

                Flats f = new Flats(RandomStringUtils.randomAlphabetic(5),
                            RandomStringUtils.randomAlphabetic(10) +
                                        " " + Integer.parseInt(RandomStringUtils.randomNumeric(1,3)),
                            Integer.parseInt(RandomStringUtils.randomNumeric(2,3)),
                            Integer.parseInt(RandomStringUtils.randomNumeric(1)),
                            Double.parseDouble(RandomStringUtils.randomNumeric(2,3))
                        );
                em.persist(f);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    public static void showMenu() {
        System.out.println("1: region");
        System.out.println("2: address");
        System.out.println("3: area");
        System.out.println("4: number of rooms");
        System.out.println("5: price");

    }

    public static void viewFlats(Scanner sc) {
        System.out.println("Choose search parameter:");
        System.out.println("'Enter': all flats");
        showMenu();

        String s = sc.nextLine();
        switch (s) {
            case "1":
                System.out.println("Input region:");
                String reg = sc.nextLine();
                showFlats("region", reg);
                break;
            case "2":
                System.out.println("Input address:");
                String address = sc.nextLine();
                showFlats("address", address);
                break;
            case "3":
                System.out.println("Input area:");
                String area = sc.nextLine();
                showFlats("area", area);
                break;
            case "4":
                System.out.println("Input number of rooms:");
                String rooms = sc.nextLine();
                showFlats("nRooms", rooms);
                break;
            case "5":
                System.out.println("Input price:");
                String price = sc.nextLine();
                showFlats("price", price);
                break;
            default:
                showFlats();
        }
    }

    public static void showFlats(Object... args) {
        Query query;
        if (args.length != 0) {
            query = em.createQuery("SELECT f FROM Flats f WHERE CAST(f." + args[0] + " as string) LIKE " + ":value", Flats.class);
            query.setParameter("value", "%" + args[1] + "%");
            System.out.println(args[0]);
            System.out.println(args[1]);
        }
        else {query = em.createQuery("SELECT f FROM Flats f", Flats.class);}

        List<Flats> list = (List<Flats>) query.getResultList();
        System.out.println("---------Results----------");
        for (Flats f: list) {
            System.out.println(f);
        }
        System.out.println("--------------------------");
    }
}
