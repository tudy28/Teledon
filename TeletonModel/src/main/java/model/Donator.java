package model;

public class Donator  extends Entity<Long>{
    private String lastname;
    private String firstname;
    private String adress;
    private String phone;

    public Donator(String firstname,String lastname,String adress,String phone){
        this.firstname=firstname;
        this.lastname=lastname;
        this.adress=adress;
        this.phone=phone;
    }

    public String getLastname(){
        return lastname;
    }

    public void setLastname(String newLastname){
        this.lastname=newLastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String newFirstname){
        this.firstname=newFirstname;
    }

    public String getAdress(){
        return adress;
    }

    public void setAdress(String newAdress){
        this.adress= newAdress;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String newPhone){
        this.phone=phone;
    }


}
