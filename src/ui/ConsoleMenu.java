package ui;

import exception.BookNotFoundException;
import exception.UserAlreadyExists;
import exception.UserNotFoundException;
import model.book.Book;
import model.user.User;
import service.Library;

import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private static final Scanner scanner = new Scanner(System.in);

    public static void start() {
        boolean running =  true;
        printMenu();
        while (running) {
            String choice = stringValidate("Ваш выбор (99 - повторно вывести меню)");//scanner.nextLine().trim();

            switch (choice) {
                case "1" -> addBook();
                case "2" -> showBooks();
                case "3" -> findBookByAll();
                case "31" -> findBookByTitle();
                case "32" -> findBookByAuthor();
                case "4" -> System.out.println("Выдать книгу");
                case "5" -> System.out.println("Вернуть книгу");
                case "6" -> addUser();
                case "7" -> showUsers();
                case "8" -> findUserById();
                case "99" -> rePrintMenu("");

                case "0" -> {
                    System.out.println("Конец.");
                    running = false;
                }
                default -> rePrintMenu("В библиотеке так не умеют, выберите что-нибудь другое");
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("**** МЕНЮ ***");
        System.out.println("** Работа с книгами:");
        System.out.println("1 - Добавить книгу");
        System.out.println("2 - Просмотр всех книг");
        System.out.println("3 - Поиск книг по: названию, автору, году");
        System.out.println("31 - Поиск книги по названию");
        System.out.println("32 - Поиск книг по автору");
        System.out.println("4 - Выдать книгу");
        System.out.println("5 - Вернуть книгу");
        System.out.println("** Работа с пользователями:");
        System.out.println("6 - Добавить пользователя");
        System.out.println("7 - Показать всех пользователей");
        System.out.println("8 - Поиск пользователя по ID");
        System.out.println("0 - Выход");
    }

    private static void rePrintMenu(String message) {
        System.out.println(message);
        printMenu();
    }

    private static void addBook() {
        System.out.println("Добавить книгу");

        String title = stringValidate("Название");
        String author = stringValidate("Автор");
        int year = intValidate("Год");
        int totalCopies = intValidate("Количество экземпляров");
        Library.addBook(title, author, year, totalCopies);

        System.out.printf("Добавлено в библиотеку: %s, %s, %s\n",title, author, year);
        rePrintMenu("Продолжить работу");
    }

    private static void findBookByAll() {
        System.out.println("Найти книгу");
        String title = stringValidate("Название");
        String author = stringValidate("Автор");
        int year = intValidate("Год");
        try {
            Book book = Library.findBookByAll(title, author, year);
            System.out.printf("Найдено в библиотеке: %s, %s, %s\n", book.getTitle(), book.getAuthor(), book.getYear());
        } catch (BookNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void findBookByTitle() {
        System.out.println("Найти книгу по Названию");

        String title = stringValidate("Название");
        try {
            List<Book> book = Library.findBookByTitle(title);
            Library.showItems(book, "Найдено в библиотеке по Названию:");
        } catch (BookNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void findBookByAuthor() {
        System.out.println("Найти книгу по Автору");

        String author = stringValidate("Автор");
        try {
            List<Book> book = Library.findBookByAuthor(author);
            Library.showItems(book, "Найдено в библиотеке по Автору:");
        } catch (BookNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showBooks() {
        Library.showItems(Library.getBooks(), "Наличие книг");
    }

    private static void addUser() {
        System.out.println("Добавить пользователя");
        String name = stringValidate("Имя");
        String email = stringValidate("email");
        try {
            Library.addUser(name, email);
            System.out.printf("Добавлен: %s, %s\n",name, email);
            rePrintMenu("Продолжить работу");
        } catch (UserAlreadyExists e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showUsers() {
        Library.showItems(Library.getUsers(), "Просмотр всех пользователей");
    }

    private static void findUserById() {
        System.out.println("Найти пользователя по Id");
        int id = intValidate("ID");
        try {
            User user = Library.findUserById(id);
            System.out.printf("Найден пользователь: %s, %s, %s\n", user.getId(), user.getName(), user.getEmail());
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String stringValidate(String message) {
        String value = null;
        while (value == null) {
            try {
                System.out.printf("%s: ", message);
                value = Library.stringValidate(scanner.nextLine());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return value;
    }

    private static int intValidate(String message) {
        Integer value = null;
        while (value == null) {
            try {
                System.out.printf("%s: ", message);
                value = Library.intValidate(scanner.nextLine());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        return value;
    }
}
