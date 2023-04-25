package pl.kurs.models;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "book")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    private Integer year;

    private String publisher;

    private String category;

    private boolean isAvailable;

    private String refNum;

    @Column(name = "isLockedForRental")
    private boolean isLockedForRental = false;

    public Book() {
    }

    public Book(String title, String author, Integer year, String publisher, String category, boolean isAvailable, String refNum, boolean isLockedForRental) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.publisher = publisher;
        this.category = category;
        this.isAvailable = isAvailable;
        this.refNum = refNum;
        this.isLockedForRental = isLockedForRental;
    }

    public Book(Long id, String title, String author, Integer year, String publisher, String category, boolean isAvailable, String refNum, boolean isLockedForRental) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.publisher = publisher;
        this.category = category;
        this.isAvailable = isAvailable;
        this.refNum = refNum;
        this.isLockedForRental = isLockedForRental;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isLockedForRental() {
        return isLockedForRental;
    }

    public void setLockedForRental(boolean lockedForRental) {
        isLockedForRental = lockedForRental;
    }

    public String getRefNum() {
        return refNum;
    }

    public void setRefNum(String refNum) {
        this.refNum = refNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return isAvailable() == book.isAvailable() && isLockedForRental() == book.isLockedForRental() && Objects.equals(getId(), book.getId()) && Objects.equals(getTitle(), book.getTitle()) && Objects.equals(getAuthor(), book.getAuthor()) && Objects.equals(getYear(), book.getYear()) && Objects.equals(getPublisher(), book.getPublisher()) && Objects.equals(getCategory(), book.getCategory()) && Objects.equals(getRefNum(), book.getRefNum());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getAuthor(), getYear(), getPublisher(), getCategory(), isAvailable(), getRefNum(), isLockedForRental());
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                ", publisher='" + publisher + '\'' +
                ", category='" + category + '\'' +
                ", isAvailable=" + isAvailable +
                ", refNum='" + refNum + '\'' +
                ", isLockedForRental=" + isLockedForRental +
                '}';
    }
}
