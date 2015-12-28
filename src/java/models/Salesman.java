package models;

public class Salesman extends User{

    public Salesman(String username) {
        super(username);
    }

    public Salesman(String username, String password) {
        super(username, password);
    }

    public Salesman(String username, String password, String role) {
        super(username, password, role);
    }

    public Salesman(String username, String password, String role, String date) {
        super(username, password, role, date);
    }

}
