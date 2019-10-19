package com.ex.controllers;
import com.ex.dao.ChangesDao;
import com.ex.dao.HistoryDao;
import com.ex.dao.RatingsDao;
import com.ex.dao.UserDao;
import com.ex.entity.Changes;
import com.ex.entity.History;
import com.ex.entity.Rating;
import com.ex.entity.User;
import com.ex.service.SecurityService;
import com.ex.service.UserService;
import com.ex.task.YouNumberOfjson;
import com.ex.validator.UserValidator;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class RestsController {
    @Autowired
    private HistoryDao historyDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ChangesDao changesDao;

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
        List<History> history = historyDao.findByUsername(auth.getName());
        String newHistory="";

        JSONObject obj = new JSONObject(string);
        String isClear = obj.getString("clear"); //was there a request to clear history in the database
        if (isClear.equals("yes")) {
            for( History hist : history)
            {
                Changes changes1 = new Changes("Delete","History","idUser",String.valueOf(hist.getUsers().getId()));
                Changes changes2 = new Changes("Delete","History","data",hist.getData());
                Changes changes3 = new Changes("Delete","History","gameNumber",String.valueOf(hist.getGameNumber()));
                changesDao.save(changes1);
                changesDao.save(changes2);
                changesDao.save(changes3);
                historyDao.delete(hist);


            }

        }
        else{
            for( History hist : history)
            {

                newHistory=newHistory +"№ игры " + hist.getGameNumber()+" Попытка: " +hist.getData() + "\n";

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
    ResponseEntity<YouNumberOfjson> newAttempt(@RequestBody YouNumberOfjson json) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        json.setYouNumber(gameController.result(json.getYouNumber(), auth));

        // After each departure of the number we save the whole history
        User user= userDao.findByUsername(auth.getName());
        Rating rating = ratingsDao.findByUsername(auth.getName());
        History history=new History(user,json.getYouNumber(),rating.getCountgame()+1);
        String id = String.valueOf(history.getUsers().getId());
        Changes changes1 = new Changes("Insert","History","idUser",id);
        Changes changes2 = new Changes("Insert","History","data",history.getData());
        Changes changes3 = new Changes("Insert","History","gameNumber",String.valueOf(history.getGameNumber()));
        changesDao.save(changes1);
        changesDao.save(changes2);
        changesDao.save(changes3);
        historyDao.save(history);

        return ResponseEntity.ok().body(json);
    }
}
