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
import lk.ijse.hostelManagementSystem.bo.custom.impl.RoomBOImpl;
import lk.ijse.hostelManagementSystem.bo.custom.RoomBO;
import lk.ijse.hostelManagementSystem.dto.RoomDTO;
import lk.ijse.hostelManagementSystem.entity.Room;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class RoomManageFormController implements Initializable {

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXComboBox<String> cmbRoomType;

    @FXML
    private JFXTextField txtMonthlyRent;

    @FXML
    private JFXTextField txtqty;

    @FXML
    private TableView<RoomDTO> tblRoom;

    @FXML
    private TableColumn<?, ?> colRoomId;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableColumn<?, ?> colMonthlyRent;

    @FXML
    private TableColumn<?, ?> colRoomQty;

    @FXML
    private TableColumn<?, ?> colAvailableQty;

    @FXML
    private TableColumn<?, ?> colAddDate;

    @FXML
    private JFXDatePicker txtAddDate;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    private final RoomBO roomBO = (RoomBO) BOFactory.getBoFactory().getBO(BOFactory.BOType.ROOM);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTimeAndDate();
        setCmbRoomType();
        getAllRoom();
        setCellValueFactory();
    }

    public void setCellValueFactory() {
        colRoomId.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colMonthlyRent.setCellValueFactory(new PropertyValueFactory<>("monthlyRent"));
        colRoomQty.setCellValueFactory(new PropertyValueFactory<>("roomsQty"));
        colAvailableQty.setCellValueFactory(new PropertyValueFactory<>("availableQty"));
        colAddDate.setCellValueFactory(new PropertyValueFactory<>("addDate"));
    }

    @FXML
    void saveOnAction(ActionEvent event) {
        String id = txtId.getText();
        String type = cmbRoomType.getValue();
        double rent = Double.parseDouble(txtMonthlyRent.getText());
        int roomQty = Integer.parseInt(txtqty.getText());
        int availabelQty = Integer.parseInt(txtqty.getText());
        LocalDate addDate = txtAddDate.getValue();


        try {
            boolean saveRoom = roomBO.saveRoom(new RoomDTO(id, type, rent, roomQty, availabelQty, addDate));
            if (saveRoom) {
                new Alert(Alert.AlertType.INFORMATION, "Save Room !").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Not Save Room !").show();
            }
            getAllRoom();
        } catch (IOException e) {
            System.out.println(e);
        }
        clearOnAction(event);
    }

    @FXML
    void searchOnAction(ActionEvent event) {
        String id = txtId.getText();
        try {
            RoomDTO room = roomBO.searchRoom(id);
            if (room != null) {
                txtMonthlyRent.setText(String.valueOf(room.getMonthlyRent()));
                cmbRoomType.setValue(room.getType());
                txtqty.setText(String.valueOf(room.getRoomsQty()));
                txtAddDate.setValue(room.getAddDate());
            } else {
                new Alert(Alert.AlertType.WARNING, "Not Found Room !").show();
            }

        } catch (IOException e) {

        }

    }

    @FXML
    void updateOnAction(ActionEvent event) {
        String id = txtId.getText();
        String type = cmbRoomType.getValue();
        double rent = Double.parseDouble(txtMonthlyRent.getText());
        int roomQty = Integer.parseInt(txtqty.getText());
        int availabelQty = Integer.parseInt(txtqty.getText());
        LocalDate addDate = txtAddDate.getValue();

        try {
            boolean updateRoom = roomBO.updateRoom(new RoomDTO(id, type, rent, roomQty, availabelQty, addDate));
            if (updateRoom) {
                new Alert(Alert.AlertType.INFORMATION, "Updated !").show();
            }
            getAllRoom();
        } catch (IOException e) {
            System.out.println(e);
        }
        clearOnAction(event);
    }

    @FXML
    void deleteOnAction(ActionEvent event) {
        String id = txtId.getText();

        try {
            boolean deleteRoom = roomBO.deleteRoom(id);
            if (deleteRoom) {
                new Alert(Alert.AlertType.INFORMATION, "Delete Room !").show();
            }
            getAllRoom();
        } catch (IOException e) {
            System.out.println(e);
        }

        clearOnAction(event);
    }

    public void getAllRoom() {
        ObservableList<RoomDTO> roomList = FXCollections.observableArrayList();

        roomList.clear();

        try {
            List<RoomDTO> list = roomBO.getAllRoom();
            for (RoomDTO roomDTO : list) {
                roomList.add(roomDTO);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        tblRoom.setItems(roomList);
    }

    public void setCmbRoomType() {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll("Non-AC", "Non-AC/Food", "AC", "AC/Food");
        cmbRoomType.setItems(list);
    }

    @FXML
    void clearOnAction(ActionEvent event) {
        txtId.clear();
        txtMonthlyRent.clear();
        cmbRoomType.getSelectionModel().clearSelection();
        txtqty.clear();
        txtAddDate.getEditor().clear();
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
