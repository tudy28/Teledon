package network;

import dto.DTOCaz;
import dto.DTODonator;
import dto.DTOVoluntar;
import model.Caz;
import model.Donatie;
import model.Donator;
import model.Voluntar;
import teleton.service.IService;
import teleton.service.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TeletonClientRPCProxy implements IService {
    private String host;
    private int port;

    private Observer client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public TeletonClientRPCProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Response>();
    }


    @Override
    public Voluntar checkBypass(String username, String password, Observer client) throws Exception {
        initializeConnection();
        this.client=client;
        DTOVoluntar voluntarDTO= new DTOVoluntar(username,password);
        Request req=new Request.Builder().type(RequestType.LOGIN).data(voluntarDTO).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.OK){
            return (Voluntar) response.data();
        }

        String err=response.data().toString();
        closeConnection();
        throw new Exception(err);

    }


    @Override
    public void adaugaDonatie(Donatie donatie) throws Exception {
        Request req=new Request.Builder().type(RequestType.ADD_DONATIE).data(donatie).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.OK){
            return;
        }

        String err=response.data().toString();
        throw new Exception(err);

    }

    @Override
    public void adaugaDonator(Donator donator) throws Exception {
        Request req=new Request.Builder().type(RequestType.ADD_DONATOR).data(donator).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.OK){
            return;
        }

        String err=response.data().toString();
        throw new Exception(err);

    }

    @Override
    public Donator findDonator(long idDonator) throws Exception {
        Request req=new Request.Builder().type(RequestType.GET_SINGLE_DONATOR).data(idDonator).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.OK){
            return (Donator) response.data();
        }
        String err=response.data().toString();
        throw new Exception(err);

    }

    @Override
    public Iterable<Donator> findAllDonatori() throws Exception {
        Request req=new Request.Builder().type(RequestType.GET_DONATOR).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.OK){
            return (Iterable<Donator>) response.data();
        }
        String err=response.data().toString();
        throw new Exception(err);
    }

    @Override
    public Donator findByPhone(String phone) throws Exception {
        Request req=new Request.Builder().type(RequestType.GET_PHONE).data(phone).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.OK){
            return (Donator) response.data();
        }
        String err=response.data().toString();
        throw new Exception(err);
    }

    @Override
    public Iterable<Donator> findByName(String name1, String name2) throws Exception {
        DTODonator donatorDTO= new DTODonator(name1,name2);
        Request req=new Request.Builder().type(RequestType.GET_DONATOR_BY_NAME).data(donatorDTO).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.OK){
            return (Iterable<Donator>) response.data();
        }
        String err=response.data().toString();
        throw new Exception(err);
    }

    @Override
    public void modificaCaz(Caz updatedCaz, long idCaz) throws Exception {
        DTOCaz cazDTO= new DTOCaz(updatedCaz,idCaz);
        Request req=new Request.Builder().type(RequestType.UPDATE_CAZ).data(cazDTO).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.OK){
            return;
        }
        String err=response.data().toString();
        throw new Exception(err);
    }

    @Override
    public Caz findCaz(long idCaz) throws Exception {
        Request req=new Request.Builder().type(RequestType.GET_SINGLE_CAZ).data(idCaz).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.OK){
            return (Caz) response.data();
        }
        String err=response.data().toString();
        throw new Exception(err);
    }

    @Override
    public Iterable<Caz> findAllCazuri() throws Exception {
        Request req=new Request.Builder().type(RequestType.GET_CAZURI).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.OK){
            return (Iterable<Caz>) response.data();
        }
        String err=response.data().toString();
        throw new Exception(err);


    }

    @Override
    public void logout(long id) throws Exception {
        Request req=new Request.Builder().type(RequestType.LOGOUT).data(id).build();
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if (response.type()== ResponseType.OK){
            return;
        }
        String err=response.data().toString();
        throw new Exception(err);


    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request)throws Exception {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new Exception("Error sending object "+e);
        }

    }

    private Response readResponse() throws Exception {
        Response response=null;
        try{

            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }


    private void initializeConnection() {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(Response response){
        if (response.type()== ResponseType.MADE_DONATION){
            System.out.println("Donation made");
            try {
                Iterable<Caz> cazuri=(Iterable<Caz>) response.data();
                client.notifyAddedDonation(cazuri);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (response.type()== ResponseType.ADDED_DONOR){
            System.out.println("Donor added");
            try {
                Iterable<Donator> donatori=(Iterable<Donator>) response.data();
                client.notifyAddedDonor(donatori);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean isUpdate(Response response){
        return response.type()== ResponseType.MADE_DONATION /*|| response.type() == ResponseType.ADDED_DONOR*/;
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
