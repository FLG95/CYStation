package io.squid.CyStation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {


    @RequestMapping("/")
    public String index(){
        return "public/index";
    }

    @RequestMapping("/about")
    public String about(){
        return "public/about";
    }

}
