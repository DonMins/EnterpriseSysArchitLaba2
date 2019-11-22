package com.ex.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "changes")

public class Changes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "change",length = 65535)
    private String change;

    @Column(name = "entityName")
    private String entityName;

    // Номер игры пользователя
    @Column(name = "entityColumn")
    private String entityColumn;

    @Column(name = "value")
    private String value;

    public Changes(String change, String entityName, String entityColumn, String value) {
        this.change = change;
        this.entityName = entityName;
        this.entityColumn=entityColumn;
        this.value=value;
    }
}

