package com.ex.controllers;

import com.ex.entity.History;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

//@Controller
//
//public class HistoryController {
//
//    @RequestMapping(value ="/xmlHistory" , consumes = "text/xml")
////    History getHistory() throws IOException {
//    public String xmlHistory(Model model) throws IOException {
//
//        History history = new History();
//        history.setData("I hope work");
//        history.setGameNumber(0);
//        model.addAttribute("xml",history);
//        return "xmlHistory";
//    }
//
//
//}
@Controller
@RequestMapping("/user")
public class HistoryController {

    @RequestMapping(value="{name}", method = RequestMethod.GET)
    public @ResponseBody History getUser(@PathVariable String name) {

        History user = new History();

        user.setData(name);
        user.setId(1);
        return user;
    }
}
