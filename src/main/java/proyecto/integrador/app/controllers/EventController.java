package proyecto.integrador.app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.integrador.app.dto.response.SuccessResponse;
import proyecto.integrador.app.entities.Event;
import proyecto.integrador.app.services.event.EventService;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventService.save(event));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<Event>> getEvent(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        SuccessResponse<Event> response = new SuccessResponse<>(true,event);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        return ResponseEntity.ok(eventService.updateEvent(id, event));
    }
}
