package ui;

import exception.BookNotFoundException;
import exception.LoanNotFoundException;
import exception.UserAlreadyExistsException;
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
                case "4" -> loan();
                case "5" -> returnBook();
                case "6" -> addUser();
                case "7" -> showUsers();
                case "8" -> findUserById();
                case "81" -> currentLoansByUser();
                case "91" -> loansByUser();
                case "92" -> loansByBook();
                case "93" -> showOverdueLoans();
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
        System.out.println("81 - Какие книги у пользователя по ID");
        System.out.println("** Просмотр истории выдач:");
        System.out.println("91 - По конкретному пользователю");
        System.out.println("92 - По конкретной книге");
        System.out.println("93 - Поиск просроченных выдач");
        System.out.println("0 - Выход");
    }

    private static void rePrintMenu(String message) {
        System.out.println(message);
        printMenu();
    }

    private static void loan() {
        System.out.println("Выдать книгу");
        int bookId = intValidate("ID книги");
        int userId = intValidate("ID пользователя");

        try {
            Library.loan(bookId, userId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void returnBook() {
        System.out.println("Вернуть книгу");
        int bookId = intValidate("ID книги");
        int userId = intValidate("ID пользователя");

        try {
            Library.returnBook(bookId, userId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void loansByUser() {
        System.out.println("Просмотр истории выдач: По конкретному пользователю");
        int id = intValidate("ID пользователя");
        try {
            User user = Library.findUserById(id);
            //поиск всех выдач пользователя
            Library.showLoans(Library.getLoans(
                    -1,
                     user.getId()),
                    "История выдачи книг по " + user.getName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void loansByBook() {
        System.out.println("Просмотр истории выдач: По конкретной книге");
        int id = intValidate("ID книги");
        try {
            Book book = Library.findBookById(id);
            //поиск всех выдач книги
            Library.showLoans(Library.getLoans(
                    book.getId(),
                    -1),
                    "История выдачи книги " + book.getTitle()+ ", " + book.getAuthor());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showOverdueLoans() {
        try {
            Library.showLoans(Library.getOverdueLoans(), "Просмотр просроченных выдач");
        } catch (LoanNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void currentLoansByUser() {
        System.out.println("Сейчас читает");
        int id = intValidate("ID пользователя");
        try {
            User user = Library.findUserById(id);
            Library.showLoans(user.getCurrentLoans(), "Сейчас читает " + user.getName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
        } catch (UserAlreadyExistsException e) {
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
