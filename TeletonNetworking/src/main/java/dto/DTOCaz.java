package dto;

import model.Caz;

import java.io.Serializable;

public class DTOCaz implements Serializable {
    Caz caz;
    Long idCaz;

    public DTOCaz(Caz caz,Long idCaz){
        this.caz=caz;
        this.idCaz=idCaz;
    }

    public Caz getCaz() {
        return caz;
    }

    public Long getIdCaz() {
        return idCaz;
    }
}
