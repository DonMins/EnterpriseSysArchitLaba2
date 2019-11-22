package com.ex.controllers;

import com.ex.dao.ChangesDao;
import com.ex.dao.HistoryDao;
import com.ex.dao.RatingsDao;
import com.ex.dao.UserDao;
import com.ex.entity.Changes;
import com.ex.entity.History;
import com.ex.entity.Rating;
import com.ex.entity.User;
import com.ex.task.YouNumberOfjson;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zdornov Maxim
 * @version 1.0
 */

@RestController
public class RestsController {
    @Autowired
    private HistoryDao historyDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ChangesDao changesDao;

    @Autowired
    private RatingsDao ratingsDao;

    @Autowired
    private GameController gameController;

    @RequestMapping(method = RequestMethod.POST, value = "/history", consumes = "text/plain")
    ResponseEntity<?> history(@RequestBody String string) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<History> history = historyDao.findByUsername(auth.getName());
        String newHistory = "";

        JSONObject obj = new JSONObject(string);
        String isClear = obj.getString("clear"); //was there a request to clear history in the database
        if (isClear.equals("yes")) {
            for (History hist : history) {
                Changes changes = new Changes("Delete", "History", "data", hist.getData());
                changesDao.save(changes);
                historyDao.delete(hist);
            }
        } else {
            for (History hist : history) {
                newHistory = newHistory + "№ игры " + hist.getGameNumber() + " Попытка: " + hist.getData() + "\n";
            }
        }
        return ResponseEntity.ok().headers(new HttpHeaders() {{
            add("Content-Type", "text/plain; charset=utf-8");
        }}).body(newHistory);
    }

    /**
     * This method gets the number passed by the user to guess the intended number
     *
     * @return Return the result to game.jsp
     */
    @RequestMapping(value = "/newAttempt", method = RequestMethod.POST, headers = {"Content-type=application/json"})
    ResponseEntity<YouNumberOfjson> newAttempt(@RequestBody YouNumberOfjson json) throws JMSException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ArrayList<String> response = new ArrayList<>();
        response = gameController.result(json.getYouNumber(), auth);
        json.setYouNumber(response.get(0));
        json.setProperties(response.get(1));

        // After each departure of the number we save the whole history
        User user = userDao.findByUsername(auth.getName());
        Rating rating = ratingsDao.findByUsername(auth.getName());
        History history = new History(user, json.getYouNumber(), rating.getCountgame() + 1);
        String id = String.valueOf(history.getUsers().getId());
        Changes changes1 = new Changes("Insert", "History", "idUser", id);
        Changes changes2 = new Changes("Insert", "History", "data", history.getData());
        Changes changes3 = new Changes("Insert", "History", "gameNumber", String.valueOf(history.getGameNumber()));
        changesDao.save(changes1);
        changesDao.save(changes2);
        changesDao.save(changes3);
        historyDao.save(history);
        return ResponseEntity.ok().body(json);
    }
}
