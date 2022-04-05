package dto;

import java.io.Serializable;

public class DTODonator implements Serializable {
    private String name1;
    private String name2;

    public DTODonator(String name1,String name2){
        this.name1=name1;
        this.name2=name2;
    }

    public String getName1() {
        return name1;
    }

    public String getName2() {
        return name2;
    }
}
