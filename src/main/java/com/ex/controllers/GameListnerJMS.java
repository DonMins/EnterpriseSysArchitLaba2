package com.ex.controllers;

import javax.jms.*;

/**
 * @author Pashina Tanya
 * @version 1.0
 */

public class GameListnerJMS implements MessageListener {
    private String text = "";

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                String text1 = ((TextMessage) message).getText();
                setText(text1);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    public String getText() {
        return text;
    }

    private void setText(String text) {
        this.text = text;
    }
}