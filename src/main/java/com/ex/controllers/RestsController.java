package com.ex.controllers;
import com.ex.dao.HistoryDao;
import com.ex.dao.RatingsDao;
import com.ex.dao.UserDao;
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
    private UserService userService;

    @Autowired
    private RatingsDao ratingsDao;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private GameController gameController;

    private static final String XML_FILE_NAME = "customer.xml";

    @RequestMapping(method = RequestMethod.POST, value = "/history", consumes = "text/plain")
    ResponseEntity<?> history(@RequestBody String string) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<History> history = historyDao.findByUsername(auth.getName());
        String newHistory="";

        for( History hist : history)
        {
            
            newHistory=newHistory +"№ игры " + hist.getGameNumber()+" Попытка: " +hist.getData() + "\n";

        }


//        ApplicationContext appContext = new ClassPathXmlApplicationContext("App.xml");
//        XMLConverter converter = (XMLConverter) appContext.getBean("XMLConverter");
//
//
//
//        History customer = new History();
//        customer.setGameNumber(1);
//        customer.setData("WOOORK");
//
//
//        converter.convertFromObjectToXML(customer, XML_FILE_NAME);




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
        historyDao.save(history);

        return ResponseEntity.ok().body(json);
    }
}
