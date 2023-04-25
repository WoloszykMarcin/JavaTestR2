package pl.kurs.dao;

import org.springframework.stereotype.Repository;
import pl.kurs.models.Book;
import pl.kurs.models.Customer;

import javax.persistence.*;
import java.util.List;

@Repository
public class CustomerDao implements ICustomerDao {

    @PersistenceUnit
    private EntityManagerFactory factory;

    @Override
    public Customer save(Customer customer) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        entityManager.persist(customer);
        tx.commit();
        entityManager.close();

        return customer;
    }

    @Override
    public Customer select(long id) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        Customer customer = entityManager.find(Customer.class, id);

        tx.commit();
        entityManager.close();

        return customer;
    }

    @Override
    public List<Customer> findAll(int pageNumber, int pageSize) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        List<Customer> listOfCustomers = entityManager.createQuery("SELECT c FROM Customer c", Customer.class)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();

        tx.commit();
        entityManager.close();

        return listOfCustomers;
    }

    @Override
    public Customer findByEmail(String email) {
        EntityManager entityManager = factory.createEntityManager();
        TypedQuery<Customer> query = entityManager.createQuery("SELECT c FROM Customer c WHERE c.email = :email", Customer.class);
        query.setParameter("email", email);

        List<Customer> customers = query.getResultList();
        entityManager.close();

        return customers.isEmpty() ? null : customers.get(0);
    }
}
