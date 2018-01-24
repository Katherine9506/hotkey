package com.myproject.hotkey.controller;

import com.myproject.hotkey.pojo.Key;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelloController {


    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String sayHelloForm(Model model) {
        model.addAttribute("key", new Key());
        return "index";
    }

    @RequestMapping(value = "/result", method = RequestMethod.POST)
    public String sayHello(@ModelAttribute Key key, Model model) {
        model.addAttribute("key", key);
        return "message";
    }


}
