package com.ex.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * A simple JavaBean domain object representing the game history of different users
 *
 * @author Zdornov Maxim
 * @version 1.0
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "history")
@XmlRootElement(name = "history")
@XmlAccessorType(XmlAccessType.FIELD)
public class History implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlElement
    private Integer id;

    @Column(name = "data",length = 65535)
    @XmlElement
    private String data;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "idUser")
    private User users;

    /** Номер игры пользователя */

    @Column(name = "gameNumber")
    @XmlElement
    private int gameNumber;


    public History(User user, String data, int gameNumber) {
        this.data = data;
        this.users = user;
        this.gameNumber=gameNumber;
    }
}
