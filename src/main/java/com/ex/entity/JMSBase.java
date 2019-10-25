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
 * A simple JavaBean domain object representing the JMS structure
 *
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "jms")

public class JMSBase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "message",length = 65535)
    private String message;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "idUser")
    private User users;

    public JMSBase(User user, String message) {
        this.message = message;
        this.users = user;
    }
}
