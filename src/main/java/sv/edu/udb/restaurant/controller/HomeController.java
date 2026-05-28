package sv.edu.udb.restaurant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sv.edu.udb.restaurant.dto.MessageResponse;

@RestController
public class HomeController {

    @GetMapping("/")
    public MessageResponse home() {
        return new MessageResponse("Restaurant API funcionando");
    }
}
