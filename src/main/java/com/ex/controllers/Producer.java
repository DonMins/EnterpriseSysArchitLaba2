package com.ex.controllers;
import javax.annotation.Resource;
import javax.jms.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class Producer extends HttpServlet {
    @Resource(name = "foo")
    private Topic fooTopic;

    @Resource(name = "bar")
    private Queue barQueue;

    @Resource
    private ConnectionFactory connectionFactory;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();

        connection.start();

        // Create a Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create a MessageProducer from the Session to the Topic or Queue
        MessageProducer producer = session.createProducer(fooTopic);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        // Create a message
        TextMessage message = session.createTextMessage("Hello World!");

        // Tell the producer to send the message
        producer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }

}
