package model;

public class Donatie extends Entity<Long>{
    private Long idDonator;
    private float sum;
    private Long idCaz;

    public Donatie(Long idDonator, float sum, Long idCaz){
        this.idDonator=idDonator;
        this.sum=sum;
        this.idCaz=idCaz;
    }

    public Long getDonatorID(){
        return idDonator;
    }

    public void setDonatorID(Long newIdDonator){
        this.idDonator=newIdDonator;
    }

    public float getSum(){
        return sum;
    }

    public void setSum(float newSum){
        this.sum=newSum;
    }

    public Long getCazID(){
        return idCaz;
    }

    public void setCazID(Long newIdCaz){
        this.idCaz=newIdCaz;
    }
}
