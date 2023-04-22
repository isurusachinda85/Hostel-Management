package lk.ijse.hostelManagementSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Room {
    @Id
    private String roomId;
    private String type;
    private double monthlyRent;
    private int roomsQty;
    private int availableQty;
    private LocalDate addDate;
    @OneToMany(mappedBy = "room" ,fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Reserve>reserveList = new ArrayList<>();

    public Room(String roomId, String type, double monthlyRent, int roomsQty, int availableQty, LocalDate addDate) {
        this.roomId = roomId;
        this.type = type;
        this.monthlyRent = monthlyRent;
        this.roomsQty = roomsQty;
        this.availableQty = availableQty;
        this.addDate = addDate;
    }
}
