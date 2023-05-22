package com.simplesocialnetwork.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Ivan Gordeev 21.05.2023
 */

@Entity
@Table(name = "usr")
@Data
public class User {
    @Id
    private String id;
    private String name;
    private String userpic;
    private String email;
    private String gender;
    private String locale;
    private LocalDateTime lastVisit;
}
