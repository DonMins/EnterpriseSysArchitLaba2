package com.ex.controllers;

import com.ex.dao.ChangesDao;
import com.ex.dao.JMSBaseDao;
import com.ex.dao.RatingsDao;
import com.ex.dao.UserDao;
import com.ex.entity.Changes;
import com.ex.entity.JMSBase;
import com.ex.entity.Rating;
import com.ex.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.*;
import java.io.IOException;

/**
 * @author Pashina Tanya
 * @version 1.0
 */

public class JMSListener implements MessageListener {
    @Autowired
    private RatingsDao ratingsDao;
    @Autowired
    private ChangesDao changesDao;
    @Autowired
    private JMSBaseDao jmsBaseDao;
    @Autowired
    private UserDao userDao;

    public void onMessage(Message message) {
        ObjectMapper mapper = new ObjectMapper();
        if (message instanceof ObjectMessage) {
            try {
                String text = (String) ((ObjectMessage) message).getObject();
                User user = mapper.readValue(text, User.class);
                Rating tempRating = ratingsDao.findByUsername(user.getUsername());
                tempRating.setCountgame(tempRating.getCountgame() + 1);
                ratingsDao.save(tempRating);
                Changes changes1 = new Changes("Update", "Rating", "countGame", String.valueOf(tempRating.getCountgame()));
                changesDao.save(changes1);
                User user1 = userDao.findByUsername(user.getUsername());
                JMSBase jmsBase = new JMSBase(user1, text);
                jmsBaseDao.save(jmsBase);

                ConnectionFactory cf = new ActiveMQConnectionFactory("tcp://localhost:61616");
                Connection conn = cf.createConnection();
                Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Destination destination = new ActiveMQTopic("spitter.topic1");
                MessageProducer producer = session.createProducer(destination);
                TextMessage msg = session.createTextMessage();
                String tex = "Поздравляем! " + user.getUsername() + " выиграл игру!";
                msg.setText(tex);
                producer.send(msg);
            } catch (JMSException | IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            throw new IllegalArgumentException("Message wrong");
        }
    }
}