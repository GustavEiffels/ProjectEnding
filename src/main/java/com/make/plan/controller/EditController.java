package com.make.plan.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/edit")
public class EditController {

    @GetMapping("")
    public String editMain()
    {
        return "/edit/editmain";
    }

    @GetMapping("/unsubscribe")
    public void unsubscribe(){};

    @GetMapping("/edit_info")
    public void editInfo(){};
}
