package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Caz;
import model.Donatie;
import model.Donator;
import teleton.service.IService;
import teleton.service.Observer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class UserController implements Observer {

    ObservableList<Donator> modelDonatori = FXCollections.observableArrayList();
    ObservableList<Caz> modelCazuri = FXCollections.observableArrayList();

    IService service;
    Long userID;
    Stage stage;

    @FXML
    private TextField textFieldLastName;

    @FXML
    private TextField textFieldFirstName;

    @FXML
    private TextField textFieldAdress;

    @FXML
    private TextField textFieldPhone;

    @FXML
    private TextField textFieldValue;

    @FXML
    private TextField textFieldSearch;



    //-----------------------TABELA DONATORI--------------------------------------------
    @FXML
    private TableView<Donator> tableViewDonatori;

    @FXML
    private TableColumn<Donator, String> tableDonatoriColumnFirstName;

    @FXML
    private TableColumn<Donator, String> tableDonatoriColumnLastName;

    @FXML
    private TableColumn<Donator, String> tableDonatoriColumnAdress;

    @FXML
    private TableColumn<Donator, String> tableDonatoriColumnPhone;

    //-----------------------------------------------------------------------------------

    //-----------------------TABELA CAZURI-----------------------------------------------


    @FXML
    private TableView<Caz> tableViewCazuri;

    @FXML
    private TableColumn<Caz,String> tableCazuriColumnName;

    @FXML
    private TableColumn<Caz, Float> tableCazuriColumnTotalSum;

    //-----------------------------------------------------------------------------------



    void initTables() throws Exception {
        tableDonatoriColumnFirstName.setCellValueFactory(new PropertyValueFactory<Donator, String>("firstname"));
        tableDonatoriColumnLastName.setCellValueFactory(new PropertyValueFactory<Donator, String>("lastname"));
        tableDonatoriColumnAdress.setCellValueFactory(new PropertyValueFactory<Donator, String>("adress"));
        tableDonatoriColumnPhone.setCellValueFactory(new PropertyValueFactory<Donator, String>("phone"));
        modelDonatori.setAll(StreamSupport.stream(service.findAllDonatori().spliterator(),false).collect(Collectors.toList()));
        tableViewDonatori.setItems(modelDonatori);

        tableCazuriColumnName.setCellValueFactory(new PropertyValueFactory<Caz, String>("name"));
        tableCazuriColumnTotalSum.setCellValueFactory(new PropertyValueFactory<Caz, Float>("totalSum"));
        modelCazuri.setAll(StreamSupport.stream(service.findAllCazuri().spliterator(),false).collect(Collectors.toList()));
        tableViewCazuri.setItems(modelCazuri);

        textFieldSearch.textProperty().addListener(x-> {
            try {
                handleFilterDonors();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }



    public void setService(IService service,Long id) throws Exception {
        this.service=service;
        this.userID=id;
        initTables();

    }

    public void setStage(Stage newStage){
        this.stage=newStage;
    }


    void reloadTableDonatori() throws Exception {
        tableViewDonatori.getItems().clear();
        modelDonatori.setAll(StreamSupport.stream(service.findAllDonatori().spliterator(),false).collect(Collectors.toList()));
        tableViewDonatori.setItems(modelDonatori);

    }

    @FXML
    void handleAddDonation(ActionEvent event) {
        try
        {
            String firstName = textFieldFirstName.getText();
            String lastName = textFieldLastName.getText();
            String adress = textFieldAdress.getText();
            String phone = textFieldPhone.getText();
            Float donation_value = Float.parseFloat(textFieldValue.getText());
            Long idCaz = tableViewCazuri.getSelectionModel().getSelectedItem().getId();
            Donator donator = new Donator(firstName, lastName, adress, phone);
            Donator foundDonator = service.findByPhone(phone);
            if (foundDonator == null)
            {   service.adaugaDonator(donator);
                foundDonator = service.findByPhone(phone);
                reloadTableDonatori();
            }
            Caz caz = service.findCaz(idCaz);
            service.modificaCaz(new Caz(caz.getName(), caz.getTotalSum() + donation_value), idCaz);
            Donatie donatie = new Donatie(idCaz,donation_value, foundDonator.getId());
            service.adaugaDonatie(donatie);
            textFieldAdress.clear();
            textFieldPhone.clear();
            textFieldValue.clear();
            textFieldFirstName.clear();
            textFieldLastName.clear();


        }
        catch (Exception ex)
        {
            MessageAlert.showErrorMessage(null,"Invalid or incomplete data!");
        }



    }

    @FXML
    void handleFillAreas(MouseEvent event) throws Exception {
        Long idDonator = tableViewDonatori.getSelectionModel().getSelectedItem().getId();
        Donator donator = service.findDonator(idDonator);
        textFieldAdress.setText(donator.getAdress());
        textFieldPhone.setText(donator.getPhone());
        textFieldLastName.setText(donator.getLastname());
        textFieldFirstName.setText(donator.getFirstname());

    }

    @FXML
    void handleFilterDonors() throws Exception {
        String name = textFieldSearch.getText();
        String[] namefull = name.split(" ");
        String firstname="";
        String lastname="";
        if (namefull.length == 2)
        {
            firstname = namefull[0];
            lastname = namefull[1];
        }

        if (namefull.length == 1)
        {
            firstname = namefull[0];
            lastname = "";
        }
        List<Donator> donatorsList=StreamSupport.stream(service.findByName(firstname,lastname).spliterator(),false).collect(Collectors.toList());
        modelDonatori.setAll(donatorsList);
        tableViewDonatori.setItems(modelDonatori);

        }

    @FXML
    void handleLogOut(ActionEvent event) throws Exception {
        Stage currentStage=(Stage)textFieldSearch.getScene().getWindow();//workaround pentru obitnerea scenei curente :P
        service.logout(userID);
        currentStage.close();
        stage.show();

    }



    @Override
    public void notifyAddedDonation(Iterable<Caz> cazuri) throws Exception {
        tableViewCazuri.getItems().clear();
        modelCazuri.setAll(StreamSupport.stream(cazuri.spliterator(),false).collect(Collectors.toList()));
        tableViewCazuri.setItems(modelCazuri);
    }

    @Override
    public void notifyAddedDonor(Iterable<Donator> donatori) throws Exception {
        tableViewDonatori.getItems().clear();
        modelDonatori.setAll(StreamSupport.stream(donatori.spliterator(),false).collect(Collectors.toList()));
        tableViewDonatori.setItems(modelDonatori);

    }


}



