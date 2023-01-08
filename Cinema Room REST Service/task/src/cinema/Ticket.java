package cinema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.UUID;

@Data
public class Ticket {

    private UUID token = UUID.randomUUID();
    private LinkedHashMap<String, Integer> ticketInfo = new LinkedHashMap<>();
    private boolean available = true;

    @JsonCreator
    public Ticket(@JsonProperty("row") int row, @JsonProperty("column") int column) {
        this.ticketInfo.put("row", row);
        this.ticketInfo.put("column", column);
        this.ticketInfo.put("price", row <= 4 ? 10 : 8);
    }

    @JsonProperty("ticket")
    public LinkedHashMap<String, Integer> getTicketInfo() {
        return ticketInfo;
    }

    @JsonIgnore
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return ticketInfo.get("row").equals(ticket.ticketInfo.get("row"))
                && ticketInfo.get("column").equals(ticket.ticketInfo.get("column"));
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketInfo.get("row"), ticketInfo.get("column"));
    }
}
