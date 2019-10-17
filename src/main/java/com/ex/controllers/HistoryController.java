package com.ex.controllers;

import com.ex.entity.History;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController

public class HistoryController {

    @RequestMapping(value ="/xmlHistory")
    History getHistory() throws IOException {

        History history = new History();
        history.setData("I hope work");
        history.setGameNumber(0);
        return history;
    }
}