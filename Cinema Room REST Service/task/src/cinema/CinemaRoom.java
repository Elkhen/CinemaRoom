package cinema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
public class CinemaRoom {
    private int totalRows;
    private int totalColumns;
    private ArrayList<Ticket> allTickets;

    public CinemaRoom() {
        this.totalRows = 9;
        this.totalColumns = 9;
        this.allTickets = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                allTickets.add(new Ticket(i, j));
            }
        }
    }

    @JsonIgnore
    public ArrayList<Ticket> getAllTickets() {
        return allTickets;
    }

    @JsonProperty("total_rows")
    public int getTotalRows() {
        return totalRows;
    }

    @JsonProperty("total_columns")
    public int getTotalColumns() {
        return totalColumns;
    }

    @JsonProperty("available_seats")
    public List<LinkedHashMap<String, Integer>> getAvailableSeats() {
        return allTickets.stream().filter(Ticket::isAvailable).map(Ticket::getTicketInfo).toList();
    }

}
