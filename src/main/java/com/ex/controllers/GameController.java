package com.ex.controllers;

import com.ex.dao.ChangesDao;
import com.ex.dao.RatingsDao;
import com.ex.entity.Changes;
import com.ex.entity.Rating;
import com.ex.entity.User;
import com.ex.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jms.*;
import java.util.*;

/**
 * @author Zdornov Maxim
 * @version 1.0
 */

@Controller
public class GameController {
    @Autowired
    private ChangesDao changesDao;
    @Autowired
    private UserService userService;
    @Autowired
    private RatingsDao ratingsDao;
    final private Integer[] randomNumbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    /**
     * Method displays user rating
     *
     * @return Return rating.jsp page
     */
    @RequestMapping(value = "/rating")
    public String rating(Model model) {
        List<Rating> ratingList = ratingsDao.findAll();
        // Создаем map с именем пользователя и его рейтингом и сразу отсортировываем по убыванию
        Map<Double, String> map = new TreeMap<>(new Comparator<Double>() {
            @Override
            public int compare(Double a, Double b) {
                if (a >= b) {
                    return 1;
                } else if (a < b)
                    return -1;
                else
                    return 0;
            }
        });
        // Пользователи с 0  getCountgame() не будут отображаться в списке
        for (Rating rating : ratingList) {
            map.put((double) rating.getAllAttempt() / rating.getCountgame(), rating.getUsers().getUsername());
        }
        model.addAttribute("rating", map);
        return "rating";
    }
    /**
     * Game Start Page
     *
     * @return Return game.jsp page
     */
    @GetMapping(value = {"/", "/game"})
    public String game() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        user.setYouNumber(genNumber()); // Как только пользователь вошел на страницу game, сразу сгенерим ему число для угадывания
        System.out.println(user.getYouNumber());
        Changes changes = new Changes("Update", "User", "youNumber", user.getYouNumber());
        changesDao.save(changes);
        userService.update(user);
        return "game";
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * This method allows you to get the history of previous user games
     *
     * @return Return the history to game.jsp to textareaHistory
     */

    private String genNumber() {
        Collections.shuffle(Arrays.asList(randomNumbers));
        System.out.println("ЧИСЛО " + (randomNumbers[0]) + randomNumbers[1] + randomNumbers[2] + randomNumbers[3]);
        return String.valueOf(randomNumbers[0]) + randomNumbers[1] + randomNumbers[2] + randomNumbers[3];
    }

    ArrayList<String> result(String stringOfYouEnteredNumber, Authentication auth) {
        ArrayList<String> st = new ArrayList<>();
        Rating tempRating = ratingsDao.findByUsername(auth.getName());
        tempRating.setAllAttempt(tempRating.getAllAttempt() + 1); // increase the number of attempts to guess in the database

        ratingsDao.save(tempRating);
        User user = userService.findByUsername(auth.getName());
        String number = user.getYouNumber(); //Get the number to guess

        ArrayList<Character> numberSymbol = new ArrayList<>();
        ArrayList<Character> strSymbol = new ArrayList<>();
        ArrayList<Character> l3;
        l3 = strSymbol;
        int bull = 0;
        int cow = 0;
        for (int i = 0; i < 4; ++i) {
            numberSymbol.add(number.charAt(i));
            strSymbol.add(stringOfYouEnteredNumber.charAt(i));
            if (number.charAt(i) == stringOfYouEnteredNumber.charAt(i)) {
                bull++;
            }
        }
        l3.retainAll(numberSymbol);
        cow = l3.size() - bull;
        if (bull == 4) {
            user.setYouNumber(genNumber());
            Changes changes2 = new Changes("Update", "Rating", "allAtempt", String.valueOf(tempRating.getAllAttempt()));
            changesDao.save(changes2);
            ratingsDao.save(tempRating);
            Changes changes = new Changes("Update", "User", "youNumber", user.getYouNumber());
            changesDao.save(changes);
            userService.update(user);
            String text = "Число угадано!";
            try {
                User user1 = new User(user.getUsername(), user.getPassword(), user.getYouNumber());
                sendObjectMessage(user1);

            } catch (JMSException | JsonProcessingException e) {
                e.printStackTrace();
            }
            st.add(stringOfYouEnteredNumber + " - " + bull + "Б" + cow + "K (число угадано) \n---------------------------\nЯ загадал еще...");
            st.add(text);
            return st;
        }
        st.add(stringOfYouEnteredNumber + " - " + bull + "Б" + cow + "K");
        st.add("null");
        return (st);
    }

    private void sendObjectMessage(User user) throws JMSException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ConnectionFactory cf = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection conn = cf.createConnection();
        Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = new ActiveMQQueue("spitter.topic");
        MessageProducer producer = session.createProducer(destination);
        ObjectMessage msg = session.createObjectMessage();
        String tex = mapper.writeValueAsString(user);
        msg.setObject(tex);
        producer.send(msg);
    }
}
