package repository;

import model.Donator;
import model.Entity;

public interface IRepositoryDonator extends Repository<Long,Donator>{

    public Iterable<Donator> getAllByName(String name1,String name2);

    public Donator findByPhone(String phone);

}
