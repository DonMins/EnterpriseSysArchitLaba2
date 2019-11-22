package com.ex.controllers;

import com.ex.dao.HistoryDao;
import com.ex.entity.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.jms.JMSException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pashina Tanya
 * @version 1.0
 */

@Controller
public class HistoryController {
    @Autowired
    HistoryDao historyDao;
    private JAXBContext myJaxbContext;

    @RequestMapping(value = "/xmlHistory", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView getHistory() throws JAXBException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<History> history = historyDao.findByUsername(auth.getName());
        Histories histories = new Histories();
        histories.setHistories(new ArrayList<History>());

        for (History hist : history) {
            History history1 = new History();
            history1.setData(hist.getData());
            history1.setGameNumber(hist.getGameNumber());
            history1.setId(hist.getId());
            histories.getHistories().add(history1);
        }
        myJaxbContext = JAXBContext.newInstance(Histories.class);
        URL localPackage = this.getClass().getResource("");
        String localDir = localPackage.getPath();
        System.out.println(localDir);
        FileOutputStream stream = new FileOutputStream(localDir + "outputFile.xml");
        Marshaller marshaller = myJaxbContext.createMarshaller();
        marshaller.marshal(histories, stream);
        String xmlFile = localDir + "outputFile.xml";
        Source source = new StreamSource(new File(xmlFile));
        ModelAndView model = new ModelAndView("XSLTView");
        model.addObject("xmlSource", source);
        return model;
    }

    @RequestMapping(value = "/jsonHistory", method = RequestMethod.GET)
    public @ResponseBody
    List<History> getJsonHistory() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<History> history = historyDao.findByUsername(auth.getName());

        List<History> histories = new ArrayList<History>();

        for (History hist : history) {
            History history1 = new History();

            history1.setData(hist.getData());
            history1.setGameNumber(hist.getGameNumber());
            history1.setId(hist.getId());
            histories.add(history1);
        }
        return histories;
    }
}
