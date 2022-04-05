package model;

public class Caz extends Entity<Long>{
    private String name;
    private Float totalSum;

    public Caz(String name,Float totalSum){
        this.name=name;
        this.totalSum=totalSum;
    }

    public String getName(){
        return name;
    }

    public void setName(String newName){
        this.name=newName;
    }

    public Float getTotalSum(){
        return totalSum;
    }

    public void setTotalSum(float newTotalSum){
        this.totalSum=newTotalSum;
    }
}
