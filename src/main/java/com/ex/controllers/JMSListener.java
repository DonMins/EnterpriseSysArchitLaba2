package com.ex.controllers;


import com.ex.dao.ChangesDao;
import com.ex.dao.JMSBaseDao;
import com.ex.dao.RatingsDao;
import com.ex.entity.Changes;
import com.ex.entity.JMSBase;
import com.ex.entity.Rating;
import com.ex.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.*;
import java.io.IOException;



public class JMSListener implements MessageListener {
    @Autowired
    private RatingsDao ratingsDao;
    @Autowired
    private ChangesDao changesDao;
    @Autowired
    private JMSBaseDao jmsBaseDao;



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
                JMSBase jmsBase = new JMSBase(user,text);
                jmsBaseDao.save(jmsBase);
            }
            catch (JMSException | IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        else {
            throw new IllegalArgumentException("Message wrong");
        }
    }
}
