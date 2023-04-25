package pl.kurs.models;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "rental")
public class Rental implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private LocalDate rentalDate;

    private LocalDate returnDate;

    private boolean isReturned;


    public Rental() {
    }

    public Rental(Customer customer, Book book, LocalDate rentalDate, LocalDate returnDate) {
        this.customer = customer;
        this.book = book;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
    }

    public Rental(Customer customer, Book book, LocalDate rentalDate, LocalDate returnDate, boolean isReturned) {
        this.customer = customer;
        this.book = book;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.isReturned = isReturned;
    }

    public Rental(Long id, Customer customer, Book book, LocalDate rentalDate, LocalDate returnDate, boolean isReturned) {
        this.id = id;
        this.customer = customer;
        this.book = book;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.isReturned = isReturned;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rental)) return false;
        Rental rental = (Rental) o;
        return isReturned() == rental.isReturned() && Objects.equals(getId(), rental.getId()) && Objects.equals(getCustomer(), rental.getCustomer()) && Objects.equals(getBook(), rental.getBook()) && Objects.equals(getRentalDate(), rental.getRentalDate()) && Objects.equals(getReturnDate(), rental.getReturnDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCustomer(), getBook(), getRentalDate(), getReturnDate(), isReturned());
    }

    @Override
    public String toString() {
        return "Rental{" +
                "id=" + id +
                ", customer=" + customer +
                ", book=" + book +
                ", rentalDate=" + rentalDate +
                ", returnDate=" + returnDate +
                '}';
    }
}
