package io.squid.CyStation.controller;

import io.squid.CyStation.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ActivationController {

    private final UserService userService;

    public ActivationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/activate")
    public String activateAccount(@RequestParam("token") String token, Model model) {
        boolean success = userService.activateAccount(token);

        if (success) {
            model.addAttribute("message", "Votre compte a bien été activé ! Vous pouvez maintenant vous connecter.");
            return "public/activation-success";
        } else {
            model.addAttribute("message", "Lien d'activation invalide ou déjà utilisé.");
            return "public/activation-error";
        }
    }
}