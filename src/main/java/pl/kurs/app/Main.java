package pl.kurs.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import pl.kurs.exceptions.*;
import pl.kurs.models.Book;
import pl.kurs.models.Customer;
import pl.kurs.models.Rental;
import pl.kurs.services.IBookService;
import pl.kurs.services.ICustomerService;
import pl.kurs.services.IRentalService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@ComponentScan(basePackages = "pl.kurs")
public class Main {
    public static void main(String[] args) throws BookLockedForRentalException, RentalNotFoundException, BookNotAvailableException, BookAlreadyExistsException, CustomerWithGivenMailAlreadyExistsException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Main.class);
        ICustomerService customerService = ctx.getBean(ICustomerService.class);
        IBookService bookService = ctx.getBean(IBookService.class);
        IRentalService rentalService = ctx.getBean(IRentalService.class);

//        Customer jan = new Customer("Jan", "Kowalski", "jankowalski@onet.pl");
//
//        Customer savedCustomer = customerService.saveCustomer(jan);
//
//        List<Customer> customerList = customerService.getAllCustomers(0, 10);
//        System.out.println(formatList(customerList));
//
//        Book lsnienie = new Book("Lśnienie", "Steven King", 2009, "Gal Anonim", "Powieść horror", true, "244", false);
//        Book savedBook = bookService.saveBook(lsnienie);
//
//        List<Book> allBooks = bookService.getAllBooks(0, 10);
//        System.out.println(formatList(allBooks));
//
//        Rental rental = new Rental(savedCustomer, savedBook, LocalDate.of(2023, 4, 23), LocalDate.of(2023, 05, 23));
//
//        rentalService.save(rental);
//
//        rentalService.returnBook(1);
//
//        List<Book> rentedBooksByCustomerId = rentalService.getRentedBooksByCustomerId(1);
//        System.out.println(formatList(rentedBooksByCustomerId));

        //-----> console program <---------------
        /*
         * 0. Database -> Jump to Query console --> CREATE SCHEMA library;
         * 1. Terminal -> mvn clean package
         * 2. cmd/bash (/target) -> java -jar JavaTestR2-1.0-SNAPSHOT-jar-with-dependencies.jar
         * */
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the library!");
        printOptions();
        int option = -1;

        do {
            try {
                switch ((option = scanner.nextInt())) {
                    case 1:
                        System.out.println("Register new Customer:");
                        System.out.println("Enter first name:");
                        scanner.nextLine();
                        String firstName = scanner.nextLine();

                        System.out.println("Enter last name:");
                        String lastName = scanner.nextLine();

                        System.out.println("Enter email address:");
                        String email = scanner.nextLine();

                        Customer newCustomer = new Customer(firstName, lastName, email);

                        customerService.saveCustomer(newCustomer);
                        System.out.println("Customer added successfully! id: " + newCustomer.getId());
                        printOptions();
                        break;
                    case 2:
                        System.out.println("Enter book title:");
                        scanner.nextLine();
                        String title = scanner.nextLine();

                        System.out.println("Enter author:");
                        String author = scanner.nextLine();

                        System.out.println("Enter publication year:");
                        int year = Integer.parseInt(scanner.nextLine());

                        System.out.println("Enter publisher:");
                        String publisher = scanner.nextLine();

                        System.out.println("Enter genre:");
                        String genre = scanner.nextLine();

                        System.out.println("Enter refNumber:");
                        String refNum = scanner.nextLine();

                        Book newBook = new Book(title, author, year, publisher, genre, true, refNum, false);

                        bookService.saveBook(newBook);

                        System.out.println("New book added: " + newBook + " id: " + newBook.getId());
                        printOptions();
                        break;
                    case 3:
                        System.out.println("Enter customer ID:");
                        int customerId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter book ID:");
                        int bookId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter rental end date (YYYY-MM-DD):");
                        LocalDate rentalEndDate = LocalDate.parse(scanner.nextLine());
                        Customer customer = customerService.select(customerId);
                        Book bookForRental = bookService.findById(bookId);

                        if (customer == null) {
                            System.out.println("Customer not found.");
                            break;
                        }

                        if (bookForRental == null) {
                            System.out.println("Book not found.");
                            break;
                        }

                        Rental newRental = new Rental(customer, bookForRental, LocalDate.now(), rentalEndDate);
                        rentalService.save(newRental);
                        System.out.println("Rental added successfully. id: " + newRental.getId());

                        printOptions();
                        break;
                    case 4:
                        System.out.println("Give pageNumber: ");
                        int pageNumber = scanner.nextInt();
                        System.out.println("Give pageSize: ");
                        int pageSize = scanner.nextInt();
                        List<Book> allBooks = bookService.getAllBooks(pageNumber, pageSize);
                        System.out.println(formatList(allBooks));
                        printOptions();
                        break;
                    case 5:
                        System.out.println("Give pageNumber: ");
                        int pageNumber2 = scanner.nextInt();
                        System.out.println("Give pageSize: ");
                        int pageSize2 = scanner.nextInt();
                        List<Customer> allCustomers = customerService.getAllCustomers(pageNumber2, pageSize2);
                        System.out.println(formatList(allCustomers));
                        printOptions();
                        break;
                    case 6:
                        System.out.println("Enter customer ID:");
                        int customerId2 = scanner.nextInt();
                        scanner.nextLine();
                        List<Book> rentedBooksByCustomerId = rentalService.getRentedBooksByCustomerId(customerId2);
                        System.out.println(formatList(rentedBooksByCustomerId));
                        printOptions();
                        break;
                    case 7:
                        System.out.println("Enter book ID to lock for rental:");
                        int bookIdToLock = scanner.nextInt();
                        scanner.nextLine();
                        Book bookToLock = bookService.findById(bookIdToLock);
                        bookService.blockForRental(bookToLock);
                        System.out.println("Book with ID " + bookIdToLock + " has been locked for rental.");
                        break;
                    case 8:
                        System.out.println("Enter rental ID to return:");
                        int rentalId = scanner.nextInt();
                        scanner.nextLine();

                        rentalService.returnBook(rentalId);
                        System.out.println("Book has been returned.");
                        printOptions();
                        break;
                    case 0:
                        System.out.println("See you later!");
                        break;
                    default:
                        System.err.println("Chosen option not recognized!");
                        printOptions();
                }
            } catch (BookAlreadyExistsException | BookLockedForRentalException | BookNotAvailableException |
                     NumberFormatException | NullPointerException | RentalNotFoundException |
                     CustomerWithGivenMailAlreadyExistsException e) {
                handleException(e);
            } catch (InputMismatchException | DateTimeParseException e) {
                handleException(e);
                scanner.nextLine();
            }
        } while (option != 0);
        scanner.close();
    }

    public static <T> String formatList(List<T> list) {
        String listString = list.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
        return listString;
    }

    private static void printOptions() {
        System.out.println("Choose option:");
        System.out.println("1 - register new Customer");
        System.out.println("2 - add new Book");
        System.out.println("3 - rent a book to the customer");
        System.out.println("4 - search for all books with pagination");
        System.out.println("5 - search for all customers with pagination");
        System.out.println("6 - search for all books rented by the customer");
        System.out.println("7 - lock the book for rental");
        System.out.println("8 - return the book");
        System.out.println("0 - exit");
    }

    private static void handleException(Throwable e) {
        if (e.getMessage() != null) System.err.println(e.getMessage());
        else System.err.println("Input error!");
        printOptions();
    }
}