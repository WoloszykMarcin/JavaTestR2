package pl.kurs.dao;

import org.springframework.stereotype.Repository;
import pl.kurs.exceptions.RentalNotFoundException;
import pl.kurs.models.Book;
import pl.kurs.models.Customer;
import pl.kurs.models.Rental;

import javax.persistence.*;
import java.util.List;

@Repository
public class RentalDao implements IRentalDao {

    @PersistenceUnit
    private EntityManagerFactory factory;

    @Override
    public Rental save(Rental rental) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        entityManager.persist(rental);
        tx.commit();
        entityManager.close();

        return rental;
    }

    @Override
    public Rental findById(long id) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        Rental rental = entityManager.find(Rental.class, id);

        tx.commit();
        entityManager.close();

        return rental;
    }

    @Override
    public Rental update(Rental rental) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        entityManager.merge(rental);

        tx.commit();
        entityManager.close();

        return rental;
    }

    @Override
    public List<Book> getRentedBooksByCustomerId(long customerId) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        TypedQuery<Book> query = entityManager.createQuery(
                "SELECT r.book FROM Rental r WHERE r.customer.id = :customerId",
                Book.class);
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }
}
