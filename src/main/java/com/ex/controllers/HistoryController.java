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
        ConnectionFactory cf =
                new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection conn = null;
        Session session = null;
        try {
            conn = cf.createConnection();
            conn.start();
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination =
                    new ActiveMQQueue("spitter.queue");
            MessageConsumer consumer = session.createConsumer(destination);
            Message message = consumer.receive();
            TextMessage textMessage = (TextMessage) message;
            System.out.println("GOT A MESSAGE: " + textMessage.getText());
            History history1 = new History();

            history1.setData(textMessage.getText());
            histories.getHistories().add(history1);
            conn.start();
        } catch (JMSException e) {
// handle exception?
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (JMSException ex) {
            }
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

        ConnectionFactory cf =
                new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection conn = cf.createConnection();
        Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = new ActiveMQQueue("spitter.queue");
            MessageProducer producer = session.createProducer(destination);
            TextMessage message = session.createTextMessage();
            message.setText("Hello world!");
            producer.send(message);



        return histories;
    }
}
