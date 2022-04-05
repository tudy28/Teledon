package dto;

import java.io.Serializable;

public class DTOVoluntar implements Serializable {
    private String username;
    private String password;

    public DTOVoluntar(String username,String password){
        this.username=username;
        this.password=password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword(){
        return password;
    }
}
