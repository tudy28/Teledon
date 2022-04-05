package service;

import model.Caz;
import model.Donatie;
import model.Donator;
import model.Voluntar;
import repository.IRepositoryCaz;
import repository.IRepositoryDonatie;
import repository.IRepositoryDonator;
import repository.IRepositoryVoluntar;
import teleton.service.IService;
import teleton.service.Observer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service implements IService {
    private IRepositoryDonatie repositoryDonatie;
    private IRepositoryDonator repositoryDonator;
    private IRepositoryVoluntar repositoryVoluntar;
    private IRepositoryCaz repositoryCaz;
    private Map<Long,Observer> loggedClients;
    private final int defaultThreadsNumber = 5;


    public Service(IRepositoryDonatie repositoryDonatie, IRepositoryDonator repositoryDonator,
                   IRepositoryVoluntar repositoryVoluntar, IRepositoryCaz repositoryCaz)
    {
        this.repositoryDonatie = repositoryDonatie;
        this.repositoryDonator = repositoryDonator;
        this.repositoryVoluntar = repositoryVoluntar;
        this.repositoryCaz = repositoryCaz;
        this.loggedClients = new ConcurrentHashMap<>();
    }

    private void notifyAddedDonation(){
        ExecutorService executorService = Executors.newFixedThreadPool(this.defaultThreadsNumber);
        loggedClients.forEach((id,client)->{
            Observer c = loggedClients.get(id);
            executorService.execute(()->{
                try{
                    System.out.println("Notifying [" + id + "]");
                    c.notifyAddedDonation(this.findAllCazuri());
                }
                catch (Exception e){
                    System.out.println("Error notifying volunteer with ID: " + id + " Message: " + e.getMessage());
                }
            });
        });
    }

    private void notifyAddedDonor(){
        ExecutorService executorService = Executors.newFixedThreadPool(this.defaultThreadsNumber);
        loggedClients.forEach((id,client)->{
            Observer c = loggedClients.get(id);
            executorService.execute(()->{
                try{
                    System.out.println("Notifying [" + id + "]");
                    c.notifyAddedDonor(this.findAllDonatori());
                }
                catch (Exception e){
                    System.out.println("Error notifying volunteer with ID: " + id + " Message: " + e.getMessage());
                }
            });
        });
    }

    public synchronized void adaugaDonatie(Donatie donatie)
    {
        repositoryDonatie.add(donatie);
        notifyAddedDonation();
    }



    public synchronized void adaugaDonator(Donator donator)
    {
        repositoryDonator.add(donator);
    //    notifyAddedDonor();
    }


    public synchronized Donator findDonator(long idDonator)
    {
        return repositoryDonator.findById(idDonator);
    }

    public synchronized Iterable<Donator> findAllDonatori()
    {
        return repositoryDonator.findAll();
    }


    public synchronized Donator findByPhone(String phone)
    {
        return repositoryDonator.findByPhone(phone);
    }

    public synchronized Iterable<Donator> findByName(String name1, String name2)
    {
        return repositoryDonator.getAllByName(name1, name2);
    }


    public synchronized Voluntar checkBypass(String username, String password, Observer client)
    {
        Voluntar voluntar = repositoryVoluntar.checkBypass(username, password);
        if(voluntar != null){
            loggedClients.put(voluntar.getId(), client);
        }
        return voluntar;
    }


    public synchronized void modificaCaz(Caz updatedCaz, long idCaz)
    {
        repositoryCaz.update(updatedCaz, idCaz);
    }

    public synchronized Caz findCaz(long idCaz)
    {
        return repositoryCaz.findById(idCaz);
    }

    public synchronized Iterable<Caz> findAllCazuri()
    {
        return repositoryCaz.findAll();
    }

    @Override
    public synchronized void logout(long id) {
        loggedClients.remove(id);
    }


}
