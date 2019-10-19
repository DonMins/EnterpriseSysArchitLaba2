package com.ex.controllers;

import com.ex.dao.ChangesDao;
import com.ex.dao.HistoryDao;
import com.ex.dao.RatingsDao;
import com.ex.entity.Changes;
import com.ex.entity.User;
import com.ex.service.SecurityService;
import com.ex.service.UserService;
import com.ex.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.UnsupportedEncodingException;

@Controller
public class RegisterController {

    @Autowired
    private HistoryDao historyDao;

    @Autowired
    private ChangesDao changesDao;

    @Autowired
    private UserService userService;

    @Autowired
    private RatingsDao ratingsDao;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) throws UnsupportedEncodingException {
        userValidator.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getConfirmPassword());

        return "redirect:/game";
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Имя пользователя или пароль неверны");
        }
        if (logout != null) {
            model.addAttribute("message", "Вышли успешно");
            return "redirect:/login";
        }
        return "login";
    }

}
