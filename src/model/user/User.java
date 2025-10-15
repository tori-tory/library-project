package model.user;

import model.Loan;

import java.util.ArrayList;
import java.util.List;

public class User {

    private static int counter = 1;
    private final int id;
    private String name;
    private String email;
    private List<Loan> currentLoans = new ArrayList<>();

    public User(String name, String email) {
        this.id = counter;
        this.name = name;
        this.email = email;
        counter++;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        int recNo = id;
        return recNo + ". " + name + ", " + email;
    }

    public List<Loan> getCurrentLoans() {
        return currentLoans;
    }
}
