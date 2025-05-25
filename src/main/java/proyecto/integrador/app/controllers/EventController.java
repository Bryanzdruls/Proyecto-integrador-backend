package proyecto.integrador.app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.integrador.app.dto.response.SuccessResponse;
import proyecto.integrador.app.entities.Event;
import proyecto.integrador.app.services.event.EventService;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventService.save(event));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<Event>>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        SuccessResponse<List<Event>> response = new SuccessResponse<>(true, events);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<Event>> getEvent(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        SuccessResponse<Event> response = new SuccessResponse<>(true,event);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<Event>> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        Event eventUpdated =eventService.updateEvent(id, event);
        SuccessResponse<Event> response = new SuccessResponse<>(true,eventUpdated);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
