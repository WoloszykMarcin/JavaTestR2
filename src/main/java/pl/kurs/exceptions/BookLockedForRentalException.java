package pl.kurs.exceptions;

public class BookLockedForRentalException extends Exception {
    public BookLockedForRentalException(String message) {
        super(message);
    }
}
