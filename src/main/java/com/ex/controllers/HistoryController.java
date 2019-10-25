package com.ex.controllers;

import com.ex.dao.HistoryDao;
import com.ex.entity.History;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.jms.*;
import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;

@Controller

public class HistoryController {
    @Autowired
    HistoryDao historyDao;


    @RequestMapping(value="/xmlHistory", method = RequestMethod.GET)
    public @ResponseBody Histories getHistory() throws JAXBException {
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


    @RequestMapping(value="/jsonHistory", method = RequestMethod.GET)
    public @ResponseBody List<History> getJsonHistory() throws JAXBException, JMSException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<History> history = historyDao.findByUsername(auth.getName());

        List<History> histories = new ArrayList<History>();

        for( History hist : history) {
            History history1 = new History();

            history1.setData(hist.getData());
            history1.setGameNumber(hist.getGameNumber());
            history1.setId(hist.getId());
            histories.add(history1);

        }


        return histories;
    }
}
