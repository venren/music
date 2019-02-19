package com.example.music.search.entities;

import javax.persistence.*;

@Entity
@Table(name = "genre")
public class Genre {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "genre", nullable = false)
    private String genre;

    public Long getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }
}
