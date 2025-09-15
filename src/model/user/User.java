package model.user;

public class User {

    private static int counter = 1;
    private int id;
    private String name;
    private String email;

    public User(String name, String email) {
        setId();
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    private void setId() {
        this.id = this.counter++;
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
}
