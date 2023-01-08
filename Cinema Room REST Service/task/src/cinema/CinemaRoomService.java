package cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CinemaRoomService {

    @Autowired
    private CinemaRoom cinemaRoom;

    public CinemaRoom getAvailableSeatsInfo() {
        return cinemaRoom;
    }

    public ResponseEntity<?> purchaseTicket(Ticket ticket) {
        ResponseEntity<?> response = getTicketAvailability(ticket);
        if (response.getStatusCode().is2xxSuccessful()) {
            cinemaRoom.getAllTickets().stream()
                    .filter(s -> s.equals(ticket))
                    .forEach(s -> s.setAvailable(false));
        }
        return response;
    }

    public ResponseEntity<?> getTicketAvailability(Ticket ticket) {
        ResponseEntity<?> ticketInfo;
        Optional<Ticket> ticketOptional = cinemaRoom.getAllTickets().stream()
                .filter(s -> s.equals(ticket))
                .findFirst();

        if (ticketOptional.isEmpty()) {
            ticketInfo = new ResponseEntity<>(Map.of("error",
                    "The number of a row or a column is out of bounds!"),
                    HttpStatus.BAD_REQUEST);
        } else if (!ticketOptional.get().isAvailable()) {
            ticketInfo = new ResponseEntity<>(Map.of("error",
                    "The ticket has been already purchased!"),
                    HttpStatus.BAD_REQUEST);
        } else {
            ticketInfo = new ResponseEntity<>(ticketOptional.get(), HttpStatus.OK);
        }
        return ticketInfo;
    }

    public ResponseEntity<?> returnTicket(Map<String, String> token) {
        Optional<Ticket> ticketOptional = cinemaRoom.getAllTickets().stream()
                .filter(s -> s.getToken().toString().equals(token.get("token")))
                .findFirst();

        if (ticketOptional.isEmpty()) {
            return new ResponseEntity<>(Map.of("error",
                    "Wrong token!"),
                    HttpStatus.BAD_REQUEST);
        }

        ticketOptional.get().setAvailable(true);

        return new ResponseEntity<>(Map.of("returned_ticket",
                ticketOptional.get().getTicketInfo()),
                HttpStatus.OK);

    }

    public ResponseEntity<?> getStats(String password) {
        LinkedHashMap<String, Integer> stats = new LinkedHashMap<>();
        List<Ticket> purchasedTickets = cinemaRoom.getAllTickets().stream()
                .filter(s -> !s.isAvailable()).toList();

        stats.put("current_income", purchasedTickets.stream()
                .map(t -> t.getTicketInfo().get("price"))
                .reduce(0, Integer::sum));
        stats.put("number_of_available_seats", cinemaRoom.getAvailableSeats().size());
        stats.put("number_of_purchased_tickets", purchasedTickets.size());

        if (password == null || !password.equals("super_secret")) {
            return new ResponseEntity<>(Map.of("error", "The password is wrong!"),
                    HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(stats, HttpStatus.OK);
        }
    }
}
