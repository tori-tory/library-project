package service;

import exception.BookLoanToUserException;
import exception.BookNotAvailableCopiesException;
import exception.BookNotFoundException;
import exception.LoanExceedException;
import exception.LoanNotFoundException;
import exception.UserAlreadyExistsException;
import exception.UserNotFoundException;
import model.Loan;
import model.book.Book;
import model.user.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Library {

    private static final Map<Integer, Book> books = new HashMap<>();
    private static final Map<Integer, User> users = new HashMap<>();
    private static List<Loan> loans = new ArrayList<>();

    private static final String BOOK_FILE = "src/data/books.txt";
    private static final String USER_FILE = "src/data/users.txt";
    private static final int MAX_LOANS = 3;


    public static void uploadData() {
        booksLoad();
        usersLoad();
    }

    private static void booksLoad() {
        List<String> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(BOOK_FILE))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                addBook(values[0], values[1], Integer.parseInt(values[2]), Integer.parseInt(values[3]));
            }

            System.out.println(books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void usersLoad() {
        List<String> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                try {
                    addUser(values[0], values[1]);
                } catch (UserAlreadyExistsException ignored) {

                }
            }

            System.out.println(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addBook(String title, String author, int year, int totalCopies) {
        try {
            Book newBook = findBookByAll(title, author, year);
            // книга уже есть - увеличим количество экземпляров
            newBook.setTotalCopies(newBook.getTotalCopies() + totalCopies);
            newBook.setAvailableCopies(newBook.getAvailableCopies() + totalCopies);
        } catch (BookNotFoundException e) {
            // такой книги не было - добавим
            Book newBook = new Book(title, author, year, totalCopies);
            books.put(newBook.getId(), newBook);
        }
    }

    // Поиск книг по: названию, автору, году
    public static Book findBookByAll(String title, String author, int year) throws BookNotFoundException {
        for (Book book : books.values()) {
            if (book.getTitle().equalsIgnoreCase(title) &&
                    book.getAuthor().equalsIgnoreCase(author) &&
                    book.getYear() == year ) {
                return book;
            }
        }
        throw new BookNotFoundException(); // не нашли ни одной книги
    }

    public static List<Book> findBookByTitle(String title) throws BookNotFoundException {
        List<Book> bookList = books.values().stream()
                .filter(book -> book.getTitle().toLowerCase()
                        .contains(title.toLowerCase())).toList();

        if (bookList.isEmpty()) {
            throw new BookNotFoundException(); // не нашли ни одной книги
        }
        return bookList;
    }
    public static List<Book> findBookByAuthor(String author) throws BookNotFoundException {
        List<Book> bookList = books.values().stream()
                .filter(book -> book.getAuthor().toLowerCase()
                        .contains(author.toLowerCase())).toList();

        if (bookList.isEmpty()) {
            throw new BookNotFoundException(); // не нашли ни одной книги
        }
        return bookList;
    }

    // Поиск книги по: ID
    public static Book findBookById(int id) throws BookNotFoundException {
        Book book = books.get(id);
        if (book == null) {
            throw new BookNotFoundException(); // выбросить исключение, если книга не найдена
        }
        return book;
    }

    public static void addUser(String name, String email) throws UserAlreadyExistsException {
        try {
            name = stringValidate(name);
            email = stringValidate(email);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при добавлении пользователя: " + e.getMessage());
            return; // выйти, если данные некорректные
        }

        try {
            User newUser = findUserByName(name);
            throw new UserAlreadyExistsException();
        } catch (UserNotFoundException e) {
            // такого пользователя не было - добавить
            User newUser = new User(name, email);
            users.put(newUser.getId(), newUser);
        }
    }

    // Поиск пользователя по: ID
    public static User findUserById(int id) throws UserNotFoundException {
        User user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException(); // выбросить исключение, если пользователь не найден
        }
        return user;
    }

    // Поиск пользователя по: имени
    public static User findUserByName(String name) throws UserNotFoundException {
        for (User user : users.values()) {
            if (user.getName().equalsIgnoreCase(name)) {
                return user;
            }
        }
        throw new UserNotFoundException(); // не нашли ни одного
    }

    public static <T> void showItems(Collection<T> items, String header) {
        System.out.println(header);
        if (items.isEmpty()) {
            System.out.println("(список пуст)");
        } else {
            for (T item : items) {
                System.out.println(item);
            }
        }
    }

    public static void showLoans(Collection<Loan> items, String header) {
        System.out.println(header);
        if (items.isEmpty()) {
            System.out.println("(список пуст)");
        } else {
            for (Loan item : items) {
                System.out.printf("Книга %s, %s, Читатель %s, выдана -%s, возвращена - %s\n",
                        books.get(item.getBookId()).getTitle(),
                        books.get(item.getBookId()).getAuthor(),
                        users.get(item.getUserId()).getName(),
                        item.getLoanDate().toString(),
                        item.getReturnDate() == null ? "--" : item.getReturnDate().toString());
            }
        }
    }

    public static Collection<Book> getBooks() {
        return books.values();
    }

    public static Collection<User> getUsers() {
        return users.values();
    }

    public static Collection<Loan> getLoans() {
        return loans;
    }

    public static Collection<Loan> getLoans(int bookId, int userId) throws LoanNotFoundException {
        List<Loan> loanList = loans.stream()
                .filter(loan -> (bookId <= 0 || loan.getBookId() == bookId) &&
                                      (userId <= 0 || loan.getUserId() == userId))
                .toList();

        if (loanList.isEmpty()) {
            throw new LoanNotFoundException(); // выбросить исключение, если выдачи не найдены
        }
        return loanList;
    }

    public static Collection<Loan> getOverdueLoans() throws LoanNotFoundException {
        List<Loan> loanList = loans.stream()
                .filter(loan ->  (loan.getReturnDate() == null)
                                    && ChronoUnit.DAYS.between(loan.getLoanDate(), LocalDate.now()) > 30)
                .toList();

        if (loanList.isEmpty()) {
            throw new LoanNotFoundException(); // выбросить исключение, если выдачи не найдены
        }
        return loanList;
    }

    public static void loan(int bookId, int userId)
            throws BookNotFoundException, UserNotFoundException, BookNotAvailableCopiesException, LoanExceedException, BookLoanToUserException {
        Book book = findBookById(bookId);
        User user = findUserById(userId);

        Loan currentLoan = user.getCurrentLoans().stream()
                .filter(l -> l.getBookId() == bookId&& l.getUserId() == userId )
                .findFirst()
                .orElse(null);

        if (currentLoan != null) {
            throw new BookLoanToUserException(); //Такая же книга уже выдана этому пользователю
        }

        if (book.getAvailableCopies() < 1) {
            throw new BookNotAvailableCopiesException(); // Все книги выданы
        }

        if (user.getCurrentLoans().size() == MAX_LOANS) {
            throw new LoanExceedException(); //Не больше трех штук в одни руки!
        }

        Loan loan = new Loan(bookId, userId);
        user.getCurrentLoans().add(loan);
        loans.add(loan);
        book.setAvailableCopies(book.getAvailableCopies() - 1);

        System.out.printf("Книга %s (%s) выдана читателю %s\n", book.getTitle(), book.getAuthor(), user.getName());
    }

    public static void returnBook(int bookId, int userId) throws BookNotFoundException, UserNotFoundException, LoanNotFoundException {
        Book book = findBookById(bookId);
        User user = findUserById(userId);

        Loan loan = user.getCurrentLoans().stream()
                .filter(l -> l.getBookId() == bookId&& l.getUserId() == userId )
                .findFirst()
                .orElseThrow(() -> new LoanNotFoundException());

        loan.setReturnDate(LocalDate.now());
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        user.getCurrentLoans().remove(loan);
    }

    public static String stringValidate(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Ошибка: строка не может быть пустой!");
        }
        return input.trim();
    }

    public static int intValidate(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Ошибка: значение не может быть пустым!");
        }

        try {
            int value = Integer.parseInt(input.trim());
            if (value == 0) {
                throw new IllegalArgumentException("Ошибка: 0 вводить нельзя!");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ошибка: введите число!");
        }
    }
}
