package controller;

import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tm.StudentsDetailsTM;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class StudentDetailsFormController {
    public Label lblSmsWelcome;
    public Label lblSmsId;
    public AnchorPane root;
    public TableView<StudentsDetailsTM> tblViewStudentDetails;
    public ListView<StudentsDetailsTM> lstStudentDetails;
    

    public TextField txtName;
    public TextField txtAge;
    public TextField txtGrade;
    public TextField txtCity;
    public Button btnUpdate;
    public Button btnDelete;
    public Label lblStudentId;


    public void initialize(){
        lblSmsId.setText(LoginFormController.enteredUserId);
        lblSmsWelcome.setText("Hello " + LoginFormController.enteredUserName+ " Welcome to Student Management System");
        loadList();
        tblViewStudentDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblViewStudentDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("user_id"));
        tblViewStudentDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblViewStudentDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("age"));
        tblViewStudentDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("grade"));
        tblViewStudentDetails.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("city"));


        txtName.setDisable(true);
        txtAge.setDisable(true);
        txtGrade.setDisable(true);
        txtCity.setDisable(true);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        lblStudentId.setDisable(true);
        tblViewStudentDetails.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                txtName.setDisable(false);
                txtAge.setDisable(false);
                txtGrade.setDisable(false);
                txtCity.setDisable(false);
                btnUpdate.setDisable(false);
                btnDelete.setDisable(false);
                lblStudentId.setDisable(false);
                StudentsDetailsTM selectedItem = tblViewStudentDetails.getSelectionModel().getSelectedItem();
                String id = selectedItem.getId();
                lblStudentId.setText(id);
                String name = selectedItem.getName();
                txtName.setText(name);
                int age = selectedItem.getAge();
                txtAge.setText(String.valueOf(age));
                int grade = selectedItem.getGrade();
                txtGrade.setText(String.valueOf(grade));
                String city = selectedItem.getCity();
                txtCity.setText(city);


            }
        });
    }

    public void btnSmsAddNewStudent(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("../view/AddStudent.fxml"));
        Scene scene = new Scene(parent);
        Stage primaryStage = (Stage) this.root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Add New Student");
        primaryStage.centerOnScreen();
    }

    public void btnLogOutOnAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to Log out", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().equals(ButtonType.YES)){
            Parent parent = FXMLLoader.load(this.getClass().getResource("../view/LoginForm.fxml"));
            Scene scene = new Scene(parent);
            Stage primaryStage = (Stage)this.root.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login Form");
            primaryStage.centerOnScreen();
        }
    }

    public void loadList(){
        Connection connection = DBConnection.getInstance().getConnection();

        ObservableList<StudentsDetailsTM> items = tblViewStudentDetails.getItems();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * FROM students WHERE user_id=?");
            preparedStatement.setObject(1,LoginFormController.enteredUserId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(1);
                String user_id = resultSet.getString(2);
                String name = resultSet.getString(3);
                int age = resultSet.getInt(4);
                int grade = resultSet.getInt(5);
                String city = resultSet.getString(6);

                StudentsDetailsTM studentsDetailsTM = new StudentsDetailsTM(id,user_id,name,age,grade,city);

                items.add(studentsDetailsTM);

            }
            tblViewStudentDetails.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String text = txtName.getText();
        String text1 = txtGrade.getText();
        String text2 = txtCity.getText();
        String text3 = txtAge.getText();
        String text4 = lblStudentId.getText();
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update students set name=?, age=?, grade=? , city=? WHERE id = ?");
            preparedStatement.setObject(1,text);
            preparedStatement.setObject(2,text3);
            preparedStatement.setObject(3,text1);
            preparedStatement.setObject(4,text2);
            preparedStatement.setObject(5,text4);
            preparedStatement.executeUpdate();

            txtName.clear();
            txtAge.clear();
            txtCity.clear();
            txtGrade.clear();
            lblStudentId.setText("");
            txtName.setDisable(true);
            txtAge.setDisable(true);
            txtGrade.setDisable(true);
            txtCity.setDisable(true);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
            lblStudentId.setDisable(true);

            Parent parent = FXMLLoader.load(this.getClass().getResource("../view/StudentDetailsForm.fxml"));
            Scene scene = new Scene(parent);
            Stage primaryStage = (Stage)this.root.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login Form");
            primaryStage.centerOnScreen();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }


    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String text = txtName.getText();
        String text1 = txtGrade.getText();
        String text2 = txtCity.getText();
        String text3 = txtAge.getText();
        String text4 = lblStudentId.getText();
        Connection connection = DBConnection.getInstance().getConnection();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure ???", ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> buttonType = alert.showAndWait();
        if(buttonType.get().equals(ButtonType.YES)){
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("delete from students where id = ?;");
                preparedStatement.setObject(1,text4);
                preparedStatement.executeUpdate();

                txtName.clear();
                txtAge.clear();
                txtCity.clear();
                txtGrade.clear();
                lblStudentId.setText("");
                txtName.setDisable(true);
                txtAge.setDisable(true);
                txtGrade.setDisable(true);
                txtCity.setDisable(true);
                btnUpdate.setDisable(true);
                btnDelete.setDisable(true);
                lblStudentId.setDisable(true);

                Parent parent = FXMLLoader.load(this.getClass().getResource("../view/StudentDetailsForm.fxml"));
                Scene scene = new Scene(parent);
                Stage primaryStage = (Stage)this.root.getScene().getWindow();
                primaryStage.setScene(scene);
                primaryStage.setTitle("Login Form");
                primaryStage.centerOnScreen();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void btnOnActionCheckPayments(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("../view/CheckPayments.fxml"));
        Scene scene = new Scene(parent);
        Stage primaryStage = (Stage)this.root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Payment");
        primaryStage.centerOnScreen();
    }
}
