package models;

public class Customer {

    private String firstname;
    private String lastname;
    private String birthDate;
    private char gender;
    private String familyStatus;
    private String homeAddress;
    private long taxID;
    private long bankAccountNo;
    private String personalCode;
    private long relateTaxID;

    public Customer() {
        this.firstname = null;
        this.lastname = null;
        this.birthDate = null;
        this.gender = '\0';
        this.familyStatus = null;
        this.homeAddress = null;
        this.taxID = -1;
        this.bankAccountNo = -1;
        this.personalCode = null;
    }

    public Customer(String firstname, String lastname, String birthDate, char gender,
            String familyStatus, String homeAddress, long taxID, long bankAccountNo,
            String personalCode, long relatetaxID) {
        
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.familyStatus = familyStatus;
        this.homeAddress = homeAddress;
        this.taxID = taxID;
        this.bankAccountNo = bankAccountNo;
        this.personalCode = personalCode;
        this.relateTaxID = relatetaxID;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getFamilyStatus() {
        return familyStatus;
    }

    public void setFamilyStatus(String familyStatus) {
        this.familyStatus = familyStatus;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public long getTaxID() {
        return taxID;
    }

    public void setTaxID(long taxID) {
        this.taxID = taxID;
    }

    public long getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(long bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getPersonalCode() {
        return personalCode;
    }

    public void setPersonalCode(String personalCode) {
        this.personalCode = personalCode;
    }

    public long getRelateTaxID() {
        return relateTaxID;
    }

    public void setRelateTaxID(long relatetaxID) {
        this.relateTaxID = relatetaxID;
    }

    @Override
    public String toString() {
        return "Customer{" + "firstname=" + firstname + ", lastname=" + lastname + ", birthDate=" + birthDate + ", gender=" + gender + ", familyStatus=" + familyStatus + ", homeAddress=" + homeAddress + ", taxID=" + taxID + ", bankAccountNo=" + bankAccountNo + ", personalCode=" + personalCode + ", relatetaxID=" + relateTaxID + '}';
    }

}
