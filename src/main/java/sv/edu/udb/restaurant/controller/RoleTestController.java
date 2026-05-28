package sv.edu.udb.restaurant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sv.edu.udb.restaurant.dto.MessageResponse;

@RestController
@RequestMapping("/api")
public class RoleTestController {

    @GetMapping("/admin/test")
    public MessageResponse adminTest() {
        return new MessageResponse("Acceso administrador correcto");
    }

    @GetMapping("/mesero/test")
    public MessageResponse meseroTest() {
        return new MessageResponse("Acceso mesero correcto");
    }

    @GetMapping("/usuario/test")
    public MessageResponse usuarioTest() {
        return new MessageResponse("Acceso usuario autenticado correcto");
    }
}
