package io.squid.CyStation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
