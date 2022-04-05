package model;

public class Voluntar extends Entity<Long> {
    private String username;
    private Integer password;

    public Voluntar(){
    }

    public Voluntar(String username, Integer password){
        this.username=username;
        this.password=password;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String newUsername){
        this.username=newUsername;
    }

    public Integer getPassword(){
        return password;
    }

    public void setPassword(Integer newPassword){
        this.password=newPassword;
    }
}
