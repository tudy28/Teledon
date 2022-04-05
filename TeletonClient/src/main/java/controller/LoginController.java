package controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Voluntar;
import teleton.service.IService;

import java.io.IOException;

public class LoginController {
    IService service;

    public void setService(IService service) {
        this.service=service;

    }
    @FXML
    private Button buttonLogIn;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private TextField textFieldPassword;


    @FXML
    void handleLogIn(ActionEvent event) throws Exception {
        String username=textFieldUsername.getText();
        String password=textFieldPassword.getText();
        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/userView.fxml"));
        Stage userStage=new Stage();
        AnchorPane root = loader.load();
        UserController ctrl = loader.getController();
        Voluntar voluntar=service.checkBypass(username, password,ctrl);
        if (service.checkBypass(username, password,ctrl) != null)
        {
            try {
                //setup
                Scene scene = new Scene(root);
                userStage.setScene(scene);
                userStage.setTitle("Teledon");
                userStage.show();
                Stage currentStage=(Stage)buttonLogIn.getScene().getWindow();//workaround pentru obitnerea scenei curente :P
                currentStage.close();
                ctrl.setStage(currentStage);
                ctrl.setService(service, voluntar.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            MessageAlert.showErrorMessage(null,"Invalid credentials");
        }
    }


}
