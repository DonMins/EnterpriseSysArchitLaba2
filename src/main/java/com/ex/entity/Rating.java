package com.ex.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * A simple JavaBean domain object representing a rating table which stores auxiliary data for its calculation
 *
 * @author Zdornov Maxim
 * @version 1.0
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer countgame;

    private Integer allAttempt;

    @OneToOne()
    @JoinColumn(name = "idUser",unique = true)
    private User users;

    public Rating(Integer countgame, Integer allAttempt, User user) {
        this.countgame = countgame;
        this.allAttempt = allAttempt;
        this.users = user;
    }
}