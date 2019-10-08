package com.ex.controllers;
import com.ex.dao.HistoryDao;
import com.ex.dao.RatingsDao;
import com.ex.entity.History;
import com.ex.service.SecurityService;
import com.ex.service.UserService;
import com.ex.task.YouNumberOfjson;
import com.ex.validator.UserValidator;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class RestsController {
    @Autowired
    private HistoryDao historyDao;

    @Autowired
    private UserService userService;

    @Autowired
    private RatingsDao ratingsDao;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private GameController gameController;

    @RequestMapping(method = RequestMethod.POST, value = "/history", consumes = "text/plain")
    ResponseEntity<?> history(@RequestBody String string) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JSONObject obj = new JSONObject(string);
        String isClear = obj.getString("clear"); //was there a request to clear history in the database
        if (isClear.equals("yes")) {
            History history = historyDao.findByUsername(auth.getName());
            history.setData("");
            historyDao.save(history);
        }
        History history = historyDao.findByUsername(auth.getName());

        return ResponseEntity.ok().headers(new HttpHeaders() {{
            add("Content-Type", "text/plain; charset=utf-8");
        }}).body(history.getData());
    }

    /**
     * This method gets the number passed by the user to guess the intended number
     *
     * @return Return the result to game.jsp
     */
    @RequestMapping(value = "/newAttempt", method = RequestMethod.POST, headers = {"Content-type=application/json"})
    ResponseEntity<YouNumberOfjson> newAttempt(@RequestBody YouNumberOfjson json) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        json.setYouNumber(gameController.result(json.getYouNumber(), auth));

        // After each departure of the number we save the whole history
        History history = historyDao.findByUsername(auth.getName());
        history.setData(history.getData() + json.getYouNumber() + "\n");
        historyDao.save(history);

        return ResponseEntity.ok().body(json);
    }
}
