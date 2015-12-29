package models;

public class Salesman extends User{

    public Salesman() {
    }

    public Salesman(String firstname, String lastname, String username, String password, String role) {
        super(firstname, lastname, username, password, role);
    }

    public Salesman(String firstname, String lastname, String username, String password, String role, String date) {
        super(firstname, lastname, username, password, role, date);
    }

}
