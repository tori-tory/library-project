package service;

import exception.BookNotFoundException;
import exception.UserAlreadyExists;
import exception.UserNotFoundException;
import model.book.Book;
import model.user.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Library {

    private static final Map<Integer, Book> books = new HashMap<>();
    private static final Map<Integer, User> users = new HashMap<>();

    private static final String BOOK_FILE = "src/data/books.txt";
    private static final String USER_FILE = "src/data/users.txt";


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
                String title = values[0];
                String author = values[1];
                int year = Integer.parseInt(values[2]);
                int totalCopies = Integer.parseInt(values[3]);
                addBook(title, author, year, totalCopies);
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
                String name = values[0];
                String email = values[1];
                try {
                    addUser(name, email);
                } catch (UserAlreadyExists ignored) {

                }
            }

            System.out.println(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addBook(String title, String author, int year, int totalCopies) {
        try {
            Book newBook = findBook(title, author, year);
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
    public static Book findBook(String title, String author, int year) throws BookNotFoundException {
        for (Book book : books.values()) {
            if (book.getTitle().equalsIgnoreCase(title) &&
                    book.getAuthor().equalsIgnoreCase(author) &&
                    book.getYear() == year ) {
                return book;
            }
        }
        throw new BookNotFoundException(); // не нашли ни одной книги
    }

    public static void addUser(String name, String email) throws UserAlreadyExists{
        try {
            name = stringValidate(name);
            email = stringValidate(email);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при добавлении пользователя: " + e.getMessage());
            return; // выйти, если данные некорректные
        }

        try {
            User newUser = findUserByName(name);
            throw new UserAlreadyExists();
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

    public static Collection<Book> getBooks() {
        return books.values();
    }

    public static Collection<User> getUsers() {
        return users.values();
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
