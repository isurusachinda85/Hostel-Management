package lk.ijse.hostelManagementSystem.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import lk.ijse.hostelManagementSystem.bo.BOFactory;
import lk.ijse.hostelManagementSystem.bo.custom.StudentBO;
import lk.ijse.hostelManagementSystem.bo.custom.impl.StudentBOImpl;
import lk.ijse.hostelManagementSystem.dto.StudentDTO;
import lk.ijse.hostelManagementSystem.entity.Student;
import lk.ijse.hostelManagementSystem.util.FactoryConfiguration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class StudentManageFormController implements Initializable {
    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtContact;

    @FXML
    private JFXDatePicker txtDob;

    @FXML
    private JFXComboBox<String> cmbGender;

    @FXML
    private TableView<StudentDTO> tblStudent;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colContact;

    @FXML
    private TableColumn<?, ?> colDob;

    @FXML
    private TableColumn<?, ?> colGender;

    @FXML
    private TableColumn<?, ?> colRegisterDate;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    private final StudentBO studentBO = (StudentBO) BOFactory.getBoFactory().getBO(BOFactory.BOType.STUDENT);


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCmbGender();
        loadTimeAndDate();
        getAllStudent();
        setCellValueFactory();
    }

    public void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("sid"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colRegisterDate.setCellValueFactory(new PropertyValueFactory<>("registerDate"));
    }

    @FXML
    void saveOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String contact = txtContact.getText();
        LocalDate dob = txtDob.getValue();
        String gender = cmbGender.getValue();
        LocalDate registerDate = LocalDate.parse(lblDate.getText());

        try {
            boolean saveStudent = studentBO.saveStudent(new StudentDTO(id, name, address, contact, dob, gender, registerDate));
            if (saveStudent) {
                new Alert(Alert.AlertType.INFORMATION, "Save Student !").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Not Save Student !").show();
            }
            getAllStudent();
        } catch (IOException e) {
            System.out.println(e);
        }

        clearTextOnAction(event);

    }

    @FXML
    void searchOnAction(ActionEvent event) {
        String id = txtId.getText();

        try {
            StudentDTO student = studentBO.searchStudent(id);
            if (student != null) {
                txtName.setText(student.getName());
                txtAddress.setText(student.getAddress());
                txtContact.setText(student.getContact());
                txtDob.setValue(student.getDob());
                cmbGender.setValue(student.getGender());
            } else {
                new Alert(Alert.AlertType.WARNING, "Not Found Student !").show();
            }
        } catch (IOException e) {
            System.out.println(e);
        }


        /*tblStudent.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                txtId.setText(newValue.getSid());
                txtName.setText(newValue.getName());
                txtAddress.setText(newValue.getAddress());
                txtContact.setText(newValue.getContact());
                txtDob.setValue(newValue.getDob());
                cmbGender.setValue(newValue.getGender());

                txtId.setDisable(false);
                txtName.setDisable(false);
                txtAddress.setDisable(false);
            }
        });*/
    }

    @FXML
    void updateOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String contact = txtContact.getText();
        LocalDate dob = txtDob.getValue();
        String gender = cmbGender.getValue();
        LocalDate registerDate = LocalDate.parse(lblDate.getText());


        try {
            boolean updateStudent = studentBO.updateStudent(new StudentDTO(id, name, address, contact, dob, gender, registerDate));
            if (updateStudent) {
                new Alert(Alert.AlertType.INFORMATION, "Updated !").show();
            }
            getAllStudent();
        } catch (IOException e) {
            System.out.println(e);
        }

        clearTextOnAction(event);
    }


    @FXML
    void deleteOnAction(ActionEvent event) {
        String id = txtId.getText();

        try {
            boolean deleteStudent = studentBO.deleteStudent(id);
            if (deleteStudent) {
                new Alert(Alert.AlertType.INFORMATION, "Delete Student !").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Not Found Student !").show();
            }
            getAllStudent();
        } catch (IOException e) {
            System.out.println(e);
        }
        clearTextOnAction(event);
    }

    public void getAllStudent() {
        ObservableList<StudentDTO> studentList = FXCollections.observableArrayList();

        studentList.clear();

        try {
            List<StudentDTO> list = studentBO.getAllStudent();
            for (StudentDTO studentDTO : list) {
                studentList.add(studentDTO);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        tblStudent.setItems(studentList);

    }

    @FXML
    void clearTextOnAction(ActionEvent event) {
        txtId.clear();
        txtName.clear();
        txtAddress.clear();
        txtContact.clear();
        cmbGender.getSelectionModel().clearSelection();
        txtDob.getEditor().clear();
    }

    public void setCmbGender() {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll("Male", "Female", "Other");
        cmbGender.setItems(list);
    }

    public void loadTimeAndDate() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        LocalDate date = LocalDate.now();
        lblDate.setText(String.valueOf(date));
    }

}
