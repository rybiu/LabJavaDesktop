
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ruby
 */
public class Stock {
    
    private int id;
    private String name;
    private String address;
    private LocalDate dateAvailable;
    private String note;

    public Stock(int id, String name, String address, LocalDate dateAvailable, String note) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.dateAvailable = dateAvailable;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateAvailable() {
        return dateAvailable;
    }

    public void setDateAvailable(LocalDate dateAvailable) {
        this.dateAvailable = dateAvailable;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
    public Vector toVector() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Vector v = new Vector();
        v.add(id);
        v.add(name);
        v.add(address);
        v.add(dateAvailable.format(formatter));
        v.add(note);
        return v;
    }

}
