package models;

public class Customer {

    private String Firstname;
    private String Lastname;
    private String BirthDate;
    private char Gender;
    private String FamilyStatus;
    private String HomeAddress;
    private long TaxID;
    private long BankAccountNo;
    private String PersonalCode;

    public Customer() {
        this.Firstname = null;
        this.Lastname = null;
        this.BirthDate = null;
        this.Gender = '\0';
        this.FamilyStatus = null;
        this.HomeAddress = null;
        this.TaxID = -1;
        this.BankAccountNo = -1;
        this.PersonalCode = null;
    }

    public Customer(String Name, String Surname, String BirthDate, char Gender, String FamilyStatus, String HomeAddress, long TaxID, long BankAccountNo, String PersonalCode) {
        this.Firstname = Name;
        this.Lastname = Surname;
        this.BirthDate = BirthDate;
        this.Gender = Gender;
        this.FamilyStatus = FamilyStatus;
        this.HomeAddress = HomeAddress;
        this.TaxID = TaxID;
        this.BankAccountNo = BankAccountNo;
        this.PersonalCode = PersonalCode;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String Firstname) {
        this.Firstname = Firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String Lastname) {
        this.Lastname = Lastname;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String BirthDate) {
        this.BirthDate = BirthDate;
    }

    public char getGender() {
        return Gender;
    }

    public void setGender(char Gender) {
        this.Gender = Gender;
    }

    public String getFamilyStatus() {
        return FamilyStatus;
    }

    public void setFamilyStatus(String FamilyStatus) {
        this.FamilyStatus = FamilyStatus;
    }

    public String getHomeAddress() {
        return HomeAddress;
    }

    public void setHomeAddress(String HomeAddress) {
        this.HomeAddress = HomeAddress;
    }

    public long getTaxID() {
        return TaxID;
    }

    public void setTaxID(long TaxID) {
        this.TaxID = TaxID;
    }

    public long getBankAccountNo() {
        return BankAccountNo;
    }

    public void setBankAccountNo(long BankAccountNo) {
        this.BankAccountNo = BankAccountNo;
    }

    public String getPersonalCode() {
        return PersonalCode;
    }

    public void setPersonalCode(String PersonalCode) {
        this.PersonalCode = PersonalCode;
    }

}
