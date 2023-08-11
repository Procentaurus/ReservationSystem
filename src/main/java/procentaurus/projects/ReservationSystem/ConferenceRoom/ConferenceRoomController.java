package procentaurus.projects.ReservationSystem.ConferenceRoom;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import procentaurus.projects.ReservationSystem.ConferenceRoom.Interfaces.ConferenceRoomControllerInterface;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/conference_rooms")
public class ConferenceRoomController implements ConferenceRoomControllerInterface {

    private final ConferenceRoomService conferenceRoomService;

    @Autowired
    public ConferenceRoomController(ConferenceRoomService conferenceRoomService) {
        this.conferenceRoomService = conferenceRoomService;
    }

    @Override
    @GetMapping(path = "/{number}")
    public ResponseEntity<?> findSingleConferenceRoom(@PathVariable int number) {
        Optional<ConferenceRoom> found =  conferenceRoomService.findSingleConferenceRoom(number);
        if(found.isPresent()) return ResponseEntity.ok(found);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No conference room of provided number.");
    }

    @Override
    @GetMapping("/")
    public ResponseEntity<List<ConferenceRoom>> findConferenceRooms(Map<String, String> params) {
        List<ConferenceRoom> found = conferenceRoomService.findConferenceRooms(params);
        return ResponseEntity.ok(found);
    }

    @Override
    @DeleteMapping(path = "/{number}")
    public ResponseEntity<?> deleteConferenceRoom(int number) {
        boolean success = conferenceRoomService.deleteConferenceRoom(number);
        if(success) return ResponseEntity.noContent().build();
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No conference room of provided number.");
    }

    @Override
    @PutMapping(path = "/{number}")
    public ResponseEntity<?> updateConferenceRoom(int number, Map<String, String> params) {
        Optional<ConferenceRoom> updated = conferenceRoomService.updateConferenceRoom(number, params);

        if(updated.isPresent()) return ResponseEntity.ok(updated);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No conference room of provided number or wrong params.");
    }

    @Override
    @PostMapping(path = "/")
    public ResponseEntity<?> createConferenceRoom(ConferenceRoom conferenceRoom) {
        Optional<ConferenceRoom> created = conferenceRoomService.createConferenceRoom(conferenceRoom);

        if(created.isPresent()) return ResponseEntity.status(HttpStatus.CREATED).body(created);
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong data passed.");
    }
}