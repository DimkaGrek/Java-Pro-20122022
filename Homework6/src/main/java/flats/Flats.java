package flats;

import javax.persistence.*;

@Entity
@Table(name="Flats")
public class Flats {
    @Id
    @GeneratedValue
    private long id;

    private String region;

    private String address;

    private int area;

    private int nRooms;

    private Double price;

    public long getId() {
        return id;
    }

    public Flats() {}

    public Flats(String region, String address, int area, int nRooms, Double price) {
        this.region = region;
        this.address = address;
        this.area = area;
        this.nRooms = nRooms;
        this.price = price;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getnRooms() {
        return nRooms;
    }

    public void setnRooms(int nRooms) {
        this.nRooms = nRooms;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Flat{ id= " + id
                + "; region= " + region
                + "; address= " + address
                + "; area (m2)= " + area
                + "; number of rooms= "+ nRooms
                + "; price= " + price
                + " }";
    }
}
