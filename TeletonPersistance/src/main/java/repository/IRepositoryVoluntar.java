package repository;

import model.Voluntar;

public interface IRepositoryVoluntar extends Repository<Long,Voluntar>{

    public Voluntar checkBypass(String username, String password);
}
