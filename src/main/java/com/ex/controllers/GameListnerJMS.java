package com.ex.controllers;

import com.ex.dao.JMSBaseDao;
import com.ex.entity.JMSBase;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.*;

/**
 * @author Pashina Tanya
 * @version 1.0
 */

public class GameListnerJMS implements MessageListener {
    @Autowired
    private JMSBaseDao jmsBaseDao;

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                String text1 = ((TextMessage) message).getText();
                JMSBase jmsBase = new JMSBase(null, text1);
                jmsBaseDao.save(jmsBase);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

}