package io.squid.CyStation.controller;

import io.squid.CyStation.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
public class MainController {


    @RequestMapping({"/" , "/index"})
    public String index(){
        return "public/index";
    }

    @RequestMapping("/about")
    public String about(){
        return "public/about";
    }

    @RequestMapping("/reactor")
    public String reactor(){ return "engineer/reactor";}

    @RequestMapping("/hydroponic")
    public String hydroponic(){ return "engineer/hydroponic";}

    @RequestMapping("/telemetry")
    public String telemetry(){ return "engineer/telemetry";}

    @RequestMapping("/astrometry")
    public String astrometry(){ return "engineer/astrometry";}

    @RequestMapping("/mission")
    public String mission() {
        return "public/mission";
    }

    @RequestMapping("/control")
    public String control() {
        return "public/control";
    }

}
