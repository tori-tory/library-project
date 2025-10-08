package service;

import exception.BookLoanToUserException;
import exception.BookNotAvailableCopiesException;
import exception.BookNotFoundException;
import exception.LoanExceedException;
import exception.LoanNotFoundException;
import exception.UserAlreadyExistsException;
import exception.UserNotFoundException;
import model.book.Book;
import model.user.User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class LibraryTest {

    @BeforeEach
    void clearUser() {
        Library.getBooks().clear();
        Library.getUsers().clear();
        Library.getLoans().clear();
    }

    @Test
    void testAddNewBook() throws BookNotFoundException {
        Library.addBook("евгений онегин", "пушкин", 1833, 2);
        Book book = Library.findBookByAll("евгений онегин", "пушкин", 1833);
        Assertions.assertEquals("пушкин", book.getAuthor());
        Assertions.assertEquals("евгений онегин", book.getTitle());
        Assertions.assertEquals(1, Library.getBooks().size());
        Assertions.assertEquals(2, book.getAvailableCopies());

        Library.addBook("евгений онегин", "пушкин", 1833, 5);
        Assertions.assertEquals(7, book.getAvailableCopies());
    }

    @Test
    void testFindBookByAllSuccess() throws BookNotFoundException {
        Library.addBook("евгений онегин", "пушкин", 1833, 2);
        Book book = Library.findBookByAll("евгений онегин", "пушкин", 1833);
        Assertions.assertNotNull(book);
        Assertions.assertEquals(2, book.getTotalCopies());
    }

    @Test
    void testFindBookByAllNotFound() throws BookNotFoundException {
        Assertions.assertThrows(BookNotFoundException.class, () -> Library.findBookByAll("", "пушкин", 1833));
    }

    @Test
    void testFindBookByTitleSuccess() throws BookNotFoundException {
        Library.addBook("евгений онегин", "пушкин", 1833, 2);
        List<Book> books = Library.findBookByTitle("евгений онегин");
        Assertions.assertNotNull(books);
        Assertions.assertEquals(1, books.size());
    }

    @Test
    void testFindBookByTitleNotFound() throws BookNotFoundException {
        Assertions.assertThrows(BookNotFoundException.class, () -> Library.findBookByTitle(""));
    }

    @Test
    void testFindBookByAuthorSuccess() throws BookNotFoundException {
        Library.addBook("евгений онегин", "пушкин", 1833, 2);
        Library.addBook("капитанская дочка", "пушкин", 1836, 1);
        List <Book> books = Library.findBookByAuthor("пушкин");
        Assertions.assertNotNull(books);
        Assertions.assertEquals(2, books.size());
    }

    @Test
    void testFindBookByAuthorNotFound() throws BookNotFoundException {
        Assertions.assertThrows(BookNotFoundException.class, () -> Library.findBookByAuthor("лермонтов"));
    }

    @Test
    void testAddNewUser() throws UserNotFoundException, UserAlreadyExistsException {
        Library.addUser("ларина", "larina@pushkin.com");
        Library.addUser("онегин", "onegin@pushkin.com");
        User user = Library.findUserByName("Ларина");
        Assertions.assertEquals("ларина", user.getName());
        Assertions.assertEquals("larina@pushkin.com", user.getEmail());
        Assertions.assertEquals(2, Library.getUsers().size());
    }

    @Test
    void testDuplicateUser() throws UserAlreadyExistsException {
        int size = Library.getUsers().size();
        Library.addUser("ларина", "larina@pushkin.com");
        Assertions.assertThrows(UserAlreadyExistsException.class, () ->Library.addUser("ларина", "larina@pushkin.com"));
        Assertions.assertEquals(size + 1, Library.getUsers().size());
    }


    @Test
    void testFindUserByIdSuccess() throws UserNotFoundException, UserAlreadyExistsException {
        Library.addUser("ларина", "larina@pushkin.com");
        User user = Library.findUserByName("ларина");

        User userById = Library.findUserById(user.getId());
        Assertions.assertEquals(user, userById);
    }

    @Test
    void testFindUserByIdNotFound() {
        int size = Library.getUsers().size();
        Assertions.assertThrows(UserNotFoundException.class, () -> Library.findUserById(size + 999));
    }

    @Test
    void testFindUserByNameSuccess() throws UserNotFoundException, UserAlreadyExistsException {
        Library.addUser("ларина", "larina@pushkin.com");
        User user = Library.findUserByName("Ларина");

        Assertions.assertNotNull(user);
        Assertions.assertEquals("ларина", user.getName());
    }

    @Test
    void testFindUserByNameNotFound() {
        Assertions.assertThrows(UserNotFoundException.class, () -> Library.findUserByName("пушкин"));
    }

    @Test
    void testStringValidateSuccess() {
        Assertions.assertDoesNotThrow(() -> Library.stringValidate("555"));
        Assertions.assertDoesNotThrow(() -> Library.stringValidate("aaa"));
        Assertions.assertDoesNotThrow(() -> Library.stringValidate(" 22 ss"));
    }

    @Test
    void testStringValidateThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Library.stringValidate(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Library.stringValidate(""));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Library.stringValidate("  "));
    }

    @Test
    void testIntValidateSuccess() {
        Assertions.assertDoesNotThrow(() -> Library.intValidate("555"));
    }

    @Test
    void testIntValidateThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Library.intValidate("aaa"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Library.intValidate(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Library.intValidate(""));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Library.intValidate("  "));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Library.intValidate("0"));
    }

    @Test
    void testLoanSuccess() throws UserAlreadyExistsException, UserNotFoundException, LoanExceedException, BookLoanToUserException, BookNotFoundException, BookNotAvailableCopiesException {
        Library.addBook("евгений онегин", "пушкин", 1833, 2);
        Library.addUser("ларина", "larina@pushkin.com");
        Library.addUser("онегин", "onegin@pushkin.com");
        int userId1 = Library.findUserByName("ларина").getId();
        int userId2 = Library.findUserByName("онегин").getId();
        int bookId = Library.findBookByAuthor("пушкин").getFirst().getId();
        Library.loan(bookId, userId1);
        Library.loan(bookId, userId2);
        Assertions.assertEquals(2, Library.getLoans().size());
    }

    @Test
    void testLoanThrows() throws UserAlreadyExistsException, UserNotFoundException, LoanExceedException,
            BookLoanToUserException, BookNotFoundException, BookNotAvailableCopiesException {
        Library.addBook("евгений онегин", "пушкин", 1833, 2);
        Library.addUser("ларина", "larina@pushkin.com");
        Library.addUser("онегин", "onegin@pushkin.com");
        Library.addUser("ленский", "lenski@pushkin.com");
        int userId1 = Library.findUserByName("ларина").getId();
        int userId2 = Library.findUserByName("онегин").getId();
        int userId3 = Library.findUserByName("ленский").getId();
        int bookId = Library.findBookByAuthor("пушкин").getFirst().getId();


        Library.loan(bookId, userId1);
        Assertions.assertThrows(BookLoanToUserException.class, () -> Library.loan(bookId, userId1));
        Library.loan(bookId, userId2);
        Assertions.assertThrows(BookNotAvailableCopiesException.class, () -> Library.loan(bookId, userId3));
    }

    @Test
    void testReturnBookSuccess() throws UserAlreadyExistsException, UserNotFoundException, LoanExceedException,
            BookLoanToUserException, BookNotFoundException, BookNotAvailableCopiesException, LoanNotFoundException {
        Library.addBook("евгений онегин", "пушкин", 1833, 2);
        Library.addUser("ларина", "larina@pushkin.com");
        int userId = Library.findUserByName("ларина").getId();
        int bookId = Library.findBookByAuthor("пушкин").getFirst().getId();
        Library.loan(bookId, userId);
        Library.returnBook(bookId,userId);
        Assertions.assertEquals(0, Library.findUserById(userId).getCurrentLoans().size());
    }

    @Test
    void testReturnBookThrows() throws UserAlreadyExistsException, UserNotFoundException, BookNotFoundException {
        Library.addBook("евгений онегин", "пушкин", 1833, 2);
        Library.addUser("ларина", "larina@pushkin.com");
        int userId = Library.findUserByName("ларина").getId();
        int bookId = Library.findBookByAuthor("пушкин").getFirst().getId();
        Assertions.assertThrows(LoanNotFoundException.class, () -> Library.returnBook(bookId,userId));
    }

    @Test
    void testgetLoansSuccess() throws UserAlreadyExistsException, UserNotFoundException, LoanExceedException,
            BookLoanToUserException, BookNotFoundException, BookNotAvailableCopiesException, LoanNotFoundException {
        Library.addBook("евгений онегин", "пушкин", 1833, 2);
        Library.addUser("ларина", "larina@pushkin.com");
        Library.addUser("онегин", "onegin@pushkin.com");
        int userId1 = Library.findUserByName("ларина").getId();
        int userId2 = Library.findUserByName("онегин").getId();
        int bookId = Library.findBookByAuthor("пушкин").getFirst().getId();
        Library.loan(bookId, userId1);
        Library.loan(bookId, userId2);
        Assertions.assertEquals(2, Library.getLoans().size());
        Assertions.assertEquals(1, Library.getLoans(bookId,userId1).size());
        Assertions.assertEquals(1, Library.getLoans(-1,userId1).size());
        Assertions.assertEquals(2, Library.getLoans(bookId,-1).size());
        Assertions.assertEquals(2, Library.getLoans(-1,-1).size());
    }

    @Test
    void testgetLoansThrow() throws UserAlreadyExistsException, UserNotFoundException, LoanExceedException,
            BookLoanToUserException, BookNotFoundException, BookNotAvailableCopiesException, LoanNotFoundException {
        Library.addBook("евгений онегин", "пушкин", 1833, 2);
        Library.addUser("ларина", "larina@pushkin.com");
        Library.addUser("онегин", "onegin@pushkin.com");
        int userId1 = Library.findUserByName("ларина").getId();
        int userId2 = Library.findUserByName("онегин").getId();
        int bookId = Library.findBookByAuthor("пушкин").getFirst().getId();
        Library.loan(bookId, userId1);
        Assertions.assertThrows(LoanNotFoundException.class, () -> Library.getLoans(2,-1));
        Assertions.assertThrows(LoanNotFoundException.class, () -> Library.getLoans(bookId,userId2));
        Assertions.assertThrows(LoanNotFoundException.class, () -> Library.getLoans(-10,5));
    }

}