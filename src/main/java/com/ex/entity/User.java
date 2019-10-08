package com.ex.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Simple JavaBean domain object that represents a User.
 *
 * @author Zdornov Maxim
 * @version 1.0
 */

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true)
    private Integer id;

    @Column(length = 50)
    private String username;

    @Column(length = 1000)
    private String password;

    @Column(name = "youNumber")
    private String youNumber;

    @Transient
    private String confirmPassword;

    @OneToOne(mappedBy = "users", fetch=FetchType.LAZY)
    private Rating rating;

    @OneToOne(mappedBy = "users", fetch=FetchType.LAZY)
    private History history;

    public User(String username, String password, String youNumber) {
        this.username = username;
        this.password = password;
        this.youNumber = youNumber;
    }





}
