
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ruby
 */
public class BookDAO {
    
    private ArrayList<Book> books;
    
    public BookDAO() {
        books = new ArrayList<>();
    }
    
    public int find(Book book) {
        return books.indexOf(book);
    }
    
    public Book get(int index) {
        return books.get(index);
    }
    
    public boolean add(Book book) {
        return books.add(book);
    }
    
    public boolean update(Book book) {
        return books.set(this.find(book), book) != null;
    }
    
    public boolean remove(String code) {
        return books.remove(this.find(new Book(code))) != null;
    }
    
}
