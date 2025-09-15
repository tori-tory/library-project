package service;

import exception.BookNotFoundException;
import exception.UserAlreadyExists;
import exception.UserNotFoundException;
import model.book.Book;
import model.user.User;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LibraryTest {

    @BeforeEach
    void clearUser() {
        Library.getBooks().clear();
        Library.getUsers().clear();
    }

    @Test
    void testAddNewBook() throws BookNotFoundException {
        Library.addBook("евгений онегин", "пушкин", 1833, 2);
        Book book = Library.findBook("евгений онегин", "пушкин", 1833);
        Assertions.assertEquals("пушкин", book.getAuthor());
        Assertions.assertEquals("евгений онегин", book.getTitle());
        Assertions.assertEquals(1, Library.getBooks().size());
        Assertions.assertEquals(2, book.getAvailableCopies());

        Library.addBook("евгений онегин", "пушкин", 1833, 5);
        Assertions.assertEquals(7, book.getAvailableCopies());
    }

    @Test
    void testFindBookSuccess() throws BookNotFoundException {
        Library.addBook("евгений онегин", "пушкин", 1833, 2);
        Book book = Library.findBook("евгений онегин", "пушкин", 1833);
        Assertions.assertNotNull(book);
        Assertions.assertEquals(2, book.getTotalCopies());
    }

    @Test
    void testFindBookNotFound() throws BookNotFoundException {
        Assertions.assertThrows(BookNotFoundException.class, () -> Library.findBook("", "пушкин", 1833));
    }

    @Test
    void testAddNewUser() throws UserNotFoundException, UserAlreadyExists {
        Library.addUser("ларина", "larina@pushkin.com");
        Library.addUser("онегин", "onegin@pushkin.com");
        System.out.println(Library.getUsers().toString());
        User user = Library.findUserByName("Ларина");
        Assertions.assertEquals("ларина", user.getName());
        Assertions.assertEquals("larina@pushkin.com", user.getEmail());
        Assertions.assertEquals(2, Library.getUsers().size());
    }

    @Test
    void testDuplicateUser() throws UserAlreadyExists {
        int size = Library.getUsers().size();
        Library.addUser("ларина", "larina@pushkin.com");
        Assertions.assertThrows(UserAlreadyExists.class, () ->Library.addUser("ларина", "larina@pushkin.com"));
        Assertions.assertEquals(size + 1, Library.getUsers().size());
    }


    @Test
    void testFindUserByIdSuccess() throws UserNotFoundException, UserAlreadyExists {
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
    void testFindUserByNameSuccess() throws UserNotFoundException, UserAlreadyExists {
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

}