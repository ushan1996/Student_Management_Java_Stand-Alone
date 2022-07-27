package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class CreateNewAccountFormController {
    public TextField txtUserName;
    public TextField txtEmail;
    public Button btnRegister;
    public PasswordField txtNewPassword;
    public PasswordField txtConformPassword;
    public Label lblUserId;
    public Label lblConformPassword;
    public Label lblNewPassword;
    public AnchorPane root;


    public void initialize(){
        txtUserName.setDisable(true);
        txtEmail.setDisable(true);
        txtNewPassword.setDisable(true);
        txtConformPassword.setDisable(true);
        btnRegister.setDisable(true);
        lblNewPassword.setVisible(false);
        lblConformPassword.setVisible(false);
    }
    public void btnAddNewUserOnAction(ActionEvent actionEvent)  {
        txtUserName.setDisable(false);
        txtEmail.setDisable(false);
        txtNewPassword.setDisable(false);
        txtConformPassword.setDisable(false);
        btnRegister.setDisable(false);
        txtUserName.requestFocus();

        autoGenarateID();

    }
    public void autoGenarateID(){
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM user Order by id desc LIMIT 1");
            boolean isExist = resultSet.next();
            if(isExist){
                String oldId = resultSet.getString(1);
                int length = oldId.length();
                String id = oldId.substring(1, length);
                int intId  = Integer.parseInt(id);
                intId = intId+1;
                if (intId<10){
                    lblUserId.setText("U00"+intId);
                }else if (intId<100){
                    lblUserId.setText("U0"+intId);
                }else{
                    lblUserId.setText("U"+intId);
                }


            }else{
                lblUserId.setText("U001");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void btnRegisterOnAction(ActionEvent actionEvent) {
        String newpassword = txtNewPassword.getText();
        String conformpassword = txtConformPassword.getText();
        boolean isEquals = newpassword.equals(conformpassword);
        if (isEquals){
            txtNewPassword.setStyle("-fx-border-color: transparent");
            txtConformPassword.setStyle("-fx-border-color: transparent");
            lblNewPassword.setVisible(false);
            lblConformPassword.setVisible(false);
            register();
        }else{
            txtNewPassword.setStyle("-fx-border-color: red");
            txtConformPassword.setStyle("-fx-border-color: red");
            txtNewPassword.requestFocus();
            lblNewPassword.setVisible(true);
            lblConformPassword.setVisible(true);
        }


    }

    public void register(){
        String id = lblUserId.getText();
        String userName = txtUserName.getText();
        String email = txtEmail.getText();
        String password = txtConformPassword.getText();
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user values(?,?,?,?) ");
            preparedStatement.setObject(1,id);
            preparedStatement.setObject(2,userName);
            preparedStatement.setObject(3,password);
            preparedStatement.setObject(4,email);
            int i = preparedStatement.executeUpdate();
            if(i != 0){
                new Alert(Alert.AlertType.CONFIRMATION,"Successfully Registed").showAndWait();
                Parent parent = FXMLLoader.load(this.getClass().getResource("../view/LoginForm.fxml"));
                Scene scene = new Scene(parent);
                Stage primaryStage = (Stage) this.root.getScene().getWindow();
                primaryStage.setScene(scene);
                primaryStage.setTitle("Login Form");
                primaryStage.centerOnScreen();
            }else{
                new Alert(Alert.AlertType.ERROR,"Error").showAndWait();
            }


        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("../view/LoginForm.fxml"));
        Scene scene = new Scene(parent);
        Stage primaryStage = (Stage)this.root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login Form");
        primaryStage.centerOnScreen();
    }
}
