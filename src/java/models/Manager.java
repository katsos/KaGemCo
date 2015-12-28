package models;

public class Manager extends User {

    public Manager(String username) {
        super(username);
    }

    public Manager(String username, String password) {
        super(username, password);
    }

    public Manager(String username, String password, String role, String date) {
        super(username, password, role, date);
    }

}
