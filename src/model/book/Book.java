package model.book;

public class Book {
    private static int counter = 1;
    private final int id;
    private String title;
    private String author;
    private int year;
    private int totalCopies;
    private int availableCopies;

    public Book(String title, String author, int year, int totalCopies) {
        this.id = counter;
        this.title = title;
        this.author = author;
        this.year = year;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        counter++;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    @Override
    public String toString() {
        int recNo = id;
        return recNo + ". " + title + " (" + author + ", " + year +
                "), всего/доступно = " + totalCopies + "/" + availableCopies;
    }
}
