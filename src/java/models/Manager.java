package models;

public class Manager extends User {

    public Manager() {
    }

    public Manager(String firstname, String lastname, String username, String password, String role) {
        super(firstname, lastname, username, password, role);
    }

    public Manager(String firstname, String lastname, String username, String password, String role, String date) {
        super(firstname, lastname, username, password, role, date);
    }

}
