package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class AddStudentController {
    public TextField txtStudentName;
    public TextField txtStudentCity;
    public Button btnAddStudent;
    public TextField txtStudentGrade;
    public TextField txtStudentAge;
    public Label lblStudentId;
    public AnchorPane root;
    public Label lblCheckText;


    public void initialize(){
        txtStudentName.setDisable(true);
        txtStudentCity.setDisable(true);
        btnAddStudent.setDisable(true);
        txtStudentGrade.setDisable(true);
        txtStudentAge.setDisable(true);
        lblCheckText.setVisible(false);
    }

    public void btnOnActionAddNew(ActionEvent actionEvent) {
        txtStudentName.setDisable(false);
        txtStudentCity.setDisable(false);
        btnAddStudent.setDisable(false);
        txtStudentGrade.setDisable(false);
        txtStudentAge.setDisable(false);
        txtStudentName.requestFocus();
        autoGenerateID();
    }

    public void btnOnActionAdd(ActionEvent actionEvent) {
        if (txtStudentName.getText().trim().isEmpty() && txtStudentCity.getText().trim().isEmpty() && txtStudentAge.getText().trim().isEmpty() && txtStudentGrade.getText().trim().isEmpty()){
            lblCheckText.setVisible(true);
            txtStudentName.requestFocus();
        }else{
            lblCheckText.setVisible(false);
            String stdId = lblStudentId.getText();
            String stdName = txtStudentName.getText();
            String stdAge = txtStudentAge.getText();
            String stdGrade = txtStudentGrade.getText();
            String stdCity = txtStudentCity.getText();
            Connection connection = DBConnection.getInstance().getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO students values(?,?,?,?,?,?) ");
                preparedStatement.setObject(1,stdId);
                preparedStatement.setObject(2,LoginFormController.enteredUserId);
                preparedStatement.setObject(3,stdName);
                preparedStatement.setObject(4,stdAge);
                preparedStatement.setObject(5,stdGrade);
                preparedStatement.setObject(6,stdCity);

                int i = preparedStatement.executeUpdate();

                if(i != 0){
                    new Alert(Alert.AlertType.CONFIRMATION,"Successfully Registed").showAndWait();
                    Parent parent = FXMLLoader.load(this.getClass().getResource("../view/StudentDetailsForm.fxml"));
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


    }
    public void autoGenerateID(){
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id FROM students Order by id desc LIMIT 1");
            boolean isExist = resultSet.next();
            if(isExist){
                String oldId = resultSet.getString(1);
                int length = oldId.length();
                String id = oldId.substring(1, length);
                int intId  = Integer.parseInt(id);
                intId = intId+1;
                if (intId<10){
                    lblStudentId.setText("S00"+intId);
                }else if (intId<100){
                    lblStudentId.setText("S0"+intId);
                }else{
                    lblStudentId.setText("S"+intId);
                }


            }else{
                lblStudentId.setText("S001");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("../view/StudentDetailsForm.fxml"));
        Scene scene = new Scene(parent);
        Stage primaryStage = (Stage)this.root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Student Details");
        primaryStage.centerOnScreen();
    }
}
