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

public class TeletonClientRPCWorker implements Runnable, Observer {
    private IService server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public TeletonClientRPCWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();
    //  private static Response errorResponse=new Response.Builder().type(ResponseType.ERROR).build();
    private Response handleRequest(Request request){
        Response response=null;
        if (request.type()== RequestType.LOGIN){
            System.out.println("Login request ..."+request.type());
            DTOVoluntar voluntaDTO=(DTOVoluntar) request.data();
            try {
                Voluntar voluntar=server.checkBypass(voluntaDTO.getUsername(), voluntaDTO.getPassword(),this);
                response = new Response.Builder().type(ResponseType.OK).data(voluntar).build();
            } catch (Exception e) {
                connected=false;
                response = new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }/*
        if (request.type()== RequestType.LOGOUT){
            System.out.println("Logout request");
            // LogoutRequest logReq=(LogoutRequest)request;
            UserDTO udto=(UserDTO)request.data();
            User user= DTOUtils.getFromDTO(udto);
            try {
                server.logout(user, this);
                connected=false;
                return okResponse;

            } catch (ChatException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }*/
        if (request.type()== RequestType.GET_CAZURI){
            System.out.println("GetCases Request ...");
            try {
                Iterable<Caz> cazuri=server.findAllCazuri();
                response = new Response.Builder().type(ResponseType.OK).data(cazuri).build();
            } catch (Exception e) {
                response = new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.GET_DONATOR){
            System.out.println("GetDonors Request ...");
            try {
                Iterable<Donator> donatori=server.findAllDonatori();
                response = new Response.Builder().type(ResponseType.OK).data(donatori).build();
            } catch (Exception e) {
                response = new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.GET_DONATOR_BY_NAME){
            System.out.println("GetDonors_By_Name Request ...");
            DTODonator donatorDTO=(DTODonator) request.data();
            try {
                Iterable<Donator> donatori=server.findByName(donatorDTO.getName1(), donatorDTO.getName2());
                response = new Response.Builder().type(ResponseType.OK).data(donatori).build();
            } catch (Exception e) {
                response = new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.GET_PHONE){
            System.out.println("GetDonors_By_Phone Request ...");
            String phone=(String) request.data();
            try {
                Donator donator=server.findByPhone(phone);
                response = new Response.Builder().type(ResponseType.OK).data(donator).build();
            } catch (Exception e) {
                response = new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.ADD_DONATIE){
            System.out.println("Add_Donation Request ...");
            Donatie donatie=(Donatie) request.data();
            try {
                server.adaugaDonatie(donatie);
                response = new Response.Builder().type(ResponseType.OK).build();
            } catch (Exception e) {
                response = new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.ADD_DONATOR){
            System.out.println("Add_Donor Request ...");
            Donator donator=(Donator) request.data();
            try {
                server.adaugaDonator(donator);
                response = new Response.Builder().type(ResponseType.OK).build();
            } catch (Exception e) {
                response = new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.GET_SINGLE_DONATOR){
            System.out.println("Get_Single_Donator Request ...");
            long idDonator=(long) request.data();
            try {
                Donator donator = server.findDonator(idDonator);
                response = new Response.Builder().type(ResponseType.OK).data(donator).build();
            } catch (Exception e) {
                response = new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.GET_SINGLE_CAZ){
            System.out.println("Get_Single_CAZ Request ...");
            long idCaz=(long) request.data();
            try {
                Caz caz = server.findCaz(idCaz);
                response = new Response.Builder().type(ResponseType.OK).data(caz).build();
            } catch (Exception e) {
                response = new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.UPDATE_CAZ){
            System.out.println("Update_CAZ Request ...");
            DTOCaz caz=(DTOCaz) request.data();
            try {
                server.modificaCaz(caz.getCaz(), caz.getIdCaz());
                response = new Response.Builder().type(ResponseType.OK).build();
            } catch (Exception e) {
                response = new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.LOGOUT){
            System.out.println("Log_Out Request ...");
            Long id=(Long) request.data();
            try {
                server.logout(id);
                connected=false;
                response = new Response.Builder().type(ResponseType.OK).build();
            } catch (Exception e) {
                response = new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }


    @Override
    public void notifyAddedDonation(Iterable<Caz> cazuri) {
        try{
            Response response = new Response.Builder().type(ResponseType.MADE_DONATION).data(cazuri).build();
            sendResponse(response);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void notifyAddedDonor(Iterable<Donator> donatori) {
        try{
            Response response = new Response.Builder().type(ResponseType.ADDED_DONOR).data(donatori).build();
            sendResponse(response);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
