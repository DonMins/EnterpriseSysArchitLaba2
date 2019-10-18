package com.ex.controllers;

import com.ex.entity.History;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "histories")
@XmlAccessorType (XmlAccessType.FIELD)
public class Histories
{
    @XmlElement(name = "history")
    private List<History> histories = null;

    public List<History> getHistories() {
        return histories;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
    }
}