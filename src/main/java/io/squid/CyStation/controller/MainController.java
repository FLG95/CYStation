package io.squid.CyStation.controller;

import io.squid.CyStation.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    private final UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping({"/" , "/index"})
    public String index(){
        return "public/index";
    }

    @RequestMapping("/about")
    public String about(Model model){
        model.addAttribute("usersList", userService.getAllUsers());
        return "public/about";
    }

    @RequestMapping("/reactor")
    public String reactor(){ return "engineer/reactor";}

    @RequestMapping("/hydroponic")
    public String hydroponic(){ return "engineer/hydroponic";}

    @RequestMapping("/telemetry")
    public String telemetry(){ return "radio";}

    @RequestMapping("/astrometry")
    public String astrometry(){ return "radar";}

    @RequestMapping("/mission")
    public String mission() {
        return "public/mission";
    }

    @RequestMapping("/control")
    public String control() {
        return "public/control";
    }

}