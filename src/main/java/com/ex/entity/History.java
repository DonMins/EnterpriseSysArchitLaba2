package com.ex.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "data",length = 65535)
    private String data;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "idUser" ,unique = true)
    private User users;

    public History(User user, String data) {
        this.data = data;
        this.users = user;
    }
}
