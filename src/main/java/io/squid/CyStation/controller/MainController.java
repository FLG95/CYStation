package io.squid.CyStation.controller;

import io.squid.CyStation.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

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
