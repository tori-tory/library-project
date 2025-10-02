import service.Library;
import ui.ConsoleMenu;

public class Main {
    public static void main(String[] args) {
        System.out.println("Домашняя работа №11 - Библиотека");

        Library.uploadData();
        ConsoleMenu.start();

    }
}