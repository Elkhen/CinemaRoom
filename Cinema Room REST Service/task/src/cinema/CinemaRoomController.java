package cinema;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@RestController
public class CinemaRoomController {

    @Autowired
    private CinemaRoomService cinemaRoomService;

    @GetMapping("/seats")
    public CinemaRoom listSeats() {
        return cinemaRoomService.getAvailableSeatsInfo();
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseTicket(@RequestBody Ticket ticket) {
        return cinemaRoomService.purchaseTicket(ticket);
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnTicket(@RequestBody Map<String, String> token) {
        return cinemaRoomService.returnTicket(token);
    }

    @PostMapping("/stats")
    public ResponseEntity<?> stats(@RequestParam(required = false) String password) {
        return cinemaRoomService.getStats(password);
    }
}
