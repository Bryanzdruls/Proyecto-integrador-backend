package proyecto.integrador.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.integrador.app.dto.response.ReportResponseDTO;
import proyecto.integrador.app.dto.response.SuccessResponse;
import proyecto.integrador.app.entities.User;
import proyecto.integrador.app.services.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<SuccessResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        SuccessResponse<List<User>> response = new SuccessResponse<>(true, users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Obtener un usuario por ID
    @GetMapping("/id/{id}")
    public ResponseEntity<SuccessResponse<User>> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        SuccessResponse<User> response = new SuccessResponse<>(true, user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<SuccessResponse<User>> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        SuccessResponse<User> response = new SuccessResponse<>(true, user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    // Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(201).body(createdUser);
    }

    // Actualizar un usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<User>> updateUser(@PathVariable Integer id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        SuccessResponse<User> response = new SuccessResponse<>(true, updatedUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Actualizar puntaje de un usuario
    @PatchMapping("/{id}/score")
    public ResponseEntity<User> updateUserScore(@PathVariable Integer id, @RequestParam Integer score) {
        User updatedUser = userService.updateUserScore(id, score);
        return ResponseEntity.ok(updatedUser);
    }
    @GetMapping("/generate-xml-report")
    public ResponseEntity<Resource> generateReport() {
        InputStreamResource resource = userService.generateXmlReport();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=usuarios_reporte.xml")
                .contentType(MediaType.APPLICATION_XML)
                .body(resource);
    }
}
