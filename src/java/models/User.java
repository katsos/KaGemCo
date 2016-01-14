package models;

public class User {

    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String role;
    private String date;

    public User() {
        
    }
    public User( String firstname, String lastname, String username, String password, String role ) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.role = role;   
    }
    public User( String firstname, String lastname, String username, String password, String role, String date ) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.role = role;
        this.date = date;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
    
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getDate() {
        return date;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setRole(String role) {
        this.role = role;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    @Override
    public String toString() {
        return username + '-' + role + '[' + date + ']' + '(' + firstname + lastname + ')' ;
    }
    
}
