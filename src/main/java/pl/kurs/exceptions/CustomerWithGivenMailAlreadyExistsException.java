package pl.kurs.exceptions;

public class CustomerWithGivenMailAlreadyExistsException extends Exception {
    public CustomerWithGivenMailAlreadyExistsException(String message) {
        super(message);
    }
}
