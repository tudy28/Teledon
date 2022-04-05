package teleton.service;

import model.Caz;
import model.Donatie;
import model.Donator;
import model.Voluntar;

public interface IService {

    void adaugaDonatie(Donatie donatie) throws Exception;


    void adaugaDonator(Donator donator) throws Exception;


    Donator findDonator(long idDonator) throws Exception;

    Iterable<Donator> findAllDonatori() throws Exception;


    Donator findByPhone(String phone) throws Exception;

    Iterable<Donator> findByName(String name1, String name2) throws Exception;



    Voluntar checkBypass(String username,String password,Observer client) throws Exception;




    void modificaCaz(Caz updatedCaz, long idCaz) throws Exception;

    Caz findCaz(long idCaz) throws Exception;

    Iterable<Caz> findAllCazuri() throws Exception;

    void logout(long id) throws Exception;


}
