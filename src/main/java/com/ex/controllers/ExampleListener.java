package com.ex.controllers;


import com.ex.dao.RatingsDao;
import com.ex.entity.History;
import com.ex.entity.Rating;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.TextMessage;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;


public class ExampleListener implements MessageListener {
    @Autowired
    private RatingsDao ratingsDao;

    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {

//
//                Rating tempRating = ratingsDao.findByUsername(auth.getName());
//                tempRating.setCountgame(tempRating.getCountgame() + 1);

                System.out.println(((TextMessage) message).getText());
            }
            catch (JMSException ex) {
                throw new RuntimeException(ex);
            }
        }
        else {
            throw new IllegalArgumentException("Message must be of type TextMessage");
        }
    }
}
