package com.ex.controllers;

import com.ex.dao.HistoryDao;
import com.ex.entity.History;
import com.thoughtworks.xstream.XStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class HistoryController {
    @Autowired
    HistoryDao historyDao;


    @RequestMapping(value="/xmlHistory", method = RequestMethod.GET)
    public @ResponseBody Histories getUser() throws JAXBException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<History> history = historyDao.findByUsername(auth.getName());

         Histories histories = new Histories();
         histories.setHistories(new ArrayList<History>());

        for( History hist : history) {
            History history1 = new History();

            history1.setData(hist.getData());
            history1.setGameNumber(hist.getGameNumber());
            history1.setId(hist.getId());
            histories.getHistories().add(history1);

        }

        return histories;
    }
}
