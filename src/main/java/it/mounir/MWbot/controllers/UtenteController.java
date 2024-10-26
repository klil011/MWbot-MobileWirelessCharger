package it.mounir.MWbot.controllers;

import it.mounir.MWbot.model.Utente;
import it.mounir.MWbot.services.UtenteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UtenteController {

    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @PostMapping("/user")
    public Utente createUser(@RequestBody Utente utente) {
        return utenteService.createOrUpdateAccount(utente);
    }

    @GetMapping("/users")
    public Iterable<Utente> getAllUsers() {
        return utenteService.getAllUsers();
    }
}
